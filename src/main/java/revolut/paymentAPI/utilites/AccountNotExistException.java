package revolut.paymentAPI.utilites;

import org.apache.log4j.Logger;

/**
 * Created by Andrew Lapushkin on 08/05/18.
 */

public class AccountNotExistException extends RevolutPaymentException {
	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(AccountNotExistException.class);

    public AccountNotExistException(String message) {
        super(message);
        logger.error(message);
    }
}
