package me.joesvart.lattemeetup.managers;

import me.joesvart.lattemeetup.scenario.Scenario;
import me.joesvart.lattemeetup.scenario.ScenarioManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.Comparator;

@Getter
public class InventoryManager {

    private Inventory scenarioInventory;

    public InventoryManager() {
        scenarioInventory = Bukkit.createInventory(null, 18, "Scenarios");
        setupScenarios();
    }

    public void setupScenarios() {
        scenarioInventory.clear();

        ScenarioManager.getScenarios()
                .stream().sorted(Comparator.comparing(Scenario::getName))
                .forEach(scenario -> scenarioInventory.addItem(ScenarioManager.getItem(scenario)));
    }
}
