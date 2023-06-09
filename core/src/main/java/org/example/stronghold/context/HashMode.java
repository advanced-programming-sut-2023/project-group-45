package org.example.stronghold.context;

import com.google.common.collect.ImmutableMap;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public enum HashMode implements Serializable {
    PLAIN,
    SHA256,
    SHA512;

    private static final HashStringFunction plainHashStringFunction = plain -> plain;
    public static final ImmutableMap<HashMode, HashStringFunction> toStringFunction
        = ImmutableMap.of(
        PLAIN, plainHashStringFunction,
        SHA256, fromHashFunction(Hashing.sha256()),
        SHA512, fromHashFunction(Hashing.sha512())
    );

    private static HashStringFunction fromHashFunction(final HashFunction function) {
        return plain -> function.hashString(plain, StandardCharsets.UTF_8).toString();
    }

    @FunctionalInterface
    public interface HashStringFunction {

        String hashString(String plain);
    }
}
