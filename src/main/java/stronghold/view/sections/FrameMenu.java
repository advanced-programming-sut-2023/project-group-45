package stronghold.view.sections;

import java.util.Scanner;
import stronghold.model.Game;
import stronghold.view.Menu;

public class FrameMenu extends Menu {

    private final Game game;

    public FrameMenu(Scanner scanner, Game game) {
        super(scanner);
        this.game = game;
    }
}
