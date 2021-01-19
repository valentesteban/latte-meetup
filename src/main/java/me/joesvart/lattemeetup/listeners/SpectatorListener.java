package me.joesvart.lattemeetup.listeners;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Gate;
import org.bukkit.material.TrapDoor;

public class SpectatorListener implements Listener {

	private LatteMeetup plugin = LatteMeetup.getInstance();

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();

		if(isSpectator(player)) {
			if(event.getClickedInventory() != null && event.getClickedInventory().getTitle().contains("Alive Players - ")) {
				event.setCancelled(true);

				ItemStack stack = event.getCurrentItem();

				if(stack != null && stack.getType() != Material.AIR) {
					String title = event.getClickedInventory().getTitle();
					String displayName = stack.getItemMeta().getDisplayName();

					int page = Integer.parseInt(title.substring(title.lastIndexOf("/") - 1, title.lastIndexOf("/")));
					int total = Integer.parseInt(title.substring(title.lastIndexOf("/") + 1, title.lastIndexOf("/") + 2));

					if(displayName.contains("Next Page")) {
						if(page + 1 > total) {
							player.sendMessage(ChatUtils.translate("&cThere are no more pages."));
							return;
						}

						player.openInventory(plugin.getSpectatorManager().getInventory(page + 1));
						return;
					} else if(displayName.contains("Previous Page")) {
						if(page == 1) {
							player.sendMessage(ChatUtils.translate("&cYou are on the first page."));
							return;
						}

						player.openInventory(plugin.getSpectatorManager().getInventory(page - 1));
					} else if(displayName.contains("Exit")) {
						player.closeInventory();
					}

					if(stack.getType().equals(Material.SKULL_ITEM)) {
						String name = ChatColor.stripColor(displayName);
						Player target = Bukkit.getPlayer(name);

						if(target != null) {
							player.teleport(target);
							player.sendMessage(ChatUtils.SECONDARY + "Teleporting you to " + target.getDisplayName() + ChatUtils.SECONDARY + ".");
							return;
						}
					}
				}
			}

			if(event.getSlotType().equals(SlotType.OUTSIDE)) {
				event.setCancelled(true);
				return;
			}

			ItemStack item = event.getCurrentItem();
			InventoryAction action = event.getAction();

			if(item == null || item.getType().equals(Material.AIR)) {
				if(action.equals(InventoryAction.HOTBAR_SWAP) || action.equals(InventoryAction.SWAP_WITH_CURSOR)) {
					event.setCancelled(true);
					return;
				}
			}

			event.setCancelled(true);

			if(item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains("Close Preview")) {
				Bukkit.getScheduler().runTaskLater(plugin, player::closeInventory, 1L);
			}
		}
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if(isSpectator(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onSilentView(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if(!isSpectator(player)) {
			return;
		}

		event.setCancelled(true);

		if(!event.hasBlock()) {
			return;
		}

		Block block = event.getClickedBlock();

		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST) && !player.isSneaking()) {
				event.setCancelled(true);

				if(!player.hasPermission("lattemeetup.staff")) {
					return;
				}

				Chest chest = (Chest) block.getState();
				Inventory inventory = Bukkit.createInventory(null, chest.getInventory().getSize());
				inventory.setContents(chest.getInventory().getContents());
				player.openInventory(inventory);
				player.sendMessage(ChatUtils.translate("&7Opening silently..."));
			}
		}

		if(block.getType() == Material.WOODEN_DOOR || block.getType() == Material.IRON_DOOR_BLOCK || block.getType() == Material.FENCE_GATE) {
			event.setCancelled(true);
			Location location = block.getLocation().setDirection(player.getLocation().getDirection());

			int height = 0;
			if(block.getType() == Material.WOODEN_DOOR || block.getType() == Material.IRON_DOOR_BLOCK) {
				Material material = block.getLocation().add(0.0D, -1.0D, 0.0D).getBlock().getType();
				if(material == Material.WOODEN_DOOR || material == Material.IRON_DOOR_BLOCK) {
					height = -1;
				}
			}

			switch (event.getBlockFace()) {
				case EAST:
					player.teleport(location.add(-0.5D, height, 0.5D));
					break;
				case NORTH:
					player.teleport(location.add(0.5D, height, 1.5D));
					break;
				case SOUTH:
					player.teleport(location.add(0.5D, height, -0.5D));
					break;
				case WEST:
					player.teleport(location.add(1.5D, height, 0.5D));
					break;
				case UP:
					if(block.getState().getData() instanceof Gate) {
						Gate gate = (Gate) block.getState().getData();
						switch(gate.getFacing()) {
							case NORTH:
							case SOUTH:
								player.teleport(location.add(player.getLocation().getX() > location.getX() ? -0.5D : 1.5D, height, 0.5D));
								break;
							case EAST:
							case WEST:
								player.teleport(location.add(0.5D, height, player.getLocation().getZ() > location.getZ() ? -0.5D : 1.5D));
								break;
						}
					}
					break;
			}
		} else if(block.getType() == Material.TRAP_DOOR && ((TrapDoor) block.getState().getData()).isOpen()) {
			Location location = block.getLocation().setDirection(player.getLocation().getDirection());
			switch (event.getBlockFace()) {
				case UP:
					player.teleport(location.add(0.5D, -1.0D, 0.5D));
					break;
				case DOWN:
					player.teleport(location.add(0.5D, 1.0D, 0.5D));
					break;
			}
		}
	}

	@EventHandler
	public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
		if(event.getTarget() instanceof Player) {
			if(isSpectator((Player) event.getTarget())) {
				event.setTarget(null);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onEntityTarget(EntityTargetEvent event) {
		if(event.getTarget() instanceof Player) {
			if(isSpectator((Player) event.getTarget())) {
				event.setTarget(null);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onHangingPlace(HangingPlaceEvent event) {
		if(event.getEntity() instanceof ItemFrame) {
			if(isSpectator(event.getPlayer())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if(isSpectator(event.getPlayer())) {
			event.setCancelled(true);
		}
	}


	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(isSpectator(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(isSpectator(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if(event.getEntity() instanceof Player) {
			if(isSpectator((Player) event.getEntity())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			if(isSpectator((Player) event.getEntity())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player) {
			if(isSpectator((Player) event.getDamager())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onVehicleEnter(VehicleEnterEvent event) {
		if(event.getEntered() instanceof Player) {
			if(isSpectator((Player) event.getEntered())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if(event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();

		if(player.getItemInHand().getType() != Material.AIR) {
			return;
		}

		if(event.getRightClicked() instanceof Player) {
			Player rightClicked = (Player) event.getRightClicked();

			if(isSpectator(player) && player.hasPermission("lattemeetup.staff")) {
				player.openInventory(plugin.getSpectatorManager().getPlayerInventory(rightClicked));
				event.setCancelled(true);
			}
		}
	}

	private boolean isSpectator(Player player) {
		return PlayerData.getByName(player.getName()).isSpectator();
	}
}
