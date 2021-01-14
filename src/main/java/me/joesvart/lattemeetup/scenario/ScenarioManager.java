package me.joesvart.lattemeetup.scenario;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattelibs.item.ItemCreator;
import me.joesvart.lattemeetup.LatteMeetup;
import lombok.Getter;
import me.joesvart.lattemeetup.scenario.types.*;
import me.joesvart.lattemeetup.util.chat.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScenarioManager {

	@Getter
	private static List<Scenario> scenarios = new ArrayList<>();

	public ScenarioManager() {
		scenarios.addAll(Arrays.asList(
				new BowlessScenario(),
				new DefaultScenario(),
				new DoNotDisturbScenario(),
				new FirelessScenario(),
				new HorselessScenario(),
				new NoCleanScenario(),
				new NoFallDamageScenario(),
				new RodlessScenario(),
				new SafeLootScenario(),
				new TimeBombScenario()
		));
	}

	public static ItemStack getItem(Scenario scenario) {
		List<String> lore = new ArrayList<>();
		lore.add("");
		lore.addAll(Arrays.asList(StringUtils.niceLore(scenario.getDescription(), ChatColor.GRAY)));
		lore.add("");
		lore.add(ChatUtils.PRIMARY + "Votes: " + ChatUtils.SECONDARY + LatteMeetup.getInstance().getVoteManager().getVotes().get(scenario));
		lore.add("");
		lore.add(ChatUtils.GRAY + ChatUtils.I + "Click to vote...");

		return new ItemCreator(scenario.getIcon().getType())
				.durability(scenario.getIcon().getDurability())
				.amount(scenario.getIcon().getAmount())
				.name(ChatUtils.GREEN + scenario.getName())
				.lore(lore).build();
	}

	public static Scenario getByName(String input) {
		return scenarios.stream().filter(scenario -> input.equalsIgnoreCase(scenario.getName())).findFirst().orElse(null);
	}
}
