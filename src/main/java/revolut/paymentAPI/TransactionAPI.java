package revolut.paymentAPI;

import java.sql.SQLException;

import javax.security.auth.login.AccountNotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import revolut.paymentAPI.models.PerformTransaction;
import revolut.paymentAPI.models.Transaction;
import revolut.paymentAPI.models.TransactionFactory;

/**
 * Created by Andrew Lapushkin on 08/05/18.
 */

@Path("revolut")
public class TransactionAPI {

	private static PerformTransaction performTransaction = new PerformTransaction();
	private static TransactionFactory transactionFactory = new TransactionFactory();

    private final static Logger logger = Logger.getLogger(PerformTransaction.class);

	@Path("transaction")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String post(String str) throws AccountNotFoundException, SQLException {
		
		System.out.println(str);
		JsonElement jelement = new JsonParser().parse(str);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    
	    int from = Integer.valueOf(jobject.get("from").getAsString());
	    int to = Integer.valueOf(jobject.get("to").getAsString());
	    double count = Double.valueOf(jobject.get("count").getAsString());
	    
		Transaction transaction = transactionFactory.createTransaction(from, to, count);
		String commit = performTransaction.commitTransaction(transaction);	
        logger.debug(commit);
		
        return transaction.toString();
    }
	
}


