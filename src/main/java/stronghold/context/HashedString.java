package stronghold.context;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public record HashedString(HashMode mode, String content) implements Serializable {
    public HashedString withMode(HashMode newMode) {
        if (mode.equals(newMode))
            return this;
        if (!mode.equals(HashMode.PLAIN))
            return null;
        String hashedContent = HashMode.toStringFunction.get(newMode).hashString(content);
        return new HashedString(newMode, hashedContent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HashedString that = (HashedString) o;
        if (mode.equals(that.mode))
            return content.equals(that.content);
        if (mode.equals(HashMode.PLAIN))
            return withMode(that.mode).equals(that);
        if (that.mode.equals(HashMode.PLAIN))
            return that.withMode(mode).equals(this);
        return false; // no way to compare different hash modes
    }
}
