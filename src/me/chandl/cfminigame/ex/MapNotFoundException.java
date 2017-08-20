package me.chandl.cfminigame.ex;

/**
 * MapNotFoundException - An Exception to be thrown when a specified {@link me.chandl.cfminigame.minigame.core.MinigameMap} could not be found.
 */
public class MapNotFoundException extends Exception {

    public MapNotFoundException(String message) {
        super(message);
    }
}
