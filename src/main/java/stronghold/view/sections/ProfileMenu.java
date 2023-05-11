package stronghold.view.sections;

import static stronghold.context.MapUtils.copyOptTo;
import static stronghold.context.MapUtils.getBoolOpt;
import static stronghold.context.MapUtils.getIntOpt;
import static stronghold.context.MapUtils.getOpt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import stronghold.model.User;
import stronghold.model.template.GameMapTemplate;
import stronghold.operator.OperatorException;
import stronghold.operator.Operators;
import stronghold.view.Menu;

public class ProfileMenu extends Menu {

    private final static List<String> SLOGANS = List.of(
            "We Are Number One! (RIP Stefan Karl)",
            // copilot wtf?
            "I'm a little teapot, short and stout",
            "Here is my handle, here is my spout",
            "When I get all steamed up, hear me shout",
            "Tip me over and pour me out!"
    );
    private final static List<String> SECURITY_QUESTIONS = List.of(
            // everyone already knows your top favorite
            "What is your third favorite movie?",
            "What is your third favorite food?",
            "What is your third favorite color?"
    );
    private final User user;

    public ProfileMenu(Scanner scanner, User user) {
        super(scanner);
        this.user = user;
        addCommand("print-security-questions", this::printSecurityQuestions);
        addCommand("change", this::changeProfile);
        addCommand("display", this::displayProfile);
        addCommand("map-editor", this::mapEditor);
        addCommand("start-game", this::startGame);
    }

    private static String generateSlogan() {
        Random random = new Random();
        return SLOGANS.get(random.nextInt(SLOGANS.size()));
    }

    private void printSecurityQuestions(Map<String, String> input) {
        System.out.println("Security questions:");
        for (int i = 0; i < SECURITY_QUESTIONS.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, SECURITY_QUESTIONS.get(i));
        }
    }

    private void changeProfile(Map<String, String> input) {
        if (input.containsKey("username")) {
            changeUsername(input);
        }
        if (input.containsKey("nickname")) {
            changeNickname(input);
        }
        if (input.containsKey("old-password") || input.containsKey("new-password")) {
            changePassword(input);
        }
        if (input.containsKey("email")) {
            changeEmail(input);
        }
        if (input.containsKey("slogan")) {
            changeSlogan(input);
        }
        if (input.containsKey("security-question")) {
            changeSecurityQA(input);
        }
    }

    private void changeUsername(Map<String, String> input) {
        Map<String, Object> req = new HashMap<>();
        req.put("user", user);
        req.put("new-username", getOpt(input, "username"));
        try {
            Operators.profile.changeUsername(req);
            System.out.println("Change username successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void changeNickname(Map<String, String> input) {
        Map<String, Object> req = new HashMap<>();
        req.put("user", user);
        req.put("new-nickname", getOpt(input, "nickname"));
        Operators.profile.changeNickname(req);
        System.out.println("Change nickname successfully");
    }

    private void changePassword(Map<String, String> input) {
        Map<String, Object> req = new HashMap<>();
        req.put("user", user);
        String newPassword = getOpt(input, "new-password");
        if (AuthMenu.isPasswordWeak(newPassword)) {
            System.out.println(
                    "Password must contain at least one lowercase letter, one uppercase letter, one digit and one special character");
            return;
        }
        req.put("old-password", AuthMenu.hashPassword(getOpt(input, "old-password")));
        req.put("new-password", AuthMenu.hashPassword(newPassword));
        try {
            Operators.profile.changePassword(req);
            System.out.println("Change password successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void changeEmail(Map<String, String> input) {
        Map<String, Object> req = new HashMap<>();
        req.put("user", user);
        req.put("new-email", getOpt(input, "email"));
        try {
            Operators.profile.changeEmail(req);
            System.out.println("Change email successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void changeSlogan(Map<String, String> input) {
        Map<String, Object> req = new HashMap<>();
        req.put("user", user);
        if (getOpt(input, "slogan").equals("random")) {
            String slogan = generateSlogan();
            input.put("slogan", slogan);
            System.out.println("Your slogan is: " + slogan);
        }
        req.put("new-slogan", getOpt(input, "slogan"));
        Operators.profile.changeSlogan(req);
        System.out.println("Change slogan successfully");
    }

    private void changeSecurityQA(Map<String, String> input) {
        Map<String, Object> req = new HashMap<>();
        req.put("user", user);
        String securityQuestion = getOpt(input, "security-question");
        req.put("new-security-question", securityQuestion);
        if (!securityQuestion.isEmpty()) {
            if (!input.containsKey("security-answer")) {
                System.out.print("Your answer: ");
                input.put("security-answer", scanner.nextLine().trim());
            }
            req.put("new-security-answer", getOpt(input, "security-answer"));
        }
        try {
            Operators.profile.changeSecurityQA(req);
            System.out.println("Change security question and answer successfully");
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void displayProfile(Map<String, String> input) {
        System.out.println("Username: " + user.getUsername());
        System.out.println("Nickname: " + user.getNickname());
        System.out.println("Email: " + user.getEmail());
        if (getBoolOpt(input, "slogan")) {
            if (user.getSlogan() == null) {
                System.out.println("Slogan is empty!");
            } else {
                System.out.println("Slogan: " + user.getSlogan());
            }
        }
    }

    private void mapEditor(Map<String, String> input) {
        GameMapTemplate gameMap;
        if (getBoolOpt(input, "new")) {
            gameMap = new GameMapTemplate(
                    getOpt(input, "name"),
                    getIntOpt(input, "width"),
                    getIntOpt(input, "height")
            );
        } else {
            try {
                gameMap = Operators.mapEditor.getGameMap(new HashMap<>() {{
                    copyOptTo(input, this, "name");
                }});
            } catch (OperatorException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
        System.out.println("Switched to map editor");
        new MapEditorMenu(scanner, gameMap).run();
    }

    private void startGame(Map<String, String> input) {
        String mapName = getOpt(input, "map");
        try {
            GameMapTemplate gameMap = Operators.mapEditor.getGameMap(new HashMap<>() {{
                put("name", mapName);
            }});
            System.out.println("Select your opponents!");
            new StartGameMenu(scanner, user, gameMap).run();
        } catch (OperatorException e) {
            System.out.println(e.getMessage());
        }
    }
}
