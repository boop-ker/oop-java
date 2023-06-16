package ru.nsu.ccfit.kuryatnikova.commands;

import ru.nsu.ccfit.kuryatnikova.Context;

public class Pop implements Command {
    @Override
    public void operate(Context context) throws CalculatorException {
        context.getStack().pop();
    }
}
