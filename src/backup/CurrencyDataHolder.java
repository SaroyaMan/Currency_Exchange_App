package backup;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The ConcreteModel class implements the interface Model. This class is the Model part of the
 * MVC architecture. all it's job is to get the data from Bank Israel website and store it in a local file.
 * @author Yoav Saroya
 */

public class CurrencyDataHolder {
	
	//get the data from the website and put it in a local file called "data.xml".
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