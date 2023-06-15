package ru.nsu.ccfit.kuryatnikova.commands;

import ru.nsu.ccfit.kuryatnikova.Context;

import java.util.NoSuchElementException;

public class Sqrt implements Command {
    @Override
    public void operate(Context context) throws CalculatorException {
        try {
            var stack = context.getStack();
            var op = stack.pop();
            stack.push(Math.sqrt(op));
        } catch (NoSuchElementException e) {
            throw new CalculatorException("Stack is empty like my brain\n");
        }
    }
}
