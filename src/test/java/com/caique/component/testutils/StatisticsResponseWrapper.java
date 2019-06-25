package com.caique.component.testutils;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

public class StatisticsResponseWrapper {

    private final BigDecimal sum;
    private final BigDecimal avg;
    private final BigDecimal max;
    private final BigDecimal min;
    private final Long count;

    public StatisticsResponseWrapper(MockHttpServletResponse response) throws UnsupportedEncodingException, JSONException {
        JSONObject responseAsJSON = new JSONObject(response.getContentAsString());

        this.sum = new BigDecimal(responseAsJSON.get("sum").toString());
        this.avg = new BigDecimal(responseAsJSON.get("avg").toString());
        this.max = new BigDecimal(responseAsJSON.get("max").toString());
        this.min = new BigDecimal(responseAsJSON.get("min").toString());
        this.count = Long.valueOf(responseAsJSON.get("count").toString());
    }

    public BigDecimal getSum() {
        return sum;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public BigDecimal getMax() {
        return max;
    }

    public BigDecimal getMin() {
        return min;
    }

    public Long getCount() {
        return count;
    }

}
