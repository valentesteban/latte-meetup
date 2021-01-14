package me.joesvart.lattemeetup.util.menu.button;

import lombok.AllArgsConstructor;
import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattelibs.item.ItemCreator;
import me.joesvart.lattemeetup.util.menu.Button;
import me.joesvart.lattemeetup.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@AllArgsConstructor
public class BackButton extends Button {

    private final Menu back;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemCreator(Material.REDSTONE)
            .name(ChatUtils.RED + ChatUtils.BOLD + "Back")
            .lore(Arrays.asList(
                ChatUtils.RED + "Click here to return to",
                ChatUtils.RED + "the previous menu.")
            )
            .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Button.playNeutral(player);
        back.openMenu(player);
    }

}
