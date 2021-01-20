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

public class KillsButton extends Button {

    @Getter
    private LatteMeetup plugin = LatteMeetup.getInstance();

    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerData playerData = PlayerData.getByName(player.getName());

        List<String> lore = new ArrayList<>();
        for (String kills : ChatUtils.translate(plugin.getLeaderboardsConfig().getStringList("KILLS-BUTTON.LORE"))) {
            kills = kills.replaceAll("<kills>", String.valueOf(playerData.getKills()));

            lore.add(kills);
        }

        return new ItemCreator(Material.IRON_SWORD)
            .name(ChatUtils.translate(plugin.getLeaderboardsConfig().getString("KILLS-BUTTON.NAME")))
            .lore(lore)
            .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        /* Do not nothing */
    }
}