package me.joesvart.lattemeetup.tasks;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.game.GameData;
import me.joesvart.lattemeetup.game.GameManager;
import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattemeetup.util.other.BungeeUtil;
import me.joesvart.lattemeetup.util.chat.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Arrays;
import java.util.Objects;

public class EndTask extends BukkitRunnable {

    private final LatteMeetup plugin = LatteMeetup.getInstance();

    private int launchedFireworks;

    public EndTask() {
        runTaskTimer(LatteMeetup.getInstance(), 0L, 20L);
    }

    @Override
    public void run() {
        GameData data = GameManager.getData();

        data.setEndTime(data.getEndTime() - 1);

        if(data.getEndTime() <= 0) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
            cancel();
            return;
        }

        if(launchedFireworks++ <= 5) {
            PlayerData.getListOfAlivePlayers().stream().map(d -> Bukkit.getPlayer(d.getUuid())).filter(Objects::nonNull).forEach(player -> {
                Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
                FireworkMeta fireworkMeta = firework.getFireworkMeta();
                fireworkMeta.addEffect(FireworkEffect.builder().flicker(false).trail(true).with(FireworkEffect.Type.BURST).withColor(Color.ORANGE).withFade(Color.YELLOW).build());
                fireworkMeta.setPower(3);
                firework.setFireworkMeta(fireworkMeta);
            });
        }

        if(data.getEndTime() == 2) {
            Bukkit.getOnlinePlayers().forEach(player -> BungeeUtil.sendToServer(player, "hub"));
        }

        if(Arrays.asList(15, 10, 5, 4, 3, 2, 1).contains(data.getEndTime())) {
            String format = ChatUtils.translate(plugin.getMessagesConfig().getString("MESSAGES.GAME-END-TIME")
                .replace("<time>", "" + data.getEndTime()));
            Msg.sendMessage(format);

            if (LatteMeetup.getInstance().getMessagesConfig().getBoolean("BOOLEANS.SOUNDS")) {
                Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1F, 1F));
            }
        }
    }
}
