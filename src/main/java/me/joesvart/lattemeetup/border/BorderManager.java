package me.joesvart.lattemeetup.border;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattemeetup.game.GameState;
import me.joesvart.lattespigot.LatteSpigot;
import me.joesvart.lattespigot.handler.MovementHandler;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.*;
import org.bukkit.entity.Player;
import me.joesvart.lattemeetup.game.GameManager;

public class BorderManager {

    public BorderManager() {
        LatteSpigot.get().addMovementHandler(new MovementHandler() {
            @Override
            public void handleUpdateLocation(Player player, Location to, Location from, PacketPlayInFlying packetPlayInFlying) {
                if(GameManager.getData().getGameState().equals(GameState.STARTING)) {
                    if(to.getX() != from.getX() || to.getZ() != from.getZ()) {
                        player.teleport(from);
                        return;
                    }
                }

                int size = GameManager.getData().getBorder();
                World world = player.getWorld();

                if(world.getName().equalsIgnoreCase("meetup_world")) {
                    if(player.getLocation().getBlockX() > size) {
                        handleEffects(player);
                        player.teleport(new Location(world, size - 2, player.getLocation().getBlockY(), player.getLocation().getBlockZ()));
                        if(player.getLocation().getBlockY() < world.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ())) {
                            player.teleport(new Location(world, player.getLocation().getBlockX(), world.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ()) + 2, player.getLocation().getBlockZ()));
                        }
                    }

                    if(player.getLocation().getBlockZ() > size) {
                        handleEffects(player);
                        player.teleport(new Location(world, player.getLocation().getBlockX(), player.getLocation().getBlockY(), size - 2));
                        if(player.getLocation().getBlockY() < world.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ())) {
                            player.teleport(new Location(world, player.getLocation().getBlockX(), world.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ()) + 2, player.getLocation().getBlockZ()));
                        }
                    }

                    if(player.getLocation().getBlockX() < -size) {
                        handleEffects(player);
                        player.teleport(new Location(world, -size + 2, player.getLocation().getBlockY(), player.getLocation().getBlockZ()));
                        if(player.getLocation().getBlockY() < world.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ())) {
                            player.teleport(new Location(world, player.getLocation().getBlockX(), world.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ()) + 2, player.getLocation().getBlockZ()));
                        }
                    }

                    if(player.getLocation().getBlockZ() < -size) {
                        handleEffects(player);
                        player.teleport(new Location(world, player.getLocation().getBlockX(), player.getLocation().getBlockY(), -size + 2));
                        if(player.getLocation().getBlockY() < world.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ())) {
                            player.teleport(new Location(world, player.getLocation().getBlockX(), world.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ()) + 2, player.getLocation().getBlockZ()));
                        }
                    }
                }
            }

            @Override
            public void handleUpdateRotation(Player player, Location location, Location location1, PacketPlayInFlying packetPlayInFlying) {

            }
        });
    }

    private void handleEffects(Player player) {
        player.getWorld().playEffect(player.getLocation(), Effect.LARGE_SMOKE,2, 2);
        player.playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 2.0f);
        player.sendMessage(ChatUtils.translate("&cYou were shrunk in the border!"));
    }
}
