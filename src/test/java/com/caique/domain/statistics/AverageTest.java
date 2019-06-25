package com.caique.domain.statistics;

import org.junit.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

public class AverageTest {

    @Test
    public void returnsZeroWhenCountIsZero() {
        BigDecimal sum = TEN;
        Long count = 0L;

        assertThat(new Average(sum, count).getValue()).isEqualTo(ZERO);
    }

    @Test
    public void returnsZeroWhenCountIsNull() {
        BigDecimal sum = TEN;
        Long count = null;

        assertThat(new Average(sum, count).getValue()).isEqualTo(ZERO);
    }

    @Test
    public void returnsProperValueWhenCountIsNotZeroOrNull() {
        BigDecimal sum = TEN.setScale(2, ROUND_HALF_UP);
        Long count = 1L;

        assertThat(new Average(sum, count).getValue()).isEqualTo(sum);
    }

}
