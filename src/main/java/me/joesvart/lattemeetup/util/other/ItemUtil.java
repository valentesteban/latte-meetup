package me.joesvart.lattemeetup.util.other;

import lombok.RequiredArgsConstructor;
import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattemeetup.util.chat.RomanNumerals;
import me.joesvart.lattemeetup.util.chat.StringUtil;
import me.joesvart.lattemeetup.util.chat.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class ItemUtil {

	private ItemUtil() {
		throw new RuntimeException("Cannot instantiate a utility class.");
	}

	public static String formatMaterial(Material material) {
		String name = material.toString();
		name = name.replace('_', ' ');
		String result = "" + name.charAt(0);
		for (int i = 1; i < name.length(); i++) {
			if (name.charAt(i - 1) == ' ') {
				result += name.charAt(i);
			} else {
				result += Character.toLowerCase(name.charAt(i));
			}
		}
		return result;
	}

	public static ItemStack createPotion(String name, PotionType type, int level, int duration) {
		ItemStack itemStack = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) itemStack.getItemMeta();

		if (name != null) {
			meta.setDisplayName(ChatUtils.translate(name));
		}

		meta.setLore(Arrays.asList(
				"", ChatUtils.GRAY + StringUtils.toNiceString(type.name()) + " " + RomanNumerals.toRoman(level) + " Potion",
			ChatUtils.GRAY + "    Duration: " + TimeUtil.millisToRoundedTime(duration * 1000L)));

		meta.addCustomEffect(new PotionEffect(type.getEffectType(), duration * 20, level - 1), false);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS);

		itemStack.setItemMeta(meta);
		return itemStack;
	}

	public static ItemStack createPotion(PotionType type, int level, int duration) {
		return createPotion(null, type, level, duration);
	}

	public static ItemStack enchantItem(ItemStack itemStack, ItemEnchant... enchantments) {
		Arrays.asList(enchantments).forEach(enchantment -> itemStack.addUnsafeEnchantment(enchantment.enchantment, enchantment.level));
		return itemStack;
	}

	public static ItemStack createItem(Material material, String name) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ChatUtils.translate(name));
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack createItem(Material material, String name, int amount) {
		ItemStack item = new ItemStack(material, amount);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ChatUtils.translate(name));
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack createItem(Material material, String name, int amount, short damage) {
		ItemStack item = new ItemStack(material, amount, damage);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ChatUtils.translate(name));
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack hideEnchants(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack setUnbreakable(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack renameItem(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ChatUtils.translate(name));
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack reloreItem(ItemStack item, String... lores) {
		return reloreItem(ReloreType.OVERWRITE, item, lores);
	}

	public static ItemStack reloreItem(ReloreType type, ItemStack item, String... lores) {
		ItemMeta meta = item.getItemMeta();

		List<String> lore = meta.getLore();
		if (lore == null) {
			lore = new LinkedList<>();
		}

		switch (type) {
			case APPEND:
				lore.addAll(Arrays.asList(lores));
				meta.setLore(ChatUtils.translate(lore));
				break;
			case PREPEND:
				List<String> nLore = new LinkedList<>(Arrays.asList(lores));
				nLore.addAll(ChatUtils.translate(lore));
				meta.setLore(ChatUtils.translate(nLore));
				break;
			case OVERWRITE:
				meta.setLore(Arrays.asList(lores));
				break;
		}

		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack addItemFlag(ItemStack item, ItemFlag flag) {
		ItemMeta meta = item.getItemMeta();

		meta.addItemFlags(flag);
		item.setItemMeta(meta);

		return item;
	}

	public enum ReloreType {
		OVERWRITE,
		PREPEND,
		APPEND
	}

	@RequiredArgsConstructor
	public static class ItemEnchant {
		private final Enchantment enchantment;
		private final int level;
	}

}
