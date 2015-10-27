package com.gamesbykevin.jezzin.boundaries;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.text.TimeFormat;

import com.gamesbykevin.jezzin.assets.Assets;
import com.gamesbykevin.jezzin.game.Game;
import com.gamesbykevin.jezzin.panel.GamePanel;
import com.gamesbykevin.jezzin.player.Player;
import com.gamesbykevin.jezzin.storage.scorecard.Score;
import com.gamesbykevin.jezzin.screen.ScreenManager;

import java.util.ArrayList;
import java.util.List;

/**
 * The list of boundaries in a level
 * @author GOD
 */
public final class Boundaries extends Entity implements IBoundaries
{
    /**
     * Animation key
     */
    public enum Key
    {
        YellowVertical,
        YellowHorizontal,
        GreenVertical,
        GreenHorizontal,
        RedVertical,
        RedHorizontal,
        BlueVertical,
        BlueHorizontal
    }
    
    //the list of bounds for the current level
    private List<Boundary> boundaries;

    //paint object to fill bounds
    private Paint paint;
    
    /**
     * The size of the boundary border
     */
    private static final float STROKE_WIDTH = 10.00f;
    
    /**
     * The pixel dimension of the progress, when rendering on screen
     */
    private static final int PROGRESS_DIMENSION = 16;
    
    //the index of the rectangle location
    private int index;
    
    //are we drawing the wall
    private boolean draw = false;
    
    //our game reference
    private final Game game;
    
    /**
     * Default bounds where the balls bounce
     */
    public static final Rect DEFAULT_BOUNDS = new Rect(10, 75, GamePanel.WIDTH - 10, GamePanel.HEIGHT - 75);
    
    /**
     * Create new container of boundaries
     * @param game Our game reference
     */
    public Boundaries(final Game game)
    {
        //store our game reference
        this.game = game;
        
        //create new list of rectangle bounds
        this.boundaries = new ArrayList<Boundary>();
        
        //create our paint object
        this.paint = new Paint();
        this.paint.setColor(Color.BLACK);
        this.paint.setStrokeWidth(STROKE_WIDTH);
        
        //yellow animation
        super.getSpritesheet().add(Key.YellowVertical, new Animation(Images.getImage(Assets.ImageGameKey.Player), 0, 0, 18, 70));
        super.getSpritesheet().add(Key.YellowHorizontal, new Animation(Images.getImage(Assets.ImageGameKey.Player), 0, 70, 70, 18));
        
        //red animation
        super.getSpritesheet().add(Key.RedVertical, new Animation(Images.getImage(Assets.ImageGameKey.Player), 18, 0, 18, 70));
        super.getSpritesheet().add(Key.RedHorizontal, new Animation(Images.getImage(Assets.ImageGameKey.Player), 0, 88, 70, 18));
        
        //green animation
        super.getSpritesheet().add(Key.GreenVertical, new Animation(Images.getImage(Assets.ImageGameKey.Player), 36, 0, 18, 70));
        super.getSpritesheet().add(Key.GreenHorizontal, new Animation(Images.getImage(Assets.ImageGameKey.Player), 0, 106, 70, 18));
        
        //blue animation
        super.getSpritesheet().add(Key.BlueVertical, new Animation(Images.getImage(Assets.ImageGameKey.Player), 54, 0, 18, 70));
        super.getSpritesheet().add(Key.BlueHorizontal, new Animation(Images.getImage(Assets.ImageGameKey.Player), 0, 124, 70, 18));

        //reset
        reset();
    }
   
