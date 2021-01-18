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

public class VoteTask extends BukkitRunnable {

    private LatteMeetup plugin = LatteMeetup.getInstance();

    public VoteTask() {
        runTaskTimer(plugin, 0L, 20L);
    }

    @Override
    public void run() {
        GameData data = GameManager.getData();

        String loading = data.getLoading();
        data.setLoading(loading.equals("") ? "." : loading.equals(".") ? ".." : loading.equals("..") ? "..." : loading.equals("...") ? "" : "");

        if(data.isCanStartCountdown()) {
            data.setVoteTime(data.getVoteTime() - 1);
        }

        if(data.getVoteTime() <= 0) {
            plugin.getGameManager().handleStarting();
            cancel();
            return;
        }

        if(Bukkit.getOnlinePlayers().size() >= LatteMeetup.MIN_PLAYERS) {
            data.setCanStartCountdown(true);
        }

        if(Arrays.asList(30, 15, 10, 5, 4, 3, 2, 1).contains(data.getVoteTime())) {
            Msg.sendMessage(ChatUtils.SECONDARY + "Voting ends in " + ChatUtils.PRIMARY + data.getVoteTime() + ChatUtils.SECONDARY + " second" + (data.getVoteTime() > 1 ? "s" : "") + ".");

            if (plugin.getMessagesConfig().getBoolean("BOOLEANS.SOUNDS")) {
                Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1F, 1F));
            }
        }
    }
}
