package com.caique.repositories;

import com.caique.domain.Transaction;
import org.junit.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryTransactionsRepositoryTest {

    @Test
    public void transactionsOlderThanLastMinuteMustNotBeReturned() {
        InMemoryTransactionsRepository repository = new InMemoryTransactionsRepository();

        Transaction recentTransaction = createTransactionAgedIn(0);
        Transaction expiredTransaction = createTransactionAgedIn(61);;

        repository.register(recentTransaction);
        repository.register(expiredTransaction);

        repository.deleteExpiredTransactions();

        Collection<Transaction> transactions = repository.getAll();

        assertThat(transactions).contains(recentTransaction);
        assertThat(transactions).doesNotContain(expiredTransaction);
    }

    private Transaction createTransactionAgedIn(Integer seconds) {
        String amount = "12.33433";
        String timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(seconds)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        return new Transaction(amount, timestamp);
    }

}
