package com.caique.component.testutils;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionRequestTestBuilder {

    private String amount;
    private String timestamp;

    public TransactionRequestTestBuilder withAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public TransactionRequestTestBuilder withTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public TransactionRequestTestBuilder withTimestampAgedIn(Integer seconds) {
        this.timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .minusSeconds(seconds)
                .format(DateTimeFormatter.ISO_DATE_TIME);

        return this;
    }

    public String build() throws JSONException {
        return new JSONObject()
                .put("amount", this.amount)
                .put("timestamp", this.timestamp)
                .toString();
    }

    public static String buildBadRequest() throws JSONException {
        return new JSONObject()
                .put("invalidfield", "invalidvalue")
                .toString();
    }

}
