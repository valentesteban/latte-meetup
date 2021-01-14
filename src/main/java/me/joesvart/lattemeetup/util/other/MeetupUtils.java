package me.joesvart.lattemeetup.util.other;

import me.joesvart.lattelibs.item.ItemCreator;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import java.io.File;
import java.util.Arrays;
import java.util.Random;

public class MeetupUtils {

    public static boolean deleteFile(File file) {
        if(file.isDirectory()) {
            for (File subfile : file.listFiles()) {
                if(!deleteFile(subfile)) {
                    return false;
                }
            }
        }

        return file.delete();
    }

    public static void deleteWorld() {
        World world = Bukkit.getWorld("meetup_world");

        if(world != null) {
            Bukkit.getServer().unloadWorld(world, false);

            deleteFile(world.getWorldFolder());
        }
    }

    public static ItemStack getGoldenHead() {
        return new ItemCreator(Material.GOLDEN_APPLE).durability(0).name("&6Golden Head").lore(
                Arrays.asList(
                        "&7Some say consuming the need of a",
                        "&7fallen foe strengthens the blood."
                )).build();
    }

    public static void clearPlayer(Player player) {
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setSaturation(12.8F);
        player.setMaximumNoDamageTicks(20);
        player.setFireTicks(0);
        player.setFallDistance(0.0F);
        player.setLevel(0);
        player.setExp(0.0F);
        player.setWalkSpeed(0.2F);
        player.getInventory().setHeldItemSlot(0);
        player.setAllowFlight(false);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.closeInventory();
        player.setGameMode(GameMode.SURVIVAL);
        player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
        ((CraftPlayer) player).getHandle().getDataWatcher().watch(9, (byte) 0);
        player.updateInventory();
    }

    public static void handleSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1F, 1F);
    }

    public static Location getScatterLocation() {
        Random r = new Random();

        int x = r.nextInt(100 * 2) - 100;
        int z = r.nextInt(100 * 2) - 100;

        return new Location(Bukkit.getWorld("meetup_world"), x, (Bukkit.getWorld("meetup_world").getHighestBlockYAt(x, z) + 1), z);
    }

    public static String formatInt(int i) {
        int r = i * 1000;
        int sec = r / 1000 % 60;
        int min = r / 60000 % 60;
        int h = r / 3600000 % 24;

        return (h > 0 ? h + ":" : "") + (min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec);
    }
}
