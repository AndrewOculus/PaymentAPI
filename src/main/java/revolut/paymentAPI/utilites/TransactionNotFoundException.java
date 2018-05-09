package revolut.paymentAPI.utilites;

import org.apache.log4j.Logger;

/**
 * Created by Andrew Lapushkin on 08/05/18.
 */
public class TransactionNotFoundException extends RevolutPaymentException {
	private static final long serialVersionUID = 1L;
    private final static Logger logger = Logger.getLogger(AccountNotExistException.class);

    public TransactionNotFoundException(String message) {
        super(message);
        logger.error(message);
    }
}