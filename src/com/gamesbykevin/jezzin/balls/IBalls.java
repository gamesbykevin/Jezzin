package com.gamesbykevin.jezzin.balls;

import com.gamesbykevin.androidframework.resources.Disposable;

import android.graphics.Canvas;

/**
 * Required methods for balls
 * @author GOD
 */
public interface IBalls extends Disposable
{
    /**
     * Render balls
     * @param canvas
     * @throws Exception 
     */
    public void render(final Canvas canvas) throws Exception;
    
    /**
     * Method to update elements
     */
    public void update();
    
    /**
     * Reset the balls
     * @param count The number of balls
     */
    public void reset(final int count);
}
