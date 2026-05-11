package com.example.adv_proj.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordUtilTest {

    @Test
    void hashPasswordProducesVerifiableHash() {
        String hash = PasswordUtil.hashPassword("Secret123!");

        assertNotEquals("Secret123!", hash);
        assertTrue(PasswordUtil.verifyPassword("Secret123!", hash));
        assertFalse(PasswordUtil.verifyPassword("WrongPassword", hash));
    }

    @Test
    void verifyPasswordStillAcceptsLegacyPlaintext() {
        assertTrue(PasswordUtil.verifyPassword("legacyPass", "legacyPass"));
        assertFalse(PasswordUtil.verifyPassword("legacyPass", "differentPass"));
    }
}