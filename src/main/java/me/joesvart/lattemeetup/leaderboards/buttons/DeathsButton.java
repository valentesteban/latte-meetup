package me.joesvart.lattemeetup.leaderboards.buttons;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattelibs.item.ItemCreator;
import me.joesvart.lattelibs.menu.Button;
import me.joesvart.lattemeetup.player.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class DeathsButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerData playerData = PlayerData.getByName(player.getName());

        return new ItemCreator(Material.SKULL_ITEM)
            .name(ChatUtils.translate("&2Your deaths"))
            .lore(ChatUtils.translate("&7&m--------------------------"))
            .lore(ChatUtils.translate("&7Deaths: &f") + playerData.getDeaths())
            .lore(ChatUtils.translate(""))
            .lore(ChatUtils.translate("&8The statistics will be updated"))
            .lore(ChatUtils.translate("&8after every game."))
            .lore(ChatUtils.translate("&7&m--------------------------"))
            .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        /* Do not nothing */
    }
}