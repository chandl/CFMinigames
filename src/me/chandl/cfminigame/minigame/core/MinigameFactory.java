package me.chandl.cfminigame.minigame.core;

import me.chandl.cfminigame.CFMinigame;
import me.chandl.cfminigame.database.MapStore;
import me.chandl.cfminigame.ex.GameTypeNotFoundException;
import me.chandl.cfminigame.ex.MapNotFoundException;

/**
 * Factory class to instantiate new {@link Minigame}s.
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 19,2017
 */
public class MinigameFactory {

    /**
     * Instantiates a new {@link Minigame} based on the type, map name, and difficulty level.
     *
     * @param typeStr The name of the {@link MinigameType}
     * @param mapName The name of the {@link MinigameMap} that is of type {@code typeStr}
     * @param difficulty The difficulty that the returning {@link Minigame} should be set to.
     * @return A new {@link Minigame} based off of the type, map name, and difficulty specified.
     *
     * @throws GameTypeNotFoundException When {@code typeStr} is not a valid {@link MinigameType}.
     * @throws MapNotFoundException When {@code mapName} is not a valid {@link MinigameMap} that is saved in the config..
     */
    public static Minigame newMinigame(String typeStr, String mapName, int difficulty)
    throws GameTypeNotFoundException,MapNotFoundException{
        MinigameType type = null;
        MinigameMap map;

        for(MinigameType mgType : MinigameType.values()){
            if(typeStr.equalsIgnoreCase(mgType.toString())){
                type = mgType;
                break;
            }
        }

        if(type == null){
            throw new GameTypeNotFoundException("No Minigame Type " + typeStr + ". Could not instantiate Minigame.");
        }

        map = MapStore.findMap(type, mapName);
        if(map == null){
            throw new MapNotFoundException("No Minigame Map " + mapName +". Could not instantiate Minigame.");
        }

        //Setup new minigame.
        Minigame game = type.toMinigame();
        game.setType(type);
        game.setMaximumPlayers(CFMinigame.DEFAULT_MAX_PLAYERS);
        game.setMinimumPlayers(CFMinigame.DEFAULT_MIN_PLAYERS);
        game.setMap(map);
        game.setDifficultyLevel(difficulty);

        return game;
    }

}
