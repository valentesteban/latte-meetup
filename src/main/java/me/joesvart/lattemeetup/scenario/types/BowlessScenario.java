package me.joesvart.lattemeetup.scenario.types;

import me.joesvart.lattemeetup.util.chat.CustomColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import me.joesvart.lattemeetup.scenario.Scenario;

/**
 * Created by Joesvart on 20.09.2020.
 */
public class BowlessScenario extends Scenario {

    public BowlessScenario() {
        super("Bowless", new ItemStack(Material.BOW), "Bows cannot be crafted/used.");
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if(event.getRecipe().getResult().getType() == Material.BOW) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
            event.getView().getPlayer().sendMessage(CustomColor.translate("&cYou cannot use bows while &eBowless&c scenario is active."));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getItem() != null && event.getItem().getType() == Material.BOW) {
            event.getPlayer().setItemInHand(null);
            event.getPlayer().updateInventory();
            event.getPlayer().sendMessage(CustomColor.translate("&cYou cannot use bows while &eBowless&c scenario is active."));
        }
    }
}
