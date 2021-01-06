package me.joesvart.lattemeetup.managers;

import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.util.other.ItemBuilder;
import me.joesvart.lattemeetup.util.other.MeetupUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Random;

public class KitManager {

    private LatteMeetup plugin = LatteMeetup.getInstance();

    private Random random = new Random();

    public void handleItems(Player player) {
        PlayerInventory inventory = player.getInventory();

        inventory.setHelmet(new ItemBuilder(getRandomMaterial("helmet"))
                .enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, getLevel()).build());

        inventory.setChestplate(new ItemBuilder(getRandomMaterial("chestplate"))
                .enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, getLevel()).build());

        inventory.setLeggings(new ItemBuilder(getRandomMaterial("leggings"))
                .enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, getLevel()).build());

        inventory.setBoots(new ItemBuilder(getRandomMaterial("boots"))
                .enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, getLevel()).build());

        boolean hasAnythingDiamond = false;

        for(ItemStack stack : inventory.getArmorContents()) {
            if(stack.getType().name().startsWith("DIAMOND_")) {
                hasAnythingDiamond = true;
                break;
            }
        }

        if(!hasAnythingDiamond) {
            inventory.setChestplate(new ItemBuilder(getRandomMaterial("chestplate"))
                    .enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, getLevel()).build());

            inventory.setLeggings(new ItemBuilder(getRandomMaterial("leggings"))
                    .enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, getLevel()).build());
        } else {
            boolean hasEverythingDiamond = false;

            if(inventory.getHelmet().getType().name().startsWith("DIAMOND")
                    && inventory.getChestplate().getType().name().startsWith("DIAMOND")
                    && inventory.getLeggings().getType().name().startsWith("DIAMOND")
                    && inventory.getBoots().getType().name().startsWith("DIAMOND")) {
                hasEverythingDiamond = true;
            }

            if(hasEverythingDiamond) {
                inventory.setHelmet(new ItemBuilder(Material.IRON_HELMET)
                        .enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, getLevel()).build());

                inventory.setBoots(new ItemBuilder(Material.IRON_BOOTS)
                        .enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, getLevel()).build());
            }
        }

        ItemStack sword = new ItemStack(getRandomMaterial("sword"));

        sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, getLevel());

        if(random.nextInt(4) == 2) {
            sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
        }

        inventory.setItem(0, sword);
        inventory.setItem(1, new ItemStack(Material.WATER_BUCKET));
        inventory.setItem(2, new ItemStack(Material.FISHING_ROD));

        ItemStack bow = new ItemStack(Material.BOW);

        bow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, random.nextInt(3) + 1);

        if(random.nextInt(10) == 7) {
            bow.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        }

        if(random.nextInt(10) == 7) {
            bow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
        }

        if(random.nextInt(100) >= 95) {
            bow.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        }

        inventory.setItem(3, bow);
        inventory.setItem(4, new ItemStack(Material.WATER_BUCKET));
        inventory.setItem(5, new ItemStack(Material.LAVA_BUCKET));

        ItemStack head = MeetupUtils.getGoldenHead();
        head.setAmount(random.nextInt(4) + 1);

        inventory.setItem(6, head);

        inventory.setItem(7, new ItemStack(Material.GOLDEN_APPLE, random.nextInt(7) + 1));

        inventory.setItem(8, new ItemStack(random.nextInt(3) == 1
                ? Material.WOOD : Material.COBBLESTONE, 64));

        inventory.setItem(9, new ItemStack(Material.COOKED_BEEF, random.nextInt(32)));
        inventory.setItem(16, new ItemStack(Material.ARROW, 64));

        inventory.setItem(18, new ItemStack(Material.ANVIL, random.nextInt(2) + 1));

        inventory.addItem(new ItemStack(Material.DIAMOND_PICKAXE));

        if(random.nextInt(2) == 1) {
            inventory.addItem(new ItemStack(Material.FLINT_AND_STEEL));
        }

        if(random.nextInt(2) == 1) {
            inventory.addItem(new ItemStack(Material.WEB, 6));
        }

        if(random.nextInt(2) == 1) {
            inventory.addItem(new ItemStack(Material.ENCHANTMENT_TABLE));
        }

        if(random.nextInt(5) == 3) {
            // horse egg
            inventory.addItem(new ItemStack(Material.getMaterial(383), 1, (short) 100));
        }

        if(random.nextInt(6) == 3) {
            inventory.addItem(new ItemStack(Material.ENDER_PEARL));
        }

        boolean hasAllPotions = false;

        if(random.nextInt(15) == 8) {
            inventory.addItem(new ItemStack(Material.POTION, 1, (short) 8194));
            inventory.addItem(new ItemStack(Material.POTION, 1, (short) 8227));

            hasAllPotions = true;
        }

        if(!hasAllPotions) {
            if(random.nextInt(5) == 3) {
                inventory.addItem(new ItemStack(Material.POTION, 1, (short) 8194));
            }

            if(random.nextInt(5) == 3) {
                inventory.addItem(new ItemStack(Material.POTION, 1, (short) 8227));
            }
        }

        inventory.addItem(new ItemStack(Material.EXP_BOTTLE, 64));

        player.updateInventory();
    }

    private Material getRandomMaterial(String type) {
        int r = random.nextInt(100);

        switch (type) {
            case "helmet": return r >= 50 ? Material.DIAMOND_HELMET : Material.IRON_HELMET;
            case "chestplate": return r >= 60 ? Material.DIAMOND_CHESTPLATE : Material.IRON_CHESTPLATE;
            case "leggings": return r >= 60 ? Material.DIAMOND_LEGGINGS : Material.IRON_LEGGINGS;
            case "boots": return r >= 50 ? Material.DIAMOND_BOOTS : Material.IRON_BOOTS;
            case "sword": return r >= 50 ? Material.DIAMOND_SWORD : Material.IRON_SWORD;
            default: return Material.GRASS;
        }
    }

    private int getLevel() {
        int r = random.nextInt(100);
        return r >= 65 ? 3 : r >= 35 ? 2 : 1;
    }
}
