package com.gamesbykevin.jezzin.boundaries;

import android.graphics.Canvas;
import com.gamesbykevin.androidframework.resources.Disposable;

/**
 * Required method for the boundaries
 * @author GOD
 */
public interface IBoundaries extends Disposable
{
    /**
     * Render boundaries
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
    
}
