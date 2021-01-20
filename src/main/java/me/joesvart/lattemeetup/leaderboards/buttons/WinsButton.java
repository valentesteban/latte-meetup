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

public class WinsButton extends Button {

    @Getter
    private LatteMeetup plugin = LatteMeetup.getInstance();

    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerData playerData = PlayerData.getByName(player.getName());

        List<String> lore = new ArrayList<>();
        for (String wins : ChatUtils.translate(plugin.getLeaderboardsConfig().getStringList("WINS-BUTTON.LORE"))) {
            wins = wins.replaceAll("<wins>", String.valueOf(playerData.getWins()));

            lore.add(wins);
        }

        return new ItemCreator(Material.GOLDEN_APPLE)
            .name(ChatUtils.translate(plugin.getLeaderboardsConfig().getString("WINS-BUTTON.NAME")))
            .lore(lore)
            .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        /* Do not nothing */
    }
}