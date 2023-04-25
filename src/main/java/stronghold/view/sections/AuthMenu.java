package stronghold.view.sections;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import java.util.random.RandomGenerator;
import stronghold.context.HashMode;
import stronghold.context.HashedString;
import stronghold.model.User;
import stronghold.operator.OperatorException;
import stronghold.operator.Operators;
import stronghold.view.Menu;

public class AuthMenu extends Menu {

    // TODO: add meaningful slogans
    static final String[] slogans = {
            "Slogan1",
            "Slogan2",
            "Slogan3"
    };
    static final String[] questions = {
            "What is my father’s name?",
            "What was my first pet’s name?",
            "What is my mother’s last name?"
    };

    public AuthMenu(Scanner scanner) {
        super(scanner);
        addCommand("login", this::login);
        addCommand("register", this::register);
        addCommand("forgot-password", this::forgotPassword);
        addCommand("print-questions", this::printQuestions);
    }

    private void register(Map<String, String> input) {
        HashMap<String, Object> req = new HashMap<>();
        if ("random".equals(input.get("password"))) {
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
            System.out.println(
                    "Your password is: " + password + "\nPlease re-enter your password here:");
            input.put("password_confirm", scanner.nextLine().trim());
            input.put("password", password.toString());
        }
        if (!input.get("password").equals(input.get("password_confirm"))) {
            System.out.println("Password mismatch");
            return;
        }
        if (!input.get("password").matches(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={};':\"|,.<>?]).{6,}$")) {
            System.out.println(
                    "Password must contain at least one lowercase letter, one uppercase letter, one digit and one special character");
            return;
        }
        if (input.get("slogan").equals("random")) {
            Random random = new Random();
            input.put("slogan", slogans[random.nextInt(slogans.length)]);
        }
        req.put("username", input.get("username"));
        req.put("password",
                HashedString.fromPlain(input.get("password")).withMode(HashMode.SHA256));
        req.put("email", input.get("email"));
        req.put("nickname", input.get("nickname"));
        req.put("slogan", input.get("slogan"));
        req.put("securityQuestion", input.get("securityQuestion"));
        req.put("securityAnswer", input.get("securityAnswer"));
        try {
            User user = Operators.auth.register(req);
            new ProfileMenu(scanner, user).run();
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printQuestions(Map<String, String> input) {
        System.out.println("Security questions:");
        for (int i = 0; i < questions.length; i++) {
            System.out.println((i + 1) + ". " + questions[i]);
        }
    }

    private void login(Map<String, String> input) {
        HashMap<String, Object> req = new HashMap<>();
        req.put("username", input.get("username"));
        req.put("password",
                HashedString.fromPlain(input.get("password")).withMode(HashMode.SHA256));
        if (req.containsKey("stay-logged-in")) {
            req.put("stay-logged-in", Boolean.parseBoolean(input.get("stay-logged-in")));
        }
        try {
            User user = Operators.auth.login(req);
            new ProfileMenu(scanner, user).run();
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void forgotPassword(Map<String, String> input) {
        HashMap<String, Object> req = new HashMap<>();
        req.put("username", input.get("username"));
        req.put("securityQuestion", input.get("securityQuestion"));
        req.put("securityAnswer", input.get("securityAnswer"));
        try {
            Operators.auth.forgotPassword(req);
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }
}
