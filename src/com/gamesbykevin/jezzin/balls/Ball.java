package com.gamesbykevin.jezzin.balls;

import com.gamesbykevin.androidframework.base.Entity;

/**
 * A single ball
 * @author GOD
 */
public final class Ball extends Entity
{
    //the animation type
    private final Balls.Type type;

    //the boundary the ball is inside
    private int index = 0;
    
    protected Ball(final Balls.Type type)
    {
        super();

        //assign type
        this.type = type;
    }
        
    /**
     * Get the animation type
     * @return The animation type of the ball
     */
    public Balls.Type getType()
    {
        return this.type;
    }

    /**
     * Set the boundary index
     * @param index The index location of the boundary the ball is trapped within
     */
    public void setIndex(final int index)
    {
        this.index = index;
    }

    /**
     * Get the assigned boundary index
     * @return The index location of the boundary the ball is trapped within
     */
    public int getIndex()
    {
        return this.index;
    }
}