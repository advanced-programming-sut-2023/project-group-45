package stronghold.view.sections;

import java.util.Map;
import java.util.Scanner;
import stronghold.model.User;
import stronghold.view.Menu;

public class ProfileMenu extends Menu {

    private User user;

    // TODO: Add commands
    public ProfileMenu(Scanner scanner, User user) {
        super(scanner);
        this.user = user;
    }

    private void changeUsername(Map<String, String> input) {
    }

    private void changeNickname(Map<String, String> input) {
    }

    private void changePassword(Map<String, String> input) {
    }

    private void changeEmail(Map<String, String> input) {
    }

    private void changeSlogan(Map<String, String> input) {
    }

    private void displayProfile(Map<String, String> input) {
    }

}
