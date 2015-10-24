package com.gamesbykevin.jezzin.player;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.jezzin.assets.Assets;
import com.gamesbykevin.jezzin.game.Game;
import com.gamesbykevin.jezzin.panel.GamePanel;

import java.util.ArrayList;
import java.util.List;

/**
 * The player that plays the game
 * @author GOD
 */
public final class Player implements IPlayer
{
    //did we start to draw the wall
    private boolean begin = false;
    
    /**
     * The different velocity speeds
     */
    private static final double VELOCITY_NONE = 0;
    private static final double VELOCITY_FAST = 30;
    
    //the starting point
    private Cell start;

    //our game reference
    private final Game game;
    
    //the players lives
    private int lives;
    
    public Player(final Game game)
    {
        super();
        
        //store our game refernce
        this.game = game;
        
        //create objects to store location
        this.start = new Cell();
        
        //reset
        reset();
    }
    
    /**
     * 
     * @param lives 
     */
    public void setLives(final int lives)
    {
        this.lives = lives;
        
        if (getLives() < 0)
            setLives(0);
    }
    
    /**
     * 
     * @return 
     */
    public int getLives()
    {
        return this.lives;
    }
    
    /**
     * Flag that we have begun to draw the wall
     * @param begin true = yes, false = no
     */
    public void setBegin(final boolean begin)
    {
        this.begin = begin;
    }
    
    /**
     * Did we begin to draw the wall?
     * @return true = yes, false = no
     */
    private boolean hasBegin()
    {
        return this.begin;
    }
    
    /**
     * Update the game based on the motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     * @throws Exception
     */
    public void updateMotionEvent(final MotionEvent event, final float x, final float y) throws Exception
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            //flag start
            setBegin(true);
            
            //set location
            start.setCol(x);
            start.setRow(y);
        }
        else if (event.getAction() == MotionEvent.ACTION_UP)
        {
            //don't continue if we did not begin
            if (!hasBegin())
                return;
            
            //identify the difference between user start and finish
            final double xDiff = (start.getCol() > x) ? start.getCol() - x : x - start.getCol();
            final double yDiff = (start.getRow() > y) ? start.getRow() - y : y - start.getRow();
            
            //result after starting draw
            boolean result = false;
            
            //determine which direction the wall is generated
            if (xDiff > yDiff)
            {
                result = game.getBoundaries().startDraw((int)start.getCol(), (int)start.getRow(), (int)x, (int)y, VELOCITY_FAST, VELOCITY_NONE);
            }
            else if (yDiff > xDiff)
            {
                result = game.getBoundaries().startDraw((int)start.getCol(), (int)start.getRow(), (int)x, (int)y, VELOCITY_NONE, VELOCITY_FAST);
            }
            
            //if not successful we can try again
            if (!result)
                setBegin(false);
        }
    }
    
    @Override
    public void update()
    {
        
    }
    
    @Override
    public void reset()
    {
        //flag false
        setBegin(false);
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        
    }
    
    @Override
    public void dispose()
    {
        this.start = null;
    }
}