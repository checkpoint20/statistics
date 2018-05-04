package org.max;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Transaction {
    private static final long ONE_MIN_IN_MILLIS = 60000;
    private final double amount;
    private final long timestamp;

    @JsonCreator
    public Transaction(
            @JsonProperty("amount") double amount,
            @JsonProperty("timestamp") long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }

    public boolean isOlderThan60Sec() {
        return (System.currentTimeMillis() - timestamp) > ONE_MIN_IN_MILLIS;
    }
}
