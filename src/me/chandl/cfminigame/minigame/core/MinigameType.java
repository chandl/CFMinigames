package me.chandl.cfminigame.minigame.core;


import me.chandl.cfminigame.minigames.race.ElytraRace;
import me.chandl.cfminigame.minigames.snowballfight.SnowballFight;

import java.io.Serializable;

/**
 * Types of Minigames.
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 20, 2017
 */
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
