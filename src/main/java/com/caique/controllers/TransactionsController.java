package com.caique.controllers;

import com.caique.controllers.requests.CreateTransactionRequestBody;
import com.caique.domain.Transaction;
import com.caique.services.ManageTransactionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
public class TransactionsController {

    private ManageTransactionsService manageTransactionsService;

    public TransactionsController(ManageTransactionsService manageTransactionsService) {
        this.manageTransactionsService = manageTransactionsService;
    }

    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity createTransaction(@RequestBody CreateTransactionRequestBody transactionRequest) {
        Transaction transaction = transactionRequest.toTransaction();

        Boolean isTransactionAccepted = this.manageTransactionsService.register(transaction);

        if (isTransactionAccepted) {
            return new ResponseEntity(CREATED);
        }

        return new ResponseEntity(NO_CONTENT);
    }

    @RequestMapping(path = "/transactions", method = RequestMethod.DELETE)
    public ResponseEntity deleteAllTransactions() {
        this.manageTransactionsService.deleteAll();

        return new ResponseEntity(NO_CONTENT);
    }

}
