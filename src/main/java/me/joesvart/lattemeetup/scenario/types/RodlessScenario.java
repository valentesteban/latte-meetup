package me.joesvart.lattemeetup.scenario.types;

import me.joesvart.lattemeetup.scenario.Scenario;
import me.joesvart.lattemeetup.util.chat.CustomColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Joesvart on 20.09.2020.
 */
public class RodlessScenario extends Scenario {

    public RodlessScenario() {
        super("Rodless", new ItemStack(Material.FISHING_ROD), "Fishing Rods cannot be crafted/used.");
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if(event.getRecipe().getResult().getType() == Material.FISHING_ROD) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
            event.getView().getPlayer().sendMessage(CustomColor.translate("&cYou cannot use fishing rods while &eRodless&c scenario is active."));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack stack = event.getItem();

        if(stack != null && stack.getType() == Material.FISHING_ROD) {
            event.getPlayer().setItemInHand(null);
            event.getPlayer().updateInventory();
            event.getPlayer().sendMessage(CustomColor.translate("&cYou cannot use fishing rods while &eRodless&c scenario is active."));
        }
    }
}
