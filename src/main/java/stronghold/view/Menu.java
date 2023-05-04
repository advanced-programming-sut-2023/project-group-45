package stronghold.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public abstract class Menu {

    protected final Scanner scanner;
    private final Map<String, Consumer<Map<String, String>>> commands = new HashMap<>();

    public Menu(Scanner scanner) {
        this.scanner = scanner;
        addCommand("where-am-i", this::whereAmI);
        addCommand("help", this::help);
    }

    protected final void addCommand(String commandName,
            Consumer<Map<String, String>> function) {
        commands.put(commandName, function);
    }

    private void processCommand(CommandLineParser parser) {
        String commandName = parser.getCommand();
        if (commands.containsKey(commandName)) {
            commands.get(commandName).accept(parser.getOptions());
        } else {
            System.out.println("Unknown command: " + commandName);
        }
    }

    private void processLine(String line) {
        try {
            Tokenizer tokenizer = new Tokenizer(line);
            CommandLineParser parser = new CommandLineParser(tokenizer.getTokens());
            processCommand(parser);
        } catch (RuntimeException e) {
            System.out.println("Command failed: " + e.getMessage());
        }
    }

    private String getMenuName() {
        return getClass().getSimpleName();
    }

    public void run() {
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equals("back")) {
                System.out.println("Exiting " + getMenuName());
                return;
            }
            processLine(line);
        }
    }

    private void whereAmI(Map<String, String> input) {
        System.out.println("You are in " + getMenuName());
    }

    private void help(Map<String, String> input) {
        System.out.println("Available commands (" + getMenuName() + "):");
        System.out.println("\tback");
        commands.keySet().forEach(x -> System.out.println("\t" + x));
    }
}
