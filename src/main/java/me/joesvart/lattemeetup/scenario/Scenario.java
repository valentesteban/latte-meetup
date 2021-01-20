package me.joesvart.lattemeetup.scenario;

import me.joesvart.lattemeetup.LatteMeetup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class Scenario implements Listener {

	private final String name;
	private final ItemStack icon;
	private final String description;

	private boolean active = false;

	public void handleToggle() {
		Bukkit.getPluginManager().registerEvents(this, LatteMeetup.getInstance());
	}
}

