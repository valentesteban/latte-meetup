package me.joesvart.lattemeetup.util.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
@Setter
public class BungeeConnectEvent extends PlayerEvent implements Cancellable {

    private boolean cancelled;
    private String server;

    public BungeeConnectEvent(Player player, String server) {
        super(player);
        this.server = server;
    }
}
