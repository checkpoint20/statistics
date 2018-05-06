package org.max.statistics.controller;

import org.max.statistics.model.Statistics;
import org.max.statistics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    @Autowired
    private StatisticsService servise;

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public Statistics getForLast60Sec() {
        return servise.getForLast60Sec();
    }

}
