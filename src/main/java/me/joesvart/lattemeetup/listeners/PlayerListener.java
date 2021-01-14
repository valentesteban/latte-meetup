package me.joesvart.lattemeetup.listeners;

import javafx.animation.Animation;
import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattelibs.utils.AnimationUtils;
import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.game.GameManager;
import me.joesvart.lattemeetup.game.GameState;
import me.joesvart.lattemeetup.leaderboards.menus.LeaderboardsMenu;
import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattemeetup.player.PlayerState;
import me.joesvart.lattemeetup.scenario.Scenario;
import me.joesvart.lattemeetup.scenario.ScenarioManager;
import me.joesvart.lattemeetup.scenario.types.TimeBombScenario;
import me.joesvart.lattemeetup.util.chat.*;
import me.joesvart.lattemeetup.util.other.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PlayerListener implements Listener {

    private final LatteMeetup plugin = LatteMeetup.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if(!GameManager.getData().isGenerated()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatUtils.RED + "Please wait for map to generate!");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData data = PlayerData.getByName(player.getName());

        event.setJoinMessage(null);

        if(!data.isLoaded()) {
            data.load();
        }

        if(!data.isLoaded()) {
            player.kickPlayer(StringUtils.LOAD_ERROR);
        }

        /* Animation for the Links on the Tablist and the Scoreboard */
        AnimationUtils animation = new AnimationUtils("Link", player.getUniqueId(), 40L);

        animation.getLines().add(ChatUtils.translate("&7&olatte.org"));
        animation.getLines().add(ChatUtils.translate("&7&oww.latte.org"));
        animation.getLines().add(ChatUtils.translate("&7&olatte.org/discord"));
        animation.getLines().add(ChatUtils.translate("&7&ots.latte.org"));
        animation.getLines().add(ChatUtils.translate("&7&o@LatteNET"));

        switch (GameManager.getData().getGameState()) {
            case WINNER:
            case PLAYING:
                plugin.getSpectatorManager().handleEnable(player);

                Location location = new Location(Bukkit.getWorld("meetupworld"), 0, Bukkit.getWorld("meetupworld").getHighestBlockYAt(0, 0) + 15, 0);
                player.teleport(location);

                Bukkit.getScheduler().runTaskLater(LatteMeetup.getInstance(), () -> {
                    if(PlayerData.getByName(player.getName()).isSpectator()) {
                        Bukkit.getOnlinePlayers().stream().filter(p -> PlayerData.getByName(p.getName()).isSpectator()).forEach(player::hidePlayer);
                    }
                }, 1L);
                break;
            case STARTING:
            case VOTE:
                MeetupUtils.clearPlayer(player);

                if(GameManager.getData().getGameState().equals(GameState.VOTE)) {
                    player.teleport(new Location(Bukkit.getWorld("world"), 0.5, 100, 0.5, 0, 0));
                    plugin.getItemManager().handleLobbyInventory(player);
                } else {
                    player.teleport(MeetupUtils.getScatterLocation());
                }

                Msg.sendMessage(ChatUtils.PRIMARY + player.getName() + ChatUtils.SECONDARY + " has joined. " + ChatUtils.GRAY + "(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ")");

                if(GameManager.getData().getGameState().equals(GameState.STARTING)) {
                    plugin.getKitManager().handleItems(player);
                }

                break;
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.setQuitMessage(null);

        plugin.getSpectatorManager().handleDisable(player);

        PlayerData playerData = PlayerData.getByName(player.getName());

        if(playerData.getLastVoted() != null) {
            plugin.getVoteManager().handleRemoveOne(ScenarioManager.getByName(playerData.getLastVoted()));
            playerData.setLastVoted(null);
            plugin.getInventoryManager().setupScenarios();
        }

        if(playerData.isAlive() && GameManager.getData().getGameState().equals(GameState.PLAYING)) {

            playerData.setPlayerState(PlayerState.SPECTATING);
            plugin.getGameManager().handleCheckWinners();

            /* If the player disconnect remove from the game and send this message */
            Msg.sendMessage("&c" + player.getName() + ChatUtils.DARK_RED + "[" + playerData.getGameKills() + "] " + ChatUtils.GRAY + "has been disqualified for disconnecting.");

            if(ScenarioManager.getByName("Time Bomb").isActive() && ScenarioManager.getByName("Safe Loot").isActive()) {
                List<ItemStack> items = new ArrayList<>();

                Stream.of(player.getInventory().getArmorContents()).filter(stack -> stack != null && stack.getType() != Material.AIR).forEach(items::add);
                Stream.of(player.getInventory().getContents()).filter(stack -> stack != null && stack.getType() != Material.AIR).forEach(items::add);

                TimeBombScenario.handleTimeBomb(player, null, items, true, true);
            } else if(ScenarioManager.getByName("Time Bomb").isActive() && !ScenarioManager.getByName("Safe Loot").isActive()) {
                List<ItemStack> items = new ArrayList<>();

                Stream.of(player.getInventory().getArmorContents()).filter(stack -> stack != null && stack.getType() != Material.AIR).forEach(items::add);
                Stream.of(player.getInventory().getContents()).filter(stack -> stack != null && stack.getType() != Material.AIR).forEach(items::add);

                TimeBombScenario.handleTimeBomb(player, null, items, true, false);
            } else if(!ScenarioManager.getByName("Time Bomb").isActive() && ScenarioManager.getByName("Safe Loot").isActive()) {
                List<ItemStack> items = new ArrayList<>();

                Stream.of(player.getInventory().getArmorContents()).filter(stack -> stack != null && stack.getType() != Material.AIR).forEach(items::add);
                Stream.of(player.getInventory().getContents()).filter(stack -> stack != null && stack.getType() != Material.AIR).forEach(items::add);

                TimeBombScenario.handleTimeBomb(player, null, items, false, true);
            }
        }

        playerData.save();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction().name().startsWith("RIGHT_")) {
            ItemStack item = event.getItem();

            if(item == null) {
                return;
            }

            Player player = event.getPlayer();

            switch (item.getType()) {
                case BOOK:
                    player.openInventory(plugin.getInventoryManager().getScenarioInventory());
                    break;
                case BED:
                    BungeeUtil.sendToServer(player, "hub");
                    break;
                case EMERALD:
                    new LeaderboardsMenu().openMenu(player);
                    break;
                case MUSHROOM_SOUP:
                    if(player.getHealth() <= 19.0D && !player.isDead()) {
                        if(player.getHealth() < 20.0D || player.getFoodLevel() < 20) {
                            player.getItemInHand().setType(Material.BOWL);
                        }

                        player.setHealth(player.getHealth() + 7.0D > 20.0D ? 20.0D : player.getHealth() +
                            7.0D);
                        player.setFoodLevel(player.getFoodLevel() + 2 > 20 ? 20 : player.getFoodLevel() + 2);
                        player.setSaturation(12.8F);
                        player.updateInventory();
                    }
                    break;
                case WORKBENCH:
                    event.setCancelled(false);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    player.sendMessage(ChatColor.RED + "Crafting tables are disabled.");
                    break;
            }

            if(PlayerData.getByName(player.getName()).isSpectator()) {
                switch (player.getItemInHand().getType()) {
                    case ITEM_FRAME:
                        if(PlayerData.getAlivePlayers() == 0) {
                            player.sendMessage(ChatUtils.translate("&cThere are currently no alive players."));
                            return;
                        }

                        player.openInventory(plugin.getSpectatorManager().getInventory(1));
                        break;
                    case DIAMOND:
                        if(PlayerData.getAlivePlayers() == 0) {
                            player.sendMessage(ChatUtils.translate("&cThere are currently no alive players."));
                            return;
                        }

                        plugin.getSpectatorManager().handleRandomTeleport(player);
                        break;
                    case BED:
                        BungeeUtil.sendToServer(player, "hub");
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if(event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        PlayerData data = PlayerData.getByName(player.getName());

        if(data.isSpectator() && !player.hasPermission("lattemeetup.staff")) {
            event.setCancelled(true);
            Bukkit.getOnlinePlayers().stream().filter(p ->
                PlayerData.getByName(p.getName()).isSpectator())
                .forEach(p -> p.sendMessage(getSpectatorFormat(player, event.getMessage())));
        }
    }

    private String getSpectatorFormat(Player player, String message) {
        if(!player.hasPermission("lattemeetup.staff")) {
            if(PlayerData.getByName(player.getName()).isSpectator()) {
                return ChatUtils.translate("&7[Spectator] " + player.getName() + "&7: ") + message;
            }
        }
        return null;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null
                || event.getCurrentItem().getType() == Material.AIR
                || !event.getCurrentItem().hasItemMeta()
                || event.getCurrentItem().getItemMeta() == null) {
            return;
        }

        String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
        String title = event.getClickedInventory().getTitle();
        Player player = (Player) event.getWhoClicked();

        if(title.equals(plugin.getInventoryManager().getScenarioInventory().getTitle())) {
            event.setCancelled(true);

            Scenario scenario = ScenarioManager.getByName(name);

            if(scenario != null) {
                if(plugin.getVoteManager().hasVoted(player)) {
                    plugin.getVoteManager().handleRemove(player, scenario);
                    plugin.getInventoryManager().setupScenarios();
                    return;
                }

                plugin.getVoteManager().handleAddVote(player, scenario);
                plugin.getInventoryManager().setupScenarios();
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(isNotStart()) {
            event.setCancelled(true);
            return;
        }

        if(!plugin.getGameManager().getWhitelistedBlocks().contains(event.getBlock().getType())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(isNotStart()) {
            event.setCancelled(true);
            return;
        }

        int max = event.getBlock().getWorld().getHighestBlockYAt(event.getBlock().getLocation()) + 4;
        if(event.getBlock().getY() > max) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCraftItemEvent(CraftItemEvent event) {
        if(!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);
        event.setResult(Event.Result.DENY);

        player.sendMessage(ChatUtils.translate("&cYou cannot craft items in UHC Meetup."));
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if(isNotStart()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        if(isNotStart()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityMount(EntityMountEvent event) {
        if(event.getEntity() instanceof Player) {
            if(PlayerData.getByName(event.getEntity().getName()).isSpectator() || isNotStart()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if(event.getEntity() instanceof Horse) {
           return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWeatherChange(WeatherChangeEvent event) {
        if(!event.toWeatherState()) return;

        event.setCancelled(true);
    }

    public boolean isNotStart() {
        return !GameManager.getData().getGameState().equals(GameState.PLAYING);
    }
}
