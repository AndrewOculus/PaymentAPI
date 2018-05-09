package revolut.paymentAPI.models;

import java.sql.SQLException;

import revolut.paymentAPI.utilites.AccountNotExistException;

/**
 * Created by Andrew Lapushkin on 08/05/18.
 */
public class AccountFactory {
    public Account getAccount(int id) throws SQLException{
    		Account account = new Account(id);
    		if(account.isExist()){
        	 	return account;
        }
        else {
        		throw new AccountNotExistException(String.format("Account %d not found in the database", id));
        }
    	}

}
