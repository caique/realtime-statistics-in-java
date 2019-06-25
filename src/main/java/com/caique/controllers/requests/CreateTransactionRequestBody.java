package com.caique.controllers.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.caique.domain.Transaction;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTransactionRequestBody {

    private final String amount;
    private final String timestamp;

    public CreateTransactionRequestBody(@JsonProperty("amount") String amount,
                                        @JsonProperty("timestamp") String timestamp) {

        this.amount = amount;
        this.timestamp = timestamp;
    }

    public Transaction toTransaction() {
        return new Transaction(amount, timestamp);
    }

}
