package com.caique.domain;

import com.caique.domain.statistics.Average;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;

public class Statistics {

    public static final Statistics EMPTY = new Statistics(ZERO, Average.ZERO, ZERO, ZERO, 0L);

    public static final int NUMBER_OF_DECIMAL_PLACES = 2;
    public static final int ROUNDING_STRATEGY = ROUND_HALF_UP;

    private final BigDecimal sum;
    private final BigDecimal average;
    private final BigDecimal maximum;
    private final BigDecimal minimum;
    private final Long count;

    public Statistics(BigDecimal sum, Average average, BigDecimal maximum, BigDecimal minimum, Long count) {
        this.sum = sum.setScale(NUMBER_OF_DECIMAL_PLACES, ROUNDING_STRATEGY);
        this.average = average.getValue().setScale(NUMBER_OF_DECIMAL_PLACES, ROUNDING_STRATEGY);
        this.maximum = maximum.setScale(NUMBER_OF_DECIMAL_PLACES, ROUNDING_STRATEGY);
        this.minimum = minimum.setScale(NUMBER_OF_DECIMAL_PLACES, ROUNDING_STRATEGY);
        this.count = count;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public BigDecimal getAverage() {
        return average;
    }

    public BigDecimal getMaximum() {
        return maximum;
    }

    public BigDecimal getMinimum() {
        return minimum;
    }

    public Long getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "{" +
                "sum = " + sum +
                ", average = " + average +
                ", maximum = " + maximum +
                ", minimum = " + minimum +
                ", count = " + count +
                '}';
    }

}
