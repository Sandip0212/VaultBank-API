package com.vaultbank.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY =
            "VaultBankSecretKeyForJwtAuthentication2026VerySecureKey";

    private static final long ACCESS_TOKEN_EXPIRATION =
            1000 * 60 * 60; // 1 hour

    private static final long REFRESH_TOKEN_EXPIRATION =
            1000L * 60 * 60 * 24 * 7; // 7 days

    private static final String TOKEN_TYPE_CLAIM =
            "tokenType";

    private static final String ACCESS_TOKEN =
            "ACCESS";

    private static final String REFRESH_TOKEN =
            "REFRESH";

    private final SecretKey key =
            Keys.hmacShaKeyFor(
                    SECRET_KEY.getBytes(StandardCharsets.UTF_8)
            );

    // =========================
    // Generate Access Token
    // =========================

    public String generateToken(String email) {

        Date now = new Date();

        Date expiry = new Date(
                now.getTime() + ACCESS_TOKEN_EXPIRATION
        );

        return Jwts.builder()
                .subject(email)
                .claim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    // =========================
    // Generate Refresh Token
    // =========================

    public String generateRefreshToken(String email) {

        Date now = new Date();

        Date expiry = new Date(
                now.getTime() + REFRESH_TOKEN_EXPIRATION
        );

        return Jwts.builder()
                .subject(email)
                .claim(TOKEN_TYPE_CLAIM, REFRESH_TOKEN)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    // =========================
    // Extract Email
    // =========================

    public String extractEmail(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // =========================
    // Check Refresh Token
    // =========================

    public boolean isRefreshToken(String token) {

        try {

            String tokenType = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get(
                            TOKEN_TYPE_CLAIM,
                            String.class
                    );

            return REFRESH_TOKEN.equals(tokenType);

        } catch (Exception exception) {

            return false;
        }
    }
}