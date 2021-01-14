package me.joesvart.lattemeetup.scenario.types;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattemeetup.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Joesvart on 20.09.2020.
 */
public class SafeLootScenario extends Scenario {

    public SafeLootScenario() {
        super("Safe Loot", new ItemStack(Material.CHEST), "Upon enemy death, the team/player is summoned a private chest that is protected for 20 seconds.");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if(block.getType().equals(Material.CHEST)) {
            Chest chest = (Chest) block.getState();

            if(chest.hasMetadata("SafeLoot") && !chest.getMetadata("SafeLoot").get(0).asString().equals(event.getPlayer().getName())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatUtils.RED + "That chest is protected.");
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!event.hasBlock()) {
            return;
        }

        Block block = event.getClickedBlock();

        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && block.getType().equals(Material.CHEST)) {
            Chest chest = (Chest) block.getState();

            if(chest.hasMetadata("SafeLoot") && !chest.getMetadata("SafeLoot").get(0).asString().equals(event.getPlayer().getName())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatUtils.RED + "That chest is protected.");
            }
        }
    }
}