package me.chandl.cfminigame.ex;

/**
 * GameTypeNotFoundException - An Exception to be thrown whenver a specified {@link me.chandl.cfminigame.minigame.core.MinigameType} could not be found.
 */
public class GameTypeNotFoundException extends Exception {

    public GameTypeNotFoundException(String message) {
        super(message);
    }
}
