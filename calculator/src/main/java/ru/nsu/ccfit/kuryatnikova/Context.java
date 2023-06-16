package ru.nsu.ccfit.kuryatnikova;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Context {
    Deque<Double> stack = new ArrayDeque<>();
    Map<String, Double> variables = new HashMap<>();
    PrintStream outputStream;

    public Context(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    public Deque<Double> getStack() {
        return stack;
    }

    public Map<String, Double> getVariables() {
        return variables;
    }

    public PrintStream getOutputStream() {
        return outputStream;
    }
}
