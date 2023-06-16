import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.ccfit.kuryatnikova.StackCalculator;
import ru.nsu.ccfit.kuryatnikova.commands.BadCommandClassException;
import ru.nsu.ccfit.kuryatnikova.commands.BadSyntaxException;

import java.io.*;

class CalculatorTests {
    @Test
    void addition() {
        String commands = """
                PUSH 4.0
                SQRT
                PRINT
                """;

        InputStream inputStream = new ByteArrayInputStream(commands.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        StackCalculator calc;
        try {
            calc = new StackCalculator(inputStream, new PrintStream(outputStream));
        } catch (BadCommandClassException e) {
            throw new RuntimeException(e);
        } catch (BadSyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        calc.start();

        var out = outputStream.toString();
        assertEquals("2.0\n", out);
    }

}