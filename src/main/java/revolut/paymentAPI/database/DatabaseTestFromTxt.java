package revolut.paymentAPI.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Created by Andrew Lapushkin on 08/05/18.
 */
public class DatabaseTestFromTxt{
	public static void createDatabaseTestFromTxt() throws SQLException {

	    File file = new File("testDataBase.txt");

	    try {

	        Scanner sc = new Scanner(file);

	        while (sc.hasNext()) {
	            int id = sc.nextInt();
	            double balance = sc.nextDouble();
	            	Database.insertIntoAccounts(id, balance);
	        }
	        sc.close();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	}
}
