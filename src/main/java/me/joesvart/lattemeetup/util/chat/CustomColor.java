package me.joesvart.lattemeetup.util.chat;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class CustomColor {

    public static String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> translate(List<String> text) {
        return text.stream().map(CustomColor::translate).collect(Collectors.toList());
    }
}

