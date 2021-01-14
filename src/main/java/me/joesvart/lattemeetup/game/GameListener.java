package me.joesvart.lattemeetup.game;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattemeetup.player.PlayerState;
import me.joesvart.lattemeetup.scenario.ScenarioManager;
import me.joesvart.lattemeetup.scenario.types.TimeBombScenario;
import me.joesvart.lattemeetup.util.chat.Msg;
import me.joesvart.lattemeetup.LatteMeetup;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GameListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = event.getEntity().getKiller();

        player.setHealth(20.0);
        player.teleport(player.getLocation());

        if(ScenarioManager.getByName("Time Bomb").isActive() && ScenarioManager.getByName("Safe Loot").isActive()) {
            List<ItemStack> items = new ArrayList<>();

            Stream.of(event.getEntity().getInventory().getArmorContents()).filter(stack -> stack != null && stack.getType() != Material.AIR).forEach(items::add);
            Stream.of(event.getEntity().getInventory().getContents()).filter(stack -> stack != null && stack.getType() != Material.AIR).forEach(items::add);

            TimeBombScenario.handleTimeBomb(player, event.getDrops(), items, true, true);
        } else if(ScenarioManager.getByName("Time Bomb").isActive() && !ScenarioManager.getByName("Safe Loot").isActive()) {
            List<ItemStack> items = new ArrayList<>();

            Stream.of(event.getEntity().getInventory().getArmorContents()).filter(stack -> stack != null && stack.getType() != Material.AIR).forEach(items::add);
            Stream.of(event.getEntity().getInventory().getContents()).filter(stack -> stack != null && stack.getType() != Material.AIR).forEach(items::add);

            TimeBombScenario.handleTimeBomb(player, event.getDrops(), items, true, false);
        } else if(!ScenarioManager.getByName("Time Bomb").isActive() && ScenarioManager.getByName("Safe Loot").isActive()) {
            List<ItemStack> items = new ArrayList<>();

            Stream.of(event.getEntity().getInventory().getArmorContents()).filter(stack -> stack != null && stack.getType() != Material.AIR).forEach(items::add);
            Stream.of(event.getEntity().getInventory().getContents()).filter(stack -> stack != null && stack.getType() != Material.AIR).forEach(items::add);

            TimeBombScenario.handleTimeBomb(player, event.getDrops(), items, false, true);
        }

        event.setDroppedExp(0);

        PlayerData playerData = PlayerData.getByName(player.getName());
        playerData.setPlayerState(PlayerState.SPECTATING);
        playerData.setDeaths(playerData.getDeaths() + 1);

        if(killer != null) {
            PlayerData killerData = PlayerData.getByName(killer.getName());

            killerData.setKills(killerData.getKills() + 1);
            killerData.setGameKills(killerData.getGameKills() + 1);
        }

        LatteMeetup.getInstance().getGameManager().handleCheckWinners();

        Bukkit.getScheduler().runTaskLater(LatteMeetup.getInstance(), () -> LatteMeetup.getInstance().getSpectatorManager().handleEnable(player), 1L);
    }

    @EventHandler
    public void onHorseSetup(CreatureSpawnEvent event) {
        if(event.getEntityType() != EntityType.HORSE
                || event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
            return;
        }

        Horse horse = (Horse) event.getEntity();

        horse.setAdult();
        horse.setAgeLock(true);

        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));

        horse.setTamed(true);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();

        if(item == null
                || item.getType() != Material.GOLDEN_APPLE
                || item.getItemMeta() == null
                || !item.getItemMeta().hasDisplayName()
                || !item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatUtils.translate("&6Golden Head"))) {
            return;
        }

        Player player = event.getPlayer();

        player.removePotionEffect(PotionEffectType.REGENERATION);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
    }

    @EventHandler
    public void onEntityDamageByEntityBow(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player entity = (Player) event.getEntity();

        if(!(event.getDamager() instanceof Arrow)) return;

        Arrow arrow = (Arrow) event.getDamager();

        if(!(arrow.getShooter() instanceof Player)) return;

        Player shooter = (Player) arrow.getShooter();

        if(entity.getName().equals(shooter.getName())) return;

        double health = Math.ceil(entity.getHealth() - event.getFinalDamage()) / 2.0D;

        if(health > 0.0D) {
            shooter.sendMessage(ChatUtils.translate( entity.getDisplayName() + ChatUtils.SECONDARY + " is now at " + ChatUtils.PRIMARY + health + "&4" + Msg.HEART + ChatUtils.SECONDARY + "."));
        }
    }
}
