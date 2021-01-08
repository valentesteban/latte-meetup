package me.joesvart.lattemeetup.providers;

import lombok.Getter;
import me.joesvart.combustion.CombustionAdapter;
import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.game.GameData;
import me.joesvart.lattemeetup.game.GameManager;
import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattemeetup.scenario.Scenario;
import me.joesvart.lattemeetup.scenario.ScenarioManager;
import me.joesvart.lattemeetup.util.chat.Animation;
import me.joesvart.lattemeetup.util.chat.CC;
import me.joesvart.lattemeetup.util.chat.StringUtil;
import me.joesvart.lattemeetup.util.player.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MeetupBoard implements CombustionAdapter {

	@Getter
	private LatteMeetup plugin = LatteMeetup.getInstance();

	Date date = new Date();
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	public String getTitle(Player player) {
		return CC.D_GREEN + CC.B + "UHC Meetup";
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

		board.add(CC.GRAY + CC.STRIKE_THROUGH + ("&7&m--------------------"));
		board.add(CC.D_GRAY + simpleDateFormat.format(date));
		board.add("");
		board.add(CC.PRIMARY + "Players" + CC.GRAY + ": " + CC.SECONDARY + Bukkit.getOnlinePlayers().size());
		board.add("");
		board.add(CC.PRIMARY + "Scenario Votes" + CC.GRAY + ":");

		int count = 1;

		for(Scenario scenario : ScenarioManager.getScenarios().stream().sorted(Comparator.comparing(Scenario::getName)).collect(Collectors.toList())) {
			int votes = plugin.getVoteManager().getVotes().get(scenario);

			if(votes > 0) {
				board.add(CC.GRAY + count + ". " + CC.YELLOW + scenario.getName() + CC.GRAY + " (" + +votes + (votes == 1 ? " vote" : " votes") + ")");
				count++;
			}
		}

		if(Bukkit.getOnlinePlayers().size() < LatteMeetup.MIN_PLAYERS) {
			board.add("");
			board.add(CC.GREEN + "Waiting for players" + GameManager.getData().getLoading());
		}

		if(data.isCanStartCountdown()) {
			board.add("");
			board.add(CC.GREEN + "Starting in " + StringUtil.niceTime(data.getVoteTime()) + GameManager.getData().getLoading());
		}

		board.add("");
		board.add(Animation.getAnimation(player.getUniqueId(), "Link").getLine());
		board.add(CC.GRAY + CC.STRIKE_THROUGH + ("&7&m--------------------"));

		return board;
	}

	public List<String> getStartingBoard(final Player player) {
		List<String> board = new ArrayList<>();

		GameData data = GameManager.getData();

		board.add(CC.GRAY + CC.STRIKE_THROUGH + ("&7&m--------------------"));
		board.add(CC.D_GRAY + simpleDateFormat.format(date));
		board.add("");
		board.add(CC.PRIMARY + "Players: " + CC.SECONDARY + Bukkit.getOnlinePlayers().size());
		board.add("");
		board.add(CC.PRIMARY + "Scenarios:");
		int votes = plugin.getVoteManager().getVotes().get(plugin.getVoteManager().getHighestVote());
		board.add(CC.GRAY + " - " + CC.SECONDARY + plugin.getVoteManager().getHighestVote().getName() + (votes > 0 ? CC.GRAY + " (" + votes + (votes == 1 ? " vote" : " votes") + ")" : ""));
		board.add("");
		board.add(CC.PRIMARY + "Starting in " + CC.SECONDARY + StringUtil.niceTime(data.getStartingTime()) + CC.PRIMARY + GameManager.getData().getLoading());
		board.add("");
		board.add(Animation.getAnimation(player.getUniqueId(), "Link").getLine());
		board.add(CC.GRAY + CC.STRIKE_THROUGH + ("&7&m--------------------"));

		return board;
	}

	public List<String> getPlayingBoard(final Player player) {
		List<String> board = new ArrayList<>();

		GameData data = GameManager.getData();
		PlayerData playerData = PlayerData.getByName(player.getName());

		board.add(CC.GRAY + CC.STRIKE_THROUGH + ("&7&m--------------------"));
		board.add(CC.D_GRAY + simpleDateFormat.format(date));
		board.add("");
		board.add(CC.PRIMARY + "Remaining: " + CC.SECONDARY + PlayerData.getAlivePlayers());

		if(playerData.isAlive()) {
			board.add(CC.PRIMARY + "Kills: " + CC.SECONDARY + playerData.getGameKills());
		}

		board.add(CC.PRIMARY + "Your ping: " + CC.SECONDARY + PlayerUtils.getPing(player));

		board.add("");
		board.add(CC.PRIMARY + "Border: " + CC.SECONDARY + data.getBorder());
		board.add(CC.PRIMARY + "Shrinking: " + CC.SECONDARY + data.getFormattedBorderStatus());

		if(playerData.isNoCleanActive()) {
			board.add("");
			board.add(CC.PRIMARY + "No Clean: " + CC.SECONDARY + StringUtil.niceTime(playerData.getNoCleanMillisecondsLeft(), false));
		}

		if(playerData.isDisturbActive()) {
			board.add("");
			board.add(CC.PRIMARY + "No Clean+: " + CC.SECONDARY + StringUtil.niceTime(playerData.getDisturbMillisecondsLeft(), false));
		}

		board.add("");
		board.add(Animation.getAnimation(player.getUniqueId(), "Link").getLine());
		board.add(CC.GRAY + CC.STRIKE_THROUGH + ("&7&m--------------------"));
		return board;
	}

	public List<String> getWinnerBoard(final Player player) {
		List<String> board = new ArrayList<>();

		GameData data = GameManager.getData();
		PlayerData playerData = PlayerData.getByName(player.getName());

		board.add(CC.GRAY + CC.STRIKE_THROUGH + ("&7&m--------------------"));
		board.add(CC.D_GRAY + simpleDateFormat.format(date));
		board.add("");
		board.add(CC.PRIMARY + "Winner: " + CC.SECONDARY + data.getWinner());
		board.add("");
		board.add(Animation.getAnimation(player.getUniqueId(), "Link").getLine());
		board.add(CC.GRAY + CC.STRIKE_THROUGH + ("&7&m--------------------"));

		return board;
	}
}
