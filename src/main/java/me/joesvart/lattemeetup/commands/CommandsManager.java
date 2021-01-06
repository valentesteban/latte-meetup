package me.joesvart.lattemeetup.commands;

import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.commands.impl.InvCommand;
import me.joesvart.lattemeetup.commands.impl.StartCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import java.lang.reflect.Field;
import java.util.Arrays;

public class CommandsManager {

    public CommandsManager() {
        Arrays.asList(

                // Meetup Commands
                new InvCommand(),
                new StartCommand()
        ).forEach(this::registerCommand);
    }

    private void registerCommand(Command cmd) {
        registerCommand(cmd, LatteMeetup.getInstance().getName());
    }

    private void registerCommand(Command cmd, String fallbackPrefix) {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            ((CommandMap) field.get(Bukkit.getServer())).register(cmd.getName(), fallbackPrefix, cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
