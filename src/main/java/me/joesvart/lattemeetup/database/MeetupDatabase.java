package me.joesvart.lattemeetup.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.joesvart.lattemeetup.LatteMeetup;
import org.bson.Document;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;

@AllArgsConstructor
@Getter
public class MeetupDatabase {

    @Getter
    private static MeetupDatabase instance;

    @Getter
    private final MongoClient client;

    @Getter private final MongoCollection<Document> meetupProfiles;
    @Getter private final MongoCollection<Document> meetupDeaths;
    @Getter private final MongoCollection<Document> meetupWins;
    @Getter private final MongoCollection<Document> meetupKills;
    @Getter private final MongoCollection<Document> meetupPlayed;

    public MeetupDatabase(LatteMeetup plugin) {
        instance = this;

        FileConfiguration config = plugin.getConfig();
        if (config.getBoolean("DATABASE.AUTHENTICATION.ENABLED")) {
            client = new MongoClient(new ServerAddress(config.getString("DATABASE.HOST"), config.getInt("DATABASE.PORT")), Collections.singletonList(MongoCredential.createCredential(config.getString("DATABASE.AUTHENTICATION.USER"), config.getString("DATABASE.AUTHENTICATION.DATABASE"), config.getString("DATABASE.AUTHENTICATION.PASSWORD").toCharArray())));
        } else {
            client = new MongoClient(new ServerAddress(config.getString("DATABASE.HOST"), config.getInt("DATABASE.PORT")));
        }

        MongoDatabase db = client.getDatabase("LatteMeetup");

        meetupWins = db.getCollection("wins");
        meetupKills = db.getCollection("kills");
        meetupProfiles = db.getCollection("profiles");
        meetupDeaths = db.getCollection("deaths");
        meetupPlayed = db.getCollection("played");

    }
}