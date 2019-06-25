package com.caique.repositories;

import com.caique.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryTransactionsRepository implements TransactionsRepository {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryTransactionsRepository.class);

    private Map<Instant, Collection<Transaction>> transactions;

    public InMemoryTransactionsRepository() {
        this.transactions = new ConcurrentHashMap();
    }

    @Override
    public void register(Transaction newTransaction) {
        Instant newTransactionInstant = newTransaction.getTimestamp();

        Collection<Transaction> existentTransactionsAtInstant =
                this.transactions.getOrDefault(newTransactionInstant, Collections.emptyList());

        Collection<Transaction> updatedTransactionsAtInstant = new ArrayList<>(existentTransactionsAtInstant);
        updatedTransactionsAtInstant.add(newTransaction);

        this.transactions.put(newTransactionInstant, updatedTransactionsAtInstant);
    }

    @Override
    public void deleteAll() {
        this.transactions = new ConcurrentHashMap<>();
    }

    @Override
    public Collection<Transaction> getAll() {
        return this.transactions.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteExpiredTransactions() {
        Instant timeBoundary = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(60)
                .toInstant();

        this.transactions
                .entrySet()
                .removeIf((record) -> record.getKey().isBefore(timeBoundary));

        logger.info("Expired transactions were removed.");
    }

}
