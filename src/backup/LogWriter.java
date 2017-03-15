package backup;
import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * LogWriter is a class that it's job is to write to a file all the records in the APP.
 * LogWriter is a class that implements the Singleton pattern.
 * the pattern of the log writing is :  %d{MM/dd/yyyy HH:mm:ss}-[%14t]-%x-%-5p:%m%n
 * Almost all other classes holds a reference to the LogWriter object to record the application to a log.
 * the Logger class is a 'thread-safe' class - means it's methods are synchronized and if 1 thread updates
 * the file - other threads cannot edit the file.
 * @author Yoav Saroya
 */

public class LogWriter {
	
	public Logger logger = null;
	private static LogWriter instance = null;
	
	 /** private constructor, 
	  * invoked when an object of this class is instantiated for the first time
	  */
	private LogWriter() {
		try{
			//initializing the logger and configures it.
			logger = Logger.getLogger(LogWriter.class.getName());
			logger.addAppender(new FileAppender(new PatternLayout(
					"%d{MM/dd/yyyy HH:mm:ss}-[%17t]-%x-%-5p:%m%n"),"data/logger.txt",true));
			logger.setLevel(Level.ALL);
		}catch(IOException e) {e.printStackTrace();}
	}
	 /** retrieving the singleton instance
	  * (lazy initialization)
	  */
	public static LogWriter getInstance() {
		//creating a single object of LogWriter.
		if (instance == null) instance = new LogWriter();
		return instance;
	}
}
