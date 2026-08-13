package com.vaultbank.util;

import java.util.UUID;

public final class AccountNumberGenerator {

    private AccountNumberGenerator() {
    }

    public static String generate() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 16);
    }
}