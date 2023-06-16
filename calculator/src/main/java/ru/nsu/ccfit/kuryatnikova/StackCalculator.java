package ru.nsu.ccfit.kuryatnikova;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.nsu.ccfit.kuryatnikova.commands.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public class StackCalculator {

    private final CommandsReader reader;
    private final InputStream inputStream;
    private final PrintStream outputStream;
    static final Logger log = LogManager.getRootLogger();


    /**
     * Takes input and output streams that calculator will use.
     *
     * @param inputStream
     * @param outputStream
     */
    public StackCalculator(InputStream inputStream, PrintStream outputStream) throws IOException, BadCommandClassException, BadSyntaxException {
        log.info("Creating calculator");
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.reader = new CommandsReader(inputStream);
    }

    public void start() {
        log.info("Calculator starts");

        Context context = new Context(outputStream);

        try {
            while (reader.hasNext()) {
                Command command = reader.getCommand();
                command.operate(context);
                reader.advance();
            }
        } catch (CalculatorException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (BadCommandClassException e) {
            throw new RuntimeException(e);
        } catch (BadSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
