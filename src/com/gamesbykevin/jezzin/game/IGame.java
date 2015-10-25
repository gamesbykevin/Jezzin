package com.gamesbykevin.jezzin.game;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.jezzin.assets.Assets;

/**
 * Game interface methods
 * @author GOD
 */
public interface IGame extends Disposable
{
    /**
     * Logic to restart the game with the same settings
     * @level The specified level
     * @throws Exception
     */
    public void reset(final int level) throws Exception;
}
