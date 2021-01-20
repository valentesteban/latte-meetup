package me.joesvart.lattemeetup.commands.impl;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattemeetup.LatteMeetup;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommnad extends Command {

    private LatteMeetup plugin = LatteMeetup.getInstance();

    public SetSpawnCommnad() {
        super("setspawn");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatUtils.translate(plugin.getMessagesConfig().getString("MESSAGES.NO-CONSOLE")));
            return true;
        }

        /* Insufficient permission message */
        if (!sender.hasPermission("lattemeetup.admin")) {
            sender.sendMessage(ChatUtils.translate(plugin.getMessagesConfig().getString("MESSAGES.NO-PERMISSION")));
            return true;
        }

        Player player = (Player) sender;
        player.performCommand("setworldspawn");
        player.sendMessage(ChatUtils.translate(plugin.getMessagesConfig().getString("MESSAGES.SPAWN-SET")));

        return false;
    }
}