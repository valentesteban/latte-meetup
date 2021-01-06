package me.joesvart.lattemeetup.menu.buttons;

import lombok.AllArgsConstructor;
import me.joesvart.lattemeetup.util.chat.CC;
import me.joesvart.lattemeetup.util.menu.Button;
import me.joesvart.lattemeetup.util.other.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class BackButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.BARRIER)
            .name(CC.translate("&cClick to close the menu."))
            .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        player.playSound(player.getLocation(), Sound.CLICK, 1 ,1);
        player.closeInventory();

    }
}