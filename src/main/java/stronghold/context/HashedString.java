package stronghold.context;

import java.io.Serializable;
import lombok.Data;

@Data
public class HashedString implements Serializable {

    private final HashMode mode;
    private final String hashedContent;

    private HashedString(HashMode mode, String hashedContent) {
        this.mode = mode;
        this.hashedContent = hashedContent;
    }

    public static HashedString fromPlain(String plainContent) {
        return new HashedString(HashMode.PLAIN, plainContent);
    }

    public HashedString withMode(HashMode newMode) {
        if (mode.equals(newMode)) {
            return this;
        }
        if (!mode.equals(HashMode.PLAIN)) {
            return null;
        }
        String hashedContent = HashMode.toStringFunction.get(newMode)
                .hashString(this.hashedContent);
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
        if (mode.equals(that.mode)) {
            return hashedContent.equals(that.hashedContent);
        }
        if (mode.equals(HashMode.PLAIN)) {
            return withMode(that.mode).equals(that);
        }
        if (that.mode.equals(HashMode.PLAIN)) {
            return that.withMode(mode).equals(this);
        }
        return false; // no way to compare different hash modes
    }
}
