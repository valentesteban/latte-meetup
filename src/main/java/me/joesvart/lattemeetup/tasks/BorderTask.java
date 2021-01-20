package me.joesvart.lattemeetup.tasks;

import me.joesvart.lattelibs.chat.ChatUtils;
import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.game.GameData;
import me.joesvart.lattemeetup.game.GameManager;
import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattemeetup.util.chat.Msg;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class BorderTask extends BukkitRunnable {

    private final LatteMeetup plugin = LatteMeetup.getInstance();

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

            /**
             * Border shrunk message
             */
            String format = ChatUtils.translate(plugin.getMessagesConfig().getString("MESSAGES.BORDER-SHRUNK")
                .replace("<border-size>", "" + data.getBorder()));
            Msg.sendMessage(format);
        } else if(Arrays.asList(120, 60, 45, 30, 15, 10, 5, 4, 3, 2, 1).contains(data.getBorderTime())) {
            /**
             * Border shrink message
             */
            String format = ChatUtils.translate(plugin.getMessagesConfig().getString("MESSAGES.BORDER-SHRINK")
                .replace("<min-sec>", "" + (data.getBorderTime() < 60 ? data.getBorderTime() + (data.getBorderTime() == 1 ? " second" : " seconds") : data.getBorderTime() / 60 + ((data.getBorderTime() / 60) == 1 ? " minute" : " minutes"))));
            Msg.sendMessage(format);
        }
    }
}
