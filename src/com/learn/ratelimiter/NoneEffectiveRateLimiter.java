package com.learn.ratelimiter;

import java.util.LinkedList;

/**
 * The naive solution for rate limiter which potentially leads to crash JVM with out of memory error.
 */
public class NoneEffectiveRateLimiter {

    private long availableTokens;
    private final long periodMillis;

    private LinkedList<Issue> issuedTokens = new LinkedList<>();

    /**
     * Creates instance of rate limiter which provides guarantee that consumption rate will be >= tokens/periodMillis
     */
    public NoneEffectiveRateLimiter(long tokens, long periodMillis) {
        this.availableTokens = tokens;
        this.periodMillis = periodMillis;
    }

    synchronized public boolean tryConsume(int numberTokens) {
        long nowMillis = System.currentTimeMillis();
        clearObsoleteIssues(nowMillis);

        if (availableTokens < numberTokens) {
            // has no requested tokens in the bucket
            return false;
        } else {
            issuedTokens.addLast(new Issue(numberTokens, nowMillis));
            availableTokens -= numberTokens;
            return true;
        }
    }

    private void clearObsoleteIssues(long nowMillis) {
        while (!issuedTokens.isEmpty()) {
            Issue issue = issuedTokens.getFirst();
            if (nowMillis - issue.timestampMillis > periodMillis) {
                availableTokens += issue.tokens;
                issuedTokens.removeFirst();
            } else {
                return;
            }
        }
    }

    private static final class Issue {
        private final long tokens;
        private final long timestampMillis;

        private Issue(long tokens, long timestampMillis) {
            this.tokens = tokens;
            this.timestampMillis = timestampMillis;
        }
    }

    private static final class Selftest {

        public static void main(String[] args) {
            // 100 tokens per 1 second
            NoneEffectiveRateLimiter limiter = new NoneEffectiveRateLimiter(100, 1000);

            long startMillis = System.currentTimeMillis();
            long consumed = 0;
            while (System.currentTimeMillis() - startMillis < 10000) {
                if (limiter.tryConsume(1)) {
                    consumed++;
                }
            }
            System.out.println(consumed);
        }

    }

}

