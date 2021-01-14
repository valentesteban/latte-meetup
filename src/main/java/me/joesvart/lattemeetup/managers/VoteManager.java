package me.joesvart.lattemeetup.managers;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattemeetup.config.MessagesFile;
import me.joesvart.lattemeetup.util.other.MeetupUtils;
import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattemeetup.scenario.Scenario;
import me.joesvart.lattemeetup.scenario.ScenarioManager;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class VoteManager {

    @Getter
    private Map<Scenario, Integer> votes = new HashMap<>();

    public VoteManager() {
        ScenarioManager.getScenarios().forEach(scenario -> votes.put(scenario, 0));
    }

    public Scenario getHighestVote() {
        Scenario highestScenario = null;
        int highestVote = 0;

        for(Map.Entry<Scenario, Integer> entry : votes.entrySet()) {
            if(entry.getValue() > highestVote) {
                highestScenario = entry.getKey();
                highestVote = entry.getValue();
            }
        }

        return highestScenario == null ? ScenarioManager.getByName("Default") : highestScenario;
    }

    public void handleAddVote(Player player, Scenario scenario) {
        votes.put(scenario, votes.get(scenario) + 1);
        PlayerData.getByName(player.getName()).setLastVoted(scenario.getName());
        player.sendMessage(ChatUtils.SECONDARY + "You have casted your vote for " + ChatUtils.PRIMARY + scenario.getName() + ChatUtils.SECONDARY + ".");

        if (MessagesFile.getConfig().getBoolean("BOOLEANS.SOUNDS")) {
            MeetupUtils.handleSound(player, Sound.NOTE_PIANO);
        }
    }

    public void handleRemove(Player player, Scenario newVote) {
        PlayerData data = PlayerData.getByName(player.getName());

        if(data.getLastVoted().equals(newVote.getName())) {
            player.sendMessage(ChatUtils.RED + "You have already voted for " + newVote.getName() + ".");

            if (MessagesFile.getConfig().getBoolean("BOOLEANS.SOUNDS")) {
                MeetupUtils.handleSound(player, Sound.DIG_GRASS);
            }
            return;
        }

        Scenario scenario = ScenarioManager.getByName(data.getLastVoted());

        votes.put(scenario, votes.get(scenario) - 1);
        data.setLastVoted(null);

        handleAddVote(player, newVote);
    }

    public void handleRemoveOne(Scenario scenario) {
        votes.put(scenario, votes.get(scenario) - 1);
    }

    public boolean hasVoted(Player player) {
        return PlayerData.getByName(player.getName()).getLastVoted() != null;
    }
}