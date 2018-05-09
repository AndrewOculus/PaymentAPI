package revolut.paymentAPI.models;

import java.sql.SQLException;

import javax.security.auth.login.AccountNotFoundException;

import org.apache.log4j.Logger;

import revolut.paymentAPI.database.Database;

/**
 * Created by Andrew Lapushkin on 08/05/18.
 */

public class PerformTransaction {
	private final static Logger logger = Logger.getLogger(PerformTransaction.class);
    private String answer;
    private final AccountFactory accountFactory;

    public PerformTransaction(){
        accountFactory = new AccountFactory();
    }

    private void setAnswer(String answer){
        this.answer = answer;
        logger.debug(answer);
    }

    public String commitTransaction(Transaction transaction) throws SQLException, AccountNotFoundException {
    		Account from = null;
    		Account to = null;
    		try {
    			from = accountFactory.getAccount(transaction.getFromId());
    		}
    		catch (SQLException e) {
    			transaction.setStatus(TransactionStatus.CANCELED);
            Database.insertIntoTransactions(transaction);
			setAnswer(String.format("Account %d not found!\n", transaction.getTransactionId()));
			return answer;
		}
    		try {
    			to = accountFactory.getAccount(transaction.getToId());
    		}
    		catch (SQLException e) {
    			transaction.setStatus(TransactionStatus.CANCELED);
            Database.insertIntoTransactions(transaction);
			setAnswer(String.format("Account %d not found!\n", transaction.getTransactionId()));
			return answer;
		}

        
        if(from.isExist() && to.isExist()){ 	
        		if(from.getId() == to.getId()){
        			setAnswer(String.format("You can not send money to yourself , %d !\n", transaction.getTransactionId()));
        			transaction.setStatus(TransactionStatus.CANCELED);
                Database.insertIntoTransactions(transaction);
        			return answer;
        		}
            if(from.getBalance() >= transaction.getCount()) {
                from.changeBalance(-transaction.getCount());
                to.changeBalance(transaction.getCount());
    				transaction.setStatus(TransactionStatus.COMMITED);
                Database.insertIntoTransactions(transaction);
                setAnswer(String.format("Successfully submitted!\n", transaction.getTransactionId()));
            }
            else{
            		setAnswer(String.format("Not enough money on the account %d !\n", transaction.getFromId()));
        			transaction.setStatus(TransactionStatus.CANCELED);
                Database.insertIntoTransactions(transaction);
            }
        }
        else {
        		setAnswer(String.format("One of the users %d and %d does not exist.\n", transaction.getFromId(),transaction.getToId()));
    			transaction.setStatus(TransactionStatus.CANCELED);
            Database.insertIntoTransactions(transaction);
        }

        return answer;
    }
}
