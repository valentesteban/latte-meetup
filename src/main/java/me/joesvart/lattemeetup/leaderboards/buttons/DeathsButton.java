package me.joesvart.lattemeetup.leaderboards.buttons;

import lombok.Getter;
import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattelibs.item.ItemCreator;
import me.joesvart.lattelibs.menu.Button;
import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.player.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DeathsButton extends Button {

    @Getter
    private LatteMeetup plugin = LatteMeetup.getInstance();

    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerData playerData = PlayerData.getByName(player.getName());

        List<String> lore = new ArrayList<>();
        for (String deaths : ChatUtils.translate(plugin.getLeaderboardsConfig().getStringList("DEATHS-BUTTON.LORE"))) {
            deaths = deaths.replaceAll("<deaths>", String.valueOf(playerData.getDeaths()));

            lore.add(deaths);
        }

        return new ItemCreator(Material.SKULL_ITEM)
            .name(ChatUtils.translate(plugin.getLeaderboardsConfig().getString("DEATHS-BUTTON.NAME")))
            .lore(lore)
            .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        /* Do not nothing */
    }
}