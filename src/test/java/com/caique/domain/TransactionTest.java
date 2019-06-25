package com.caique.domain;

import com.caique.exceptions.MissingTransactionDataException;
import com.caique.exceptions.UnprocessableTransactionException;
import org.junit.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class TransactionTest {

    @Test
    public void throwsMissingTransactionDataExceptionWhenAmountIsMissing() {
        String amount = null;
        String timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(59)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        Throwable throwable = catchThrowable(() -> new Transaction(amount, timestamp));

        assertThat(throwable).isInstanceOf(MissingTransactionDataException.class);
    }

    @Test
    public void throwsMissingTransactionDataExceptionWhenTimestampIsMissing() {
        String amount = "12.3433";
        String timestamp = null;

        Throwable throwable = catchThrowable(() -> new Transaction(amount, timestamp));

        assertThat(throwable).isInstanceOf(MissingTransactionDataException.class);
    }

    @Test
    public void throwsUnprocessableTransactionExceptionWhenAmountIsANonNumericValue() {
        String amount = "nonnumericvalue";
        String timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(59)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        Throwable throwable = catchThrowable(() -> new Transaction(amount, timestamp));

        assertThat(throwable).isInstanceOf(UnprocessableTransactionException.class);
    }

    @Test
    public void throwsUnprocessableTransactionExceptionWhenAmountIsBlank() {
        String amount = "";
        String timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(59)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        Throwable throwable = catchThrowable(() -> new Transaction(amount, timestamp));

        assertThat(throwable).isInstanceOf(UnprocessableTransactionException.class);
    }

    @Test
    public void throwsUnprocessableTransactionExceptionWhenTimestampIsNotParseable() {
        String amount = "12.3433";
        String timestamp = "nontemporalvalue";

        Throwable throwable = catchThrowable(() -> new Transaction(amount, timestamp));

        assertThat(throwable).isInstanceOf(UnprocessableTransactionException.class);
    }

    @Test
    public void throwsUnprocessableTransactionExceptionWhenTimestampIsBlank() {
        String amount = "12.3433";
        String timestamp = "";

        Throwable throwable = catchThrowable(() -> new Transaction(amount, timestamp));

        assertThat(throwable).isInstanceOf(UnprocessableTransactionException.class);
    }

    @Test
    public void throwsUnprocessableTransactionExceptionWhenTimestampIsInTheFuture() {
        String amount = "12.3433";
        String timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .plusMonths(1)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        Throwable throwable = catchThrowable(() -> new Transaction(amount, timestamp));

        assertThat(throwable).isInstanceOf(UnprocessableTransactionException.class);
    }

    @Test
    public void isWithinTimeBoundaryWhenTimestampIsNotBeforeThan60SecAgo() {
        String amount = "12.33433";
        String timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(59)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        Transaction transaction = new Transaction(amount, timestamp);

        assertThat(transaction.isWithinTimeBoundary()).isTrue();
    }

    @Test
    public void isNotWithinTimeBoundaryWhenTimestampIsBeforeThan60SecAgo() {
        String amount = "12.33433";
        String timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(61)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        Transaction transaction = new Transaction(amount, timestamp);

        assertThat(transaction.isWithinTimeBoundary()).isFalse();
    }

    @Test
    public void returnTimestampWhenTheTransactionOccured() {
        String amount = "12.33433";
        ZonedDateTime timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(61);

        String timestampAsString = timestamp.format(DateTimeFormatter.ISO_DATE_TIME);

        Transaction transaction = new Transaction(amount, timestampAsString);

        assertThat(transaction.getTimestamp()).isEqualTo(timestamp.toInstant());
    }

}
