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

import revolut.paymentAPI.models.Account;
import revolut.paymentAPI.models.AccountFactory;
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
	private static AccountFactory accountFactory = new AccountFactory();

    private final static Logger logger = Logger.getLogger(PerformTransaction.class);

	@Path("create-transaction")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String transaction(String str) throws AccountNotFoundException, SQLException {
        logger.debug(str);

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
	
	@Path("get-transaction")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String getTransactionById(String str) throws AccountNotFoundException, SQLException {
		
		JsonElement jelement = new JsonParser().parse(str);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    
	    int id = Integer.valueOf(jobject.get("id").getAsString());
	    Transaction transaction = null;
	    try {
	    		 transaction = transactionFactory.getTransactionById(id);
	    }
	    catch (SQLException e) {
	    		logger.error(String.format("Transaction id %d not found!", id));
			return String.format(" {\"id\": \"%d\" , \"from\": \"-1\", \"to\": \"-1\" , \"count\": \"-1\" , \"status\": \"-1\"}" , id);
		}
        logger.debug(transaction.toString());
		
        return transaction.toString();
    }
	
	@Path("get-account-balance")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String getAccountBalance(String str) throws AccountNotFoundException, SQLException {
		
		JsonElement jelement = new JsonParser().parse(str);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    
	    int id = Integer.valueOf(jobject.get("id").getAsString());
	    
	    Account account = null;
	    try {
			account = accountFactory.getAccount(id);
	    }
	    catch (SQLException e) {
	    		logger.error(String.format("Account id %d not found!", id));
			return String.format(" {\"id\": \"%d\" , \"balance\":\"-1\"}" , id);
	    }
	    String ans = String.format(" {\"id\": \"%d\" , \"balance\": \"%f\"}", id , account.getBalance());
        logger.debug(ans);
	    return ans;		
    }
}


