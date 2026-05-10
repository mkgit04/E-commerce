package com.example.adv_proj.service;

import redis.clients.jedis.Jedis;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class SessionDao {

    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    private SessionDao() {}

    public static void createSession(String sessionId, String username, int ttlSeconds) {
        try {
            try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
                jedis.setex("session:" + sessionId, ttlSeconds, username);
            }
        } catch (Exception e) {
            Logger.getLogger(SessionDao.class.getName()).log(Level.WARNING, "Failed to create Redis session, continuing without Redis", e);
        }
    }

    public static String getSessionUser(String sessionId) {
        try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            return jedis.get("session:" + sessionId);
        }
    }

    public static void deleteSession(String sessionId) {
        try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            jedis.del("session:" + sessionId);
        }
    }
}
