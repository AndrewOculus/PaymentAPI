package revolut.paymentAPI.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import revolut.paymentAPI.database.Database;
import revolut.paymentAPI.utilites.TransactionNotFoundException;

/**
 * Created by Andrew Lapushkin on 08/05/18.
 */
public class Transaction {
	private int transactionId;
	private int fromId;
	private int toId;
	private TransactionStatus status;
	private double count;
	
    private final static Logger logger = Logger.getLogger(Database.class);
	private final static AtomicInteger counter = new AtomicInteger();
	
	private final String transactionInfoInq = "SELECT FromId, ToId, Count, Status FROM TRANSACTIONS WHERE TransactionId = ?";
	private final String transactionExistsInq = "SELECT COUNT(*) FROM TRANSACTIONS WHERE TransactionId = ?";
	private final String transactionStatusInq = "SELECT Status FROM TRANSACTIONS WHERE TransactionId = ?";
	
	public Transaction(int fromId, int toId, double count){
		this.setTransactionId(counter.get());
		counter.incrementAndGet();
		this.setFromId(fromId);
		this.setToId(toId);
		this.setCount(count);
		this.setStatus(TransactionStatus.NONE);
	}
	public Transaction(int tranId) throws SQLException {
        this.transactionId = tranId;
        if(isExist()){
            logger.debug(String.format("Transaction %d is valid!", transactionId));
            PreparedStatement statement = Database.getStatement(transactionInfoInq);
            statement.setInt(1, transactionId);
            ResultSet resultSet = Database.selectStatement(statement);
            if(resultSet.next()) {
            		this.setStatus(TransactionStatus.values()[resultSet.getInt(4)]);
                this.setFromId(resultSet.getInt(1));
                this.setToId(resultSet.getInt(2));
                this.setCount(resultSet.getDouble(3));
            } else {
                throw new TransactionNotFoundException(String.format("Transaction %d was just deleted", transactionId));
            }
        } else {
            throw new TransactionNotFoundException(String.format("Transaction %d not found in the database!", transactionId));
        }
    }
	
	public TransactionStatus getTransactionStatus() throws SQLException{
		PreparedStatement statement = Database.getStatement(transactionStatusInq);
        statement.setInt(1, transactionId);
        ResultSet state = Database.selectStatement(statement);
        TransactionStatus stateResult = TransactionStatus.CANCELED;
        if(state.next())
            stateResult = TransactionStatus.values()[state.getInt(1)];
        statement.close();
        return stateResult;
	}
	
	public boolean isExist() throws SQLException {
		PreparedStatement statement = Database.getStatement(transactionExistsInq);
        statement.setInt(1, transactionId);
        ResultSet meta = Database.selectStatement(statement); 
        boolean exists = false;
        if(meta.next()) {
            exists = meta.getInt(1) == 1;
        }
        statement.close();
        return exists;
	}
	
	public int getTransactionId() {
		return transactionId;
	}
	private void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public int getFromId() {
		return fromId;
	}
	private void setFromId(int fromId) {
		this.fromId = fromId;
	}
	public int getToId() {
		return toId;
	}
	private void setToId(int toId) {
		this.toId = toId;
	}
	public double getCount() {
		return count;
	}
	private void setCount(double count) {
		this.count = count;
	}
	public TransactionStatus getStatus() {
		return status;
	}
	public void setStatus(TransactionStatus status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return String.format(" {\"id\": \"%d\" , \"from\": %d , \"to\": \"%d\" , \"count\": \"%f\" , \"status\" : \"%s\" }", transactionId , fromId , toId ,count , status.toString()) ;
	}
}
