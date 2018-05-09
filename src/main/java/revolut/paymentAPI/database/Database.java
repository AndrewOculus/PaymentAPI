package revolut.paymentAPI.database;

import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.log4j.Logger;

import revolut.paymentAPI.models.Transaction;

import java.sql.*;

/**
 * Created by Andrew Lapushkin on 08/05/18.
 */
public class Database {
    private final static Logger logger = Logger.getLogger(Database.class);
    private static Connection conn;
    private static boolean initialised = false;
    
    private static final String createAccountTableInq =
            "CREATE TABLE ACCOUNTS"+
                    "( UserId INTEGER NOT NULL AUTO_INCREMENT," +
                    "Balance DOUBLE NOT NULL DEFAULT 0," +
                    "CONSTRAINT chk_balance CHECK (Balance >= 0)," +
                    "PRIMARY KEY (UserId))";
    private static final String createTransactionTableInq =
    		"CREATE TABLE TRANSACTIONS"+
    	            "( TransactionId INTEGER NOT NULL," +
    	            "Status INTEGER NOT NULL DEFAULT 0," +
    	            "FromId INTEGER NOT NULL," +
    	            "ToId INTEGER NOT NULL," +
    	            "Count DOUBLE NOT NULL," +
    	            "CONSTRAINT chk_count CHECK (Count > 0)," +
    	            "PRIMARY KEY (TransactionId) )";
    private static final String insertAccountInq =
            "INSERT INTO ACCOUNTS VALUES (?,?)";
    private static final String insertTransactionExpr =
            "INSERT INTO TRANSACTIONS VALUES (?,?,?,?,?)";


    public static void initDb() throws SQLException {
        if(!initialised){
            try {
                Class.forName("org.h2.Driver");
                conn = DriverManager.getConnection("jdbc:h2:mem:InMemoryTest");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                logger.error("Driver not Found. Unrecoverable error - exiting");
                System.exit(0);
            }
            createAccountsTable();
            createTransactionsTable();
            initialised = true;
        } else {
            logger.debug("Database already initialised");
        }
    }
    
    public static PreparedStatement getStatement(String query) throws SQLException {
        return conn.prepareStatement(query);
    }
    
    public static void closeConnection() throws SQLException {
        conn.close();
    }

    public static ResultSet selectStatement(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = null;
        try {
            logger.debug(String.format("Executing statement %s\n", statement));
            resultSet = statement.executeQuery();
            logger.debug(String.format("Retrieved result %s\n", resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(String.format("Statement %s was not executed\n", statement));
        }
        return resultSet;
    }

    public static boolean updateStatement(PreparedStatement statement) throws SQLException {
        try {
            logger.debug(String.format("Executing statement %s\n", statement));
            statement.executeUpdate();
            logger.debug(String.format("Executing %s completed!\n", statement));
        } catch (SQLException e) {
            e.printStackTrace();
            logger.debug(String.format("Statement %s was not executed\n", statement));
            return false;
        }
        return true;
    }

    private static void createAccountsTable() throws SQLException {
        PreparedStatement statement = conn.prepareStatement( createAccountTableInq);
        updateStatement(statement);
    }

    private static void createTransactionsTable() throws SQLException {
        PreparedStatement statement = conn.prepareStatement(createTransactionTableInq);
        updateStatement(statement);
    }

    public static void insertIntoAccounts(int userId, double balance) throws SQLException {
    	System.out.println(balance);
        PreparedStatement statement = conn.prepareStatement(insertAccountInq);
        statement.setInt(1, userId);
        statement.setDouble(2, balance);
        updateStatement(statement);
        statement.close();
        conn.commit();
    }

    public static void insertIntoTransactions(Transaction transaction) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(insertTransactionExpr);
        statement.setInt(1, transaction.getTransactionId());
        statement.setInt(2, transaction.getStatus().getValue());
        statement.setInt(3, transaction.getFromId());
        statement.setInt(4, transaction.getToId());
        statement.setDouble(5, transaction.getCount());
        updateStatement(statement);
        statement.close();
        conn.commit();
    }
}
