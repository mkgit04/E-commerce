package com.example.adv_proj.service;

import redis.clients.jedis.Jedis;

public final class RequestRateLimiter {

    private static final int MAX_REQUESTS = 5;
    private static final int WINDOW_SECONDS = 10;

    private RequestRateLimiter() {
    }

    public static boolean allow(String user, String endpoint) {
        if (user == null || user.isBlank()) {
            return false;
        }
        if (endpoint == null || endpoint.isBlank()) {
            return false;
        }

        try (Jedis jedis = new Jedis("localhost", 6379)) {
            String key = keyFor(user, endpoint);
            long currentCount = jedis.incr(key);
            if (currentCount == 1) {
                jedis.expire(key, WINDOW_SECONDS);
            }
            return currentCount <= MAX_REQUESTS;
        }
    }

    private static String keyFor(String user, String endpoint) {
        return "rate:" + user + ":" + endpoint;
    }
}