package me.joesvart.lattemeetup.scenario.types;

import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.util.chat.CC;
import me.joesvart.lattemeetup.util.other.MeetupUtils;
import me.joesvart.lattemeetup.scenario.Scenario;
import me.joesvart.lattemeetup.util.chat.Msg;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * Created by Joesvart on 20.09.2020.
 */
public class TimeBombScenario extends Scenario {

    public TimeBombScenario() {
        super("Time Bomb", new ItemStack(Material.TNT), "When a player dies, their loot will drop into a chest. After 30s, the chest will explode.");
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(block -> block.getType() == Material.BEDROCK);
    }

    public static void handleTimeBomb(Entity entity, List<ItemStack> drops, List<ItemStack> items, boolean tb, boolean sf) {
        if(drops != null) {
            drops.clear();
        }

        Location where = entity.getLocation();

        where.getBlock().setType(Material.CHEST);
        Chest chest = (Chest) where.getBlock().getState();

        where.add(1, 0, 0).getBlock().setType(Material.CHEST);
        where.add(0, 1, 0).getBlock().setType(Material.AIR);
        where.add(1, 1, 0).getBlock().setType(Material.AIR);

        Chest secondChest;

        try {
            secondChest = (Chest) chest.getLocation().add(1, 0, 0).getBlock().getState();
        } catch (Exception e) {
            secondChest = chest;
        }

        if(sf && entity instanceof Player) {
            Player player = (Player) entity;
            Player killer = player.getKiller();

            if(killer != null) {
        }

        // Should never happend but yea
        items.stream().filter(stack -> stack != null && stack.getType() != Material.AIR).forEach(stack -> chest.getInventory().addItem(stack));

        // always adding 1 head
        chest.getInventory().addItem(MeetupUtils.getGoldenHead());

        if(tb) {

            new BukkitRunnable() {
                private int time = 30;

                @Override
                public void run() {
                    time--;

                    if(time == 0) {
                        cancel();

                        where.getWorld().spigot().strikeLightning(where, true);
                        where.getWorld().createExplosion(where, 8f);

                        Msg.sendMessage(CC.DARK_GRAY + "[" + CC.B_PRIMARY + "Time Bomb" + CC.DARK_GRAY + "] " + CC.SECONDARY + ChatColor.stripColor(entity.getName()) + "'s corpse has exploded.");
                    }
                }
            }.runTaskTimer(LatteMeetup.getInstance(), 0L, 20L);
        }
    }
}
}
