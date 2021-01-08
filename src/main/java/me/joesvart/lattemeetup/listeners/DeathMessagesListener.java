package me.joesvart.lattemeetup.listeners;

import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattemeetup.util.chat.CC;
import me.joesvart.lattemeetup.util.chat.CustomColor;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Objects;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class DeathMessagesListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String message = event.getDeathMessage();

        if(message == null || message.isEmpty()) {
            return;
        }

        event.setDeathMessage(null);

        if(event.getEntity().getLastDamageCause() != null && event.getEntity().getLastDamageCause().getCause() != null && event.getEntity().getLastDamageCause().getCause().equals(DamageCause.PROJECTILE)) {
            Projectile projectile = (Projectile) ((EntityDamageByEntityEvent) event.getEntity().getLastDamageCause()).getDamager();

            if(projectile.getShooter() instanceof Player) {
                Player shooter = (Player) projectile.getShooter();

                if(event.getEntity().getLastDamageCause().getCause().equals(DamageCause.PROJECTILE)) {
                    int distance = (int) Math.round(shooter.getLocation().distance(event.getEntity().getLocation()));
                    handleMessage(message, event, shooter.getItemInHand() != null && !shooter.getItemInHand().getType().equals(Material.AIR) && shooter.getItemInHand().getType().equals(Material.BOW) ? CC.GRAY + " from " + CC.RED + distance + (distance == 1 ? " block" : " blocks") : "");
                    return;
                }
            }
        }

        handleMessage(message, event, "");
    }

    private String getDeathMessage(String input, Entity entity, Entity killer, String pColor, String kColor) {
        String pCS = Objects.equals(pColor, CC.GREEN) ? CC.DARK_GREEN : CC.DARK_RED;
        String kCS = Objects.equals(kColor, CC.GREEN) ? CC.DARK_GREEN : CC.DARK_RED;

        if(entity != null) {
            input = input.replaceFirst("(?i)" + getEntityName(entity), pColor + getNiceDisplayName(entity, pCS) + CC.GRAY);
        }

        if(killer != null && (entity == null || !killer.equals(entity))) {
            input = input.replaceFirst("(?i)" + getEntityName(killer), kColor + getNiceDisplayName(killer, kCS) + CC.GRAY);
        }

        return input;
    }

    private String getEntityName(Entity entity) {
        return entity instanceof Player ? entity.getName() : ((CraftEntity) entity).getHandle().getName();
    }

    private String getNiceDisplayName(Entity entity, String color) {
        if(entity instanceof Player) {
            Player player = (Player) entity;
            PlayerData data = PlayerData.getByName(entity.getName());
            return player.getName() + color + '[' + data.getGameKills() + ']';
        } else {
            return WordUtils.capitalizeFully(entity.getType().name().replace('_', ' '));
        }
    }

    private CraftEntity getKiller(PlayerDeathEvent event) {
        EntityLiving lastAttacker = ((CraftPlayer) event.getEntity()).getHandle().getLastDamager();
        return lastAttacker == null ? null : lastAttacker.getBukkitEntity();
    }

    private void handleMessage(String message, PlayerDeathEvent event, String toAdd) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            String color1 = event.getEntity().getName().equals(player.getName()) ? CC.GREEN : CC.RED;
            String color2 = ((CraftPlayer) event.getEntity()).getHandle().getLastDamager() != null ? ((CraftPlayer) event.getEntity()).getHandle().getLastDamager().getName().equals(player.getName()) ? CC.GREEN : CC.RED : CC.RED;

            player.sendMessage(getDeathMessage(message, event.getEntity(), getKiller(event), color1, color2) + CustomColor.translate(toAdd + CC.GRAY + "."));
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
        });
    }
}