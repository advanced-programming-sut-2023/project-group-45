package org.example.stronghold.operator.sections;

import static org.example.stronghold.context.MapUtils.getReqAs;
import static org.example.stronghold.context.MapUtils.getReqString;
import static org.example.stronghold.operator.OperatorPreconditions.checkEmailFormat;
import static org.example.stronghold.operator.OperatorPreconditions.checkIsNull;
import static org.example.stronghold.operator.OperatorPreconditions.checkTrue;
import static org.example.stronghold.operator.OperatorPreconditions.checkUserExists;
import static org.example.stronghold.operator.OperatorPreconditions.checkUsernameFormat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.example.stronghold.context.HashedString;
import org.example.stronghold.model.Database;
import org.example.stronghold.model.User;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.OperatorException.Type;

@Data
public class AuthOperator {

    private final Database database;

    public User findUser(Map<String, Object> req) throws OperatorException {
        String username = getReqString(req, "username");
        return checkUserExists(database, username);
    }

    public User register(Map<String, Object> req) throws OperatorException {
        String username = getReqString(req, "username");
        System.out.println(username);
        HashedString password = getReqAs(req, "password", HashedString.class);
        String nickname = getReqString(req, "nickname");
        String email = getReqString(req, "email");
        checkUsernameFormat(username);
        checkEmailFormat(email);
        checkIsNull(database.getUserFromUsername(username), Type.NOT_UNIQUE_USERNAME);
        checkIsNull(database.getUserFromEmail(email), Type.NOT_UNIQUE_EMAIL);
        User user = User.builder()
            .username(username)
            .password(password)
            .nickname(nickname)
            .email(email)
            .score(0)
            .build();
        try {
            new File("assets/avatars").mkdir();
            Files.copy(new File("assets/others/default_avatar.jpg").toPath(), user.getAvatar());
        } catch (IOException e) {
            e.printStackTrace();
            throw new OperatorException(Type.IO_EXCEPTION);
        }
        database.addUser(user);
        return user;
    }

    private User updateStayLoggedInUser(Map<String, Object> req, User user) {
        if (req.containsKey("stay-logged-in")) {
            database.setStayLoggedInUser(
                getReqAs(req, "stay-logged-in", Boolean.class) ? user : null);
        }
        return user;
    }

    public User login(Map<String, Object> req) throws OperatorException {
        String username = getReqString(req, "username");
        if (database.isStayLoggedInUsername(username)) {
            return updateStayLoggedInUser(req, database.getStayLoggedInUser());
        }
        HashedString password = getReqAs(req, "password", HashedString.class);
        User user = checkUserExists(database, username);
        checkTrue(password.equals(user.getPassword()), Type.INCORRECT_PASSWORD);
        return updateStayLoggedInUser(req, user);
    }

    public void forgotPassword(Map<String, Object> req) throws OperatorException {
        String username = getReqString(req, "username");
        HashedString newPassword = getReqAs(req, "new-password", HashedString.class);
        String securityQuestion = getReqString(req, "security-question");
        String securityAnswer = getReqString(req, "security-answer");
        User user = checkUserExists(database, username);
        checkTrue(
            securityQuestion.equals(user.getSecurityQuestion()) &&
                securityAnswer.equals(user.getSecurityAnswer()),
            Type.INCORRECT_SECURITY_QA
        );
        user.setPassword(newPassword);
    }

    public List<User> getUsers(Map<String, Object> req) {
        return database.getUsers();
    }
}
