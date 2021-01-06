package me.joesvart.lattemeetup.menu.buttons;

import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattemeetup.util.chat.CC;
import me.joesvart.lattemeetup.util.menu.Button;
import me.joesvart.lattemeetup.util.other.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class KillsButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerData playerData = PlayerData.getByName(player.getName());

        return new ItemBuilder(Material.BARRIER)
            .name(CC.translate("&2&lYour kills"))
            .lore(CC.translate("&7&m--------------------------"))
            .lore(CC.translate("&7Kills: &f") + playerData.getKills())
            .lore(CC.translate(""))
            .lore(CC.translate("&8The statistics will be updated"))
            .lore(CC.translate("&8after every game."))
            .lore(CC.translate("&7&m--------------------------"))
            .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        /* Do not nothing */
    }
}