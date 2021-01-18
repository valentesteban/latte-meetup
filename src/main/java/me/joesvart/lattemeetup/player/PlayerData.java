package me.joesvart.lattemeetup.player;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import me.joesvart.lattemeetup.LatteMeetup;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class PlayerData {

    @Getter private static Map<UUID, PlayerData> playerDatas = new HashMap<>();

    @Setter private int wins, kills, deaths, played, gameKills;

    private String name, disturb;
    private UUID uuid;
    private String realName;
    private String lastVoted = null;

    private int id;
    private long noClean, distrubCooldown;

    private PlayerState playerState = PlayerState.LOBBY;

    private boolean loaded;

    public PlayerData(String name) {
        this.name = name;
        this.uuid = Bukkit.getOfflinePlayer(name).getUniqueId();

        load();

        playerDatas.put(uuid, this);
    }

    public void load() {
        Document document = LatteMeetup.getInstance().getMongoDatabase().getCollection("profiles").find(Filters.eq("uuid", uuid.toString())).first();

        if (document != null) {
            if (document.containsKey("kills")) {
                kills = document.getInteger("kills");
            }
            if (document.containsKey("deaths")) {
                deaths = document.getInteger("deaths");
            }
            if (document.containsKey("wins")) {
                wins = document.getInteger("wins");
            }
            if (document.containsKey("played")) {
                played = document.getInteger("played");
            }
        }

        loaded = true;
    }

    public void save() {
        Document document = new Document();
        document.put("name", name.toLowerCase());
        document.put("realName", name);
        document.put("uuid", uuid.toString());

        document.put("kills", kills);
        document.put("deaths", deaths);
        document.put("wins", wins);
        document.put("played", played);

        LatteMeetup.getInstance().getMongoDatabase().getCollection("profiles").replaceOne(Filters.eq("uuid", uuid.toString()), document, new UpdateOptions().upsert(true));

        loaded = false;
    }

    public static PlayerData getByName(String name) {
        UUID uuid = Bukkit.getPlayer(name) == null ? Bukkit.getOfflinePlayer(name).getUniqueId() : Bukkit.getPlayer(name).getUniqueId();

        return playerDatas.get(uuid) == null ? new PlayerData(name) : playerDatas.get(uuid);
    }

    public static PlayerData getByUuid(UUID uuid) {
        return getByName(Bukkit.getOfflinePlayer(uuid).getName());
    }

    public boolean isAlive() {
        return playerState.equals(PlayerState.PLAYING);
    }

    public boolean isSpectator() {
        return playerState.equals(PlayerState.SPECTATING);
    }

    public static int getAlivePlayers() {
        return (int) playerDatas.values().stream().filter(PlayerData::isAlive).count();
    }

    public static List<PlayerData> getListOfAlivePlayers() {
        return playerDatas.values().stream().filter(PlayerData::isAlive).collect(Collectors.toList());
    }

    public void applyNoClean() {
        noClean = System.currentTimeMillis() + (20 * 1000);
    }

    public boolean isNoCleanActive() {
        return System.currentTimeMillis() < noClean;
    }

    public void removeNoCleanCooldown() {
        noClean = 0L;
    }

    public long getNoCleanMillisecondsLeft() {
        return Math.max(noClean - System.currentTimeMillis(), 0L);
    }

    public void applyDisturb(String target) {
        disturb = target;
        distrubCooldown = System.currentTimeMillis() + (30 * 1000);
    }

    public boolean isDisturbActive() {
        return System.currentTimeMillis() < distrubCooldown;
    }

    public long getDisturbMillisecondsLeft() {
        return Math.max(distrubCooldown - System.currentTimeMillis(), 0L);
    }
}
