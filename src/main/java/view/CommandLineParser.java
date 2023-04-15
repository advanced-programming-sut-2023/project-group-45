package view;

import java.util.HashMap;
import java.util.Map;

public class CommandLineParser {

    private String command;
    private Map<String, String> options;

    CommandLineParser() {
        command = null;
        options = new HashMap<>();
    }

    public void parse(String input) {
    }

    public String getCommand() {
        return command;
    }

    public Map<String, String> getOptions() {
        return options;
    }
}
