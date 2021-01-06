package me.joesvart.lattemeetup.providers;

import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.game.GameData;
import me.joesvart.lattemeetup.game.GameManager;
import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattemeetup.util.chat.CC;
import me.joesvart.lattemeetup.util.player.PlayerUtils;
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
        return CC.translate("&2&lLatteMeetup");
    }

    @Override
    public String getFooter(Player player) {
        return CC.translate("&7There are currently &f" + Bukkit.getOnlinePlayers().size() + " &7players in the Meetup game.");
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
            case WINNER: {
                return getWinnerTab(player);
            }
            case VOTE:
                return getVoteTab(player);
        }
        return null;
    }

    public List<TabEntry> getVoteTab(Player player) {
        List<TabEntry> tabEntries = new ArrayList<>();

        GameData data = GameManager.getData();
        PlayerData playerData = PlayerData.getByName(player.getName());

        /* First Column */

        // Game State
        if(Bukkit.getOnlinePlayers().size() < LatteMeetup.MIN_PLAYERS) {
            tabEntries.add(new TabEntry(0, 5, CC.translate("&2Game state")));
            tabEntries.add(new TabEntry(0, 6, CC.translate("&aWaiting for players") + GameManager.getData().getLoading()));
        }
        else {
            tabEntries.add(new TabEntry(0, 5, CC.translate("&2Game state")));
            tabEntries.add(new TabEntry(0, 6, CC.translate("&cUnknown")));
        }

        // Store
        tabEntries.add(new TabEntry(0, 17, CC.translate("&aStore")));
        tabEntries.add(new TabEntry(0, 18, CC.translate("&fstore.lattemeetup.us")).setSkin(Skin.SHOP_SKIN));

        /* Second Column */

        // Player Information
        tabEntries.add(new TabEntry(1, 2, CC.translate("&2&lYou")));
        tabEntries.add(new TabEntry(1, 3, CC.translate("&f") + player.getDisplayName()).setSkin(Skin.getPlayer(player)));

        tabEntries.add(new TabEntry(1, 5, CC.translate("&7Your ping: &f") + PlayerUtils.getPing(player)));

        // Discord
        tabEntries.add(new TabEntry(1, 17, CC.translate("&9Discord")));
        tabEntries.add(new TabEntry(1, 18, CC.translate("&fdiscord.lattemeetup.us")).setSkin(Skin.DISCORD_SKIN));

        /* Third column */

        // Twitter
        tabEntries.add(new TabEntry(2, 17, CC.translate("&bTwitter")));
        tabEntries.add(new TabEntry(2, 18, CC.translate("&f@LatteMeetup")).setSkin(Skin.TWITTER_SKIN));

        return tabEntries;
    }

    public List<TabEntry> getStartingTab(Player player) {
        List<TabEntry> tabEntries = new ArrayList<>();

        GameData data = GameManager.getData();
        PlayerData playerData = PlayerData.getByName(player.getName());

        /* First Column */

        // Game state
        tabEntries.add(new TabEntry(0, 5, CC.translate("&2Game state")));
        tabEntries.add(new TabEntry(0, 6, CC.translate("&eStarting") + GameManager.getData().getLoading()));

        // Store
        tabEntries.add(new TabEntry(0, 17, CC.translate("&aStore")));
        tabEntries.add(new TabEntry(0, 18, CC.translate("&fstore.lattemeetup.us")).setSkin(Skin.SHOP_SKIN));

        /* Second Column */

        // Player Information
        tabEntries.add(new TabEntry(1, 2, CC.translate("&2&lYou")));
        tabEntries.add(new TabEntry(1, 3, CC.translate("&f") + player.getDisplayName()).setSkin(Skin.getPlayer(player)));

        tabEntries.add(new TabEntry(1, 5, CC.translate("&7Your kills: &f") + playerData.getGameKills()));
        tabEntries.add(new TabEntry(1, 6, CC.translate("&7Your ping: &f") + PlayerUtils.getPing(player)));

        // Discord
        tabEntries.add(new TabEntry(1, 17, CC.translate("&9Discord")));
        tabEntries.add(new TabEntry(1, 18, CC.translate("&fdiscord.lattemeetup.us")).setSkin(Skin.DISCORD_SKIN));

        /* Third column */

        // Game Information
        tabEntries.add(new TabEntry(2, 5, CC.translate("&2Game Information")));
        tabEntries.add(new TabEntry(2, 7, CC.translate("&7Players playing: &f") + PlayerData.getAlivePlayers()));
        tabEntries.add(new TabEntry(2, 8, CC.translate("&7Border size: &f") + data.getBorder()));

        // Twitter
        tabEntries.add(new TabEntry(2, 17, CC.translate("&bTwitter")));
        tabEntries.add(new TabEntry(2, 18, CC.translate("&f@LatteMeetup")).setSkin(Skin.TWITTER_SKIN));

        return tabEntries;
    }

    public List<TabEntry> getPlayingTab(Player player) {
        List<TabEntry> tabEntries = new ArrayList<>();

        GameData data = GameManager.getData();
        PlayerData playerData = PlayerData.getByName(player.getName());

        /* First Column */

        // Game state
        tabEntries.add(new TabEntry(0, 5, CC.translate("&2Game state")));
        tabEntries.add(new TabEntry(0, 6, CC.translate("&ePlaying") + GameManager.getData().getLoading()));

        // Store
        tabEntries.add(new TabEntry(0, 17, CC.translate("&aStore")));
        tabEntries.add(new TabEntry(0, 18, CC.translate("&fstore.lattemeetup.us")).setSkin(Skin.SHOP_SKIN));

        /* Second Column */

        // Player Information
        tabEntries.add(new TabEntry(1, 2, CC.translate("&2&lYou")));
        tabEntries.add(new TabEntry(1, 3, CC.translate("&b") + player.getDisplayName()).setSkin(Skin.getPlayer(player)));

        tabEntries.add(new TabEntry(1, 5, CC.translate("&7Your kills: &f") + playerData.getGameKills()));
        tabEntries.add(new TabEntry(1, 6, CC.translate("&7Your ping: &f") + PlayerUtils.getPing(player)));

        // Discord
        tabEntries.add(new TabEntry(1, 17, CC.translate("&9Discord")));
        tabEntries.add(new TabEntry(1, 18, CC.translate("&fdiscord.lattemeetup.us")).setSkin(Skin.DISCORD_SKIN));

        /* Third column */

        // Game Information
        tabEntries.add(new TabEntry(2, 5, CC.translate("&2Game Information")));
        tabEntries.add(new TabEntry(2, 7, CC.translate("&7Players playing: &f") + PlayerData.getAlivePlayers()));
        tabEntries.add(new TabEntry(2, 8, CC.translate("&7Border size: &f") + data.getBorder()));

        // Twitter
        tabEntries.add(new TabEntry(2, 17, CC.translate("&bTwitter")));
        tabEntries.add(new TabEntry(2, 18, CC.translate("&f@LatteMeetup")).setSkin(Skin.TWITTER_SKIN));

        return tabEntries;
    }

    public List<TabEntry> getWinnerTab(Player player) {
        List<TabEntry> tabEntries = new ArrayList<>();

        GameData data = GameManager.getData();
        PlayerData playerData = PlayerData.getByName(player.getName());

        /* First Column */

        // Game state
        tabEntries.add(new TabEntry(0, 5, CC.translate("&2Game state")));
        tabEntries.add(new TabEntry(0, 6, CC.translate("&cEnded") + GameManager.getData().getLoading()));

        // Store
        tabEntries.add(new TabEntry(0, 17, CC.translate("&aStore")));
        tabEntries.add(new TabEntry(0, 18, CC.translate("&fstore.lattemeetup.us")).setSkin(Skin.SHOP_SKIN));

        /* Second Column */

        // Player Information
        tabEntries.add(new TabEntry(1, 2, CC.translate("&2&lYou")));
        tabEntries.add(new TabEntry(1, 3, CC.translate("&f") + player.getDisplayName()).setSkin(Skin.getPlayer(player)));

        tabEntries.add(new TabEntry(1, 5, CC.translate("&7Your kills: &f") + playerData.getGameKills()));
        tabEntries.add(new TabEntry(1, 6, CC.translate("&7Your ping: &f") + PlayerUtils.getPing(player)));

        // Discord
        tabEntries.add(new TabEntry(1, 17, CC.translate("&9Discord")));
        tabEntries.add(new TabEntry(1, 18, CC.translate("&fdiscord.lattemeetup.us")).setSkin(Skin.DISCORD_SKIN));

        /* Third column */

        // Game Information
        tabEntries.add(new TabEntry(2, 5, CC.translate("&2Game Winner")));
        tabEntries.add(new TabEntry(2, 7, CC.translate("&7Winner: &f") + data.getWinner()));

        // Twitter
        tabEntries.add(new TabEntry(2, 17, CC.translate("&bTwitter")));
        tabEntries.add(new TabEntry(2, 18, CC.translate("&f@LatteMeetup")).setSkin(Skin.TWITTER_SKIN));

        return tabEntries;
    }
}