package com.caique.controllers;

import com.caique.controllers.responses.StatisticsResponseBody;
import com.caique.domain.Statistics;
import com.caique.services.CalculateStatisticsService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

public class StatisticsControllerTest {

    private StatisticsController controller;
    private CalculateStatisticsService calculateStatisticsService;

    @Before
    public void setUp() {
        this.calculateStatisticsService = mock(CalculateStatisticsService.class);
        this.controller = new StatisticsController(calculateStatisticsService);
    }

    @Test
    public void callsCalculateStatisticsServiceWhenRetrievingStatistics() {
        ResponseEntity response = this.controller.retrieveStatistics();

        verify(calculateStatisticsService).retrieveMostRecentStatistics();
    }

    @Test
    public void returnsRetrieviedStatistics() {
        Statistics statistics = Statistics.EMPTY;
        when(calculateStatisticsService.retrieveMostRecentStatistics()).thenReturn(statistics);

        ResponseEntity response = this.controller.retrieveStatistics();

        assertThat(response.getBody()).isEqualToComparingFieldByField(new StatisticsResponseBody(statistics));
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }
}
