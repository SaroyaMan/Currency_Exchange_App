import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConcreteModel implements Model {
	
	@Override //get the data from the website and put it in a local file.
	public void readToFile() throws IOException, MalformedURLException {
		URL url = null;
		HttpURLConnection con = null;
		InputStream in = null;
		FileOutputStream fos = null;
		try {
			url = new URL("http://boi.org.il/currency.xml");
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			in = con.getInputStream();
			fos = new FileOutputStream("data.xml");
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1)
				fos.write(bytes, 0, read);
		}

		finally {
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