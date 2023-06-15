package ru.nsu.ccfit.kuryatnikova.commands;

import ru.nsu.ccfit.kuryatnikova.Context;

public class Operand implements Command {
    private Double operand;
    Operand(Double operand) {
        this.operand =  operand;
    }
    @Override
    public void operate(Context context) throws CalculatorException {
        context.getStack().push(operand);
    }
}
