package stronghold.view;

import java.util.Scanner;

public class CaptchaChecker {

    private static final boolean CAPTCHA_ENABLED = false;
    private CaptchaGenerator captchaGenerator;

    public void check(Scanner scanner) {
        if (!CAPTCHA_ENABLED)
            return;
        refresh();
        loop:
        while (true) {
            System.out.print(
                    "Enter captcha answer or type 'refresh' to refresh or 'cancel' to cancel: ");
            String input = scanner.nextLine();
            switch (input) {
                case "refresh" -> refresh();
                case "cancel" -> throw new RuntimeException("Canceled on captcha");
                default -> {
                    try {
                        int answer = Integer.parseInt(input);
                        if (answer == captchaGenerator.getAnswer())
                            break loop;
                        System.out.println("Wrong answer");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input");
                    }
                }
            }
        }
    }

    private void refresh() {
        captchaGenerator = new CaptchaGenerator();
        System.out.println(captchaGenerator);
    }
}
