package com.caique.controllers.advices;

import com.caique.exceptions.MissingTransactionDataException;
import com.caique.exceptions.UnprocessableTransactionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestControllerAdvice
public class TransactionsControllerAdvice {

    @ExceptionHandler(MissingTransactionDataException.class)
    public ResponseEntity handleMissingTransactionDataException(MissingTransactionDataException exception) {
        return new ResponseEntity(BAD_REQUEST);
    }

    @ExceptionHandler(UnprocessableTransactionException.class)
    public ResponseEntity handleUnprocessableTransactionException(UnprocessableTransactionException exception) {
        return new ResponseEntity(UNPROCESSABLE_ENTITY);
    }

}
