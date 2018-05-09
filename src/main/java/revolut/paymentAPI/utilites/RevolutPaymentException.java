package revolut.paymentAPI.utilites;

import java.sql.SQLException;

/**
 * Created by Andrew Lapushkin on 08/05/18.
 */

public abstract class RevolutPaymentException extends SQLException {
	private static final long serialVersionUID = 1L;
    protected final String message;

    protected RevolutPaymentException(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}