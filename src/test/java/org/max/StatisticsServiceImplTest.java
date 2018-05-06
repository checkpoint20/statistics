package org.max;

import org.junit.Test;

import static org.junit.Assert.*;

public class StatisticsServiceImplTest {

    @Test
    public void push() {
        Integer[] given = {1, 2, 3, 4, 5};
        StatisticsServiceImpl.push(given, 6);

        Integer[] expected = {2, 3, 4, 5, 6};
        assertArrayEquals(expected, given);
    }

    @Test
    public void collect() {
        Statistics s1 = new Statistics(10, 10, 10, 1);
        Statistics s2 = new Statistics(15, 12.5, 2.5, 2);
        Statistics s3 = new Statistics(15, 7.5, 7.5, 2);

        Statistics buf[] = {s1, s2, s3};
        Statistics actual = StatisticsServiceImpl.collect(buf);
        Statistics expected = new Statistics(40, 12.5, 2.5, 5);
        assertEquals(expected, actual);
        assertEquals(expected.getAvg(), actual.getAvg(), 0.000000001);
    }

    @Test
    public void collect_whenEmpty() {
        Statistics s1 = Statistics.EMPTY;
        Statistics s2 = Statistics.EMPTY;
        Statistics s3 = Statistics.EMPTY;

        Statistics buf[] = {s1, s2, s3};
        Statistics actual = StatisticsServiceImpl.collect(buf);
        Statistics expected = new Statistics(0, 0, 0, 0);
        assertEquals(expected, actual);
        assertEquals(expected.getAvg(), actual.getAvg(), 0.000000001);
    }

    @Test
    public void snap() {
        Statistics s1 = new Statistics(10, 10, 10, 1);
        Statistics s2 = new Statistics(15, 12.5, 2.5, 2);
        Statistics s3 = new Statistics(15, 7.5, 7.5, 2);

        Statistics provided[] = {s1, s2, s3};
        Statistics actual[] = StatisticsServiceImpl.snap(provided);
        assertArrayEquals(provided, actual);
        assertNotEquals(provided.hashCode(), actual.hashCode());
    }

    @Test
    public void applyTransaction() {
        Statistics s1 = Statistics.EMPTY;
        Statistics s2 = Statistics.EMPTY;
        Statistics s3 = Statistics.EMPTY;

        Statistics buf[] = {s1, s2, s3};
        Transaction t1 = new Transaction(10, 99000);
        Transaction t2 = new Transaction(5,  99000);
        Transaction t3 = new Transaction(11, 98000);
        long now = 100l;
        StatisticsServiceImpl.applyTransaction(t1, now, buf);
        StatisticsServiceImpl.applyTransaction(t2, now, buf);
        StatisticsServiceImpl.applyTransaction(t3, now, buf);

        Statistics s4 = new Statistics(15, 10, 5, 2);
        Statistics s5 = new Statistics(11, 11, 11, 1);
        Statistics expected[] = {s5, s4, s3};

        assertArrayEquals(expected, buf);
    }

    @Test
    public void applyTransaction_whenHeartBeatTransaction() {
        Statistics s1 = Statistics.EMPTY;
        Statistics s2 = Statistics.EMPTY;
        Statistics s3 = Statistics.EMPTY;

        Statistics buf[] = {s1, s2, s3};
        Transaction t1 = Transaction.heartBeat();
        long now = 100l;
        try {
            StatisticsServiceImpl.applyTransaction(t1, now, buf);
            fail();
        } catch (AssertionError e) {
            // do nothing
        }
    }

    @Test
    public void shiftBuffer() {
        Statistics s1 = new Statistics(10, 10, 10, 1);
        Statistics s2 = new Statistics(15, 12.5, 2.5, 2);
        Statistics s3 = new Statistics(15, 7.5, 7.5, 2);

        Statistics buf[] = {s1, s2, s3};
        long now = 100l;
        long last = 99l;
        long actualNow = StatisticsServiceImpl.shiftBuffer(now, last, buf);
        assertEquals(now, actualNow);

        Statistics expected[] = {s2, s3, Statistics.EMPTY};
        assertArrayEquals(expected, buf);
    }
}