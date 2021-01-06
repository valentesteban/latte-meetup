package me.joesvart.lattemeetup.border;

import org.bukkit.World;
import me.joesvart.lattemeetup.game.GameManager;

public class Border {
    
    public Border(World world, int border) {
        GameManager.getData().setBorder(border);
        BorderHelper.addBedrockBorder(world.getName(), border, 5);

        if(border == 25) {
            GameManager.getData().setBorderTime(-1);
        }
    }
}
