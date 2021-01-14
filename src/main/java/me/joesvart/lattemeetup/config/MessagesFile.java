package me.joesvart.lattemeetup.config;

import me.joesvart.lattemeetup.LatteMeetup;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class MessagesFile extends YamlConfiguration {

    private static MessagesFile config;

    private Plugin plugin;
    private File configFile;

    public static MessagesFile getConfig() {
        if (MessagesFile.config == null) {
            MessagesFile.config = new MessagesFile();
        }
        return MessagesFile.config;
    }

    private Plugin main() {
        return LatteMeetup.getPlugin();
    }

    public MessagesFile() {
        this.plugin = this.main();
        this.configFile = new File(this.plugin.getDataFolder(), "messages.yml");
        this.saveDefault();
        this.reload();
    }
    
    public void reload() {
        try {
            super.load(this.configFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void save() {
        try {
            super.save(this.configFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void saveDefault() {
        this.plugin.saveResource("messages.yml", false);
    }
}
