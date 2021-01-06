package me.joesvart.lattemeetup.util.chat;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Msg {

    public static char HEART = '\u2764';

    public static void sendMessage(String message, Sound sound) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(CustomColor.translate(message));
            player.playSound(player.getLocation(), sound, 1f, 1f);
        }

        logConsole(message);
    }

    public static void sendMessage(String message) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(CustomColor.translate(message));
        }

        logConsole(message);
    }

    public static void logConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(CustomColor.translate(message));
    }
}
