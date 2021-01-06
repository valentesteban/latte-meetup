package me.joesvart.lattemeetup.scenario;

import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.util.chat.CC;
import me.joesvart.lattemeetup.util.other.ItemBuilder;
import me.joesvart.lattemeetup.util.chat.StringUtil;
import lombok.Getter;
import me.joesvart.lattemeetup.scenario.types.*;
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
		lore.addAll(Arrays.asList(StringUtil.niceLore(scenario.getDescription(), ChatColor.GRAY)));
		lore.add("");
		lore.add(CC.PRIMARY + "Votes: " + CC.SECONDARY + LatteMeetup.getInstance().getVoteManager().getVotes().get(scenario));
		lore.add("");
		lore.add(CC.GRAY + CC.I + "Click to vote...");

		return new ItemBuilder(scenario.getIcon().getType())
				.durability(scenario.getIcon().getDurability())
				.amount(scenario.getIcon().getAmount())
				.name(CC.GREEN + scenario.getName())
				.lore(lore).build();
	}

	public static Scenario getByName(String input) {
		return scenarios.stream().filter(scenario -> input.equalsIgnoreCase(scenario.getName())).findFirst().orElse(null);
	}
}
