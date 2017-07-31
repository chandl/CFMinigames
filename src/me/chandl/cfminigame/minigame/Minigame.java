package me.chandl.cfminigame.minigame;

import java.sql.Date;

public abstract class Minigame {
    private int id;
    private Date startTime;
    private MinigameType type;
    private int maximumPlayers, minimumPlayers;
    private long queueTimeLimit;
    private MinigameMap map;
    private int difficultyLevel;


    public abstract void start ();
    public abstract void stop ();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public MinigameType getType() {
        return type;
    }

    public void setType(MinigameType type) {
        this.type = type;
    }

    public int getMaximumPlayers() {
        return maximumPlayers;
    }

    public void setMaximumPlayers(int maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }

    public int getMinimumPlayers() {
        return minimumPlayers;
    }

    public void setMinimumPlayers(int minimumPlayers) {
        this.minimumPlayers = minimumPlayers;
    }

    public long getQueueTimeLimit() {
        return queueTimeLimit;
    }

    public void setQueueTimeLimit(long queueTimeLimit) {
        this.queueTimeLimit = queueTimeLimit;
    }

    public MinigameMap getMap() {
        return map;
    }

    public void setMap(MinigameMap map) {
        this.map = map;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
}
