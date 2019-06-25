package com.caique.controllers.responses;

import com.caique.domain.Statistics;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StatisticsResponseBodyTest {

    @Test
    public void returnsTheSameStatisticsValues() {
        Statistics baseStatistics = Statistics.EMPTY;
        StatisticsResponseBody statisticsResponseBody = new StatisticsResponseBody(baseStatistics);

        assertThat(statisticsResponseBody.getSum()).isEqualTo(baseStatistics.getSum().toString());
        assertThat(statisticsResponseBody.getAvg()).isEqualTo(baseStatistics.getAverage().toString());
        assertThat(statisticsResponseBody.getMax()).isEqualTo(baseStatistics.getMaximum().toString());
        assertThat(statisticsResponseBody.getMin()).isEqualTo(baseStatistics.getMinimum().toString());
        assertThat(statisticsResponseBody.getCount()).isEqualTo(baseStatistics.getCount());
    }

}
