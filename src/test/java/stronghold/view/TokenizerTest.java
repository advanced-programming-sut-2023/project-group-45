package stronghold.view;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class TokenizerTest {
    @Test
    void testMultiword() {
        Tokenizer tokenizer = new Tokenizer("do this then that");
        assertEquals(List.of("do", "this", "then", "that"), tokenizer.getTokens());
    }

    @Test
    void testWhiteSpaceAround() {
        Tokenizer tokenizer = new Tokenizer(" do\tthis then\nthat  ");
        assertEquals(List.of("do", "this", "then", "that"), tokenizer.getTokens());
    }

    @Test
    void testEscape() {
        Tokenizer tokenizer = new Tokenizer("\"big word\" \"in a \\\"small\\\" word\" \"escape \\\\ from reality\"");
        assertEquals(List.of("big word", "in a \"small\" word", "escape \\ from reality"), tokenizer.getTokens());
    }
}