package context;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public record HashedString(String mode, String content) implements Serializable {
    public boolean comparableTo(final HashedString oth) {
        return superiorMode(mode, oth.mode()) != null;
    }

    public boolean sameAs(final HashedString oth) {
        if (!comparableTo(oth))
            return false;
        String newMode = superiorMode(mode, oth.mode());
        return withMode(newMode).content().equals(oth.withMode(newMode).content());
    }

    public HashedString withMode(final String newMode) {
        if (mode.equals(newMode))
            return this;
        if (newMode.equals("plain") || !mode.equals("plain"))
            return null;
        try {
            return new HashedString(newMode, hashAs(content, newMode));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); // no such hash function
        }
        // never reached
    }

    private static String superiorMode(String mode1, String mode2) {
        if (mode1.equals("plain"))
            return mode2;
        if (mode2.equals("plain"))
            return mode1;
        if (mode1.equals(mode2))
            return mode1;
        return null;
    }

    private static String hashAs(String content, String hashFunction)
            throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(hashFunction);
        byte[] encodedHash = digest.digest(
                content.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            // bytes are signed in java. promoting byte directly to int keep its sign
            // 0xff & b promotes byte to int, ignoring the sign of b
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
