package org.max.statistics.model;

import org.junit.Assert;
import org.junit.Test;

public class StatisticsTest {

    @Test
    public void add() {
        Statistics given = new Statistics(10, 10, 10, 1);
        Transaction t = new Transaction(5, 100);
        Statistics actual = given.add(t);
        Statistics expected = new Statistics(15, 10, 5, 2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addToEmpty() {
        Statistics given = Statistics.EMPTY;
        Transaction t = new Transaction(5, 100);
        Statistics actual = given.add(t);
        Statistics expected = new Statistics(5, 5, 5, 1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void from() {
        Transaction t = new Transaction(10, 100);
        Statistics actual = Statistics.from(t);
        Statistics expected = new Statistics(10, 10, 10, 1);
        Assert.assertEquals(expected, actual);
    }
}