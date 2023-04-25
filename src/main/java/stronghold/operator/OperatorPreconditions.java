package stronghold.operator;

import static com.google.common.base.Preconditions.checkNotNull;

import stronghold.operator.OperatorException.Type;

public class OperatorPreconditions {

    /*
     * checkCastable throws RuntimeException. If it used in body of a function, say foo,
     * caller of foo should have checked for sanity of arguments. Other exceptions such as format
     * of specific strings, must be an OperatorException not a RuntimeException.
     */
    public static <T> T checkCastable(Object obj, Class<T> clazz) {
        if (clazz.isInstance(obj)) {
            return clazz.cast(obj);
        } else {
            throw new IllegalArgumentException("Object is not castable to " + clazz.getName());
        }
    }

    public static <T> T checkNotNullCastable(Object obj, Class<T> clazz) {
        return checkNotNull(checkCastable(obj, clazz));
    }

    public static String checkNotNullString(Object obj) {
        return checkNotNullCastable(obj, String.class);
    }

    public static void checkExpression(boolean expression, Type exceptionType)
            throws OperatorException {
        if (!expression) {
            throw new OperatorException(exceptionType);
        }
    }
}
