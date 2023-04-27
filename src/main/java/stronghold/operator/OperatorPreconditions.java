package stronghold.operator;

import stronghold.model.Database;
import stronghold.model.User;
import stronghold.operator.OperatorException.Type;

public class OperatorPreconditions {

    public static void checkExpression(boolean expression, Type exceptionType)
            throws OperatorException {
        if (!expression) {
            throw new OperatorException(exceptionType);
        }
    }

    public static void checkIsNull(Object obj, Type exceptionType) throws OperatorException {
        checkExpression(obj == null, exceptionType);
    }

    public static <T> T checkNotNull(T t, Type exceptionType) throws OperatorException {
        checkExpression(t != null, exceptionType);
        return t;
    }

    public static User checkUserExists(Database database, String username)
            throws OperatorException {
        return checkNotNull(database.getUserFromUsername(username), Type.USER_NOT_FOUND);
    }

    public static void checkUsernameFormat(String username) throws OperatorException {
        checkExpression(username.matches("[A-Za-z0-9_]+"), Type.INVALID_USERNAME);
    }

    public static void checkEmailFormat(String email) throws OperatorException {
        checkExpression(email.matches("[A-Za-z0-9_.]+@[A-Za-z0-9_.]+\\.[A-Za-z0-9_]+"),
                Type.INVALID_EMAIL);
    }
}
