package me.joesvart.lattemeetup;

import lombok.Setter;
import me.joesvart.lattelibs.scoreboard.Board;
import me.joesvart.lattelibs.scoreboard.BoardStyle;
import me.joesvart.lattemeetup.commands.CommandsManager;
import me.joesvart.lattemeetup.database.MeetupDatabase;
import me.joesvart.lattemeetup.game.GameListener;
import me.joesvart.lattemeetup.game.GameManager;
import me.joesvart.lattemeetup.border.BorderManager;
import me.joesvart.lattemeetup.managers.KitManager;
import me.joesvart.lattemeetup.listeners.LobbyListener;
import me.joesvart.lattemeetup.listeners.PlayerListener;
import me.joesvart.lattemeetup.providers.MeetupTab;
import me.joesvart.lattemeetup.scenario.ScenarioManager;
import me.joesvart.lattemeetup.managers.VoteManager;
import me.joesvart.lattemeetup.listeners.DeathMessagesListener;
import me.joesvart.lattemeetup.listeners.SpectatorListener;
import me.joesvart.lattemeetup.managers.InventoryManager;
import me.joesvart.lattemeetup.managers.ItemManager;
import me.joesvart.lattemeetup.managers.SpectatorManager;
import me.joesvart.lattemeetup.providers.MeetupBoard;
import me.joesvart.lattemeetup.tasks.VoteTask;
import me.joesvart.lattemeetup.util.menu.MenuListener;
import me.joesvart.lattemeetup.util.other.MeetupUtils;
import lombok.Getter;
import me.joesvart.lattetab.LatteTab;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

@Getter @Setter
public class LatteMeetup extends JavaPlugin {

    @Getter
    private static LatteMeetup instance;

    @Getter
    private static LatteMeetup plugin;

    public static int MIN_PLAYERS = 8;

    private BorderManager borderManager;
    private GameManager gameManager;
    private InventoryManager inventoryManager;
    private KitManager kitManager;
    private ScenarioManager scenarioManager;
    private SpectatorManager spectatorManager;
    private ItemManager itemManager;
    private VoteManager voteManager;
    private MeetupDatabase meetupDatabase;

    @Override
    public void onEnable() {
        instance = this;
        plugin = this;

        /* Initialize the Mongo Database */
        meetupDatabase = new MeetupDatabase(this);

        /* Setup configuration */
        getConfig().options().copyDefaults(true);
        saveConfig();

        /* Register the commons */
        registerCommands();
        registerListeners();
        registerManagers();
        registerBoard();
        registerTab();

        /* Initialize the Vote stage */
        new VoteTask();
    }

    @Override
    public void onDisable() {
        /* Delete the current world */
        MeetupUtils.deleteWorld();

        /* Close and save the database */
        MeetupDatabase.getInstance().getClient().close();

    }

    private void registerCommands() {
        /* Register the commands */
        new CommandsManager();
    }

    private void registerListeners() {
        /* Register the listeners */
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new LobbyListener(), this);
        Bukkit.getPluginManager().registerEvents(new GameListener(), this);
        Bukkit.getPluginManager().registerEvents(new DeathMessagesListener(), this);
        Bukkit.getPluginManager().registerEvents(new SpectatorListener(), this);
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
    }

    private void registerManagers() {
        /* Register the Managers */
        borderManager = new BorderManager();
        scenarioManager = new ScenarioManager();
        voteManager = new VoteManager();
        gameManager = new GameManager();
        kitManager = new KitManager();
        itemManager = new ItemManager();
        spectatorManager = new SpectatorManager();
        inventoryManager = new InventoryManager();
    }

    private void registerBoard() {
        /* Register the Scoreboard */
        Board board = new Board(this, new MeetupBoard());
        board.setTicks(2);
        board.setBoardStyle(BoardStyle.MODERN);
    }

    private void registerTab() {
        /* Register the Tablist */
        new LatteTab(this, new MeetupTab());
    }

    public void setWorldProperties() {
        /* Setup the perfect world for the game */
        Bukkit.getWorlds().forEach(world -> world.getEntities().forEach(Entity::remove));

        World uhc = Bukkit.getWorld("meetup_world");
        uhc.setGameRuleValue("doMobSpawning", "false");
        uhc.setGameRuleValue("doDaylightCycle", "false");
        uhc.setGameRuleValue("naturalRegeneration", "false");
        uhc.setGameRuleValue("doFireTick", "false");
        uhc.setGameRuleValue("difficulty", "0");
        uhc.setTime(0);
    }
}
