package revolut.paymentAPI;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class Application {
	
    private final static Logger logger = Logger.getLogger(Application.class);

	public static void main(String[] args) throws Exception {
		new Thread(new Runnable() {
			
			public void run() {
				try {
					TransferTest.init();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
		
		Thread.sleep(5000);
		
		logger.setLevel(Level.DEBUG);
		JUnitCore runner = new JUnitCore();
        Result result = runner.run(TransferTest.class);
        logger.debug("run time: " + result.getRunTime());
        logger.debug("failed tests: " + result.getFailureCount());
        logger.debug("ignored tests: " + result.getIgnoreCount());
        logger.debug("success: " + result.wasSuccessful()); 
	}

}
