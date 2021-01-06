package me.joesvart.lattemeetup.commands.impl;

import me.joesvart.lattemeetup.LatteMeetup;
import me.joesvart.lattemeetup.game.GameData;
import me.joesvart.lattemeetup.game.GameManager;
import me.joesvart.lattemeetup.game.GameState;
import me.joesvart.lattemeetup.player.PlayerData;
import me.joesvart.lattemeetup.util.chat.CustomColor;
import me.joesvart.lattemeetup.util.chat.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class InvCommand extends Command {

    public InvCommand() {
        super("uinv");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            sender.sendMessage(CustomColor.translate("&cOnly players can perform this command."));
            return false;
        }

        Player player = (Player) sender;
        PlayerData data = PlayerData.getByName(player.getName());

        /* If the player is alive cannot do this */
        if(data.isAlive()) {
            player.sendMessage(CustomColor.translate("&cYou cannot do this now."));
            return false;
        }

        GameData gameData = GameManager.getData();

        /* If the game state is not equal to WINNER cannot do this */
        if(!gameData.getGameState().equals(GameState.WINNER)) {
            player.sendMessage(CustomColor.translate("&cYou cannot do this now."));
            return false;
        }

        /* Usage message */
        if(args.length == 0) {
            player.sendMessage(CustomColor.translate("&cUsage: /uinv <player>"));
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        /* If player is null send this message */
        if(target == null) {
            player.sendMessage(StringUtil.PLAYER_NOT_FOUND);
            return false;
        }

        player.openInventory(LatteMeetup.getInstance().getSpectatorManager().getPlayerInventory(target));
        return false;
    }
}