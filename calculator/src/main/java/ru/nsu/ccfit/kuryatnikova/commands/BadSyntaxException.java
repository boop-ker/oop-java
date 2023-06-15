package ru.nsu.ccfit.kuryatnikova.commands;

/**
 * * May be thrown if there is no Character specified in cfg.properties.
 */
public class BadSyntaxException extends Exception {
    private final String mes;

    public BadSyntaxException(String mes) {
        this.mes = mes;
    }

    @Override
    public String getMessage() {
        return mes;
    }
}
