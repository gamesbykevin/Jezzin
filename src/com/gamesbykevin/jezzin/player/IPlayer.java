package com.gamesbykevin.jezzin.player;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Disposable;

/**
 * Required player methods
 * @author GOD
 */
public interface IPlayer extends Disposable
{
    /**
     * Render player
     * @param canvas
     * @throws Exception 
     */
    public void render(final Canvas canvas) throws Exception;
    
    /**
     * Method to update elements
     */
    public void update();
    
    /**
     * Logic to reset player
     */
    public void reset();
    
    /**
     * Update the game based on the motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     * @throws Exception
     */
    public void updateMotionEvent(final MotionEvent event, final float x, final float y) throws Exception;
}
