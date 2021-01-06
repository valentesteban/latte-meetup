package me.joesvart.lattemeetup.managers;

import me.joesvart.lattemeetup.util.chat.CC;
import me.joesvart.lattemeetup.util.other.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ItemManager {
    
    public void handleLobbyInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.BOOK).name(CC.GREEN + "Scenarios").build());
        player.getInventory().setItem(7, new ItemBuilder(Material.EMERALD).name(CC.GREEN + "Stats").build());
        player.getInventory().setItem(8, new ItemBuilder(Material.BED).name(CC.GREEN + "Leave to Lobby").build());
        
        player.updateInventory();
    }

    void handleSpectatorInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.ITEM_FRAME).name(CC.SECONDARY + "Spectate Menu").build());
        player.getInventory().setItem(1, new ItemBuilder(Material.DIAMOND).name(CC.SECONDARY + "Random Teleport").build());
        player.getInventory().setItem(8, new ItemBuilder(Material.COMPASS).name(CC.SECONDARY + "Navigation Compass").build());
        
        player.updateInventory();
    }
}
