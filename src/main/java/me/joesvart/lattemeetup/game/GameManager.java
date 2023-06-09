package me.joesvart.lattemeetup.game;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattelibs.chat.Clickable;
import me.joesvart.lattelibs.utils.PlayerUtils;
import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattemeetup.player.PlayerState;
import me.joesvart.lattemeetup.scenario.Scenario;
import me.joesvart.lattemeetup.tasks.EndTask;
import me.joesvart.lattemeetup.tasks.BorderTask;
import me.joesvart.lattemeetup.tasks.StartingTask;
import me.joesvart.lattemeetup.tasks.WorldGenTask;
import lombok.Getter;
import me.joesvart.lattemeetup.util.chat.Msg;
import me.joesvart.lattemeetup.util.other.*;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private LatteMeetup plugin = LatteMeetup.getInstance();

    @Getter
    private static GameData data = new GameData();

    @Getter
    private List<Material> whitelistedBlocks = new ArrayList<>();

    private boolean broadcasted;

    public GameManager() {
        new WorldGenTask(this).runTaskTimer(LatteMeetup.getInstance(), 0L, 20L);
    }

    public void handleStart() {
        GameManager.getData().setGameState(GameState.PLAYING);

        Scenario scenario = plugin.getVoteManager().getHighestVote();
        scenario.setActive(true);
        scenario.handleToggle();

        /* Game successfully started message */
        Msg.sendMessage("");
        String format = ChatUtils.translate(plugin.getMessagesConfig().getString("MESSAGES.GAME-SCENARIO")
            .replace("<scenario>", scenario.getName())
            .replace("<votes>", "" + plugin.getVoteManager().getVotes().get(scenario)));
        Msg.sendMessage(format);
        Msg.sendMessage("");

        Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerData data = PlayerData.getByName(player.getName());
            data.setPlayed(data.getPlayed() + 1);
            data.setPlayerState(PlayerState.PLAYING);
            player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1F, 1F);

            /* Send a title saying the game has started */
            if (plugin.getMessagesConfig().getBoolean("BOOLEANS.TITLES")) {
                player.sendTitle(ChatUtils.GREEN + ChatUtils.B + "BEGUN", ChatUtils.YELLOW + "Good luck!");
            }
        });

        /* Game error message */
        if (Bukkit.getOnlinePlayers().size() < plugin.getMessagesConfig().getInteger("GAME.MIN-PLAYERS")) {
            Bukkit.broadcastMessage(ChatUtils.translate(""));
            Bukkit.broadcastMessage(ChatUtils.translate("&8[&4Error&8] &7The game cannot continue as there are not enough players to play!"));
            Bukkit.broadcastMessage(ChatUtils.translate(""));

            plugin.getGameManager().handleCheckWinners();
        }

        data.setScenario(scenario.getName());
        data.setRemaining(PlayerData.getAlivePlayers());
        data.setInitial(PlayerData.getAlivePlayers());

        new BorderTask();
    }

    public void handleStarting() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            MeetupUtils.clearPlayer(player);
            player.teleport(MeetupUtils.getScatterLocation());
            plugin.getKitManager().handleItems(player);
        });

        GameManager.getData().setGameState(GameState.STARTING);
        new StartingTask();
    }

    public void handleCheckWinners() {
        if(!GameManager.getData().getGameState().equals(GameState.PLAYING)) {
            return;
        }

        if(PlayerData.getAlivePlayers() == 1 && !broadcasted) {
            PlayerData.getListOfAlivePlayers().forEach(this::handleSelectWinner);
        }
    }

    private void handleSelectWinner(PlayerData winner) {
        broadcasted = true;

        GameData data = GameManager.getData();

        /* Add wins to the profile of the player */
        winner.setWins(winner.getWins() + 1);
        data.setWinner(winner.getName());

        /* Game successfully ended message */
        for (String line : ChatUtils.translate(plugin.getMessagesConfig().getStringList("WINNER-MESSAGE"))) {
            line = line.replaceAll("<winner>", data.getWinner());
            line = line.replaceAll("<winner-kills>", String.valueOf(winner.getGameKills()));
            line = line.replaceAll("<winner-total-wins>", String.valueOf(winner.getWins()));

            Msg.sendMessage(line);
        }

        /* Set the game state */
        data.setGameState(GameState.ENDED);

        new EndTask();
    }

    public void handleSetWhitelistedBlocks() {
        whitelistedBlocks.add(Material.LOG);
        whitelistedBlocks.add(Material.LOG_2);
        whitelistedBlocks.add(Material.WOOD);
        whitelistedBlocks.add(Material.LEAVES);
        whitelistedBlocks.add(Material.LEAVES_2);
        whitelistedBlocks.add(Material.WATER);
        whitelistedBlocks.add(Material.STATIONARY_WATER);
        whitelistedBlocks.add(Material.LAVA);
        whitelistedBlocks.add(Material.STATIONARY_LAVA);
        whitelistedBlocks.add(Material.LONG_GRASS);
        whitelistedBlocks.add(Material.YELLOW_FLOWER);
        whitelistedBlocks.add(Material.COBBLESTONE);
        whitelistedBlocks.add(Material.CACTUS);
        whitelistedBlocks.add(Material.SUGAR_CANE_BLOCK);
        whitelistedBlocks.add(Material.DOUBLE_PLANT);
        whitelistedBlocks.add(Material.OBSIDIAN);
        whitelistedBlocks.add(Material.SNOW);
        whitelistedBlocks.add(Material.YELLOW_FLOWER);
        whitelistedBlocks.add(Material.RED_ROSE);
        whitelistedBlocks.add(Material.BROWN_MUSHROOM);
        whitelistedBlocks.add(Material.WEB);
        whitelistedBlocks.add(Material.ANVIL);
        whitelistedBlocks.add(Material.DEAD_BUSH);
        whitelistedBlocks.add(Material.RED_MUSHROOM);
        whitelistedBlocks.add(Material.HUGE_MUSHROOM_1);
        whitelistedBlocks.add(Material.HUGE_MUSHROOM_2);
    }

    /* Basically load all the chunks so you don't have problems */
    public void handleLoadChunks() {
        Bukkit.getScheduler().runTaskLater(LatteMeetup.getInstance(), () -> {
            for(int x = -110; x < 110; x++) {
                for(int z = -110; z < 110; z++) {
                    Location location = new Location(Bukkit.getWorld("meetup_world"), x, 60, z);

                    if(!location.getChunk().isLoaded()) {
                        location.getWorld().loadChunk(x, z);
                    }
                }
            }
        }, 100L);

        Bukkit.getScheduler().runTaskLater(LatteMeetup.getInstance(), () ->
                GameManager.getData().setGenerated(true), 200L);
    }
}
