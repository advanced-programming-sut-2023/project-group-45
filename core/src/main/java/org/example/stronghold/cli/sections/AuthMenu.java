package org.example.stronghold.cli.sections;

import static org.example.stronghold.context.MapUtils.copyOptTo;
import static org.example.stronghold.context.MapUtils.getBoolOpt;
import static org.example.stronghold.context.MapUtils.getOpt;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import org.example.stronghold.cli.CaptchaChecker;
import org.example.stronghold.cli.Menu;
import org.example.stronghold.context.HashMode;
import org.example.stronghold.context.HashedString;
import org.example.stronghold.model.User;
import org.example.stronghold.operator.OperatorException;
import org.example.stronghold.operator.Operators;

public class AuthMenu extends Menu {

    private int timeout, timeoutLevel = -2;

    public AuthMenu(Scanner scanner) {
        super(scanner);
        addCommand("register", this::register);
        addCommand("login", this::login);
        addCommand("forgot-password", this::forgotPassword);
    }

    public static String generatePassword() {
        // Generate random password with at least 6 characters which contains at least one
        // lowercase letter, one uppercase letter, one digit and one special character
        Random random = new Random();
        int length = random.nextInt(6) + 6;
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int type = random.nextInt(4);
            if (i < 4) {
                type = i;
            }
            switch (type) {
                case 0 -> password.append((char) (random.nextInt(26) + 'a'));
                case 1 -> password.append((char) (random.nextInt(26) + 'A'));
                case 2 -> password.append((char) (random.nextInt(10) + '0'));
                case 3 -> password.append((char) (random.nextInt(15) + '!'));
            }
        }
        // Shuffle the password
        for (int i = 0; i < length; i++) {
            int j = random.nextInt(length);
            char c = password.charAt(i);
            password.setCharAt(i, password.charAt(j));
            password.setCharAt(j, c);
        }
        return password.toString();
    }

    public static boolean isPasswordWeak(String password) {
        return !password.matches(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={};':\"|,.<>?/]).{6,}$");
    }

    public static HashedString hashPassword(String plainPassword) {
        return HashedString.fromPlain(plainPassword).withMode(HashMode.SHA512);
    }

    private void register(Map<String, String> input) {
        Map<String, Object> req = new HashMap<>();
        if (getOpt(input, "password").equals("random")) {
            String password = generatePassword();
            System.out.println(
                "Your password is: " + password + "\nPlease re-enter your password here:");
            input.put("password-confirm", scanner.nextLine().trim());
            input.put("password", password);
        }
        if (!getOpt(input, "password").equals(getOpt(input, "password-confirm"))) {
            System.out.println("Password mismatch");
            return;
        }
        if (isPasswordWeak(getOpt(input, "password"))) {
            System.out.println(
                "Password must contain at least one lowercase letter, one uppercase letter, one digit and one special character");
            return;
        }
        copyOptTo(input, req, "username", "email", "nickname");
        req.put("password", hashPassword(getOpt(input, "password")));
        new CaptchaChecker().check(scanner);
        try {
            User user = Operators.auth.register(req);
            System.out.println("Register successful");
            new ProfileMenu(scanner, user).run();
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void login(Map<String, String> input) {
        int time = (int) (System.currentTimeMillis() / 1000);
        if (time < timeout) {
            System.out.println("You have been blocked for " + (timeout - time) + " seconds");
            return;
        }
        Map<String, Object> req = new HashMap<>();
        copyOptTo(input, req, "username");
        if (input.containsKey("password")) {
            req.put("password", hashPassword(getOpt(input, "password")));
        }
        if (input.containsKey("stay-logged-in")) {
            req.put("stay-logged-in", getBoolOpt(input, "stay-logged-in"));
        }
        new CaptchaChecker().check(scanner);
        try {
            User user = Operators.auth.login(req);
            System.out.println("Login successful");
            new ProfileMenu(scanner, user).run();
            timeoutLevel = -2;
        } catch (OperatorException e) {
            timeoutLevel++;
            if (timeoutLevel > 0) {
                timeout = time + timeoutLevel * 5;
            }
            System.out.println(e.getMessage());
        }
    }

    private void forgotPassword(Map<String, String> input) {
        Map<String, Object> req = new HashMap<>();
        copyOptTo(input, req, "username", "security-question", "security-answer");
        req.put("new-password", hashPassword(getOpt(input, "new-password")));
        new CaptchaChecker().check(scanner);
        try {
            Operators.auth.forgotPassword(req);
            System.out.println("Password reset successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }
}
