package org.max;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public final class Transaction {
    private static final long ONE_MIN_IN_MILLIS = 60000;
    private final double amount;
    private final long timestamp;
    private final boolean heartBeat;

    @JsonCreator
    public Transaction(
            @JsonProperty("amount") double amount,
            @JsonProperty("timestamp") long timestamp) {
        this(amount, timestamp, false);
    }

    private Transaction(double amount, long timestamp, boolean heartBeat) {
        this.amount = amount;
        this.timestamp = timestamp;
        this.heartBeat = heartBeat;
    }

    public static Transaction heartBeat() {
        return new Transaction(
                0,
                System.currentTimeMillis(),
                true
        );
    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getTimestampSeconds() {
        return Instant.ofEpochMilli(timestamp).getEpochSecond();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", timestamp=" + timestamp +
                ", heartBeat=" + heartBeat +
                '}';
    }

    public boolean isInvalid() {
        long offset = System.currentTimeMillis() - timestamp;
        return offset > ONE_MIN_IN_MILLIS || offset < 0;
    }

    public boolean isNotHeartBeat() {
        return !heartBeat;
    }

}
