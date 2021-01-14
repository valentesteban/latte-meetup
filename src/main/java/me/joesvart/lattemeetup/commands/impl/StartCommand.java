package me.joesvart.lattemeetup.commands.impl;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.util.chat.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class StartCommand extends Command {

    private LatteMeetup plugin = LatteMeetup.getInstance();

    public StartCommand() {
        super("start");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatUtils.translate("&COnly players can perform this command."));
            return true;
        }

        /* Insufficient permission message */
        if(!sender.hasPermission("lattemeetup.admin")) {
            sender.sendMessage(ChatUtils.RED + "You do not have permission for execute this command.");
            return true;
        }

        Player player = (Player) sender;
        this.plugin.getGameManager().handleStarting();

        /* Game successfully started message */
        Msg.sendMessage("");
        Msg.sendMessage(ChatUtils.DARK_GREEN + player.getName() + ChatUtils.GREEN + " forced the game to start!");
        Msg.sendMessage("");

        return false;
    }
}
