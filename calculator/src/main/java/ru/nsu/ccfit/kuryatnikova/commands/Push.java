package ru.nsu.ccfit.kuryatnikova.commands;

import ru.nsu.ccfit.kuryatnikova.Context;

public class Push implements Command {
    private String operand;

    public void setOperand(String operand) {
        this.operand = operand;
    }

    @Override
    public void operate(Context context) throws CalculatorException {
        try {
            var value = Double.parseDouble(operand);
            context.getStack().push(value);
        } catch (NumberFormatException ignored) {
            var variables = context.getVariables();

            if (variables.containsKey(operand))
                context.getStack().push(variables.get(operand));
            else throw new CalculatorException("Undfined variable");
        }
    }
}
