package stronghold.view;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.Test;

class CommandLineParserTest {

    private CommandLineParser getCommandLineParser(String line) {
        return new CommandLineParser(new Tokenizer(line).getTokens());
    }

    @Test
    void testSimpleCommand() {
        String line = "do --this=yes --and=that";
        CommandLineParser commandLineParser = getCommandLineParser(line);
        assertEquals("do", commandLineParser.getCommand());
        assertEquals(
                Map.of(
                        "this", "yes",
                        "and", "that"
                ),
                commandLineParser.getOptions()
        );
    }

    @Test
    void testSubcommand() {
        String line1 = "do this --with=that";
        String line2 = "do --subcommand=this --with=that";
        CommandLineParser commandLineParser1 = getCommandLineParser(line1);
        CommandLineParser commandLineParser2 = getCommandLineParser(line2);
        assertEquals(commandLineParser2.getCommand(), commandLineParser1.getCommand());
        assertEquals(commandLineParser2.getOptions(), commandLineParser1.getOptions());
    }
}