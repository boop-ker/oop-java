package ru.nsu.ccfit.kuryatnikova.commands;

import ru.nsu.ccfit.kuryatnikova.Context;

public class Define implements Command {
    String name;
    Double value;

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public void operate(Context context) throws CalculatorException {
        context.getVariables().put(name, value);
    }
}
