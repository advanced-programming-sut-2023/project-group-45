package org.example.stronghold.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HashedStringTest {

    private final String plainContent = "something like a password";
    // generated with sha256sum(1)
    private final String sha256Content = "8ab9ada3a67596b39484c7561d429739aa510be1122c9b5505295f13b2b8b8c0";
    // generated with sha512sum(1)
    private final String sha512Content = "805cacee595004a10f7f93e5aca6ee8c4a44e72ecd2e13b6590b266840b0aa91ffcc4ed44ce5ce984e577aa9f2507c81d2b9287efeffeba3d76e8d8bf2664063";

    HashedString plain, sha256, sha512;

    @BeforeEach
    void initPlainHashedString() {
        plain = HashedString.fromPlain(plainContent);
        sha256 = plain.withMode(HashMode.SHA256);
        sha512 = plain.withMode(HashMode.SHA512);
    }

    @Test
    void testPlainHash() {
        assertEquals(plainContent, plain.getHashedContent());
    }

    @Test
    void testSha256() {
        assertEquals(sha256Content, sha256.getHashedContent());
    }

    @Test
    void testSha512() {
        assertEquals(sha512Content, sha512.getHashedContent());
    }

    @Test
    void testReHash() {
        assertNull(sha256.withMode(HashMode.SHA512));
    }

    @Test
    void testEqualityOfHashedStrings() {
        assertEquals(plain, sha256);
        assertEquals(plain, sha512);
        assertNotEquals(sha256, sha512);
        // reverse order
        assertEquals(sha256, plain);
        assertEquals(sha512, plain);
        assertNotEquals(sha512, sha256);

        // different objects but same content
        HashedString otherPlain = HashedString.fromPlain(plainContent);
        assertEquals(otherPlain, plain);
        assertEquals(otherPlain.withMode(HashMode.SHA256), sha256);
        assertEquals(otherPlain.withMode(HashMode.SHA512), sha512);

        // content with a tweak
        HashedString plainOtherContent = HashedString.fromPlain(plainContent + "\n");
        assertNotEquals(plainOtherContent, plain);
        assertNotEquals(plainOtherContent, sha256);
        assertNotEquals(plainOtherContent, sha512);
    }
}
