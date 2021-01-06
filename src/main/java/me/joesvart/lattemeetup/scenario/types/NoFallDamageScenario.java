package me.joesvart.lattemeetup.scenario.types;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import me.joesvart.lattemeetup.scenario.Scenario;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Created by Joesvart on 20.09.2020.
 */
public class NoFallDamageScenario extends Scenario {

    public NoFallDamageScenario() {
        super("NoFallDamage", new ItemStack(Material.DIAMOND_BOOTS), "You cannot take fall damage.");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            if(event.getCause().equals(DamageCause.FALL)) {
                event.setCancelled(true);
            }
        }
    }
}
