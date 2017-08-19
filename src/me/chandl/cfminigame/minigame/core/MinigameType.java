package me.chandl.cfminigame.minigame.core;


import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigames.race.ElytraRace;

import java.io.Serializable;

public enum MinigameType implements Serializable{
   RACE, ELYTRARACE, SNOWBALLFIGHT;


    @Override
    public String toString() {
        switch(this){
            case ELYTRARACE:
                return "ELYTRARACE";
            case SNOWBALLFIGHT:
                return "SNOWBALLFIGHT";
            default:
                return null;
        }
    }
    public Minigame toMinigame(){
        switch(this){
            case ELYTRARACE:
                return new ElytraRace();
            case SNOWBALLFIGHT:
                return new SnowballFight();
            default:
                return null;
        }
    }
}
