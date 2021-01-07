package me.joesvart.lattemeetup.managers;

import me.joesvart.lattemeetup.util.chat.CC;
import me.joesvart.lattemeetup.util.other.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ItemManager {
    
    public void handleLobbyInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.BOOK)
            .name(CC.GREEN + "Scenarios")
            .lore(CC.GRAY + "Right click to vote for the scenarios.")
            .build());

        player.getInventory().setItem(7, new ItemBuilder(Material.EMERALD)
            .name(CC.GREEN + "Leaderboards Menu")
            .lore(CC.GRAY + "Right click to open the menu.")
            .build());

        player.getInventory().setItem(8, new ItemBuilder(Material.BED)
            .name(CC.GREEN + "Return to Lobby")
            .lore(CC.GRAY + "Right click to return to the main Lobby.")
            .build());
        
        player.updateInventory();
    }

    void handleSpectatorInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.ITEM_FRAME)
            .name(CC.AQUA + "Spectate Menu")
            .lore(CC.GRAY + ("Right click to open the menu."))
            .build());

        player.getInventory().setItem(1, new ItemBuilder(Material.DIAMOND)
            .name(CC.PRIMARY + "Random Teleport")
            .lore(CC.GRAY + ("Right click to teleport to a random player."))
            .build());

        player.getInventory().setItem(4, new ItemBuilder(Material.COMPASS)
            .name(CC.PINK + "Navigation Compass")
            .lore(CC.GRAY + "Right click to teleport to that position.")
            .build());

        player.getInventory().setItem(8, new ItemBuilder(Material.BED)
            .name(CC.GREEN + "Return to Lobby")
            .lore(CC.GRAY + "Right click to return to the main Lobby.")
            .build());
        
        player.updateInventory();
    }
}
