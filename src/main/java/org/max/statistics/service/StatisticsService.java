package org.max.statistics.service;

import org.max.statistics.model.Statistics;
import org.max.statistics.model.Transaction;

public interface StatisticsService {
    void add(Transaction t);
    Statistics getForLast60Sec();
}
