/*package com.yoav.currencyExchange;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
*/
/**
 * This class is the Controller part of the MVC architecture. this is the intermediary between the 
 * Model and the View. The Controller holds a reference to Model and View so it can takes the
 * data from the Model, and update the View when necessary.
 * The Controller class implements the Runnable interface, cause it needs to update the Model
 * and the View through all way.
 * @author Yoav Saroya
 */
/*
public class Controller implements Runnable {

	private View view;
	private CurrencyDataHolder model;

	public Controller(CurrencyDataHolder m) {
		model = m;
	}

	public void setView(View view) {this.view = view;}

	//Updating the current data from Model, and updating the View to show the correct information.
	public synchronized void update() {
		try{
			model.readToFile();
		}catch(IOException e) {
			e.printStackTrace();
			LogWriter.getInstance().logger.error("The connection with IsraelBank has been failed."); 
		}
		if (view != null) {
			view.updateDataTable(true);
			LogWriter.getInstance().logger.trace("Updating the local file and the View."); 
		}
	}

	@Override
	//Every 30 seconds, the update method will be called (till the APP is closed).
	public void run() {
		while (true) {
			update();	
			try {Thread.sleep(30000);}
			catch(InterruptedException e) {
				LogWriter.getInstance().logger.error("The thread of controller has been interrupted.");
				e.printStackTrace();
			}
		}
	}

	public ArrayList<Currency> parseToCurrencies() {
		//	This is the method that parse the local XML file to a vector of Currency objects.

		//initializing the variables that needed for later.
		FileInputStream fis = null;
		ArrayList<Currency> currs = new ArrayList<>();
		try {
			//opening the local XML file.
			fis = new FileInputStream("data/data.xml");
			//parsing the XML file.
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(fis);
			NodeList nameList = doc.getElementsByTagName("NAME");
			NodeList codeList = doc.getElementsByTagName("CURRENCYCODE");
			NodeList countryList = doc.getElementsByTagName("COUNTRY");
			NodeList rateList = doc.getElementsByTagName("RATE");
			NodeList changeList = doc.getElementsByTagName("CHANGE");
			NodeList unitList = doc.getElementsByTagName("UNIT");
			currs.add(new Currency("Shekel",1,"Israel","ILS",1.0,1.0));
			for (int i=0; i<nameList.getLength(); i++) {
				//passing through all list and create a new Currency object in each iterates.
				currs.add(new Currency(nameList.item(i).getFirstChild().getNodeValue(),
						Integer.parseInt(unitList.item(i).getFirstChild().getNodeValue()),
						countryList.item(i).getFirstChild().getNodeValue(),
						codeList.item(i).getFirstChild().getNodeValue(),
						Double.parseDouble(rateList.item(i).getFirstChild().getNodeValue()),
						Double.parseDouble(changeList.item(i).getFirstChild().getNodeValue())));
			}
		}
		catch(IOException | ParserConfigurationException | SAXException e) {
			LogWriter.getInstance().logger.error("The parsing of the local XML has been failed.");
			e.printStackTrace();
		}
		finally { //closing all the resources.
			if (fis != null) {
				try { fis.close();}
				catch(IOException e) {e.printStackTrace();}
			}
		}
		return currs;
	}

	//parse the currency data to a table (a 2 dimentional string).
	public String[][] parseToTable(ArrayList<Currency> currs) {
		//initializing the data, with one record less, cause the table doesn't show the 'ILS' currency.
		String[][] data = null;
		data = new String[currs.size()-1][6];
		for (int i=0; i<data.length; i++) {		//passing through all data and parsing it to a table.
			data[i][0] = currs.get(i+1).getName();
			data[i][1] = String.valueOf(currs.get(i+1).getUnit());
			data[i][2] = currs.get(i+1).getCountry();
			data[i][3] = currs.get(i+1).getCode();
			data[i][4] = String.valueOf(currs.get(i+1).getRate());
			data[i][5] = String.valueOf(currs.get(i+1).getChange());
		}
		return data;	//returning the parsed data.
	}

	public double convert(double amount, Currency currCurrency, Currency toCurrency) {
		/*
		 * This method is the converting function. The main function that the APP is based on.
		 * The method gets the amount, and 2 currencies and convert it's value to the new currency value.
		 */
/*
		if (currCurrency.getRate() == toCurrency.getRate())
			return amount;
		else if (currCurrency.getCode().equals("ILS"))
			return amount/toCurrency.getRate()*toCurrency.getUnit();
		else if (toCurrency.getCode().equals("ILS"))
			return amount*(currCurrency.getRate()/currCurrency.getUnit());
		else
			return (amount*(currCurrency.getRate()/currCurrency.getUnit()))/(toCurrency.getRate()/toCurrency.getUnit());
	}
}
*/