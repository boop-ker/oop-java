package ru.nsu.ccfit.kuryatnikova;

import ru.nsu.ccfit.kuryatnikova.commands.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class CommandsReader {
    private final StringBuilder token = new StringBuilder();
    private final CommandFactory factory = new CommandFactory();
    private final Scanner scanner;
    private Command currentCommand;

    public CommandsReader(InputStream inputStream) throws IOException, BadCommandClassException, BadSyntaxException {
        this.scanner = new Scanner(inputStream);
        advance();
    }

    public Command getCommand() {
        return currentCommand;
    }

    public void advance() throws IOException, BadCommandClassException, BadSyntaxException {
        if (!scanner.hasNextLine()) {
            currentCommand = null;
            return;
        }
        String line = scanner.nextLine();
        while (line.startsWith("#") && scanner.hasNextLine()) {
            line = scanner.nextLine();
        }

        if (line.startsWith("PUSH")) {
            currentCommand = factory.getCommand("PUSH");
            ((Push) currentCommand).setOperand(line.substring(5));
        } else if (line.startsWith("DEFINE")) {
            currentCommand = factory.getCommand("DEFINE");
            var substrings = line.split("\\s+", 2);
            var def = ((Define) currentCommand);
            def.setName(substrings[1]);
            def.setValue(Double.parseDouble(substrings[2]));
        } else {
            currentCommand = factory.getCommand(line);
        }
    };

    public boolean hasNext() {
        return currentCommand !=null;
    }
}
