package me.joesvart.lattemeetup.scenario.types;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import me.joesvart.lattemeetup.scenario.Scenario;

public class DefaultScenario extends Scenario {

    public DefaultScenario() {
        super("Default", new ItemStack(Material.INK_SACK), "Basic game, no scenarios!");
    }
}