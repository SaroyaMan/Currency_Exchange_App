import java.io.IOException;

import java.net.MalformedURLException;

import junit.framework.TestCase;

public class ConcreteModelTest extends TestCase {

	private ConcreteModel model;

	public ConcreteModelTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		model = new ConcreteModel();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		model = null;
	}

	public final void testReadToFile() {
		Throwable ex = null;
		try{
			model.readToFile();
		}catch(MalformedURLException e){ex = e;}
		catch(IOException e) {ex = e;}
		assertFalse(ex instanceof MalformedURLException);
		assertFalse(ex instanceof IOException);
	}
}