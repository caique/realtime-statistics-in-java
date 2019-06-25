package com.caique.domain;

import com.caique.exceptions.MissingTransactionDataException;
import com.caique.exceptions.UnprocessableTransactionException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

public class Transaction {

    private final BigDecimal amount;
    private final Instant timestamp;

    public Transaction(String amount, String timestamp) {
        if (amount == null || timestamp == null) throw new MissingTransactionDataException();

        try {
            this.amount = new BigDecimal(amount);
            this.timestamp = Instant.parse(timestamp);
        } catch (NumberFormatException | DateTimeParseException exception) {
            throw new UnprocessableTransactionException();
        }

        Instant now = ZonedDateTime.now(ZoneOffset.UTC).toInstant();

        if (this.timestamp.isAfter(now)) throw new UnprocessableTransactionException();
    }

    public Boolean isWithinTimeBoundary() {
        Instant timeBoundary = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(60)
                .toInstant();

        if (this.timestamp.isBefore(timeBoundary)) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

}
