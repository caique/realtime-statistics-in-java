package com.caique.controllers;

import com.caique.controllers.requests.CreateTransactionRequestBody;
import com.caique.exceptions.MissingTransactionDataException;
import com.caique.exceptions.UnprocessableTransactionException;
import com.caique.services.ManageTransactionsService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

public class TransactionsControllerTest {

    private TransactionsController controller;
    private ManageTransactionsService manageTransactionsService;

    @Before
    public void setUp() {
        this.manageTransactionsService = mock(ManageTransactionsService.class);
        this.controller = new TransactionsController(manageTransactionsService);
    }

    @Test
    public void throwsMissingTransactionDataExceptionWhenAmountIsNull() {
        String amount = null;
        String timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(59)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        CreateTransactionRequestBody requestBody = new CreateTransactionRequestBody(amount, timestamp);

        Throwable throwable = catchThrowable(() -> this.controller.createTransaction(requestBody));

        assertThat(throwable).isInstanceOf(MissingTransactionDataException.class);
    }

    @Test
    public void throwsMissingTransactionDataExceptionWhenTimestampIsNull() {
        String amount = "12.33433";
        String timestamp = null;

        CreateTransactionRequestBody requestBody = new CreateTransactionRequestBody(amount, timestamp);

        Throwable throwable = catchThrowable(() -> this.controller.createTransaction(requestBody));

        assertThat(throwable).isInstanceOf(MissingTransactionDataException.class);
    }

    @Test
    public void throwsUnprocessableTransactionExceptionWhenAmountIsNotProcessable() {
        String amount = "";
        String timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(59)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        CreateTransactionRequestBody requestBody = new CreateTransactionRequestBody(amount, timestamp);

        Throwable throwable = catchThrowable(() -> this.controller.createTransaction(requestBody));

        assertThat(throwable).isInstanceOf(UnprocessableTransactionException.class);
    }

    @Test
    public void throwsUnprocessableTransactionExceptionWhenTimestampIsNotProcessable() {
        String amount = "12.33433";
        String timestamp = "";

        CreateTransactionRequestBody requestBody = new CreateTransactionRequestBody(amount, timestamp);

        Throwable throwable = catchThrowable(() -> this.controller.createTransaction(requestBody));

        assertThat(throwable).isInstanceOf(UnprocessableTransactionException.class);
    }

    @Test
    public void throwsUnprocessableTransactionExceptionWhenTimestampIsInTheFuture() {
        String amount = "12.33433";
        String timestampInTheFuture = ZonedDateTime
                .now(ZoneOffset.UTC)
                .plusMonths(1)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        CreateTransactionRequestBody requestBody = new CreateTransactionRequestBody(amount, timestampInTheFuture);

        Throwable throwable = catchThrowable(() -> this.controller.createTransaction(requestBody));

        assertThat(throwable).isInstanceOf(UnprocessableTransactionException.class);
    }

    @Test
    public void rejectTransactionOlderThan60Sec() {
        String amount = "12.33433";
        String timestampOlderThan60Sec = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(61)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        CreateTransactionRequestBody request = new CreateTransactionRequestBody(amount, timestampOlderThan60Sec);

        ResponseEntity response = this.controller.createTransaction(request);

        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    public void acceptTransactionNewerThan60Sec() {
        when(manageTransactionsService.register(any())).thenReturn(Boolean.TRUE);

        String amount = "12.33433";
        String timestampNewerThan60Sec = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(59)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        CreateTransactionRequestBody request = new CreateTransactionRequestBody(amount, timestampNewerThan60Sec);

        ResponseEntity response = this.controller.createTransaction(request);

        assertThat(response.getStatusCode()).isEqualTo(CREATED);
    }

    @Test
    public void callsManageTransactionsServiceToRegisterAcceptedTransaction() {
        String amount = "12.33433";
        String timestampNewerThan60Sec = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(59)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        CreateTransactionRequestBody request = new CreateTransactionRequestBody(amount, timestampNewerThan60Sec);

        this.controller.createTransaction(request);

        verify(manageTransactionsService, times(1)).register(any());
    }

    @Test
    public void callsManageTransactionsServiceEvenForRejectedTransaction() {
        String amount = "12.33433";
        String timestampNewerThan60Sec = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(61)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        CreateTransactionRequestBody request = new CreateTransactionRequestBody(amount, timestampNewerThan60Sec);

        this.controller.createTransaction(request);

        verify(manageTransactionsService, times(1)).register(any());
    }

    @Test
    public void deleteAllTransactions() {
        ResponseEntity response = this.controller.deleteAllTransactions();

        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    public void callsManageTransactionsServiceToDeleteAllransactions() {
        ResponseEntity response = this.controller.deleteAllTransactions();

        verify(manageTransactionsService, times(1)).deleteAll();
    }

}
