package com.gamesbykevin.jezzin.boundaries;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Required methods for the boundary
 * @author GOD
 */
public interface IBoundary
{
    /**
     * Render boundary
     * @param canvas
     * @param paint
     * @throws Exception 
     */
    public void render(final Canvas canvas, final Paint paint) throws Exception;
}