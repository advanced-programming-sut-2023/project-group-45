package stronghold.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Menu {

    private final Scanner scanner;
    private final Map<String, Consumer<Map<String, String>>> commands = new HashMap<>();

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
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal input: " + e.getMessage());
        }
    }

    public void run() {
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equals("back"))
                return;
            processLine(line);
        }
    }
}
