package me.joesvart.lattemeetup.tasks;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.game.GameData;
import me.joesvart.lattemeetup.game.GameManager;
import me.joesvart.lattemeetup.border.Border;
import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattemeetup.util.chat.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class BorderTask extends BukkitRunnable {

    public BorderTask() {
        runTaskTimer(LatteMeetup.getInstance(), 0L, 20L);
    }

    @Override
    public void run() {
        GameData data = GameManager.getData();

        if(PlayerData.getAlivePlayers() <= 4 && data.getBorder() >= 50
                && data.getBorderTime() > 30) {
            data.setBorderTime(30);
        }

        if(data.decrementBorderTime() == 0) {
            if(data.getNextBorder() != 25) {
                data.setBorderTime(120);
            }

            new Border(Bukkit.getWorld("meetup_world"), data.getNextBorder());
            Msg.sendMessage(ChatUtils.SECONDARY + "The border has shrunk to " + ChatUtils.PRIMARY + data.getBorder() + ChatUtils.SECONDARY + ".", Sound.CLICK);
        } else if(Arrays.asList(120, 60, 45, 30, 15, 10, 5, 4, 3, 2, 1).contains(data.getBorderTime())) {
            Msg.sendMessage(ChatUtils.SECONDARY + "The border will shrink to " + ChatUtils.PRIMARY + data.getNextBorder() + ChatUtils.SECONDARY + " in " + ChatUtils.PRIMARY + (data.getBorderTime() < 60 ? data.getBorderTime() + ChatUtils.SECONDARY + (data.getBorderTime() == 1 ? " second" : " seconds") : data.getBorderTime() / 60 + ChatUtils.SECONDARY + ((data.getBorderTime() / 60) == 1 ? " minute" : " minutes")) + ChatUtils.SECONDARY + ".", Sound.ORB_PICKUP);
        }
    }
}
