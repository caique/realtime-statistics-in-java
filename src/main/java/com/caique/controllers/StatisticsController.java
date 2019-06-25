package com.caique.controllers;

import com.caique.controllers.responses.StatisticsResponseBody;
import com.caique.domain.Statistics;
import com.caique.services.CalculateStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class StatisticsController {

    private CalculateStatisticsService calculateStatisticsService;

    public StatisticsController(CalculateStatisticsService calculateStatisticsService) {
        this.calculateStatisticsService = calculateStatisticsService;
    }

    @RequestMapping(path = "/statistics", method = RequestMethod.GET)
    public ResponseEntity retrieveStatistics() {
        Statistics statistics = this.calculateStatisticsService.retrieveMostRecentStatistics();

        StatisticsResponseBody statisticsResponseBody = new StatisticsResponseBody(statistics);

        return new ResponseEntity(statisticsResponseBody, OK);
    }

}
