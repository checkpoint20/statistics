package org.max.statistics.service;

import org.max.statistics.model.Statistics;
import org.max.statistics.model.Transaction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final int SEC_60 = 60;
    public static final long ONE_SECOND = 1000L;

    private final LinkedBlockingQueue<Transaction> transactions = new LinkedBlockingQueue<>();

    // The latest 60 sec statistics snapshot.
    volatile private Statistics[] minute = new Statistics[SEC_60];

    // Buffer for update statistics for the latest 60 sec.
    private Statistics[] buffer = new Statistics[SEC_60];


    // The last processed second from epoch.
    // The last element of buffer always contains statistics for the last second.
    private long lastProcessedSec;

    public StatisticsServiceImpl() {
        Arrays.fill(minute, Statistics.EMPTY);
        Arrays.fill(buffer, Statistics.EMPTY);
        lastProcessedSec = Instant.now().getEpochSecond();
    }

    @Override
    public void add(Transaction t) {
        transactions.add(t);
    }

    @Override
    public Statistics getForLast60Sec() {
        return collect(minute);
    }

    static Statistics collect(Statistics[] buf) {
        return Arrays.stream(buf)
                .filter(Statistics::notEmpty)
                .reduce(Statistics::combine)
                .orElse(Statistics.EMPTY);
    }

    /**
     * Adding a fake transaction every second to ensure
     * that internal buffer is always actual.
     */
    @Scheduled(fixedRate = ONE_SECOND)
    void heartBeat() {
        add(Transaction.heartBeat());
    }


    /**
     * We processing input transactions in one thread.
     * 1. because of mechanical sympathy (no context switches) it's going to be very fast;
     * 2. it helps to handle concurrency properly.
     */
    @Scheduled(fixedDelay = 1)
    void pollInput() {
        while(true) {
            Transaction t;

            try {
                t = transactions.take();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }

            long now = Instant.now().getEpochSecond();
            lastProcessedSec = shiftBuffer(now, lastProcessedSec, buffer);
            if (t.isNotHeartBeat()) {
                applyTransaction(t, now, buffer);
            }

            // Using java reference assignment atomicity to substitute changed buffer.
            this.minute = snap(buffer);
        }
    }

    /**
     * Takes a snapshot of the internal buffer.
     * @param buf The buffer.
     * @return The snapshot.
     */
    static Statistics[] snap(Statistics[] buf) {
        Statistics[] internal = new Statistics[buf.length];
        System.arraycopy(buf, 0, internal, 0, buf.length);
        return internal;
    }

    /**
     * Calculates proper position of a transaction in the buffer and
     * puts it to the buffer.
     * @param t The transaction to put to the buffer.
     * @param now The current timestamp. It corresponds to the rightest element of the buffer.
     * @param buf The buffer.
     */
    static void applyTransaction(Transaction t, long now, Statistics[] buf) {
        assert t.isNotHeartBeat();
        long offset = now - t.getTimestampSeconds();
        if (offset <= buf.length - 1) {
            int i = (int) (buf.length - 1 - offset);
            buf[i] = buf[i].add(t);
        }
    }

    /**
     * Shifts slots in the buffer back to the past.
     * @param now The current timestamp in sec.
     * @param last The last processed timestamp in sec.
     * @param buf The buffer.
     * @return The current timestamp.
     */
    static long shiftBuffer(long now, long last, Statistics[] buf) {
        long offset = now - last;
        for(int i = 0; i != offset; i++) {
            push(buf, Statistics.EMPTY);
        }
        return now;
    }

    /**
     * Shifts elements of the array to the left.
     * Puts new element e to the rightest position.
     */
    static <T> void push(T[] array, T e) {
        for(int i = 0; i != array.length - 1; i++) {
            array[i] = array[i + 1];
        }
        array[array.length - 1] = e;
    }
}
