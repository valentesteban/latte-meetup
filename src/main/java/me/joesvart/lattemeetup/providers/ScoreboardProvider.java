package me.joesvart.lattemeetup.providers;

import lombok.Getter;
import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattelibs.scoreboard.BoardAdapter;
import me.joesvart.lattelibs.utils.PlayerUtils;
import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.game.GameData;
import me.joesvart.lattemeetup.game.GameManager;
import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattemeetup.util.chat.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardProvider implements BoardAdapter {

    @Getter
    private LatteMeetup plugin = LatteMeetup.getInstance();

    @Override
    public String getTitle(Player player) {
        return ChatUtils.translate(plugin.getScoreboardConfig().getString("SCOREBOARD.TITLE"));
    }

    @Override
    public List<String> getLines(Player player) {
        GameData data = GameManager.getData();

        switch (data.getGameState()) {
            case LOBBY: {
                return getLobbyBoard(player);
            }
            case STARTING: {
                return getStartingBoard(player);
            }
            case PLAYING: {
                return getPlayingBoard(player);
            }
            case ENDED: {
                return getEndedBoard(player);
            }
        }
        return null;
    }

    public List<String> getLobbyBoard(final Player player) {
        List<String> board = new ArrayList<>();

        GameData data = GameManager.getData();

        Integer integer = plugin.getMessagesConfig().getInteger("GAME.MIN-PLAYERS");

        for (String line : ChatUtils.translate(plugin.getScoreboardConfig().getStringList("SCOREBOARD.LOBBY"))) {
            line = line.replaceAll("<players>", String.valueOf(Bukkit.getOnlinePlayers().size()));
            line = line.replaceAll("<players-needed>", String.valueOf(integer - Bukkit.getOnlinePlayers().size()));
            line = line.replaceAll("<loading-animation>", GameManager.getData().getLoading());

            board.add(line);
        }

        return board;
    }

    public List<String> getStartingBoard(final Player player) {
        List<String> board = new ArrayList<>();

        GameData data = GameManager.getData();

        for (String line : ChatUtils.translate(plugin.getScoreboardConfig().getStringList("SCOREBOARD.STARTING"))) {
            line = line.replaceAll("<players>", String.valueOf(Bukkit.getOnlinePlayers().size()));
            line = line.replaceAll("<max-players>", String.valueOf(plugin.getMessagesConfig().getInteger("GAME.MAX-PLAYERS")));
            line = line.replaceAll("<starting-countdown>", StringUtils.niceTime(data.getStartingTime()) + GameManager.getData().getLoading());

            board.add(line);
        }

        return board;
    }

    public List<String> getPlayingBoard(final Player player) {
        List<String> board = new ArrayList<>();

        GameData data = GameManager.getData();
        PlayerData playerData = PlayerData.getByName(player.getName());

        for (String line : ChatUtils.translate(plugin.getScoreboardConfig().getStringList("SCOREBOARD.PLAYING"))) {
            line = line.replaceAll("<remaining>", "" + PlayerData.getAlivePlayers());
            line = line.replaceAll("<kills>", "" + playerData.getGameKills());
            line = line.replaceAll("<player-ping>", "" + PlayerUtils.getPing(player));
            line = line.replaceAll("<border>", "" + data.getBorder() + data.getFormattedBorderStatus());
            line = line.replaceAll("<max-players>", String.valueOf(plugin.getMessagesConfig().getInteger("GAME.MAX-PLAYERS")));

            board.add(line);
        }

        return board;
    }

    public List<String> getEndedBoard(final Player player) {
        List<String> board = new ArrayList<>();

        GameData data = GameManager.getData();
        PlayerData playerData = PlayerData.getByName(player.getName());

        for (String line : ChatUtils.translate(plugin.getScoreboardConfig().getStringList("SCOREBOARD.ENDED"))) {
            line = line.replaceAll("<winner>", "" + data.getWinner());

            board.add(line);
        }

        return board;
    }
}
