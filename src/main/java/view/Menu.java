package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

public abstract class Menu {
    private final Scanner scanner;
    private final HashMap<String, Consumer<HashMap<String, String>>> commands;

    Menu(Scanner scanner){
        this.scanner = scanner;
        this.commands = new HashMap<>();
    }

    protected final void addCommand(String commandName, Consumer<HashMap<String, String>> function){
        return;
    }

    private void processCommands(String input){
        return;
    }

    public void run(){
        return;
    }
}
