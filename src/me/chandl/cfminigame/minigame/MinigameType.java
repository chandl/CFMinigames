package me.chandl.cfminigame.minigame;


import me.chandl.cfminigame.minigames.race.ElytraRace;

import java.io.Serializable;

public enum MinigameType implements Serializable{
   RACE, ELYTRARACE;


    @Override
    public String toString() {
        switch(this){
            case ELYTRARACE:
                return "ELYTRARACE";
            default:
                return null;
        }
    }
    public Minigame toMinigame(){
        switch(this){
            case ELYTRARACE:
                return new ElytraRace();
            default:
                return null;
        }
    }
}
