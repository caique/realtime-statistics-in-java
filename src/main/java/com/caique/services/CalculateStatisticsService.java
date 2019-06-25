package com.caique.services;

import com.caique.domain.Statistics;
import com.caique.domain.Transaction;
import com.caique.domain.statistics.Average;
import com.caique.repositories.TransactionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;

@Service
public class CalculateStatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(CalculateStatisticsService.class);
    private static final int REFRESH_STATISTICS_RATE_IN_MILLIS = 10;

    private TransactionsRepository transactionsRepository;
    private Statistics mostRecentStatistics;

    public CalculateStatisticsService(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
        this.mostRecentStatistics = Statistics.EMPTY;
    }

    public Statistics retrieveMostRecentStatistics() {
        return this.mostRecentStatistics;
    }

    @Scheduled(fixedRate = REFRESH_STATISTICS_RATE_IN_MILLIS)
    public void refreshStatistics() {
        this.transactionsRepository.deleteExpiredTransactions();

        Collection<Transaction> transactions = this.transactionsRepository.getAll();

        Collection<BigDecimal> amounts = transactions.stream().map(Transaction::getAmount).collect(Collectors.toList());

        BigDecimal sum = amounts.stream().reduce(ZERO, BigDecimal::add);
        BigDecimal maximum = amounts.stream().reduce(ZERO, BigDecimal::max);
        BigDecimal minimum = amounts.stream().reduce(BigDecimal::min).orElse(ZERO);
        Long count = amounts.stream().count();
        Average average = new Average(sum, count);

        this.mostRecentStatistics = new Statistics(sum, average, maximum, minimum, count);

        logger.info("Statistics were updated! Current values are: " + this.mostRecentStatistics);
    }

}
