package org.max;

public final class Statistics {
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
        return new Statistics(
                s1.sum + s2.sum,
                s1.max > s2.max ? s1.max : s2.max,
                s1.min < s2.sum ? this.min : s2.sum,
                s1.count + s2.count
        );
    }


}
