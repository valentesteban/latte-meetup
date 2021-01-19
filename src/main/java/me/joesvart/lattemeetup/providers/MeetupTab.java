package me.joesvart.lattemeetup.providers;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattelibs.utils.PlayerUtils;
import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.game.GameData;
import me.joesvart.lattemeetup.game.GameManager;
import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattetab.adapter.TabAdapter;
import me.joesvart.lattetab.entry.TabEntry;
import me.joesvart.lattetab.skin.Skin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MeetupTab implements TabAdapter {

    private final LatteMeetup plugin = LatteMeetup.getInstance();

    @Override
    public String getHeader(Player player) {
        return ChatUtils.translate("&2&lLatteMeetup");
    }

    @Override
    public String getFooter(Player player) {
        return ChatUtils.translate("&7There are currently &f" + Bukkit.getOnlinePlayers().size() + " &7players in the Meetup game.");
    }

    @Override
    public List<TabEntry> getLines(Player player) {
        List<TabEntry> tabEntries = new ArrayList<>();

        GameData data = GameManager.getData();

        if (player == null) {
            return null;
        }

        switch (data.getGameState()) {
            case STARTING: {
                return getStartingTab(player);
            }
            case PLAYING: {
                return getPlayingTab(player);
            }
            case ENDED: {
                return getWinnerTab(player);
            }
            case LOBBY:
                return getLobbyTab(player);
        }
        return null;
    }

    public List<TabEntry> getLobbyTab(Player player) {
        List<TabEntry> tabEntries = new ArrayList<>();

        GameData data = GameManager.getData();
        PlayerData playerData = PlayerData.getByName(player.getName());

        /* First Column */

        // Game State
        if(Bukkit.getOnlinePlayers().size() < plugin.getMessagesConfig().getInteger("GAME.MIN-PLAYERS")) {
            tabEntries.add(new TabEntry(0, 5, ChatUtils.translate("&2Game state")));
            tabEntries.add(new TabEntry(0, 6, ChatUtils.translate("&aWaiting for players") + GameManager.getData().getLoading()));
        }
        else {
            tabEntries.add(new TabEntry(0, 5, ChatUtils.translate("&2Game state")));
            tabEntries.add(new TabEntry(0, 6, ChatUtils.translate("&eStarting") + GameManager.getData().getLoading()));
        }

        // Store
        tabEntries.add(new TabEntry(0, 17, ChatUtils.translate("&aStore")));
        tabEntries.add(new TabEntry(0, 18, ChatUtils.translate("&fstore.lattemeetup.us")).setSkin(Skin.SHOP_SKIN));

        /* Second Column */

        // Player Information
        tabEntries.add(new TabEntry(1, 2, ChatUtils.translate("&2&lYou")));
        tabEntries.add(new TabEntry(1, 3, ChatUtils.translate("&f") + player.getDisplayName()).setSkin(Skin.getPlayer(player)));

        tabEntries.add(new TabEntry(1, 5, ChatUtils.translate("&7Your ping: &f") + PlayerUtils.getPing(player)));

        // Discord
        tabEntries.add(new TabEntry(1, 17, ChatUtils.translate("&9Discord")));
        tabEntries.add(new TabEntry(1, 18, ChatUtils.translate("&fdiscord.lattemeetup.us")).setSkin(Skin.DISCORD_SKIN));

        /* Third column */

        // Beta warning
        tabEntries.add(new TabEntry(2, 5, ChatUtils.translate("&4&lWARNING")));
        tabEntries.add(new TabEntry(2, 7, ChatUtils.translate("&cThis plugin is a beta and")));
        tabEntries.add(new TabEntry(2, 8, ChatUtils.translate("&cis under development")));
        tabEntries.add(new TabEntry(2, 9, ChatUtils.translate("&cif you find some bug")));
        tabEntries.add(new TabEntry(2, 10, ChatUtils.translate("&cplease contact with")));
        tabEntries.add(new TabEntry(2, 11, ChatUtils.translate("&cthe Network.")));

        // Twitter
        tabEntries.add(new TabEntry(2, 17, ChatUtils.translate("&bTwitter")));
        tabEntries.add(new TabEntry(2, 18, ChatUtils.translate("&f@LatteMeetup")).setSkin(Skin.TWITTER_SKIN));

        return tabEntries;
    }

    public List<TabEntry> getStartingTab(Player player) {
        List<TabEntry> tabEntries = new ArrayList<>();

        GameData data = GameManager.getData();
        PlayerData playerData = PlayerData.getByName(player.getName());

        /* First Column */

        // Game state
        tabEntries.add(new TabEntry(0, 5, ChatUtils.translate("&2Game state")));
        tabEntries.add(new TabEntry(0, 6, ChatUtils.translate("&eStarting") + GameManager.getData().getLoading()));

        // Store
        tabEntries.add(new TabEntry(0, 17, ChatUtils.translate("&aStore")));
        tabEntries.add(new TabEntry(0, 18, ChatUtils.translate("&fstore.lattemeetup.us")).setSkin(Skin.SHOP_SKIN));

        /* Second Column */

        // Player Information
        tabEntries.add(new TabEntry(1, 2, ChatUtils.translate("&2&lYou")));
        tabEntries.add(new TabEntry(1, 3, ChatUtils.translate("&f") + player.getDisplayName()).setSkin(Skin.getPlayer(player)));

        tabEntries.add(new TabEntry(1, 5, ChatUtils.translate("&7Your kills: &f") + playerData.getGameKills()));
        tabEntries.add(new TabEntry(1, 6, ChatUtils.translate("&7Your ping: &f") + PlayerUtils.getPing(player)));

        // Discord
        tabEntries.add(new TabEntry(1, 17, ChatUtils.translate("&9Discord")));
        tabEntries.add(new TabEntry(1, 18, ChatUtils.translate("&fdiscord.lattemeetup.us")).setSkin(Skin.DISCORD_SKIN));

        /* Third column */

        // Game Information
        tabEntries.add(new TabEntry(2, 5, ChatUtils.translate("&2Game Information")));
        tabEntries.add(new TabEntry(2, 7, ChatUtils.translate("&7Players playing: &f") + PlayerData.getAlivePlayers()));
        tabEntries.add(new TabEntry(2, 8, ChatUtils.translate("&7Border size: &f") + data.getBorder()));

        // Twitter
        tabEntries.add(new TabEntry(2, 17, ChatUtils.translate("&bTwitter")));
        tabEntries.add(new TabEntry(2, 18, ChatUtils.translate("&f@LatteMeetup")).setSkin(Skin.TWITTER_SKIN));

        return tabEntries;
    }

    public List<TabEntry> getPlayingTab(Player player) {
        List<TabEntry> tabEntries = new ArrayList<>();

        GameData data = GameManager.getData();
        PlayerData playerData = PlayerData.getByName(player.getName());

        /* First Column */

        // Game state
        tabEntries.add(new TabEntry(0, 5, ChatUtils.translate("&2Game state")));
        tabEntries.add(new TabEntry(0, 6, ChatUtils.translate("&ePlaying") + GameManager.getData().getLoading()));

        // Store
        tabEntries.add(new TabEntry(0, 17, ChatUtils.translate("&aStore")));
        tabEntries.add(new TabEntry(0, 18, ChatUtils.translate("&fstore.lattemeetup.us")).setSkin(Skin.SHOP_SKIN));

        /* Second Column */

        // Player Information
        tabEntries.add(new TabEntry(1, 2, ChatUtils.translate("&2&lYou")));
        tabEntries.add(new TabEntry(1, 3, ChatUtils.translate("&f") + player.getDisplayName()).setSkin(Skin.getPlayer(player)));

        tabEntries.add(new TabEntry(1, 5, ChatUtils.translate("&7Your kills: &f") + playerData.getGameKills()));
        tabEntries.add(new TabEntry(1, 6, ChatUtils.translate("&7Your ping: &f") + PlayerUtils.getPing(player)));

        // Discord
        tabEntries.add(new TabEntry(1, 17, ChatUtils.translate("&9Discord")));
        tabEntries.add(new TabEntry(1, 18, ChatUtils.translate("&fdiscord.lattemeetup.us")).setSkin(Skin.DISCORD_SKIN));

        /* Third column */

        // Game Information
        tabEntries.add(new TabEntry(2, 5, ChatUtils.translate("&2Game Information")));
        tabEntries.add(new TabEntry(2, 7, ChatUtils.translate("&7Players playing: &f") + PlayerData.getAlivePlayers()));
        tabEntries.add(new TabEntry(2, 8, ChatUtils.translate("&7Border size: &f") + data.getBorder()));

        // Twitter
        tabEntries.add(new TabEntry(2, 17, ChatUtils.translate("&bTwitter")));
        tabEntries.add(new TabEntry(2, 18, ChatUtils.translate("&f@LatteMeetup")).setSkin(Skin.TWITTER_SKIN));

        return tabEntries;
    }

    public List<TabEntry> getWinnerTab(Player player) {
        List<TabEntry> tabEntries = new ArrayList<>();

        GameData data = GameManager.getData();
        PlayerData playerData = PlayerData.getByName(player.getName());

        /* First Column */

        // Game state
        tabEntries.add(new TabEntry(0, 5, ChatUtils.translate("&2Game state")));
        tabEntries.add(new TabEntry(0, 6, ChatUtils.translate("&cEnded") + GameManager.getData().getLoading()));

        // Store
        tabEntries.add(new TabEntry(0, 17, ChatUtils.translate("&aStore")));
        tabEntries.add(new TabEntry(0, 18, ChatUtils.translate("&fstore.lattemeetup.us")).setSkin(Skin.SHOP_SKIN));

        /* Second Column */

        // Player Information
        tabEntries.add(new TabEntry(1, 2, ChatUtils.translate("&2&lYou")));
        tabEntries.add(new TabEntry(1, 3, ChatUtils.translate("&f") + player.getDisplayName()).setSkin(Skin.getPlayer(player)));

        tabEntries.add(new TabEntry(1, 5, ChatUtils.translate("&7Your kills: &f") + playerData.getGameKills()));
        tabEntries.add(new TabEntry(1, 6, ChatUtils.translate("&7Your ping: &f") + PlayerUtils.getPing(player)));

        // Discord
        tabEntries.add(new TabEntry(1, 17, ChatUtils.translate("&9Discord")));
        tabEntries.add(new TabEntry(1, 18, ChatUtils.translate("&fdiscord.lattemeetup.us")).setSkin(Skin.DISCORD_SKIN));

        /* Third column */

        // Game Information
        tabEntries.add(new TabEntry(2, 5, ChatUtils.translate("&2Game Winner")));
        tabEntries.add(new TabEntry(2, 7, ChatUtils.translate("&7Winner: &f") + data.getWinner()));

        // Twitter
        tabEntries.add(new TabEntry(2, 17, ChatUtils.translate("&bTwitter")));
        tabEntries.add(new TabEntry(2, 18, ChatUtils.translate("&f@LatteMeetup")).setSkin(Skin.TWITTER_SKIN));

        return tabEntries;
    }
}