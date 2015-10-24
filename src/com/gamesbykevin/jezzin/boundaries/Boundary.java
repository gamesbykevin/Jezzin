package com.gamesbykevin.jezzin.boundaries;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * A single boundary
 * @author GOD
 */
public final class Boundary implements IBoundary
{
    //boundary coordinates
    private final Rect bounds;
    
    //is the boundary solid
    private boolean solid;
    
    protected Boundary(final int x, final int y, final int w, final int h)
    {
        //store coordinates
        this.bounds = new Rect(x, y, x + w, y + h);
    }
    
    /**
     * Get the rectangle coordinates
     * @return The rectangle coordinates
     */
    public Rect getRect()
    {
        return this.bounds;
    }
    
    /**
     * Is the location contained inside this boundary?
     * @param x x-coordinate
     * @param y y-coordinate
     * @return true = yes, false = no
     */
    public boolean contains(final int x, final int y)
    {
        return getRect().contains(x, y);
    }
    
    /**
     * Is the boundary solid?
     * @return true = yes, false = no
     */
    public boolean isSolid()
    {
        return this.solid;
    }
    
    /**
     * Assign the solid flag
     * @param solid true if this boundary contains 0 balls, false otherwise
     */
    public void setSolid(final boolean solid)
    {
        this.solid = solid;
    }
    
    @Override
    public void render(final Canvas canvas, final Paint paint) throws Exception
    {
        //only render the square if not solid
        if (!isSolid())
        {
            //set the style to fill
            paint.setStyle(Paint.Style.FILL);
            
            //draw the boundary
            canvas.drawRect(getRect(), paint);
        }
    }
}