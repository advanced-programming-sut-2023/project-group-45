package stronghold.view.sections;

import static stronghold.context.MapUtils.copyOptTo;
import static stronghold.context.MapUtils.getBoolOpt;
import static stronghold.context.MapUtils.getOpt;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import stronghold.context.HashMode;
import stronghold.context.HashedString;
import stronghold.model.User;
import stronghold.operator.OperatorException;
import stronghold.operator.Operators;
import stronghold.view.Menu;

public class AuthMenu extends Menu {

    private int timeout, timeoutLevel = -2;

    public AuthMenu(Scanner scanner) {
        super(scanner);
        addCommand("register", this::register);
        addCommand("login", this::login);
        addCommand("forgot-password", this::forgotPassword);
    }

    private String generatePassword() {
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

    private boolean isPasswordStrong(String password) {
        return password.matches(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={};':\"|,.<>?]).{6,}$");
    }

    public HashedString hashPassword(String plainPassword) {
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
        if (!isPasswordStrong(getOpt(input, "password"))) {
            System.out.println(
                    "Password must contain at least one lowercase letter, one uppercase letter, one digit and one special character");
            return;
        }
        copyOptTo(input, req, "username", "email", "nickname");
        req.put("password", hashPassword(getOpt(input, "password")));
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
        req.put("password", hashPassword(getOpt(input, "password")));
        if (req.containsKey("stay-logged-in")) {
            req.put("stay-logged-in", getBoolOpt(input, "stay-logged-in"));
        }
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
        req.put("username", input.get("username"));
        req.put("security-question", input.get("security-question"));
        req.put("security-answer", input.get("security-answer"));
        try {
            Operators.auth.forgotPassword(req);
            System.out.println("Password reset successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }
}
