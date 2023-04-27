package stronghold.operator.sections;

import static stronghold.context.MapUtils.getReqAs;
import static stronghold.context.MapUtils.getReqString;
import static stronghold.operator.OperatorPreconditions.checkEmailFormat;
import static stronghold.operator.OperatorPreconditions.checkExpression;
import static stronghold.operator.OperatorPreconditions.checkIsNull;
import static stronghold.operator.OperatorPreconditions.checkUsernameFormat;

import java.util.Map;
import lombok.Data;
import stronghold.context.HashedString;
import stronghold.model.Database;
import stronghold.model.User;
import stronghold.operator.OperatorException;
import stronghold.operator.OperatorException.Type;

@Data
public final class ProfileOperator {

    private final Database database;

    public void changePassword(Map<String, Object> req) throws OperatorException {
        User user = getReqAs(req, "user", User.class);
        HashedString oldPassword = getReqAs(req, "old-password", HashedString.class);
        HashedString newPassword = getReqAs(req, "new-password", HashedString.class);
        checkExpression(user.getPassword().equals(oldPassword), Type.INCORRECT_PASSWORD);
        user.setPassword(newPassword);
    }

    public void changeUsername(Map<String, Object> req) throws OperatorException {
        User user = getReqAs(req, "user", User.class);
        String newUsername = getReqString(req, "new-username");
        checkUsernameFormat(newUsername);
        checkIsNull(database.getUserFromUsername(newUsername), Type.NOT_UNIQUE_USERNAME);
        user.setUsername(newUsername);
    }

    public void changeNickname(Map<String, Object> req) {
        User user = getReqAs(req, "user", User.class);
        String newNickname = getReqString(req, "new-nickname");
        user.setNickname(newNickname);
    }

    public void changeEmail(Map<String, Object> req) throws OperatorException {
        User user = getReqAs(req, "user", User.class);
        String newEmail = getReqString(req, "new-email");
        checkEmailFormat(newEmail);
        checkIsNull(database.getUserFromEmail(newEmail), Type.NOT_UNIQUE_EMAIL);
        user.setEmail(newEmail);
    }

    public void changeSlogan(Map<String, Object> req) {
        User user = getReqAs(req, "user", User.class);
        String newSlogan = getReqString(req, "new-slogan");
        user.setSlogan(newSlogan);
    }

    public void changeSecurityQA(Map<String, Object> req) throws OperatorException {
        User user = getReqAs(req, "user", User.class);
        String newSecurityQuestion = getReqString(req, "new-security-question");
        String newSecurityAnswer = getReqString(req, "new-security-answer");
        user.setSecurityQuestion(newSecurityQuestion);
        user.setSecurityAnswer(newSecurityAnswer);
    }
}
