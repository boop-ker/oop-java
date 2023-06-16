package ru.nsu.ccfit.kuryatnikova.commands;

import ru.nsu.ccfit.kuryatnikova.Context;

import java.util.NoSuchElementException;

public class Division implements Command {
    @Override
    public void operate(Context context) throws CalculatorException {
        try {
            var stack = context.getStack();
            var first = stack.pop();
            var second = stack.pop();
            if (second == 0.0)
                throw new CalculatorException("Div by zero.");
            stack.push(first / second);
        } catch (NoSuchElementException e) {
            throw new CalculatorException("Stack is empty like my brain\n");
        }
    }
}