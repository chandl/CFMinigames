package me.chandl.cfminigame.minigame;


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
}
