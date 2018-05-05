package org.max;

import java.util.Objects;

public final class Statistics {

    public static final Statistics EMPTY =
            new Statistics(0, 0, 0, 0);

    private final double sum;
    private final double max;
    private final double min;
    private final long count;

    public Statistics(double sum, double max, double min, long count) {
        this.sum = sum;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public double getSum() {
        return sum;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public long getCount() {
        return count;
    }

    public double getAvg() {
        return sum / count;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "sum=" + sum +
                ", max=" + max +
                ", min=" + min +
                ", count=" + count +
                '}';
    }

    public static Statistics combine(Statistics s1, Statistics s2) {
        assert s1.notEmpty() && s2.notEmpty();
        return new Statistics(
                s1.sum + s2.sum,
                max(s1.max, s2.max),
                min(s1.min, s2.min),
                s1.count + s2.count
        );
    }

    public Statistics add(Transaction t) {
        return notEmpty() ?
            new Statistics(
                this.sum + t.getAmount(),
                max(this.max, t.getAmount()),
                min(this.min, t.getAmount()),
                this.count + 1
            )
        : from(t);
    }

    public boolean notEmpty() {
        return count != 0;
    }

    public static Statistics from(Transaction t) {
        return new Statistics(
                t.getAmount(),
                t.getAmount(),
                t.getAmount(),
                1
        );
    }

    public static double max(double d1, double d2) {
        return d1 > d2 ? d1 : d2;
    }

    public static double min(double d1, double d2) {
        return d1 < d2 ? d1 : d2;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statistics that = (Statistics) o;
        return Double.compare(that.sum, sum) == 0 &&
                Double.compare(that.max, max) == 0 &&
                Double.compare(that.min, min) == 0 &&
                count == that.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sum, max, min, count);
    }
}
