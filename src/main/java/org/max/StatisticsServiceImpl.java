package org.max;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final LinkedBlockingQueue<Transaction> input = new LinkedBlockingQueue<>();
    private Statistics[] minute = new Statistics[60];

    @Override
    public void add(Transaction t) {
        input.add(t);
    }

    @Override
    public Statistics getForLast60Sec() {
        return collect(minute);
    }

    static Statistics collect(Statistics[] snapshot) {
        return Arrays.stream(snapshot)
                .reduce(Statistics::combine)
                .orElseThrow(() -> new IllegalStateException("Empty 1 min statistics snapshot"));
    }

    @Scheduled
    void pollInput() throws InterruptedException {
        while (true) {
            Transaction t = input.take();

        }
    }
}
