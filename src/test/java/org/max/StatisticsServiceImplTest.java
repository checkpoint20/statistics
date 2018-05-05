package org.max;

import org.junit.Assert;
import org.junit.Test;

public class StatisticsServiceImplTest {

    @Test
    public void push() {
        Integer[] given = {1, 2, 3, 4, 5};
        StatisticsServiceImpl.push(given, 6);

        Integer[] expected = {2, 3, 4, 5, 6};
        Assert.assertArrayEquals(expected, given);
    }

    @Test
    public void collect() {
        Statistics s1 = new Statistics(10, 10, 10, 1);
        Statistics s2 = new Statistics(15, 12.5, 2.5, 2);
        Statistics s3 = new Statistics(15, 7.5, 7.5, 2);

        Statistics buf[] = {s1, s2, s3};
        Statistics actual = StatisticsServiceImpl.collect(buf);
        Statistics expected = new Statistics(40, 12.5, 2.5, 5);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected.getAvg(), actual.getAvg(), 0.000000001);
    }

    @Test
    public void collect_whenEmpty() {
        Statistics s1 = Statistics.EMPTY;
        Statistics s2 = Statistics.EMPTY;
        Statistics s3 = Statistics.EMPTY;

        Statistics buf[] = {s1, s2, s3};
        Statistics actual = StatisticsServiceImpl.collect(buf);
        Statistics expected = new Statistics(0, 0, 0, 0);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected.getAvg(), actual.getAvg(), 0.000000001);
    }
}