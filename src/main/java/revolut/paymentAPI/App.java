package revolut.paymentAPI;

import java.sql.SQLException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import revolut.paymentAPI.database.Database;
import revolut.paymentAPI.database.DatabaseTestFromTxt;

/**
 * Created by Andrew Lapushkin on 08/05/18.
 */
public class App 
{
	private static Server jettyServer;
	
    public static void main( String[] args ) throws Exception
    {
    		
    		Database.initDb();
    		DatabaseTestFromTxt.createDatabaseTestFromTxt();

    		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
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
    }
    public static void close() throws SQLException
    {
		Database.closeConnection();
    		jettyServer.destroy();
    }
}
