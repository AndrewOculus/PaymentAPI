package revolut.paymentAPI;

import org.apache.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;

/**
 * Created by Andrew Lapushkin on 09/05/18.
 */
public class TransferTest {

    private final static Logger logger = Logger.getLogger(TransferTest.class);
	
	private static final String createTransactionUrl = "http://localhost:8080/revolut/create-transaction";
	private static final String getTransactionUrl = "http://localhost:8080/revolut/get-transaction";
	private static final String getAccountUrl = "http://localhost:8080/revolut/get-account-balance";

	private static final String transactionContent = "{ \"from\": 1,\"to\": \"0\",\"count\": \"123.45\" }";
	private static final String firstTransaction = "{ \"id\": \"0\"}";
	private static final String wrongTransaction = "{ \"id\": \"90\"}";
	private static final String firstAccount = "{ \"id\": \"0\"}";
	private static final String wrongAccount = "{ \"id\": \"101\"}";


	public static void main(String[] args) throws Exception {
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

	}
	private static ContentResponse request(String url ,String jsonContent) throws Exception {
		 HttpClient httpClient = new HttpClient();
		 httpClient.start();
		 Request request = httpClient.POST(url);
		 request.content(new StringContentProvider(jsonContent), "application/json");
	     ContentResponse response = request.send();

		 httpClient.stop();
		 return response;
	}
}
