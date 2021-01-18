package me.joesvart.lattemeetup.scenario.types;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattemeetup.scenario.Scenario;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class DoNotDisturbScenario extends Scenario {

    public DoNotDisturbScenario() {
        super(
            "DoNotDisturb", new ItemStack(Material.BED),
            "No disturbing fights! Once you hit a player, you can only hit them. This features removes after 30 seconds.");
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            if(event.getDamager() instanceof Player) {
                handleCheck((Player) event.getEntity(), (Player) event.getDamager(), event);
            } else if(event.getDamager() instanceof Projectile) {
                Projectile d1 = (Projectile) event.getDamager();

                if(!(d1.getShooter() instanceof Player)) {
                    return;
                }

                Player damager = (Player) d1.getShooter();
                if(damager == event.getEntity()) {
                    return;
                }

                handleCheck((Player) event.getEntity(), damager, event);
            }
        }
    }

    private void handleCheck(Player player, Player damager, Cancellable event) {
        PlayerData damagerData = PlayerData.getByName(damager.getName());

        if(!damagerData.isDisturbActive() && damagerData.getDisturb() != null) {
            damagerData.setDisturb(null);
        }

        PlayerData playerData = PlayerData.getByName(player.getName());

        if(!playerData.isDisturbActive() && playerData.getDisturb() != null) {
            playerData.setDisturb(null);
        }

        if(damagerData.getDisturb() == null && playerData.getDisturb() == null) {
            damagerData.applyDisturb(player.getName());
            playerData.applyDisturb(damager.getName());

            damager.sendMessage(ChatUtils.SECONDARY + "You are now linked to " + player.getDisplayName() + ChatUtils.SECONDARY + ".");
            player.sendMessage(ChatUtils.SECONDARY + "You are now linked to " + damager.getDisplayName() + ChatUtils.SECONDARY  + ".");
        } else if((damagerData.getDisturb() != null && !damagerData.getDisturb().equals(player.getName()) && damagerData.isDisturbActive()) || (playerData.getDisturb() != null && !playerData.getDisturb().equals(damager.getName()) && playerData.isDisturbActive())) {
            event.setCancelled(true);
            damager.sendMessage(ChatUtils.RED + player.getName() + " is not linked to you.");
        }
    }
}