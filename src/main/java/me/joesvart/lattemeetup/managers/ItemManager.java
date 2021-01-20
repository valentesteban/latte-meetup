package me.joesvart.lattemeetup.managers;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattelibs.item.ItemCreator;
import me.joesvart.lattemeetup.LatteMeetup;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemManager {
    
    public void handleLobbyInventory(Player player) {

        /**
         * Scenarios item
         */
        if (LatteMeetup.getPlugin().getItemsConfig().getBoolean("BOOLEANS.SCENARIOS-ITEM.ENABLED")) {
            ItemStack item = new ItemStack(LatteMeetup.getPlugin().getItemsConfig().getInteger("ITEMS.SCENARIOS-ITEM.ID"));

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatUtils.translate(LatteMeetup.getPlugin().getItemsConfig().getString("ITEMS.SCENARIOS-ITEM.NAME")));

            if (LatteMeetup.getPlugin().getItemsConfig().getBoolean("BOOLEANS.SCENARIOS-ITEM.LORE")) {
                meta.setLore(ChatUtils.translate(LatteMeetup.getPlugin().getItemsConfig().getStringList("ITEMS.SCENARIOS-ITEM.LORE")));
            }

            item.setItemMeta(meta);
            player.getInventory().setItem(LatteMeetup.getPlugin().getItemsConfig().getInteger("ITEMS.SCENARIOS-ITEM.SLOT"), item);
        }

        /**
         * Leaderboards item
         */
        if (LatteMeetup.getPlugin().getItemsConfig().getBoolean("BOOLEANS.LEADERBOARDS-ITEM.ENABLED")) {
            ItemStack item = new ItemStack(LatteMeetup.getPlugin().getItemsConfig().getInteger("ITEMS.LEADERBOARDS-ITEM.ID"));

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatUtils.translate(LatteMeetup.getPlugin().getItemsConfig().getString("ITEMS.LEADERBOARDS-ITEM.NAME")));

            if (LatteMeetup.getPlugin().getItemsConfig().getBoolean("BOOLEANS.LEADERBOARDS-ITEM.LORE")) {
                meta.setLore(ChatUtils.translate(LatteMeetup.getPlugin().getItemsConfig().getStringList("ITEMS.LEADERBOARDS-ITEM.LORE")));
            }

            item.setItemMeta(meta);
            player.getInventory().setItem(LatteMeetup.getPlugin().getItemsConfig().getInteger("ITEMS.LEADERBOARDS-ITEM.SLOT"), item);
        }

        /**
         * Lobby item
         */
        if (LatteMeetup.getPlugin().getItemsConfig().getBoolean("BOOLEANS.LOBBY-ITEM.ENABLED")) {
            ItemStack item = new ItemStack(LatteMeetup.getPlugin().getItemsConfig().getInteger("ITEMS.LOBBY-ITEM.ID"));

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatUtils.translate(LatteMeetup.getPlugin().getItemsConfig().getString("ITEMS.LOBBY-ITEM.NAME")));

            if (LatteMeetup.getPlugin().getItemsConfig().getBoolean("BOOLEANS.LOBBY-ITEM.LORE")) {
                meta.setLore(ChatUtils.translate(LatteMeetup.getPlugin().getItemsConfig().getStringList("ITEMS.LOBBY-ITEM.LORE")));
            }

            item.setItemMeta(meta);
            player.getInventory().setItem(LatteMeetup.getPlugin().getItemsConfig().getInteger("ITEMS.LOBBY-ITEM.SLOT"), item);
        }

        player.updateInventory();
    }

    void handleSpectatorInventory(Player player) {

        /**
         * Spectate item
         */
        if (LatteMeetup.getPlugin().getItemsConfig().getBoolean("BOOLEANS.SPECTATE-MENU-ITEM.ENABLED")) {
            ItemStack item = new ItemStack(LatteMeetup.getPlugin().getItemsConfig().getInteger("ITEMS.SPECTATE-MENU-ITEM.ID"));

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatUtils.translate(LatteMeetup.getPlugin().getItemsConfig().getString("ITEMS.SPECTATE-MENU-ITEM.NAME")));

            if (LatteMeetup.getPlugin().getItemsConfig().getBoolean("BOOLEANS.SPECTATE-MENU-ITEM.LORE")) {
                meta.setLore(ChatUtils.translate(LatteMeetup.getPlugin().getItemsConfig().getStringList("ITEMS.SPECTATE-MENU-ITEM.LORE")));
            }

            item.setItemMeta(meta);
            player.getInventory().setItem(LatteMeetup.getPlugin().getItemsConfig().getInteger("ITEMS.SPECTATE-MENU-ITEM.SLOT"), item);
        }

        /**
         * Random teleport item
         */
        if (LatteMeetup.getPlugin().getItemsConfig().getBoolean("BOOLEANS.RANDOM-TELEPORT-ITEM.ENABLED")) {
            ItemStack item = new ItemStack(LatteMeetup.getPlugin().getItemsConfig().getInteger("ITEMS.RANDOM-TELEPORT-ITEM.ID"));

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatUtils.translate(LatteMeetup.getPlugin().getItemsConfig().getString("ITEMS.RANDOM-TELEPORT-ITEM.NAME")));

            if (LatteMeetup.getPlugin().getItemsConfig().getBoolean("BOOLEANS.RANDOM-TELEPORT-ITEM.LORE")) {
                meta.setLore(ChatUtils.translate(LatteMeetup.getPlugin().getItemsConfig().getStringList("ITEMS.RANDOM-TELEPORT-ITEM.LORE")));
            }

            item.setItemMeta(meta);
            player.getInventory().setItem(LatteMeetup.getPlugin().getItemsConfig().getInteger("ITEMS.RANDOM-TELEPORT-ITEM.SLOT"), item);
        }

        /**
         * Navigation Compass Item
         */
        if (LatteMeetup.getPlugin().getItemsConfig().getBoolean("BOOLEANS.NAVIGATION-COMPASS-ITEM.ENABLED")) {
            ItemStack item = new ItemStack(LatteMeetup.getPlugin().getItemsConfig().getInteger("ITEMS.NAVIGATION-COMPASS-ITEM.ID"));

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatUtils.translate(LatteMeetup.getPlugin().getItemsConfig().getString("ITEMS.NAVIGATION-COMPASS-ITEM.NAME")));

            if (LatteMeetup.getPlugin().getItemsConfig().getBoolean("BOOLEANS.NAVIGATION-COMPASS-ITEM.LORE")) {
                meta.setLore(ChatUtils.translate(LatteMeetup.getPlugin().getItemsConfig().getStringList("ITEMS.NAVIGATION-COMPASS-ITEM.LORE")));
            }

            item.setItemMeta(meta);
            player.getInventory().setItem(LatteMeetup.getPlugin().getItemsConfig().getInteger("ITEMS.NAVIGATION-COMPASS-ITEM.SLOT"), item);
        }

        if (LatteMeetup.getPlugin().getItemsConfig().getBoolean("BOOLEANS.LEAVE-GAME-ITEM.ENABLED")) {
            ItemStack item = new ItemStack(LatteMeetup.getPlugin().getItemsConfig().getInteger("ITEMS.LEAVE-GAME-ITEM.ID"));

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatUtils.translate(LatteMeetup.getPlugin().getItemsConfig().getString("ITEMS.LEAVE-GAME-ITEM.NAME")));

            if (LatteMeetup.getPlugin().getItemsConfig().getBoolean("BOOLEANS.LEAVE-GAME-ITEM.LORE")) {
                meta.setLore(ChatUtils.translate(LatteMeetup.getPlugin().getItemsConfig().getStringList("ITEMS.LEAVE-GAME-ITEM.LORE")));
            }

            item.setItemMeta(meta);
            player.getInventory().setItem(LatteMeetup.getPlugin().getItemsConfig().getInteger("ITEMS.LEAVE-GAME-ITEM.SLOT"), item);
        }
        
        player.updateInventory();
    }
}
