package me.joesvart.lattemeetup.managers;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattelibs.item.ItemCreator;
import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattemeetup.player.PlayerState;
import me.joesvart.lattemeetup.util.chat.StringUtils;
import me.joesvart.lattemeetup.util.other.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class SpectatorManager {

    private LatteMeetup plugin = LatteMeetup.getInstance();

    public void handleEnable(Player player) {
        player.setAllowFlight(true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setGameMode(GameMode.SURVIVAL);

        plugin.getItemManager().handleSpectatorInventory(player);

        if(Bukkit.getWorld("meetup_world") != null) {
            player.teleport(new Location(Bukkit.getWorld("meetup_world"), 0, 100, 0));
            player.setGameMode(GameMode.SURVIVAL);
        }

        Bukkit.getScheduler().runTask(LatteMeetup.getInstance(), () -> Bukkit.getOnlinePlayers().forEach(online -> online.hidePlayer(player)));
        player.sendMessage("");
        player.sendMessage(ChatUtils.GRAY + "You are now in spectator mode.");
        player.setFlySpeed(0.2F);

        /* Send a title saying you has been killed, and now you are a spectator. */
        if (plugin.getMessagesConfig().getBoolean("BOOLEANS.TITLES")) {
            player.sendTitle(ChatUtils.RED + ChatUtils.B + "DEAD", ChatUtils.YELLOW + "You are now a spectator!");
        }

        PlayerData data = PlayerData.getByName(player.getName());
        data.setPlayerState(PlayerState.SPECTATING);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if(!data.isAlive() && !player.getGameMode().equals(GameMode.CREATIVE)) {
                player.setGameMode(GameMode.CREATIVE);
            }
        }, 10L);
    }

    public void handleDisable(Player player) {
        PlayerData data = PlayerData.getByName(player.getName());

        if(data.isSpectator()) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);

            if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            }

            player.setFlySpeed(0.1F);
            Bukkit.getScheduler().runTask(LatteMeetup.getInstance(), () -> Bukkit.getOnlinePlayers().stream().filter(online -> !online.canSee(player)).forEach(online -> online.showPlayer(player)));
            data.setPlayerState(PlayerState.LOBBY);
        }
    }

    public void handleRandomTeleport(Player player) {
        if(PlayerData.getAlivePlayers() == 0) {
            player.sendMessage(ChatUtils.translate("&cThere are currently no alive players."));
            return;
        }

        PlayerData data = PlayerData.getListOfAlivePlayers().get(new Random().nextInt(PlayerData.getListOfAlivePlayers().size()));
        Player target = Bukkit.getPlayer(data.getName());

        if(target != null) {
            player.teleport(target);
            player.sendMessage(ChatUtils.GRAY + "Teleporting you to " + ChatUtils.PRIMARY + target.getDisplayName() + ChatUtils.GRAY + ".");
        }
    }

    public Inventory getInventory(int page) {
        int max = (int) Math.ceil(PlayerData.getAlivePlayers() / 18.0);

        Inventory inventory = Bukkit.createInventory(null, 27, ChatUtils.translate("Player Remaining ┃ " + page + "/" + (max == 0 ? 1 : max)));
        inventory.setItem(0, new ItemCreator(Material.ARROW).name(ChatUtils.GRAY + "Previous Page").build());
        inventory.setItem(8, new ItemCreator(Material.ARROW).name(ChatUtils.GREEN + "Next Page").build());

        int minIndex = (int) ((double) (page - 1) * 18);
        int maxIndex = (int) ((double) (page) * 18);

        List<PlayerData> toLoop = new ArrayList<>(PlayerData.getPlayerDatas().values());
        Collections.reverse(toLoop);
        toLoop.removeIf(data -> !data.isAlive() || Bukkit.getPlayer(data.getName()) == null);

        toLoop.forEach(data -> {
            int number = toLoop.indexOf(data);

            if(number >= minIndex && number < maxIndex) {
                number -= (int) ((double) (18) * (page - 1)) - 9;

                inventory.setItem(number, new ItemCreator(Material.SKULL_ITEM)
                        .name(ChatUtils.PRIMARY + data.getName())
                        .lore(ChatUtils.GRAY + ChatUtils.STRIKE_THROUGH + "-----------------------------")
                        .lore(ChatUtils.GREEN + "Click to teleport to " + ChatUtils.PRIMARY + data.getName())
                        .lore(ChatUtils.GRAY + ChatUtils.STRIKE_THROUGH + "-----------------------------").build());
            }
        });

        return inventory;
    }

    public Inventory getPlayerInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 6 * 9, "Player " + player.getName());

        ItemStack[] contents = player.getInventory().getContents();
        ItemStack[] armor = player.getInventory().getArmorContents();

        double health = player.getHealth();
        double food = player.getFoodLevel();

        List<String> potionEffectStrings = new ArrayList<>();

        player.getActivePotionEffects().forEach(potionEffect -> potionEffectStrings.add(ChatUtils.SECONDARY + StringUtils.toNiceString(potionEffect.getType().getName().toLowerCase()) + " " + MathUtil.convertToRomanNumeral(potionEffect.getAmplifier() + 1) + ChatUtils.PRIMARY + " (" + MathUtil.convertTicksToMinutes(potionEffect.getDuration()) + ")"));

        for(int i = 0; i < 9; i++) {
            inventory.setItem(i + 27, contents[i]);
            inventory.setItem(i + 18, contents[i + 27]);
            inventory.setItem(i + 9, contents[i + 18]);
            inventory.setItem(i, contents[i + 9]);
        }

        int gapples = (int) Arrays.stream(contents).filter(Objects::nonNull).filter(d -> d.getType() == Material.GOLDEN_APPLE).mapToInt(ItemStack::getAmount).count();

        inventory.setItem(47, new ItemCreator(Material.GOLDEN_APPLE).amount(gapples).name(ChatUtils.PRIMARY + "Golden Apples: " + ChatUtils.SECONDARY + gapples).build());
        inventory.setItem(48, ItemUtil.createItem(Material.SKULL_ITEM, ChatUtils.PRIMARY + "Hearts: " + ChatUtils.SECONDARY + MathUtil.roundToHalves(health / 2.0D) + " / 10 ❤", (int) Math.round(health / 2.0D)));
        inventory.setItem(49, ItemUtil.createItem(Material.COOKED_BEEF, ChatUtils.PRIMARY + "Hunger: " + ChatUtils.SECONDARY + MathUtil.roundToHalves(food / 2.0D) + " / 10 ❤", (int) Math.round(food / 2.0D)));
        inventory.setItem(50, ItemUtil.reloreItem(ItemUtil.createItem(Material.BREWING_STAND_ITEM, ChatUtils.PRIMARY + "Potion Effects", potionEffectStrings.size()), potionEffectStrings.toArray(new String[]{})));

        PlayerData data = PlayerData.getByName(player.getName());

        inventory.setItem(45, new ItemCreator(Material.PAPER).name(ChatUtils.PRIMARY + "Close Preview").build());
        inventory.setItem(53, new ItemCreator(Material.PAPER).name(ChatUtils.PRIMARY + "Close Preview").build());

        for(int i = 36; i < 40; i++) {
            inventory.setItem(i, armor[39 - i]);
        }

        return inventory;
    }
}