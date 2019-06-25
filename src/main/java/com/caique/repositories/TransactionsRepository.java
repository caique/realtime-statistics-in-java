package com.caique.repositories;

import com.caique.domain.Transaction;

import java.util.Collection;

public interface TransactionsRepository {

    void register(Transaction transaction);

    void deleteAll();

    void deleteExpiredTransactions();

    Collection<Transaction> getAll();

}
