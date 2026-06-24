package com.taskflow.server.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    @Test
    void generateAndValidate() {
        JwtTokenProvider provider = new JwtTokenProvider(
                "TaskFlowSecretKeyForJWTTokenGeneration2026MustBeLongEnough", 3600000);
        String token = provider.generateToken(1L, "user@test.com");
        assertTrue(provider.validate(token));
        assertEquals(1L, provider.getUserId(token));
    }
}
