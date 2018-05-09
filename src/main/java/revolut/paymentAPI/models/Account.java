package revolut.paymentAPI.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.security.auth.login.AccountNotFoundException;


import revolut.paymentAPI.database.Database;

/**
 * Created by Andrew Lapushkin on 08/05/18.
 */
public class Account {
	
	private int id;
    private final String accountExistsExpr="SELECT COUNT(*) FROM ACCOUNTS WHERE UserId=?";
    private final String accountBalanceExpr="SELECT Balance FROM ACCOUNTS WHERE UserId=?";
    private final String updateBalanceExpr="UPDATE ACCOUNTS SET Balance=? WHERE UserId=?";
	
	public Account(int id) {
		this.id = id;
    }

    public boolean isExist() throws SQLException{
    		PreparedStatement statement = Database.getStatement(accountExistsExpr);
        statement.setInt(1, id);
        ResultSet meta = Database.selectStatement(statement);
        boolean exists = false;
        if(meta.next()) {
            exists = meta.getInt(1) == 1;
        }
        statement.close();
        return exists;
    }

    public double getBalance() throws SQLException, AccountNotFoundException {
    	
    		PreparedStatement statement = Database.getStatement(accountBalanceExpr);
        statement.setInt(1, id);
        ResultSet meta = Database.selectStatement(statement);
        double balanceResult;
        if(meta.next())
            balanceResult = meta.getDouble(1);
        else
            throw new AccountNotFoundException(String.format("Balance for user %d was not retrieved!", id));
        statement.close();
        return balanceResult;
    }

    public void changeBalance(double change) throws AccountNotFoundException, SQLException{
    		double balance = getBalance() + change;
        PreparedStatement statement = Database.getStatement(updateBalanceExpr);
        statement.setDouble(1, balance);
        statement.setInt(2, id);
        Database.updateStatement(statement);
    }
    
    public int getId(){
		return id;
    }
}