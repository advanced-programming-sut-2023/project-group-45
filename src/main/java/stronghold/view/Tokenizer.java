package stronghold.view;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/* inputs a line and break it into List of tokens */
public class Tokenizer {

    @Getter
    private final List<String> tokens = new ArrayList<>();

    public Tokenizer(String line) {
        tokenize(line);
    }

    private void tokenize(String line) {
        StringBuilder currentWord = new StringBuilder();
        boolean inQuotes = false;
        boolean escaped = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (escaped) {
                currentWord.append(c);
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ' ' && !inQuotes) {
                if (currentWord.length() > 0) {
                    tokens.add(currentWord.toString());
                    currentWord = new StringBuilder();
                }
            } else {
                currentWord.append(c);
            }
        }
        if (currentWord.length() > 0) {
            tokens.add(currentWord.toString());
        }
    }
}
