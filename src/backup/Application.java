package backup;
import java.awt.EventQueue;


/**
 * This application has come to easely converts between one currency to another currency
 * according to amount of money the user inputs.
 * The app shows the up-to-date currencies rates, according to Bank Israel official website.
 * This app has been done for the final project in JAVA course in shenkar.
 * I chose to do the the following bonuses: 1,2,5,6,7,8.
 * The app is based on the MVC pattern, and based on 3 API's: JAVA 8 API, Log4J API, JUnit API.
 * @see: bank Israel official website : http://www.boi.org.il/currency.xml
 * @category: financial application.
 * @last_update: 25/07/2016.
 * @author Yoav Saroya - 304835887.
 */

public class Application {

	public static void main(String[] args) {
		
		LogWriter.getInstance().logger.info("Opening the application.");
		//creating the ConcreteModel object.
		CurrencyDataHolder model = new CurrencyDataHolder();
		//creating the Controller object.
		Controller controller = new Controller(model);
		//creating the View in a seperate thread.
		EventQueue.invokeLater(() -> {
			Thread.currentThread().setName("View Thread");
			View view = new View(controller);
			controller.setView(view);
		});
		
		//sleep until the initializing of the View object is finished.
		try {Thread.sleep(500);}
		catch(InterruptedException e) {
			e.printStackTrace();
			LogWriter.getInstance().logger.error("The thread of controller has been interrupted.");
		}
		
		//activates the run method of the controller, which updates the data and the view accordingly.
		new Thread(controller, "Controller Thread").start();
	}
}