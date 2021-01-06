package me.joesvart.lattemeetup.border;

import me.joesvart.lattemeetup.LatteMeetup;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

final class BorderHelper {

    @Getter
    private static List<Material> blockedWallBlocks = Arrays.asList(Material.LOG, Material.LOG_2, Material.LEAVES, Material.LEAVES_2,
            Material.AIR, Material.WATER, Material.STATIONARY_WATER, Material.LAVA, Material.STATIONARY_LAVA,
            Material.HUGE_MUSHROOM_1, Material.HUGE_MUSHROOM_2, Material.DOUBLE_PLANT, Material.LONG_GRASS,
            Material.VINE, Material.YELLOW_FLOWER, Material.RED_ROSE, Material.CACTUS, Material.DEAD_BUSH,
            Material.SUGAR_CANE_BLOCK, Material.ICE, Material.SNOW);

    static void addBedrockBorder(String world, int radius, int blocksHigh) {
        for(int i = 0; i < blocksHigh; i++) {
            Bukkit.getScheduler().runTaskLater(LatteMeetup.getInstance(), () -> addBedrockBorder(world, radius), i);
        }
    }

    private static void figureOutBlockToMakeBedrock(String world, int x, int z) {
        Block block = Bukkit.getWorld(world).getHighestBlockAt(x, z);
        Block below = block.getRelative(BlockFace.DOWN);
        while (blockedWallBlocks.contains(below.getType()) && below.getY() > 1) {
            below = below.getRelative(BlockFace.DOWN);
        }

            below.getRelative(BlockFace.UP).setType(Material.BEDROCK);
    }

    private static void addBedrockBorder(String world, int radius) {
        new BukkitRunnable() {
            private int counter = -radius - 1;
            private boolean phase1;
            private boolean phase2;
            private boolean phase3;

            @Override
            public void run() {
                if(!phase1) {
                    int maxCounter = counter + 500;
                    int x = -radius - 1;
                    for(int z = counter; z <= radius && counter <= maxCounter; z++, counter++) {
                        figureOutBlockToMakeBedrock(world, x, z);
                    }

                    if(counter >= radius) {
                        counter = -radius - 1;
                        phase1 = true;
                    }

                    return;
                }

                if(!phase2) {
                    int maxCounter = counter + 500;
                    for(int z = counter; z <= radius && counter <= maxCounter; z++, counter++) {
                        figureOutBlockToMakeBedrock(world, radius, z);
                    }

                    if(counter >= radius) {
                        counter = -radius - 1;
                        phase2 = true;
                    }

                    return;
                }

                if(!phase3) {
                    int maxCounter = counter + 500;
                    int z = -radius - 1;
                    for(int x = counter; x <= radius && counter <= maxCounter; x++, counter++) {
                        if(x == radius || x == -radius - 1) {
                            continue;
                        }

                        figureOutBlockToMakeBedrock(world, x, z);
                    }

                    if(counter >= radius) {
                        counter = -radius - 1;
                        phase3 = true;
                    }

                    return;
                }


                int maxCounter = counter + 500;
                for(int x = counter; x <= radius && counter <= maxCounter; x++, counter++) {
                    if(x == radius || x == -radius - 1) {
                        continue;
                    }

                    figureOutBlockToMakeBedrock(world, x, radius);
                }

                if(counter >= radius) {
                    cancel();
                }
            }
        }.runTaskTimer(LatteMeetup.getInstance(), 0L, 5L);
    }
}