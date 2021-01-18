package me.joesvart.lattemeetup.leaderboards.menus;

import me.joesvart.lattelibs.menu.Button;
import me.joesvart.lattelibs.menu.Menu;
import me.joesvart.lattemeetup.leaderboards.buttons.*;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class LeaderboardsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Statistics menu";
    }

    @Override
    public int getSize() {
        return 6 * 9;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        /**
         * Buttons for the
         * statistics menu
         */
        buttons.put(19, new KillsButton());
        buttons.put(21, new DeathsButton());
        buttons.put(23, new WinsButton());
        buttons.put(25, new GamesPlayedButton());

        buttons.put(40, new BackButton());

        return buttons;
    }
}
