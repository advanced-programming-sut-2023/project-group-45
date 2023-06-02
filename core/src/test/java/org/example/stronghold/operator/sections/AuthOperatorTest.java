package org.example.stronghold.operator.sections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.stronghold.context.HashMode;
import org.example.stronghold.context.HashedString;
import org.example.stronghold.model.Database;
import org.example.stronghold.model.User;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.sections.AuthOperator;

import java.util.HashMap;
import java.util.Map;

public class AuthOperatorTest {

    private Database database;
    private AuthOperator authOperator;
    private static final HashedString password = HashedString.fromPlain("password").withMode(HashMode.SHA256);

    @BeforeEach
    public void setUp() {
        database = new Database();
        authOperator = new AuthOperator(database);
    }

    @Test
    public void testFindUser() throws OperatorException {
        User user = User.builder()
                .username("TestUser")
                .password(password)
                .nickname("TestNickname")
                .email("test@test.com")
                .build();
        database.getUsers().add(user);

        Map<String, Object> req = new HashMap<>();
        req.put("username", "TestUser");

        User foundUser = authOperator.findUser(req);

        Assertions.assertEquals(user, foundUser);
    }

    @Test
    public void testRegister() throws OperatorException {
        Map<String, Object> req = new HashMap<>();
        req.put("username", "TestUser");
        req.put("password", password);
        req.put("nickname", "TestNickname");
        req.put("email", "test@test.com");

        User registeredUser = authOperator.register(req);

        User expectedUser = User.builder()
                .username("TestUser")
                .password(password)
                .nickname("TestNickname")
                .email("test@test.com")
                .build();
        Assertions.assertEquals(expectedUser, registeredUser);
        Assertions.assertTrue(database.getUsers().contains(registeredUser));
    }

    @Test
    public void testRegisterWithInvalidUsername() {
        Map<String, Object> req = new HashMap<>();
        req.put("username", "invalid username");
        req.put("password", password);
        req.put("nickname", "TestNickname");
        req.put("email", "test@test.com");

        Assertions.assertThrows(OperatorException.class, () -> authOperator.register(req));
    }

    @Test
    public void testRegisterWithInvalidEmail() {
        Map<String, Object> req = new HashMap<>();
        req.put("username", "TestUser");
        req.put("password", password);
        req.put("nickname", "TestNickname");
        req.put("email", "invalid email");

        Assertions.assertThrows(OperatorException.class, () -> authOperator.register(req));
    }

    @Test
    public void testRegisterWithDuplicateUsername() {
        User user = User.builder()
                .username("TestUser")
                .password(password)
                .nickname("TestNickname")
                .email("test@test.com")
                .build();
        database.getUsers().add(user);

        Map<String, Object> req = new HashMap<>();
        req.put("username", "TestUser");
        req.put("password", password);
        req.put("nickname", "TestNickname");
        req.put("email", "test2@test.com");

        Assertions.assertThrows(OperatorException.class, () -> authOperator.register(req));
    }

    @Test
    public void testRegisterWithDuplicateEmail() {
        User user = User.builder()
                .username("TestUser")
                .password(password)
                .nickname("TestNickname")
                .email("test@test.com")
                .build();
        database.getUsers().add(user);

        Map<String, Object> req = new HashMap<>();
        req.put("username", "TestUser2");
        req.put("password", password);
        req.put("nickname", "TestNickname2");
        req.put("email", "test@test.com");

        Assertions.assertThrows(OperatorException.class, () -> authOperator.register(req));
    }
}
