package me.joesvart.lattemeetup.tasks;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.game.GameData;
import me.joesvart.lattemeetup.game.GameManager;
import me.joesvart.lattemeetup.util.chat.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Arrays;

public class StartingTask extends BukkitRunnable {

    private LatteMeetup plugin = LatteMeetup.getInstance();

    public StartingTask() {
        runTaskTimer(plugin, 0L, 20L);
    }

    @Override
    public void run() {
        GameData data = GameManager.getData();

        String loading = data.getLoading();
        data.setLoading(loading.equals("") ? "." : loading.equals(".") ? ".." : loading.equals("..") ? "..." : loading.equals("...") ? "" : "");

        data.setStartingTime(data.getStartingTime() - 1);

        if(data.getStartingTime() <= 0) {
            cancel();

        plugin.getGameManager().handleStart();

        }

        if(Arrays.asList(15, 10, 5, 4, 3, 2, 1).contains(data.getStartingTime())) {
            String format = ChatUtils.translate(plugin.getMessagesConfig().getString("MESSAGES.GAME-STARTING-TIME")
                .replace("<time>", "" + data.getStartingTime()));
            Msg.sendMessage(format);
        }

        if (LatteMeetup.getInstance().getMessagesConfig().getBoolean("BOOLEANS.SOUNDS")) {
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1F, 1F));
        }
    }
}

