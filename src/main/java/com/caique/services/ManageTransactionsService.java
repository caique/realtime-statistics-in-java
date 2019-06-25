package com.caique.services;

import com.caique.domain.Transaction;
import com.caique.repositories.TransactionsRepository;
import org.springframework.stereotype.Service;

@Service
public class ManageTransactionsService {

    private TransactionsRepository transactionsRepository;
    private CalculateStatisticsService calculateStatisticsService;

    public ManageTransactionsService(TransactionsRepository transactionsRepository,
                                     CalculateStatisticsService calculateStatisticsService) {

        this.transactionsRepository = transactionsRepository;
        this.calculateStatisticsService = calculateStatisticsService;
    }

    public Boolean register(Transaction transaction) {
        if (transaction.isWithinTimeBoundary()) {
            this.transactionsRepository.register(transaction);
            this.calculateStatisticsService.refreshStatistics();

            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public void deleteAll() {
        this.transactionsRepository.deleteAll();
        this.calculateStatisticsService.refreshStatistics();
    }

}
