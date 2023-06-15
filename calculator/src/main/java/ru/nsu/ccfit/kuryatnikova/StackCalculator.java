package ru.nsu.ccfit.kuryatnikova;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.nsu.ccfit.kuryatnikova.commands.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class StackCalculator {

    private final CommandsReader reader;
    private final CommandFactory factory = new CommandFactory();
    private final InputStream inputStream;
    private final OutputStream outputStream;
    static final Logger log = LogManager.getRootLogger();


    /**
     * Takes input and output streams that calculator will use.
     *
     * @param inputStream
     * @param outputStream
     */
    public StackCalculator(InputStream inputStream, OutputStream outputStream) {
        log.info("Creating calculator");
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.reader = new CommandsReader(inputStream);
    }

    public void start(List<Character> input) {

        log.info("Calculator starts");

        Context context = new Context();

        try {
            while (reader.hasNext()) {
                Command command = factory.getCommand(reader.getToken());
                command.operate(context);
                reader.advance();
            }
        } catch (BadCommandClassException e) {
            throw new RuntimeException(e);
        } catch (BadSyntaxException e) {
            throw new RuntimeException(e);
        } catch (CalculatorException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
