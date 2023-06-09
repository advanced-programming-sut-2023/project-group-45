package org.example.stronghold.cli;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

public class CommandLineParser {

    @Getter
    private String command;
    @Getter
    private Map<String, String> options;

    CommandLineParser(List<String> tokens) {
        parse(tokens);
    }

    private void parse(List<String> tokens) {
        checkArgument(tokens.size() > 0, "No command provided");
        command = tokens.get(0);
        options = new HashMap<>();
        for (int i = 1; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (token.startsWith("--")) {
                String[] option = token.substring(2).split("=", 2);
                if (option.length == 1) {
                    options.put(option[0], "true");
                } else {
                    options.put(option[0], option[1]);
                }
            } else {
                checkArgument(!token.startsWith("-"), "Invalid option: %s", token);
                checkArgument(!options.containsKey("subcommand"), "Invalid option: %s", token);
                options.put("subcommand", token);
            }
        }
    }
}
