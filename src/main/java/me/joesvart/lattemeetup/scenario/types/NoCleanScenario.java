package me.joesvart.lattemeetup.scenario.types;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattemeetup.player.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import me.joesvart.lattemeetup.scenario.Scenario;

/**
 * Created by Joesvart on 20.09.2020.
 */
public class NoCleanScenario extends Scenario {

    public NoCleanScenario() {
        super("No Clean", new ItemStack(Material.DIAMOND_SWORD), "When player dies he will be invincible for 20 seconds.");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getKiller();

        if(player != null) {
            PlayerData.getByName(player.getName()).applyNoClean();
            player.sendMessage(ChatUtils.translate("&a[No Clean] You have a 20 second invincibility timer."));
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            PlayerData playerData = PlayerData.getByName(player.getName());

            if(playerData.isNoCleanActive()) {
                damager.sendMessage(ChatUtils.translate("&c[No Clean] " + player.getName() + " has No Clean invincibility timer."));
                event.setCancelled(true);
                return;
            }

            PlayerData damagerData = PlayerData.getByName(damager.getName());

            if(damagerData.isNoCleanActive()) {
                damagerData.removeNoCleanCooldown();
                damager.sendMessage(ChatUtils.translate("&c[No Clean] Your No Clean invincibility timer has been removed."));
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerData playerData = PlayerData.getByName(player.getName());

            if(playerData.isNoCleanActive()) {
                event.setCancelled(true);
            }
        }
    }
}
