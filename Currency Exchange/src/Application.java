package com.yoav.currencyExchange;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This application has come to easely converts between one currency to another currency
 * according to amount of money the user inputs.
 * The app shows the up-to-date currencies rates, according to Bank Israel official website.
 * This app has been done for the final project in JAVA course in shenkar.
 * I chose to do the the following bonuses: 1,2,5,6,7,8.
 * The app is based on the MVC pattern, and based on 3 API's: JAVA 8 API, Log4J API, JUnit API.
 * 
 * This class is the Controller part of the MVC architecture. this is the intermediary between the 
 * Model and the View. The class holds a reference to Model and View so it can takes the
 * data from the Model, and update the View when necessary.
 * @see: bank Israel official website : http://www.boi.org.il/currency.xml
 * @category: financial application.
 * @last_update: 02/08/2016.
 * @author Yoav Saroya - 304835887.
 */

public class Application {

	private MainView view;
	private boolean updater;
	private CurrencyDataHolder model;

	public Application(CurrencyDataHolder m) {
		updater = false;
		model = m;
	}

	public boolean getUpdater() {return updater;}
	public void setUpdater(boolean f) {updater = f;}

	public void setView(MainView view) {this.view = view;}
	public MainView getView() {return view;}

	//Updating the current data from model, and updating the View to show the correct information.
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
		if (currCurrency.getRate() == toCurrency.getRate())
			return amount;
		else if (currCurrency.getCode().equals("ILS"))
			return amount/toCurrency.getRate()*toCurrency.getUnit();
		else if (toCurrency.getCode().equals("ILS"))
			return amount*(currCurrency.getRate()/currCurrency.getUnit());
		else
			return (amount*(currCurrency.getRate()/currCurrency.getUnit()))/(toCurrency.getRate()/toCurrency.getUnit());
	}

	public static void main(String[] args) {

		LogWriter.getInstance().logger.info("Opening the application.");

		CurrencyDataHolder model = new CurrencyDataHolder(); //creating the CurrencyDataHolder object.
		Application controller = new Application(model);	// creating the Controller object.


		//creating the View in a seperate thread.
		EventQueue.invokeLater(() -> {
			Thread.currentThread().setName("View Thread");
			MainView view = new MainView(controller, model);
			controller.setView(view);
		});

		//sleep until the initializing of the View object is finished.
		try {Thread.sleep(2500);}
		catch(InterruptedException e) {
			e.printStackTrace();
			LogWriter.getInstance().logger.error("The thread of controller has been interrupted.");
		}

		//as long as the window is opened, the APP updates the data and the view accordingly.
		long start = System.currentTimeMillis();
		while (controller.getView().isVisible()) {
			try{Thread.sleep(10);}
			catch(InterruptedException e) {
				LogWriter.getInstance().logger.error("The thread of controller has been interrupted.");
				e.printStackTrace();
			}
			
			if (System.currentTimeMillis() - start > 30000) {
				controller.update();
				start = System.currentTimeMillis();
			}
			if (controller.getUpdater() == true) {
				controller.update();
				controller.setUpdater(false);
			}
		}
	}
}