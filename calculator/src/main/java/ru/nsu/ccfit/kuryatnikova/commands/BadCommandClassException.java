package ru.nsu.ccfit.kuryatnikova.commands;

/**
 * May be thrown if there is no class specified in cfg.properties.
 */
public class BadCommandClassException extends Exception {
    private final String mes;

    public BadCommandClassException(String mes) {
        this.mes = mes;
    }

    @Override
    public String getMessage() {
        return mes;
    }
}
