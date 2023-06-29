package org.example.stronghold.operator.sections;

import static org.example.stronghold.context.MapUtils.getReqAs;
import static org.example.stronghold.context.MapUtils.getReqString;
import static org.example.stronghold.operator.OperatorPreconditions.checkEmailFormat;
import static org.example.stronghold.operator.OperatorPreconditions.checkTrue;
import static org.example.stronghold.operator.OperatorPreconditions.checkIsNull;
import static org.example.stronghold.operator.OperatorPreconditions.checkUsernameFormat;

import java.util.Map;
import lombok.Data;
import org.example.stronghold.context.HashedString;
import org.example.stronghold.model.Database;
import org.example.stronghold.model.User;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.OperatorException.Type;

@Data
public final class ProfileOperator {

    private final Database database;

    public void changePassword(Map<String, Object> req) throws OperatorException {
        User user = getReqAs(req, "user", User.class);
        HashedString oldPassword = getReqAs(req, "old-password", HashedString.class);
        HashedString newPassword = getReqAs(req, "new-password", HashedString.class);
        checkTrue(user.getPassword().equals(oldPassword), Type.INCORRECT_PASSWORD);
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
        user.setSlogan(newSlogan.isEmpty() ? null : newSlogan);
    }

    public void changeSecurityQA(Map<String, Object> req) throws OperatorException {
        User user = getReqAs(req, "user", User.class);
        String newSecurityQuestion = getReqString(req, "new-security-question");
        if (newSecurityQuestion.isEmpty()) {
            user.setSecurityQuestion(null);
            user.setSecurityAnswer(null);
            return;
        }
        String newSecurityAnswer = getReqString(req, "new-security-answer");
        user.setSecurityQuestion(newSecurityQuestion);
        user.setSecurityAnswer(newSecurityAnswer);
    }
}
