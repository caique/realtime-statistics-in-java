package com.caique.controllers.responses;

import com.caique.domain.Statistics;

public class StatisticsResponseBody {

    private Statistics statistics;

    public StatisticsResponseBody(Statistics statistics) {
        this.statistics = statistics;
    }

    public String getSum() {
        return String.valueOf(statistics.getSum());
    }

    public String getAvg() {
        return String.valueOf(statistics.getAverage());
    }

    public String getMax() {
        return String.valueOf(statistics.getMaximum());
    }

    public String getMin() {
        return String.valueOf(statistics.getMinimum());
    }

    public Long getCount() {
        return statistics.getCount();
    }

}
