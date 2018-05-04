package org.max;

import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private BlockingQueue<Transaction> input = new LinkedBlockingQueue<>();
    private BlockingQueue<Statistics> minute = new ArrayBlockingQueue<>(60);

    @Override
    public void add(Transaction t) {
        input.add(t);
    }

    @Override
    public Statistics getForLast60Sec() {
        return snap(minute);
    }

    private static Statistics snap(BlockingQueue<Statistics> snapshot) {
        return snapshot.stream()
                .reduce(Statistics::combine)
                .orElseThrow(() -> new IllegalStateException("Empty 1 min statistics snapshot"));
    }
}
