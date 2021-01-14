package me.joesvart.lattemeetup.providers;

import lombok.Getter;
import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattelibs.scoreboard.BoardAdapter;
import me.joesvart.lattelibs.utils.AnimationUtils;
import me.joesvart.lattelibs.utils.PlayerUtils;
import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.game.GameData;
import me.joesvart.lattemeetup.game.GameManager;
import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattemeetup.scenario.Scenario;
import me.joesvart.lattemeetup.scenario.ScenarioManager;
import me.joesvart.lattemeetup.util.chat.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MeetupBoard implements BoardAdapter {

	@Getter
	private LatteMeetup plugin = LatteMeetup.getInstance();

	Date date = new Date();
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	public String getTitle(Player player) {
		return ChatUtils.D_GREEN + ChatUtils.B + "UHC Meetup";
	}

	@Override
	public List<String> getLines(Player player) {
		GameData data = GameManager.getData();

		switch (data.getGameState()) {
			case STARTING: {
				return getStartingBoard(player);
			}
			case PLAYING: {
				return getPlayingBoard(player);
			}
			case WINNER: {
				return getWinnerBoard(player);
			}
			case VOTE:
				return getLobbyBoard(player);
		}
		return null;
	}

	public List<String> getLobbyBoard(final Player player) {
		List<String> board = new ArrayList<>();

		GameData data = GameManager.getData();

		board.add(ChatUtils.GRAY + ChatUtils.STRIKE_THROUGH + ("&7&m--------------------"));
		board.add(ChatUtils.D_GRAY + simpleDateFormat.format(date));
		board.add("");
		board.add(ChatUtils.PRIMARY + "Players" + ChatUtils.GRAY + ": " + ChatUtils.SECONDARY + Bukkit.getOnlinePlayers().size());
		board.add("");
		board.add(ChatUtils.PRIMARY + "Scenario Votes" + ChatUtils.GRAY + ":");

		int count = 1;

		for(Scenario scenario : ScenarioManager.getScenarios().stream().sorted(Comparator.comparing(Scenario::getName)).collect(Collectors.toList())) {
			int votes = plugin.getVoteManager().getVotes().get(scenario);

			if(votes > 0) {
				board.add(ChatUtils.GRAY + count + ". " + ChatUtils.YELLOW + scenario.getName() + ChatUtils.GRAY + " (" + +votes + (votes == 1 ? " vote" : " votes") + ")");
				count++;
			}
		}

		if(Bukkit.getOnlinePlayers().size() < LatteMeetup.MIN_PLAYERS) {
			board.add("");
			board.add(ChatUtils.GREEN + "Waiting for players" + GameManager.getData().getLoading());
		}

		if(data.isCanStartCountdown()) {
			board.add("");
			board.add(ChatUtils.GREEN + "Starting in " + StringUtils.niceTime(data.getVoteTime()) + GameManager.getData().getLoading());
		}

		board.add("");
		board.add(AnimationUtils.getAnimation(player.getUniqueId(), "Link").getLine());
		board.add(ChatUtils.GRAY + ChatUtils.STRIKE_THROUGH + ("&7&m--------------------"));

		return board;
	}

	public List<String> getStartingBoard(final Player player) {
		List<String> board = new ArrayList<>();

		GameData data = GameManager.getData();

		board.add(ChatUtils.GRAY + ChatUtils.STRIKE_THROUGH + ("&7&m--------------------"));
		board.add(ChatUtils.D_GRAY + simpleDateFormat.format(date));
		board.add("");
		board.add(ChatUtils.PRIMARY + "Players: " + ChatUtils.SECONDARY + Bukkit.getOnlinePlayers().size());
		board.add("");
		board.add(ChatUtils.PRIMARY + "Scenarios:");
		int votes = plugin.getVoteManager().getVotes().get(plugin.getVoteManager().getHighestVote());
		board.add(ChatUtils.GRAY + " - " + ChatUtils.SECONDARY + plugin.getVoteManager().getHighestVote().getName() + (votes > 0 ? ChatUtils.GRAY + " (" + votes + (votes == 1 ? " vote" : " votes") + ")" : ""));
		board.add("");
		board.add(ChatUtils.PRIMARY + "Starting in " + ChatUtils.SECONDARY + StringUtils.niceTime(data.getStartingTime()) + ChatUtils.PRIMARY + GameManager.getData().getLoading());
		board.add("");
		board.add(AnimationUtils.getAnimation(player.getUniqueId(), "Link").getLine());
		board.add(ChatUtils.GRAY + ChatUtils.STRIKE_THROUGH + ("&7&m--------------------"));

		return board;
	}

	public List<String> getPlayingBoard(final Player player) {
		List<String> board = new ArrayList<>();

		GameData data = GameManager.getData();
		PlayerData playerData = PlayerData.getByName(player.getName());

		board.add(ChatUtils.GRAY + ChatUtils.STRIKE_THROUGH + ("&7&m--------------------"));
		board.add(ChatUtils.D_GRAY + simpleDateFormat.format(date));
		board.add("");
		board.add(ChatUtils.PRIMARY + "Remaining: " + ChatUtils.SECONDARY + PlayerData.getAlivePlayers());

		if(playerData.isAlive()) {
			board.add(ChatUtils.PRIMARY + "Kills: " + ChatUtils.SECONDARY + playerData.getGameKills());
		}

		board.add(ChatUtils.PRIMARY + "Your ping: " + ChatUtils.SECONDARY + PlayerUtils.getPing(player));

		board.add("");
		board.add(ChatUtils.PRIMARY + "Border: " + ChatUtils.SECONDARY + data.getBorder());
		board.add(ChatUtils.PRIMARY + "Shrinking: " + ChatUtils.SECONDARY + data.getFormattedBorderStatus());

		if(playerData.isNoCleanActive()) {
			board.add("");
			board.add(ChatUtils.PRIMARY + "No Clean: " + ChatUtils.SECONDARY + StringUtils.niceTime(playerData.getNoCleanMillisecondsLeft(), false));
		}

		if(playerData.isDisturbActive()) {
			board.add("");
			board.add(ChatUtils.PRIMARY + "No Clean+: " + ChatUtils.SECONDARY + StringUtils.niceTime(playerData.getDisturbMillisecondsLeft(), false));
		}

		board.add("");
		board.add(AnimationUtils.getAnimation(player.getUniqueId(), "Link").getLine());
		board.add(ChatUtils.GRAY + ChatUtils.STRIKE_THROUGH + ("&7&m--------------------"));
		return board;
	}

	public List<String> getWinnerBoard(final Player player) {
		List<String> board = new ArrayList<>();

		GameData data = GameManager.getData();
		PlayerData playerData = PlayerData.getByName(player.getName());

		board.add(ChatUtils.GRAY + ChatUtils.STRIKE_THROUGH + ("&7&m--------------------"));
		board.add(ChatUtils.D_GRAY + simpleDateFormat.format(date));
		board.add("");
		board.add(ChatUtils.PRIMARY + "Winner: " + ChatUtils.SECONDARY + data.getWinner());
		board.add("");
		board.add(AnimationUtils.getAnimation(player.getUniqueId(), "Link").getLine());
		board.add(ChatUtils.GRAY + ChatUtils.STRIKE_THROUGH + ("&7&m--------------------"));

		return board;
	}
}
