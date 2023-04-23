package stronghold.operator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OperatorException extends Exception {
    public static enum Type {
        NOT_UNIQUE_USERNAME,
        NOT_UNIQUE_EMAIL,
        USER_NOT_FOUND,
        INCORRECT_PASSWORD,
        INCORRECT_SECURITY_QA
    }

    private final Type type;
}
