package com.gamesbykevin.jezzin.boundaries;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Images;

import com.gamesbykevin.jezzin.assets.Assets;
import com.gamesbykevin.jezzin.balls.Ball;
import com.gamesbykevin.jezzin.game.Game;
import com.gamesbykevin.jezzin.panel.GamePanel;

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
    
    //the velocity of the render
    private Cell velocity;
    
    //the index of the rectangle location
    private int index;
    
    //are we drawing the wall
    private boolean draw = false;
    
    //our game reference
    private final Game game;
    
    //the progress of the wall
    private Rect progress;
    
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
     * Start drawing the wall
     * @param startX starting x-coordinate
     * @param startY y-coordinate
     * @param finishX x-coordinate
     * @param finishY y-coordinate
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
            for (int i = 0; i < boundaries.size(); i++)
            {
                //store the index locations
                if (getBoundary(i).contains(startX, startY))
                    startI = i;
                if (getBoundary(i).contains(finishX, finishY))
                    finishI = i;
            }
            
            //if the start and finish are not in the same boundary, return false
            if (startI != finishI)
                return false;
            
            //if the index was not found, return false
            if (startI < 0 || finishI < 0)
                return false;
            
            //if either is solid, return false
            if (getBoundary(startI).isSolid() || getBoundary(finishI).isSolid())
                return false;
            
            //store the current index
            this.index = startI;
            
            //can only move either veritcal or horizontal
            this.velocity.setCol((dy == 0) ? dx : 0);
            this.velocity.setRow((dx == 0) ? dy : 0);
            
            //assign draw info
            progress.bottom = startY;
            progress.top = startY;
            progress.right = startX;
            progress.left = startX;
            
            //add a little (width/height) based on velocity
            if (velocity.getCol() != 0)
            {
                progress.top--;
            }
            else
            {
                progress.left--;
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
        return this.boundaries.get(index);
    }
    
    @Override
    public void reset()
    {
        //clear list
        boundaries.clear();
        
        //create default boundaru
        Boundary boundary = new Boundary(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        
        //the default will not be solid
        boundary.setSolid(false);
        
        //add default boundary to list
        boundaries.add(boundary);
        
        //the index of the current rectangle
        index = 0;
        
        if (velocity == null)
            velocity = new Cell();
        if (progress == null)
            progress = new Rect();
        
        //reset values
        velocity.setCol(0);
        velocity.setRow(0);
        progress.setEmpty();
    }
    
    @Override
    public void update()
    {
        //render the animation if exists
        if (hasDraw())
        {
            //update the progress
            progress.left -= velocity.getCol();
            progress.right += velocity.getCol();
            progress.top -= velocity.getRow();
            progress.bottom += velocity.getRow();
            
            //if the progress is not inside the boundary, we hit the wall and are done
            boolean progress1Complete = !getBoundary(index).contains(progress.left, progress.top);
            boolean progress2Complete = !getBoundary(index).contains(progress.right, progress.bottom);
            
            //make sure we stay in bounds
            adjustProgress();
            
            //if both have completed we need to separate
            if (progress1Complete && progress2Complete)
            {
                //split the boundary into 2
                splitBoundary();
                
                //make each boundary solid at first
                for (int i = 0; i < boundaries.size(); i++)
                {
                    getBoundary(i).setSolid(true);
                }
                
                //reassign the ball to its current boundary
                reassignBoundary();
                
                //remove flag from player
                game.getPlayer().setBegin(false);
                
                //we are done drawing
                setDraw(false);
            }
            else
            {
                if (hasBallCollision())
                {
                    //remove a life
                    game.getPlayer().setLives(game.getPlayer().getLives() - 1);
                    
                    //remove flag from player
                    game.getPlayer().setBegin(false);
                    
                    //we are done drawing
                    setDraw(false);
                    
                    //reset progress
                    progress.setEmpty();
                }
            }
        }
    }
    
    /**
     * Here we check every ball and assign it to the current boundary it is located in
     */
    private void reassignBoundary()
    {
        //assign the balls to their respected boundary
        for (Ball ball : game.getBalls().getBalls())
        {
            //check each boundary
            for (int i = 0; i < boundaries.size(); i++)
            {
                //get the current boundary
                Boundary boundary = getBoundary(i);

                /**
                 * If the ball is inside the boundary, we assign it to this boundary
                 * We also make the boundary NOT solid
                 */
                if (boundary.contains((int)ball.getX(), (int)ball.getY()))
                {
                    //assign the boundary index
                    ball.setIndex(i);

                    //set flag false, because this can't be solid
                    boundary.setSolid(false);
                }
            }
        }
    }
    
    /**
     * Do we have ball collision with the current progress?<br>
     * This should be checked when creating our wall.
     * @return true = the line intersects with a ball, false otherwise
     */
    private boolean hasBallCollision()
    {
        //check each ball
        for (Ball ball : game.getBalls().getBalls())
        {
            //if the ball is located in another boundary, we can have collision
            if (ball.getIndex() != index)
                continue;
            
            //get the radius of the ball, will use to detect collison
            final double radius = (ball.getWidth() / 2);
            
            if (velocity.getCol() != 0)
            {
                //if are y-coordinate is within the ball
                if (progress.top >= ball.getY() - radius && progress.top <= ball.getY() + radius)
                {
                    //check all x-coordinates for collision in our progress object
                    for (int x = progress.left; x <= progress.right; x++)
                    {
                        //if the distance is within the radius we have collision
                        if (ball.getDistance(x, progress.top) <= radius)
                            return true;
                    }
                }
            }
            else if (velocity.getRow() != 0)
            {
                //make sure x-coordinate is within the ball
                if (progress.left >= ball.getX() - radius && progress.right <= ball.getX() + radius)
                {
                    for (int y = progress.top; y <= progress.bottom; y++)
                    {
                        //if the distance is within the radius we have collision
                        if (ball.getDistance(progress.left, y) <= radius)
                            return true;
                    }
                }
            }
        }
        
        //no collision was found
        return false;
    }
    
    /**
     * Make sure progress stays within current boundary
     */
    private void adjustProgress()
    {
        if (velocity.getCol() != 0)
        {
            if (progress.right > getBoundary(index).getRect().right)
                progress.right = getBoundary(index).getRect().right;
            if (progress.left < getBoundary(index).getRect().left)
                progress.left = getBoundary(index).getRect().left;
        }
        else if (velocity.getRow() != 0)
        {
            if (progress.bottom > getBoundary(index).getRect().bottom)
                progress.bottom = getBoundary(index).getRect().bottom;
            if (progress.top < getBoundary(index).getRect().top)
                progress.top = getBoundary(index).getRect().top;
        }
    }
    
    /**
     * Split the current boundary into 2 separate boundaries
     */
    private void splitBoundary()
    {
        //get this temporary
        Rect tmp = getBoundary(index).getRect();

        //remove the boundary from the list
        boundaries.remove(index);

        //the velocity will determine how the boundary is split
        if (velocity.getCol() != 0)
        {
            final int y = (int)progress.top;
            final int w = tmp.right - tmp.left;
            boundaries.add(new Boundary(tmp.left, tmp.top, w, y - tmp.top));
            boundaries.add(new Boundary(tmp.left, y, w, tmp.bottom - y));
        }
        else if (velocity.getRow() != 0)
        {
            final int x = (int)progress.left;
            final int h = tmp.bottom - tmp.top;
            boundaries.add(new Boundary(tmp.left, tmp.top, x - tmp.left, h));
            boundaries.add(new Boundary(x, tmp.top, tmp.right - x, h));
        }
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        if (boundaries != null)
        {
            //fill in all boundaries
            for (Boundary boundary : boundaries)
            {
                if (boundary != null)
                    boundary.render(canvas, paint);
            }
        }
        
        //make sure we are drawing a ling
        paint.setStyle(Paint.Style.STROKE);
        
        //draw the line
        canvas.drawRect(progress, paint);
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
        
        this.velocity = null;
        this.paint = null;
        this.progress = null;
    }
}