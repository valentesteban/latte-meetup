package me.joesvart.lattemeetup.game;

import lombok.Getter;
import lombok.Setter;
import me.joesvart.lattelibs.chat.ChatUtils;

@Getter
@Setter
public class GameData {

    private int remaining;
    private int initial;
    private int border = 100;

    private int gameTime;
    private int startingTime = 15;
    private int voteTime = 45;
    private int endTime = 10;
    private int borderTime = 60;
    private long time = 0L, startTime = -1L;

    private boolean canStartCountdown;
    private boolean canAnnounce;
    private boolean generated;

    private String winner;

    private String scenario = "None", loading = "";

    private GameState gameState = GameState.VOTE;

    public int getNextBorder() {
        switch (border) {
            case 100: return 75;
            case 75: return 50;
            case 50: return 25;
            default: return 100;
        }
    }

    public String getStatus() {
        return gameState == GameState.PLAYING ? "&aPlaying" : gameState == GameState.STARTING ? "&eStarting" : gameState == GameState.VOTE ? "&eVoting" : gameState == GameState.WINNER ? "&bEnding" : "&cSetup";
    }

    public String getFormattedBorderStatus() {
        return borderTime > 0 ? ChatUtils.SECONDARY + "" + (borderTime <= 60 ? borderTime + "s" : (borderTime / 60 + 1) + "m") + "" : "";
    }

    public int decrementBorderTime() {
        return --borderTime;
    }

    public int getGameTime() {
        return (int) (System.currentTimeMillis() - startTime) / 1000;
    }

    public int decrementEndTime() {
        return --endTime;
    }
}
