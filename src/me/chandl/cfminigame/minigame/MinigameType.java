package me.chandl.cfminigame.minigame;


import me.chandl.cfminigame.minigames.race.ElytraRace;

public enum MinigameType {
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
