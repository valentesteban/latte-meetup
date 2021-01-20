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

public class GamesPlayedButton extends Button {

    @Getter
    private LatteMeetup plugin = LatteMeetup.getInstance();

    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerData playerData = PlayerData.getByName(player.getName());

        List<String> lore = new ArrayList<>();
        for (String played : ChatUtils.translate(plugin.getLeaderboardsConfig().getStringList("GAMES-PLAYED-BUTTON.LORE"))) {
            played = played.replaceAll("<games-played>", String.valueOf(playerData.getPlayed()));

            lore.add(played);
        }

        return new ItemCreator(Material.EXP_BOTTLE)
            .name(ChatUtils.translate(plugin.getLeaderboardsConfig().getString("GAMES-PLAYED-BUTTON.NAME")))
            .lore(lore)
            .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        /* Do not nothing */
    }
}
