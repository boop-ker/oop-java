package ru.nsu.ccfit.kuryatnikova.commands;

import ru.nsu.ccfit.kuryatnikova.Context;

public interface Command {
    void operate(Context context) throws CalculatorException;
}
