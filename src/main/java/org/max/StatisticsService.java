package org.max;

public interface StatisticsService {
    void add(Transaction t);
    Statistics getForLast60Sec();
}
