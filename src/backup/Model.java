package backup;
import java.io.IOException;

/**
 * The model interface. the class that implements it will be the Model part of the MVC.
 * The Model interface is a functional interface (has only 1 method).
 * @author Yoav Saroya
 */

@FunctionalInterface
public interface Model {	
	
	public void readToFile() throws IOException;
}