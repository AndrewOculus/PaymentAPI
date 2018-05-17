package revolut.paymentAPI;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Test;

import revolut.paymentAPI.database.Database;
import revolut.paymentAPI.database.DatabaseTestFromTxt;
import revolut.paymentAPI.models.Account;
import revolut.paymentAPI.models.AccountFactory;

/**
 * Created by Andrew Lapushkin on 09/05/18.
 */
public class TransferTest {

    private final static Logger logger = Logger.getLogger(TransferTest.class);
	
	private static final String createTransactionUrl = "http://localhost:8080/revolut/create-transaction";
	/*
	private static final String getTransactionUrl = "http://localhost:8080/revolut/get-transaction";
	private static final String getAccountUrl = "http://localhost:8080/revolut/get-account-balance";

	private static final String transactionContent = "{ \"from\": 1,\"to\": \"0\",\"count\": \"123.45\" }";
	private static final String firstTransaction = "{ \"id\": \"0\"}";
	private static final String wrongTransaction = "{ \"id\": \"90\"}";
	private static final String firstAccount = "{ \"id\": \"0\"}";
	private static final String wrongAccount = "{ \"id\": \"101\"}";
	*/

    
	public static void init() throws Exception {
    		Database.initDb();
		DatabaseTestFromTxt.createDatabaseTestFromTxt();

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

    		Server jettyServer = new Server(8080);
    		jettyServer.setHandler(context);

    		ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
    		jerseyServlet.setInitOrder(0);

    		jerseyServlet.setInitParameter(
            "jersey.config.server.provider.classnames",
            TransactionAPI.class.getCanonicalName());

    		try {
    			jettyServer.start();
    			jettyServer.join();
    		} finally {
    			jettyServer.destroy();
    		}		
		/*
		//create transaction
		ContentResponse response = request(createTransactionUrl ,transactionContent );
		logger.debug(String.format(" Response: %S ", response.getContentAsString()));
		
		//get transaction with id
		response = request(getTransactionUrl ,firstTransaction );
		logger.debug(String.format(" Response: %S ", response.getContentAsString()));
		
		//get transaction with id
		response = request(getTransactionUrl ,wrongTransaction );
		logger.debug(String.format(" Response: %S ", response.getContentAsString()));

		//get first account balance
		response = request(getAccountUrl ,firstAccount );
		logger.debug(String.format(" Response: %S ", response.getContentAsString()));

		//wrong account id
		response = request(getAccountUrl ,wrongAccount );
		logger.debug(String.format(" Response: %S ", response.getContentAsString()));
		*/

	}
	
	public void close() throws SQLException {
		
	}
	private ContentResponse request(String url ,String jsonContent) throws Exception {
		 HttpClient httpClient = new HttpClient();
		 httpClient.start();
		 Request request = httpClient.POST(url);
		 request.content(new StringContentProvider(jsonContent), "application/json");
	     ContentResponse response = request.send();

		 httpClient.stop();
		 return response;
	}
	private void transaction(int id1, int id2 ,double count) throws Exception {
		String jsonContent = String.format(Locale.ROOT,"{ \"from\": \"%d\",\"to\": \"%d\",\"count\": \"%.5f\" }" , id1 , id2 , count);
		logger.debug(jsonContent);
		request(createTransactionUrl, jsonContent);
	}
	
	@Test
    public void testSimpleTransfer() throws Exception {
        logger.debug("Starting test transfer");
        double transactionAmount = 10.0;
		AccountFactory accountFactory = new AccountFactory();
        Account user1 = accountFactory.getAccount(1);
        Account user2 = accountFactory.getAccount(2);

        double user1Balance = user1.getBalance();
        double user2Balance = user2.getBalance();
        
        transaction(user1.getId(), user2.getId(), transactionAmount);
        assertTrue( user1.getBalance() == user1Balance - transactionAmount);
        assertTrue( user2.getBalance() == user2Balance + transactionAmount);
    }
}
