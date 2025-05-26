package id.ac.ui.cs.advprog.buildingstore.salesTransaction.model;

public enum TransactionStatus {
    PENDING(0),
    PAID(1);

    private final int value;

    TransactionStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TransactionStatus fromValue(int value) {
        for (TransactionStatus status : TransactionStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid transaction status value: " + value);
    }
} 