package stronghold.operator.sections;

import static stronghold.context.MapUtils.getReqAs;
import static stronghold.context.MapUtils.getReqString;
import static stronghold.operator.OperatorPreconditions.checkExpression;
import static stronghold.operator.OperatorPreconditions.checkIsNull;
import static stronghold.operator.OperatorPreconditions.checkUsername;

import java.util.Map;
import lombok.Data;
import stronghold.context.HashedString;
import stronghold.model.Database;
import stronghold.model.User;
import stronghold.operator.OperatorException;
import stronghold.operator.OperatorException.Type;

@Data
public class AuthOperator {

    private final Database database;

    public User register(Map<String, Object> req) throws OperatorException {
        String username = getReqString(req, "username");
        HashedString password = getReqAs(req, "password", HashedString.class);
        String nickname = getReqString(req, "nickname");
        String email = getReqString(req, "email");
        // TODO: check for username and email format
        checkExpression(username.matches("[A-Za-z0-9_]+"), Type.INVALID_USERNAME);
        checkExpression(email.matches("[A-Za-z0-9_.]+@[A-Za-z0-9_.]+\\.[A-Za-z0-9_]+"),
                Type.INVALID_EMAIL);
        checkIsNull(database.getUserFromUsername(username), Type.NOT_UNIQUE_USERNAME);
        checkIsNull(database.getUserFromEmail(email), Type.NOT_UNIQUE_EMAIL);
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
        String username = getReqString(req, "username");
        HashedString password = getReqAs(req, "password", HashedString.class);
        User user = checkUsername(database, username);
        checkExpression(password.equals(user.getPassword()), Type.INCORRECT_PASSWORD);
        return user;
    }

    public void forgotPassword(Map<String, Object> req) throws OperatorException {
        String username = getReqString(req, "username");
        HashedString newPassword = getReqAs(req, "new-password", HashedString.class);
        String securityQuestion = getReqString(req, "security-question");
        String securityAnswer = getReqString(req, "security-answer");
        User user = checkUsername(database, username);
        checkExpression(
                securityQuestion.equals(user.getSecurityQuestion()) &&
                        securityAnswer.equals(user.getSecurityAnswer()),
                Type.INCORRECT_SECURITY_QA
        );
        user.setPassword(newPassword);
    }
}
