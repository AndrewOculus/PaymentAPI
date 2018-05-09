package revolut.paymentAPI.models;

/**
 * Created by Andrew Lapushkin on 08/05/18.
 */
public enum TransactionStatus {
	NONE(0),
	CANCELED(1),
    COMMITED(2);

    private final int value;

    private TransactionStatus(int value) {
        this.value = value;
    }

    public int getValue(){ return value; }

}
