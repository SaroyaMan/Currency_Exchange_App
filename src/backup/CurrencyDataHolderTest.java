package backup;
import java.io.IOException;

import java.net.MalformedURLException;

import junit.framework.TestCase;

/**
 * This is a class that represents a test to validate that ConcereteModel class is doing it's job.
 * It uses the JUnit API, and if one method (or more) throws an exception - the test failed.
 * And if it runs without throwing an exceptions - the test succeeded.
 * @author Yoav Saroya
 */

public class CurrencyDataHolderTest extends TestCase {
	
	//The ConcreteModelTest has a reference to the tested class - ConcreteModel.
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

	//the test method that check that the Model is doing what expected.
	public final void testReadToFile() {
		Throwable ex = null;
		try{model.readToFile();}
		catch(MalformedURLException e){ex = e;}
		catch(IOException e) {ex = e;}
		//if MalformedURLException or IOException is throwed, so we mark the test as a fail.
		//otherwise, the test succeeded.
		assertFalse(ex instanceof MalformedURLException);
		assertFalse(ex instanceof IOException);
	}
}