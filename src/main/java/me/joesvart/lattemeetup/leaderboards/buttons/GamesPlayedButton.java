package me.joesvart.lattemeetup.leaderboards.buttons;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattelibs.item.ItemCreator;
import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattemeetup.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GamesPlayedButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerData playerData = PlayerData.getByName(player.getName());

        return new ItemCreator(Material.EXP_BOTTLE)
            .name(ChatUtils.translate("&2Your games"))
            .lore(ChatUtils.translate("&7&m--------------------------"))
            .lore(ChatUtils.translate("&7Games played: &f") + playerData.getPlayed())
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
