package ru.nsu.ccfit.kuryatnikova;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Context {
    Deque<Double> stack = new ArrayDeque<>();
    Map<String, Double> variables = new HashMap<>();

    public Deque<Double> getStack() {
        return stack;
    }

    public Map<String, Double> getVariables() {
        return variables;
    }
}
