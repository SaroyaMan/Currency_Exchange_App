import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class LogWriter {
	
	public Logger logger = null;
	private static LogWriter instance = null;
	private LogWriter() {
		try{
			logger = Logger.getLogger(LogWriter.class.getName());
			logger.addAppender(new FileAppender(new PatternLayout(
					"%d{MM/dd/yyyy HH:mm:ss}-[%t]-%x-%-5p-%-10c:%m%n"),"logger.txt",true));
			logger.setLevel(Level.ALL);
		}catch(IOException e) {e.printStackTrace();}
	}
	public static LogWriter getInstance() {
		if (instance == null) {
			instance = new LogWriter();
		}
		return instance;
	}
}
