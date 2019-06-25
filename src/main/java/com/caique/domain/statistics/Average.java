package com.caique.domain.statistics;

import java.math.BigDecimal;

import static com.caique.domain.Statistics.NUMBER_OF_DECIMAL_PLACES;
import static com.caique.domain.Statistics.ROUNDING_STRATEGY;

public class Average {

    public static final Average ZERO = new Average(BigDecimal.ZERO, 0L);

    private BigDecimal value;

    public Average(BigDecimal sum, Long count) {
        if (count != null && count > 0) {
            this.value = sum.divide(BigDecimal.valueOf(count), NUMBER_OF_DECIMAL_PLACES, ROUNDING_STRATEGY);
        } else {
            this.value = BigDecimal.ZERO;
        }
    }

    public BigDecimal getValue() {
        return this.value;
    }

}
