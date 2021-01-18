package me.joesvart.lattemeetup.scenario.types;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import me.joesvart.lattemeetup.scenario.Scenario;

public class HorselessScenario extends Scenario {

    public HorselessScenario() {
        super("Horseless", new ItemStack(Material.DIAMOND_BARDING), "You cannot tame horses. You cannot tame donkeys.");
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if(event.getRightClicked().getType().equals(EntityType.HORSE)) {
            event.setCancelled(true);
        }
    }
}
