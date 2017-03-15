package com.yoav.currencyExchange;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The CurrencyDataHolder class is the Model part of the MVC architecture.
 * The class responsible for getting the data from Bank Israel website and store it in a local file,
 * pasring the locl XML file and create a Currency objects from the data.
 * @author Yoav Saroya
 */

public class CurrencyDataHolder {
	
	private LinkedHashMap<String,Currency> currencies;
	
	public CurrencyDataHolder(){
		try {
			currencies = new LinkedHashMap<>();
			readToFile();
			currencies = parseToCurrencies();
		}
		catch(IOException e) {
			LogWriter.getInstance().logger.error("failed to get the currency data.");
			e.printStackTrace();
		}
		catch (ParserConfigurationException | SAXException e) {
			LogWriter.getInstance().logger.error("The parsing of the local XML has been failed.");
			e.printStackTrace();
		}
	}
	
	/**
	 * This is the method that parse the local XML file to a vector of Currency objects.
	 * @return LinkedHashMap<String,Currency> of the data
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public LinkedHashMap<String,Currency> parseToCurrencies() throws SAXException, IOException, ParserConfigurationException {
		//initializing the variables that needed for later.
		FileInputStream fis = null;
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
			currencies.put("ILS",new Currency("Shekel",1,"Israel","ILS",1.0,1.0));
			for (int i=0; i<nameList.getLength(); i++) {
				//passing through all list and create a new Currency object in each iterates.
				currencies.put(codeList.item(i).getFirstChild().getNodeValue()
						,new Currency(nameList.item(i).getFirstChild().getNodeValue(),
						Integer.parseInt(unitList.item(i).getFirstChild().getNodeValue()),
						countryList.item(i).getFirstChild().getNodeValue(),
						codeList.item(i).getFirstChild().getNodeValue(),
						Double.parseDouble(rateList.item(i).getFirstChild().getNodeValue()),
						Double.parseDouble(changeList.item(i).getFirstChild().getNodeValue())));
			}	
		}
		finally { //closing all the resources.
			if (fis != null) {
				try { fis.close();}
				catch(IOException e) {e.printStackTrace();}
			}
		}
		return currencies;
	}
	/**
	 * get the data from Bank Israel website and put it in a local file called "data.xml".
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public void readToFile() throws IOException, MalformedURLException {
		
		//initializing the variables.
		URL url = null;
		HttpURLConnection con = null;
		InputStream in = null;
		FileOutputStream fos = null;
		try {
			//create a connection to Bank Israel site.
			url = new URL("http://boi.org.il/currency.xml");
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			//copy all the data from the XML of bank israel to a local file.
			in = con.getInputStream();
			fos = new FileOutputStream("data/data.xml");
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1)
				fos.write(bytes, 0, read);
		}

		finally {	//closing all the resources.
			if (con != null) {con.disconnect();}
			if (in != null) {
				try {in.close();}
				catch (IOException e) {e.printStackTrace();}
			}
			if (fos != null) {
				try {fos.close();}
				catch (IOException e) {e.printStackTrace();}
			}
		}
	}
}