package ru.nsu.ccfit.kuryatnikova.commands;

import ru.nsu.ccfit.kuryatnikova.Context;

import java.util.NoSuchElementException;

public class Power implements Command {
    @Override
    public void operate(Context context) throws CalculatorException {
        try {
            var stack = context.getStack();
            var first = stack.pop();
            var second = stack.pop();
            stack.push(Math.pow(first, second));
        } catch (NoSuchElementException e) {
            throw new CalculatorException("Stack is empty like my brain\n");
        }
    }
}
