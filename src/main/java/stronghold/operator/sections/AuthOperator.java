package stronghold.operator.sections;

import static stronghold.operator.OperatorPreconditions.checkExpression;
import static stronghold.operator.OperatorPreconditions.checkNotNullCastable;
import static stronghold.operator.OperatorPreconditions.checkNotNullString;

import java.util.Map;
import java.util.Optional;
import lombok.Data;
import stronghold.context.HashedString;
import stronghold.model.Database;
import stronghold.model.User;
import stronghold.operator.OperatorException;
import stronghold.operator.OperatorException.Type;

@Data
public class AuthOperator {

    private final Database database;

    public Optional<User> getUserFromUsername(String username) {
        return database.getUsers().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public User getUserFromUsernameOrThrow(String username) throws OperatorException {
        return getUserFromUsername(username)
                .orElseThrow(() -> new OperatorException(Type.USER_NOT_FOUND));
    }

    public Optional<User> getUserFromEmail(String email) {
        return database.getUsers().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public Optional<User> findUser(Map<String, Object> req) {
        String username = checkNotNullString(req.get("username"));
        return getUserFromUsername(username);
    }

    public User register(Map<String, Object> req) throws OperatorException {
        String username = checkNotNullString(req.get("username"));
        HashedString password = checkNotNullCastable(req.get("password"), HashedString.class);
        String nickname = checkNotNullString(req.get("nickname"));
        String email = checkNotNullString(req.get("email"));
        // TODO: check for username and email format
        checkExpression(username.matches("[A-Za-z0-9_]+"), Type.INVALID_USERNAME);
        checkExpression(email.matches("[A-Za-z0-9_.]+@[A-Za-z0-9_.]+\\.[A-Za-z0-9_]+"),
                Type.INVALID_EMAIL);
        // check for unique username and email
        checkExpression(getUserFromUsername(username).isEmpty(), Type.NOT_UNIQUE_USERNAME);
        checkExpression(getUserFromEmail(email).isEmpty(), Type.NOT_UNIQUE_EMAIL);
        User user = User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .email(email)
                .build();
        database.getUsers().add(user);
        return user;
    }

    public User login(Map<String, Object> req) throws OperatorException {
        String username = checkNotNullString(req.get("username"));
        HashedString password = checkNotNullCastable(req.get("password"), HashedString.class);
        // check user exists
        User user = getUserFromUsernameOrThrow(username);
        // check password matches
        checkExpression(password.equals(user.getPassword()), Type.INCORRECT_PASSWORD);
        return user;
    }

    public void forgotPassword(Map<String, Object> req) throws OperatorException {
        String username = checkNotNullString(req.get("username"));
        HashedString newPassword = checkNotNullCastable(req.get("newPassword"), HashedString.class);
        String securityQuestion = checkNotNullString(req.get("securityQuestion"));
        String securityAnswer = checkNotNullString(req.get("securityAnswer"));
        // check user exists
        User user = getUserFromUsernameOrThrow(username);
        // check security question and answer matches
        checkExpression(
                securityQuestion.equals(user.getSecurityQuestion()) &&
                        securityAnswer.equals(user.getSecurityAnswer()),
                Type.INCORRECT_SECURITY_QA
        );
        user.setPassword(newPassword);
    }
}
