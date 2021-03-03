package xyz.lightningcapes.sources.manager;

import xyz.lightningcapes.api.Command;

import java.util.Arrays;
import java.util.List;

public class CommandManager {

    private final List<Command> commands;

    public CommandManager(Command... commands) {
        this.commands = Arrays.asList(commands);
    }

    public Command getByName(String name) {
        return commands.stream()
                .filter(command -> command.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}