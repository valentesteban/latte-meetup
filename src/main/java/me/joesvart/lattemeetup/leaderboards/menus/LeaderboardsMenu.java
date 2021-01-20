package me.joesvart.lattemeetup.leaderboards.menus;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattelibs.menu.Button;
import me.joesvart.lattelibs.menu.Menu;
import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.leaderboards.buttons.*;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class LeaderboardsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return ChatUtils.translate(LatteMeetup.getPlugin().getLeaderboardsConfig().getString("LEADERBOARDS.MENU-TITLE"));
    }

    @Override
    public int getSize() {
        return LatteMeetup.getPlugin().getLeaderboardsConfig().getInteger("LEADERBOARDS.MENU-SIZE");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        /**
         * Kills Button
         */
        if (LatteMeetup.getPlugin().getLeaderboardsConfig().getBoolean("KILLS-BUTTON.ENABLED")) {
            buttons.put(LatteMeetup.getPlugin().getLeaderboardsConfig().getInteger("KILLS-BUTTON.SLOT"), new KillsButton());
        }

        /**
         * Deaths Button
         */
        if (LatteMeetup.getPlugin().getLeaderboardsConfig().getBoolean("DEATHS-BUTTON.ENABLED")) {
            buttons.put(LatteMeetup.getPlugin().getLeaderboardsConfig().getInteger("DEATHS-BUTTON.SLOT"), new DeathsButton());
        }

        /**
         * Wins Button
         */
        if (LatteMeetup.getPlugin().getLeaderboardsConfig().getBoolean("WINS-BUTTON.ENABLED")) {
            buttons.put(LatteMeetup.getPlugin().getLeaderboardsConfig().getInteger("WINS-BUTTON.SLOT"), new WinsButton());
        }

        /**
         * Games played button
         */
        if (LatteMeetup.getPlugin().getLeaderboardsConfig().getBoolean("GAMES-PLAYED-BUTTON.ENABLED")) {
            buttons.put(LatteMeetup.getPlugin().getLeaderboardsConfig().getInteger("GAMES-PLAYED-BUTTON.SLOT"), new GamesPlayedButton());
        }

        /**
         * Back button
         */
        if (LatteMeetup.getPlugin().getLeaderboardsConfig().getBoolean("BACK-BUTTON.ENABLED")) {
            buttons.put(LatteMeetup.getPlugin().getLeaderboardsConfig().getInteger("BACK-BUTTON.SLOT"), new BackButton());
        }

        return buttons;
    }
}
