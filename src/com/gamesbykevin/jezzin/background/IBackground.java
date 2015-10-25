package com.gamesbykevin.jezzin.background;

import android.graphics.Canvas;

import com.gamesbykevin.androidframework.resources.Disposable;

/**
 * Required methods for the background
 * @author GOD
 */
public interface IBackground extends Disposable
{
    public void reset() throws Exception;
    
    /**
     * Render the background
     * @param canvas Object used to render pixel data
     * @throws Exception 
     */
    public void render(final Canvas canvas) throws Exception;
}
