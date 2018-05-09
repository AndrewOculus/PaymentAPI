package revolut.paymentAPI.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Scanner;

import org.apache.log4j.Logger;

/**
 * Created by Andrew Lapushkin on 08/05/18.
 */
public class DatabaseTestFromTxt{
	
    private final static Logger logger = Logger.getLogger(DatabaseTestFromTxt.class);
	
	public static void createDatabaseTestFromTxt() throws SQLException {

	    File file = new File("testDataBase.txt");

	    try {

	        Scanner sc = new Scanner(file);

	        while (sc.hasNext()) {
	            int id = sc.nextInt();
	            double balance = sc.nextDouble();
	            logger.debug(String.format("add id: %d , balance: %f",id , balance) );
	            	Database.insertIntoAccounts(id, balance);
	        }
	        sc.close();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	}
}
