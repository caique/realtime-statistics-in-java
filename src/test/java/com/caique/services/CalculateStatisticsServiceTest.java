package com.caique.services;

import com.caique.domain.Statistics;
import com.caique.domain.Transaction;
import com.caique.repositories.InMemoryTransactionsRepository;
import com.caique.repositories.TransactionsRepository;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class CalculateStatisticsServiceTest {

    private CalculateStatisticsService calculateStatisticsService;
    private TransactionsRepository transactionsRepository;

    @Before
    public void setUp() {
        this.transactionsRepository = spy(new InMemoryTransactionsRepository());
        this.calculateStatisticsService = new CalculateStatisticsService(transactionsRepository);
    }

    @Test
    public void callsTransactionsRepositoryToGetAllTransactions() {
        this.calculateStatisticsService.refreshStatistics();

        verify(transactionsRepository).getAll();
    }

    @Test
    public void retrieveMostRecentStatistics() {
        registerTransactionAgedIn(0);
        registerTransactionAgedIn(0);
        registerTransactionAgedIn(0);

        this.calculateStatisticsService.refreshStatistics();

        Statistics statistics = this.calculateStatisticsService.retrieveMostRecentStatistics();

        assertThat(statistics.getSum()).isEqualByComparingTo(BigDecimal.valueOf(30));
        assertThat(statistics.getAverage()).isEqualByComparingTo(BigDecimal.valueOf(10));
        assertThat(statistics.getMaximum()).isEqualByComparingTo(BigDecimal.valueOf(10));
        assertThat(statistics.getMinimum()).isEqualByComparingTo(BigDecimal.valueOf(10));
        assertThat(statistics.getCount()).isEqualTo(3L);
    }

    @Test
    public void expiredTransactionsMustNotBeConsideredOnStatistics() {
        registerTransactionAgedIn(0);
        registerTransactionAgedIn(61);

        this.calculateStatisticsService.refreshStatistics();

        Statistics statistics = this.calculateStatisticsService.retrieveMostRecentStatistics();

        assertThat(statistics.getSum()).isEqualByComparingTo(BigDecimal.valueOf(10));
        assertThat(statistics.getAverage()).isEqualByComparingTo(BigDecimal.valueOf(10));
        assertThat(statistics.getMaximum()).isEqualByComparingTo(BigDecimal.valueOf(10));
        assertThat(statistics.getMinimum()).isEqualByComparingTo(BigDecimal.valueOf(10));
        assertThat(statistics.getCount()).isEqualTo(1L);
    }

    private void registerTransactionAgedIn(Integer seconds) {
        String amount = "10.00";
        String timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(seconds)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        Transaction transaction = new Transaction(amount, timestamp);

        this.transactionsRepository.register(transaction);
    }

}
