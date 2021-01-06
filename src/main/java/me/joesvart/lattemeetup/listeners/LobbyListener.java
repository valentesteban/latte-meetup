package me.joesvart.lattemeetup.listeners;

import me.joesvart.lattemeetup.game.GameManager;
import me.joesvart.lattemeetup.game.GameState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class LobbyListener implements Listener {

    @EventHandler
    public void onInventoryMoveItem(PlayerInteractEvent event) {
        if(isNotStart()) {
            event.setCancelled(true);
            }
        }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (isNotStart()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (isNotStart()) {
            if (event.getDamager() instanceof Player) {
                    return;
                }

                event.setCancelled(true);
            }
        }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (isNotStart()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (isNotStart()) {
            event.setCancelled(true);
        }
    }

    public boolean isNotStart() {
        return !GameManager.getData().getGameState().equals(GameState.PLAYING);
    }
}
