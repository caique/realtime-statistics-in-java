package com.caique.services;

import com.caique.domain.Transaction;
import com.caique.repositories.InMemoryTransactionsRepository;
import com.caique.repositories.TransactionsRepository;
import org.junit.Before;
import org.junit.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ManageTransactionsServiceTest {

    private ManageTransactionsService manageTransactionsService;

    private TransactionsRepository transactionsRepository;
    private CalculateStatisticsService calculateStatisticsService;

    @Before
    public void setUp() {
        this.transactionsRepository = spy(new InMemoryTransactionsRepository());
        this.calculateStatisticsService = mock(CalculateStatisticsService.class);

        this.manageTransactionsService = new ManageTransactionsService(transactionsRepository, calculateStatisticsService);
    }

    @Test
    public void returnsTrueWhenTransactionIsWithinTimeBoundary() {
        String amount = "12.33433";
        String timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(59)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        Transaction transaction = new Transaction(amount, timestamp);

        Boolean isTransactionRegistered = this.manageTransactionsService.register(transaction);

        assertThat(isTransactionRegistered).isTrue();
    }

    @Test
    public void callsManageTransactionsServiceToRegisterWhenTransactionIsAccepted() {
        String amount = "12.33433";
        String timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(59)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        Transaction transaction = new Transaction(amount, timestamp);

        this.manageTransactionsService.register(transaction);

        verify(transactionsRepository, times(1)).register(transaction);
    }

    @Test
    public void callsCalculateStatisticsServiceToRefreshStatisticsWhenTransactionIsAccepted() {
        String amount = "12.33433";
        String timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(59)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        Transaction transaction = new Transaction(amount, timestamp);

        this.manageTransactionsService.register(transaction);

        verify(calculateStatisticsService, times(1)).refreshStatistics();
    }

    @Test
    public void returnsFalseWhenTransactionIsNotWithinTimeBoundary() {
        String amount = "12.33433";
        String timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(61)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        Transaction transaction = new Transaction(amount, timestamp);

        Boolean isTransactionRegistered = this.manageTransactionsService.register(transaction);

        assertThat(isTransactionRegistered).isFalse();
    }

    @Test
    public void doNotCallManageTransactionsServiceWhenTransactionsIsRejected() {
        String amount = "12.33433";
        String timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(61)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        Transaction transaction = new Transaction(amount, timestamp);

        this.manageTransactionsService.register(transaction);

        verify(transactionsRepository, never()).register(transaction);
    }

    @Test
    public void doNotCallsCalculateStatisticsServiceToRefreshStatisticsWhenTransactionIsRejected() {
        String amount = "12.33433";
        String timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(61)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        Transaction transaction = new Transaction(amount, timestamp);

        this.manageTransactionsService.register(transaction);

        verify(calculateStatisticsService, never()).refreshStatistics();
    }

    @Test
    public void callsTransactionsRepositoryToDeleteAllTransactions() {
        this.manageTransactionsService.deleteAll();

        verify(transactionsRepository, times(1)).deleteAll();
    }

    @Test
    public void callsCalculateStatisticsServiceToRefreshStatisticsWhenDeletingAllTransactions() {
        this.manageTransactionsService.deleteAll();

        verify(calculateStatisticsService, times(1)).refreshStatistics();
    }

}
