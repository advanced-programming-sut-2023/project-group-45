package org.example.stronghold.operator;

import org.example.stronghold.model.Database;
import org.example.stronghold.model.User;
import org.example.stronghold.operator.OperatorException.Type;

public class OperatorPreconditions {

    public static void checkTrue(boolean expression, Type exceptionType)
            throws OperatorException {
        if (!expression) {
            throw new OperatorException(exceptionType);
        }
    }

    public static void checkIsNull(Object obj, Type exceptionType) throws OperatorException {
        checkTrue(obj == null, exceptionType);
    }

    public static <T> T checkNotNull(T t, Type exceptionType) throws OperatorException {
        checkTrue(t != null, exceptionType);
        return t;
    }

    public static User checkUserExists(Database database, String username)
            throws OperatorException {
        return checkNotNull(database.getUserFromUsername(username), Type.USER_NOT_FOUND);
    }

    public static void checkUsernameFormat(String username) throws OperatorException {
        checkTrue(username.matches("[A-Za-z0-9_]+"), Type.INVALID_USERNAME);
    }

    public static void checkEmailFormat(String email) throws OperatorException {
        checkTrue(email.matches("[A-Za-z0-9_.]+@[A-Za-z0-9_.]+\\.[A-Za-z0-9_]+"),
                Type.INVALID_EMAIL);
    }
}
