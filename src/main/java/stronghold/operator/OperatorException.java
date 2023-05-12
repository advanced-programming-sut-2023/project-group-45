package stronghold.operator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OperatorException extends Exception {

    private final Type type;

    @Override
    public String getMessage() {
        return toString();
    }

    public enum Type {
        NOT_UNIQUE_USERNAME,
        NOT_UNIQUE_EMAIL,
        USER_NOT_FOUND,
        INCORRECT_PASSWORD,
        INCORRECT_SECURITY_QA,
        INVALID_USERNAME,
        INVALID_EMAIL,
        MAP_NOT_FOUND,
        INVALID_GAME_PARAMETERS,
        UNIT_NOT_FOUND,
        BUILDING_NOT_FOUND,
        INVALID_POSITION,
        NOT_ENOUGH_RESOURCE,
        NOT_ENOUGH_GOLD,
    }
}
