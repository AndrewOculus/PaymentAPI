package revolut.paymentAPI.models;

import java.sql.SQLException;

/**
 * Created by Andrew Lapushkin on 08/05/18.
 */
public class TransactionFactory {
	public Transaction createTransaction(int fromId, int toId, double count){
		return new Transaction(fromId, toId, count);
	}
	public static Transaction getTransactionById( int tranId ) throws SQLException {
        return new Transaction(tranId);
    }
}