    /**
     * Start drawing the wall.<br>
     * We make sure the start and finish coordinates are in the same boundary.<br>
     * If no, the draw will not happen.
     * @param startX starting x-coordinate
     * @param startY starting y-coordinate
     * @param finishX finish x-coordinate
     * @param finishY finish y-coordinate
     * @param dx x-velocity
     * @param dy y-velocity
     * @return true if we are successful in starting the draw, false otherwise
     */
    public boolean startDraw(final int startX, final int startY, final int finishX, final int finishY, final double dx, final double dy)
    {
        if (boundaries != null)
        {
            //track the start and finish indexes
            int startI = -1, finishI = -1;
            
            //check each boundary to locate the start/finish indeex
            for (int i = 0; i < getBoundaries().size(); i++)
            {
                //store the index locations
                if (getBoundary(i).contains(startX, startY))
                    startI = i;
                if (getBoundary(i).contains(finishX, finishY))
                    finishI = i;
            }
            
            //if the start index was not found, return false
            if (startI < 0)
                return false;
            
            //if the start is solid, return false
            if (getBoundary(startI).isSolid())
                return false;
            
            //store the current index
            this.index = startI;
            
            //reset progress
            resetProgress();
            
            //can only move either veritcal or horizontal
            setDX((dy == 0) ? dx : 0);
            setDY((dx == 0) ? dy : 0);
            
            //start
            super.setX(startX);
            super.setY(startY);
            
            //setup location
            if (getDX() != 0)
            {
                super.getSpritesheet().setKey(Key.BlueHorizontal);
                super.setWidth(1);
                super.setHeight(PROGRESS_DIMENSION);
                super.setY(getY() - (getHeight() / 2));
            }
            else
            {
                super.getSpritesheet().setKey(Key.BlueVertical);
                super.setWidth(PROGRESS_DIMENSION);
                super.setHeight(1);
                super.setX(getX() - (getWidth() / 2));
            }
            
            //flag draw start
            setDraw(true);
            
            //we were successful
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Get the total progress.<br>
     * This will be the total area of all solid boundaries.
     * @return The progress of the boundaries between 0 - 100
     */
    public int getTotalProgress()
    {
        double area = 0;
        
        //calculate the total area completed
        for (Boundary boundary : getBoundaries())
        {
            //if solid add to the total
            if (boundary.isSolid())
                area += boundary.getArea();
        }
        
        //return the result
        return (int)(100 * (area / ((DEFAULT_BOUNDS.right - DEFAULT_BOUNDS.left) * (DEFAULT_BOUNDS.bottom - DEFAULT_BOUNDS.top))));
    }
    
    /**
     * Get the boundaries 
     * @return The list of boundaries
     */
    public List<Boundary> getBoundaries()
    {
        return this.boundaries;
    }
    
    /**
     * Flag that we are drawing the wall
     * @param draw true = yes, false = no
     */
    public void setDraw(final boolean draw)
    {
        this.draw = draw;
    }
    
    /**
     * Are we drawing the wall?
     * @return true = yes, false = no
     */
    public boolean hasDraw()
    {
        return this.draw;
    }
    
    /**
     * Get the boundary at the specified index
     * @param index The index location of the boundary
     * @return The index location of the boundary
     */
    public Boundary getBoundary(final int index)
    {
        return getBoundaries().get(index);
    }
    
    /**
     * Get the boundary
     * @return The current assigned boundary
     */
    public Boundary getBoundary()
    {
        return (getBoundary(getIndex()));
    }
    
    /**
     * Get the game
     * @return The game object reference
     */
    protected Game getGame()
    {
        return this.game;
    }
    
    @Override
    public void reset()
    {
        //clear list
        getBoundaries().clear();
        
        //create default boundaru
        Boundary boundary = new Boundary(
            DEFAULT_BOUNDS.left, 
            DEFAULT_BOUNDS.top, 
            DEFAULT_BOUNDS.right - DEFAULT_BOUNDS.left, 
            DEFAULT_BOUNDS.bottom - DEFAULT_BOUNDS.top
        );
        
        //the default will not be solid
        boundary.setSolid(false);
        
        //add default boundary to list
        getBoundaries().add(boundary);
        
        //the index of the current rectangle
        index = 0;
        
        //stop drawing
        setDraw(false);
        
        //reset values
        resetProgress();
    }
    
    @Override
    public void update()
    {
        //render the animation if exists
        if (hasDraw())
        {
            //update the progress
            super.setX(getX() - getDX());
            super.setWidth(getWidth() + getDX() + getDX());
            super.setY(getY() - getDY());
            super.setHeight(getHeight() + getDY() + getDY());
            
            //if the progress is not inside the boundary, we hit the wall and are done
            boolean progress1Complete = !getBoundary().contains((int)getX(), (int)getY());
            boolean progress2Complete = !getBoundary().contains((int)(getX() + getWidth()), (int)(getY() + getHeight()));
            
            //make sure we stay in bounds
            BoundariesHelper.checkProgress(this);
            
            //if both have completed we need to separate
            if (progress1Complete && progress2Complete)
            {
                //split the boundary into 2 smaller ones
                BoundariesHelper.splitBoundary(this);
                
                //reassign the balls to their current boundary
                BoundariesHelper.assignBoundary(this);
                
                //reset progress
                resetProgress();
                
                //remove flag from player
                getGame().getPlayer().setBegin(false);
                
                //we are done drawing
                setDraw(false);
                
                //if we have met the progress the level is complete
                if (getTotalProgress() >= Player.PROGRESS_GOAL)
                {
                    //set the state
                    getGame().getMainScreen().setState(ScreenManager.State.GameOver);
                    
                    //update the score
                    final boolean result = getGame().getScoreCard().updateScore(
                        getGame().getMainScreen().getScreenOptions().getModeIndex(), 
                        getGame().getMainScreen().getScreenOptions().getDifficultyIndex(), 
                        getGame().getPlayer().getLevel(), 
                        getGame().getPlayer().getTime()
                    );
                    
                    if (result)
                    {
                        //assign message to display to user
                        getGame().getMainScreen().getScreenGameover().setMessage("New record");
                    }
                    else
                    {
                        //assign message to display to user
                        getGame().getMainScreen().getScreenGameover().setMessage("You win");
                    }
                    
                    //play sound effect
                    Audio.play(Assets.AudioGameKey.ProgressComplete);
                }
                else
                {
                    //play sound effect
                    Audio.play(Assets.AudioGameKey.ProgressAdd);
                }
            }
            else
            {
                //if there is collision with the progress tracker
                if (BoundariesHelper.hasProgressCollision(this))
                {
                    //remove a life
                    getGame().getPlayer().setLives(getGame().getPlayer().getLives() - 1);
                    
                    //remove flag from player
                    getGame().getPlayer().setBegin(false);
                    
                    //we are done drawing
                    setDraw(false);
                    
                    //reset progress
                    resetProgress();
                    
                    //if no more lives, the game is over
                    if (getGame().getPlayer().getLives() < 1)
                    {
                        getGame().getMainScreen().setState(ScreenManager.State.GameOver);
                        
                        //assign message to display to user
                        getGame().getMainScreen().getScreenGameover().setMessage("No More Lives");
                        
                        //play sound effect
                        Audio.play(Assets.AudioGameKey.NoLives);
                    }
                    else
                    {
                        //play sound effect
                        Audio.play(Assets.AudioGameKey.LoseLife);
                    }
                }
            }
        }
    }
    
    /**
     * Reset the progress
     */
    private void resetProgress()
    {
        super.setX(0);
        super.setY(0);
        super.setWidth(0);
        super.setHeight(0);
        super.setDX(0);
        super.setDY(0);
    }
    
    /**
     * Get the index
     * @return The index of the current boundary
     */
    protected int getIndex()
    {
        return this.index;
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        if (getBoundaries() != null)
        {
            //fill in all boundaries
            for (Boundary boundary : getBoundaries())
            {
                if (boundary != null)
                    boundary.render(canvas, paint);
            }
        }
        
        //draw the progress
        if (getWidth() != 0 && getHeight() != 0)
            super.render(canvas);
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        if (boundaries != null)
        {
            boundaries.clear();
            boundaries = null;
        }
        
        this.paint = null;
    }
}