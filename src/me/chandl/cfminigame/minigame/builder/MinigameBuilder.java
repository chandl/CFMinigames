package me.chandl.cfminigame.minigame.builder;


import me.chandl.cfminigame.minigame.MinigamePlayer;

public class MinigameBuilder {
    private MinigamePlayer builder;
    private MinigameBuilderStates status;

    public MinigameBuilder(MinigamePlayer builder) {
        this.builder = builder;
        this.status = MinigameBuilderStates.GETTING_SPAWNPOINT;
    }

    public MinigamePlayer getBuilder() {
        return builder;
    }

    public MinigameBuilderStates getStatus() {
        return status;
    }

    public boolean handleCommands(String[] args){



        return false;
    }
}
