package me.joesvart.lattemeetup.scenario.types;

import me.joesvart.lattemeetup.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

public class FirelessScenario extends Scenario {

    public FirelessScenario() {
        super("Fireless", new ItemStack(Material.FLINT_AND_STEEL), "You cannot take fire damage.");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            if(event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK) || event.getCause().equals(DamageCause.LAVA)) {
                event.setCancelled(true);
            }
        }
    }
}
