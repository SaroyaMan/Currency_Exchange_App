package com.yoav.currencyExchange;
import java.io.IOException;

import java.net.MalformedURLException;
import java.util.LinkedHashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import junit.framework.TestCase;

/**
 * This is a class that represents a test to validate that CurrencyDataHolder class is doing it's job.
 * It uses the JUnit API, and if one method (or more) throws an exception - the test failed.
 * And if it runs without throwing an exceptions - the test succeeded.
 * @author Yoav Saroya
 */

public class CurrencyDataHolderTest extends TestCase {
	
	//The CurrencyDataHolderTest has a reference to the tested class - CurrencyDataHolder.
	private CurrencyDataHolder model;

	public CurrencyDataHolderTest(String name) {
		super(name);
	}

	//Setting up the test when it's created.
	protected void setUp() throws Exception {
		super.setUp();
		model = new CurrencyDataHolder();
	}
	
	//when the test is finished, the tested object is teared down.
	protected void tearDown() throws Exception {
		super.tearDown();
		model = null;
	}

	/**
	 * the test method that check that the CurrencyDataHolder downloads from
	 * Bank Israel the data sucessfully.
	 */
	public final void testReadToFile() {
		Throwable ex = null;
		try{model.readToFile();}
		catch(IOException e){ex = e;}
		//if MalformedURLException or IOException is throwed, so we mark the test as a fail.
		//otherwise, the test succeeded.
		assertFalse(ex instanceof MalformedURLException || ex instanceof IOException);
	}
	
	/**
	 * the test method that check that the CurrencyDataHolder parsing successfully the data.
	 */
	public final LinkedHashMap<String,Currency> testParseToCurrencies() {
		Throwable ex = null;
		LinkedHashMap<String,Currency> temp = null;
		try{ temp = model.parseToCurrencies();}
		catch(ParserConfigurationException| SAXException | IOException e) {ex = e;}
		assertFalse(ex instanceof ParserConfigurationException || ex instanceof IOException
				|| ex instanceof SAXException || temp == null);
		return temp;
	}
}