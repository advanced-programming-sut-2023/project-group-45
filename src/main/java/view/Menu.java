package view;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import lombok.Data;

@Data
public abstract class Menu {

    private final Scanner scanner;
    private final Map<String, Consumer<Map<String, String>>> commands = new HashMap<>();

    protected final void addCommand(String commandName,
            Consumer<HashMap<String, String>> function) {
    }

    private void processCommands(String input) {
    }

    public void runMenu() {
    }
}
