package ru.nsu.ccfit.kuryatnikova.commands;

import ru.nsu.ccfit.kuryatnikova.Context;

import java.io.IOException;

public class Print implements Command {
    @Override
    public void operate(Context context) throws CalculatorException {
        var val = context.getStack().pop();
        context.getOutputStream().println(val);
    }
}
