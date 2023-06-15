package ru.nsu.ccfit.kuryatnikova.commands;
import org.apache.log4j.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Factory for commands. Configures with cfg.proprties file and outputs arbitrary
 * classes implementing Command interface.
 */
public class CommandFactory {
    private final Map<String,  Constructor<?>> commandMap = new HashMap<>();
    private final Properties configuration = new Properties();
    private static final String defaultCfgFile = "cfg.properties";

    static final  Logger log = LogManager.getRootLogger();

    public CommandFactory() {
        log.info("Creating CommandFactory\nTrying to open defaultCfgFile");
        try (InputStream cfgFile = ClassLoader.getSystemResourceAsStream(defaultCfgFile)) {
            if (cfgFile != null) {
                log.info("CfgFile oppened succesfully\n Trying To load proprties");
                configuration.load(cfgFile);
                log.info("Properties opened succesfully");
            }
        } catch (IOException e) {
            System.err.println("Cannot load configuration file");
            log.error("Cannot load configuration file");
            log.error(e.getMessage());
        }
    }

    /**
     * Method that takes Character from program and outputs Command class
     * according to cfg.properties. May throw exceptions if there's no
     * such class or no such character.
     * @param input
     * @return
     * @throws BadCommandClassException
     * @throws BadSyntaxException
     */
    public Command getCommand(String input) throws BadCommandClassException, BadSyntaxException {
        if (input.startsWith("$"))
            return
        try {
            var number = Double.parseDouble(input);
            return new Operand(number);
        } catch (NumberFormatException ignored) {}

        try {
            StringBuilder mes = new StringBuilder("Trying to get Command class for ");
            log.info(mes.append(input));
            if (commandMap.containsKey(input)) {
                Command commandClass = (Command) commandMap.get(input).newInstance();
                mes.replace(0, 32, "For ").append(" found already loaded")
                        .append(commandClass.getClass().getName()).append(" class");
                log.info(mes);
                return commandClass;
            } else {
                mes.replace(0,32, "Class for ");
                log.info(mes.append(" wasn't found yet"));

                Class<?> commandClass;
                if (configuration.containsKey(input)) {
                    log.info("Found command class name in configuration. Trying to load it");
                    commandClass = Class.forName((String) configuration.get(input));

                    StringBuilder str = new StringBuilder(commandClass.getName());
                    log.info(str.append("Class was loaded successfully. Adding it to cache").toString());
                    commandMap.put(input, commandClass.getDeclaredConstructor());
                    return getCommand(input);
                }

                throw new BadSyntaxException(new StringBuilder("No class in configuration for ")
                        .append(input).append(" symbol").toString());
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 ClassNotFoundException | NoSuchMethodException e) {
            log.error(e.getMessage());
            throw new BadCommandClassException(e.getMessage());
        }
    }
}

