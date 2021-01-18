package me.joesvart.lattemeetup.managers;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattelibs.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ItemManager {
    
    public void handleLobbyInventory(Player player) {
        player.getInventory().setItem(0, new ItemCreator(Material.BOOK)
            .name(ChatUtils.GREEN + "Scenarios")
            .lore(ChatUtils.GRAY + "Right click to vote for the scenarios.")
            .build());

        player.getInventory().setItem(7, new ItemCreator(Material.EMERALD)
            .name(ChatUtils.GREEN + "Leaderboards")
            .lore(ChatUtils.GRAY + "Right click to open the menu.")
            .build());

        player.getInventory().setItem(8, new ItemCreator(Material.BED)
            .name(ChatUtils.GREEN + "Return to Lobby")
            .lore(ChatUtils.GRAY + "Right click to return to the main Lobby.")
            .build());
        
        player.updateInventory();
    }

    void handleSpectatorInventory(Player player) {
        player.getInventory().setItem(0, new ItemCreator(Material.ITEM_FRAME)
            .name(ChatUtils.AQUA + "Spectate")
            .lore(ChatUtils.GRAY + ("Right click to open the menu."))
            .build());

        player.getInventory().setItem(1, new ItemCreator(Material.DIAMOND)
            .name(ChatUtils.PRIMARY + "Random Teleport")
            .lore(ChatUtils.GRAY + ("Right click to teleport to a random player."))
            .build());

        player.getInventory().setItem(4, new ItemCreator(Material.COMPASS)
            .name(ChatUtils.PINK + "Navigation Compass")
            .lore(ChatUtils.GRAY + "Right click to teleport to that position.")
            .build());

        player.getInventory().setItem(8, new ItemCreator(Material.BED)
            .name(ChatUtils.GREEN + "Return to Lobby")
            .lore(ChatUtils.GRAY + "Right click to return to the main Lobby.")
            .build());
        
        player.updateInventory();
    }
}
