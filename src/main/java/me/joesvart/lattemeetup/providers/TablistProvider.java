package me.joesvart.lattemeetup.providers;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattelibs.utils.PlayerUtils;
import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.game.GameData;
import me.joesvart.lattemeetup.game.GameManager;
import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattetab.adapter.TabAdapter;
import me.joesvart.lattetab.entry.TabEntry;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TablistProvider implements TabAdapter {

    private final LatteMeetup plugin = LatteMeetup.getInstance();

    @Override
    public String getHeader(Player player) {
        return ChatUtils.translate(plugin.getTablistConfig().getString("TABLIST.HEADER"));
    }

    @Override
    public String getFooter(Player player) {
        return ChatUtils.translate(plugin.getTablistConfig().getString("TABLIST.FOOTER"));
    }

    @Override
    public List<TabEntry> getLines(Player player) {
        List<TabEntry> tabEntries = new ArrayList();

        GameData data = GameManager.getData();

        switch (data.getGameState()) {
            case LOBBY: {
                return getLobbyTab(player);
            }
            case STARTING: {
                return getStartingTab(player);
            }
            case PLAYING: {
                return getPlayingTab(player);
            }
            case ENDED: {
                return getEndedTab(player);
            }
        }
        return null;
    }

    public List<TabEntry> getLobbyTab(Player player) {
        List<TabEntry> tabEntries = new ArrayList<>();

        for (int i = 0; i <= 19; ++i) {
            tabEntries.add(new TabEntry(0, i, replace(plugin.getTablistConfig().getStringList("TABLIST.LOBBY.LEFT").get(i), player)));
            tabEntries.add(new TabEntry(1, i, replace(plugin.getTablistConfig().getStringList("TABLIST.LOBBY.CENTER").get(i), player)));
            tabEntries.add(new TabEntry(2, i, replace(plugin.getTablistConfig().getStringList("TABLIST.LOBBY.RIGHT").get(i), player)));
        }

        return tabEntries;
    }

    public List<TabEntry> getStartingTab(Player player) {
        List<TabEntry> tabEntries = new ArrayList<>();

        for (int i = 0; i <= 19; ++i) {
            tabEntries.add(new TabEntry(0, i, replace(plugin.getTablistConfig().getStringList("TABLIST.STARTING.LEFT").get(i), player)));
            tabEntries.add(new TabEntry(1, i, replace(plugin.getTablistConfig().getStringList("TABLIST.STARTING.CENTER").get(i), player)));
            tabEntries.add(new TabEntry(2, i, replace(plugin.getTablistConfig().getStringList("TABLIST.STARTING.RIGHT").get(i), player)));
        }

        return tabEntries;
    }

    public List<TabEntry> getPlayingTab(Player player) {
        List<TabEntry> tabEntries = new ArrayList<>();

        for (int i = 0; i <= 19; ++i) {
            tabEntries.add(new TabEntry(0, i, replace(plugin.getTablistConfig().getStringList("TABLIST.PLAYING.LEFT").get(i), player)));
            tabEntries.add(new TabEntry(1, i, replace(plugin.getTablistConfig().getStringList("TABLIST.PLAYING.CENTER").get(i), player)));
            tabEntries.add(new TabEntry(2, i, replace(plugin.getTablistConfig().getStringList("TABLIST.PLAYING.RIGHT").get(i), player)));
        }

        return tabEntries;
    }

    public List<TabEntry> getEndedTab(Player player) {
        List<TabEntry> tabEntries = new ArrayList<>();

        for (int i = 0; i <= 19; ++i) {
            tabEntries.add(new TabEntry(0, i, replace(plugin.getTablistConfig().getStringList("TABLIST.ENDED.LEFT").get(i), player)));
            tabEntries.add(new TabEntry(1, i, replace(plugin.getTablistConfig().getStringList("TABLIST.ENDED.CENTER").get(i), player)));
            tabEntries.add(new TabEntry(2, i, replace(plugin.getTablistConfig().getStringList("TABLIST.ENDED.RIGHT").get(i), player)));
        }

        return tabEntries;
    }

    public String replace(String input, Player player) {
        GameData data = GameManager.getData();
        PlayerData playerData = PlayerData.getByName(player.getName());

        input = input.replace("<loading-animation>", String.valueOf(data.getLoading()));
        input = input.replace("<player-ping>", String.valueOf(PlayerUtils.getPing(player)));
        input = input.replace("<player-name>", String.valueOf(player.getName()));
        input = input.replace("<player-kills>", String.valueOf(playerData.getGameKills()));
        input = input.replace("<alive-players>", String.valueOf(PlayerData.getAlivePlayers()));
        input = input.replace("<border-size>", String.valueOf(data.getBorder()));
        input = input.replace("<winner>", String.valueOf(data.getWinner()));

        return input;
    }


}