package me.chandl.cfminigame.minigames.snowballfight.builder;

import me.chandl.cfminigame.minigame.builder.MinigameBuilder;
import me.chandl.cfminigame.minigame.core.MinigameMap;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;

/**
 * MinigameBuidler for SnowballFight minigames.
 *
 * @author Chandler
 * @version 1.0
 * @since Aug 20, 2017
 */
public class SnowballFightBuilder extends MinigameBuilder{

    public SnowballFightBuilder(MinigamePlayer builder){
        super(builder);
    }

    @Override
    public MinigameMap createMap() {
        return null;
    }
}
