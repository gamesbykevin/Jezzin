package com.gamesbykevin.jezzin.balls;

import android.graphics.Canvas;
import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.jezzin.assets.Assets;
import com.gamesbykevin.jezzin.boundaries.Boundary;
import com.gamesbykevin.jezzin.game.Game;
import com.gamesbykevin.jezzin.panel.GamePanel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will manage the balls in the game
 * @author GOD
 */
public class Balls extends Entity implements IBalls
{
    /**
     * The default size of a ball animation
     */
    private static final int DEFAULT_ANIMATION_DIMENSION = 64;
    
    /**
     * The sizes of the balls
     */
    public static final int BALL_DIMENSION_MEDIUM = 64;
    public static final int BALL_DIMENSION_LARGE = 80;
    public static final int BALL_DIMENSION_XLARGE = 96;
    public static final int BALL_DIMENSION_XSMALL = 32;
    public static final int BALL_DIMENSION_SMALL = 48;
    
    /**
     * The maximum velocity for the balls
     */
    private static final double MAX_VELOCITY = (double)DEFAULT_ANIMATION_DIMENSION / 5.0;
    
    /**
     * The minimum velocity for the balls
     */
    private static final double MIN_VELOCITY = (double)DEFAULT_ANIMATION_DIMENSION / 20.0;
    
    //list of balls in play
    private List<Ball> balls;
    
    //do we apply ball collision with the other balls
    private boolean collision = false;
    
    //the dimensions
    private int dimension;
    
    /**
     * The different types of balls
     */
    public enum Type
    {
        Ball1, Ball2, Ball3, Ball4, Ball5, Ball6, 
        Ball7, Ball8, Ball9, Ball10, Ball11, Ball12, 
        Ball13, Ball14, Ball15, Ball16, Ball17, Ball18, 
        Ball19, Ball20, Ball21, Ball22, Ball23, Ball24, 
    }
    
    //our game reference object
    private final Game game;
    
    public Balls(final Game game)
    {
        //store our reference object
        this.game = game;
        
        //create new list
        this.balls = new ArrayList<Ball>();
        
        //set default dimension
        setDimension(BALL_DIMENSION_MEDIUM);
        
        //set ball collision true
        setCollision(true);
        
        int index = 0;
        
        //animation dimension
        final int d = DEFAULT_ANIMATION_DIMENSION;
        
        for (int col = 0; col < 6; col++)
        {
            for (int row = 0; row < 4; row++)
            {
                final int x = col * d;
                final int y = row * d;
                
                //create animation with single frame
                Animation animation = new Animation(Images.getImage(Assets.ImageGameKey.Balls), x, y, d, d);
                
                //add to spritesheet
                super.getSpritesheet().add(Type.values()[index], animation);
                
                index++;
            }
        }
    }
    
    /**
     * Set the dimension size of a single ball
     * @param dimension The width/height of a ball
     */
    public final void setDimension(final int dimension)
    {
        this.dimension = dimension;
    }
    
    /**
     * Get the dimension size of a single ball
     * @return The width/height of a ball
     */
    public int getDimension()
    {
        return this.dimension;
    }
    
    /**
     * Assign collision to the balls
     * @param collision true means the balls can collide with one another, false no collision
     */
    public final void setCollision(final boolean collision)
    {
        this.collision = collision;
    }
    
    /**
     * Is ball collision setup?
     * @return true if the balls collide and bounce off one another, otherwise false
     */
    public boolean hasCollision()
    {
        return this.collision;
    }
    
    /**
     * Reset the balls in the container.<br>
     * The balls will be placed randomly with a random velocity
     * @param count The number of balls
     */
    public void reset(final int count)
    {
        //create new list
        List<Type> options = new ArrayList<Type>();
        
        //add all types to the list
        for (Type type : Type.values())
        {
            options.add(type);
        }
        
        //remove any existing balls
        getBalls().clear();
        
        //continue until we reach the count
        while (getBalls().size() < count)
        {
            //pick random type
            final int index = GamePanel.RANDOM.nextInt(options.size());
            
            //create a new ball of type
            Ball ball = new Ball(Type.values()[index]);
            
            //assign dimensions
            ball.setWidth(getDimension());
            ball.setHeight(getDimension());
            
            //pick random location
            ball.setX(GamePanel.RANDOM.nextInt(GamePanel.WIDTH - getDimension()) + getDimension());
            ball.setY(GamePanel.RANDOM.nextInt(GamePanel.HEIGHT - getDimension()) + getDimension());
            
            //if we want to apply collision
            if (hasCollision())
            {
                //continue until this ball does not collide with another
                while (getBall(ball) != null)
                {
                    //pick random location
                    ball.setX(GamePanel.RANDOM.nextInt(GamePanel.WIDTH));
                    ball.setY(GamePanel.RANDOM.nextInt(GamePanel.HEIGHT));
                }
            }
            
            //pick random velocity
            ball.setDX((GamePanel.RANDOM.nextDouble() * (MAX_VELOCITY - MIN_VELOCITY)) + MIN_VELOCITY);
            ball.setDY((GamePanel.RANDOM.nextDouble() * (MAX_VELOCITY - MIN_VELOCITY)) + MIN_VELOCITY);
            
            //if we have at least 2 options remove the selected
            if (options.size() > 1)
            {
                //remove option from our list
                options.remove(index);
            }
            
            //add ball to list
            getBalls().add(ball);
        }
    }
    
    @Override
    public void update()
    {
        if (getBalls() != null)
        {
            for (int index = 0; index < getBalls().size(); index++)
            {
                Ball ball = getBall(index);

                //skip if the ball doesn't exist
                if (ball == null)
                    continue;
                
                //is the collision check option enabled
                if (hasCollision())
                {
                    //check collision with another ball
                    Ball tmp = getBall(ball);

                    //if ball exists, there was collision
                    if (tmp != null)
                    {
                        //store velocity
                        final double dx1 = ball.getDX();
                        final double dy1 = ball.getDY();
                        final double dx2 = tmp.getDX();
                        final double dy2 = tmp.getDY();
                        
                        //switch velocity
                        ball.setDX(dx2);
                        ball.setDY(dy2);
                        tmp.setDX(dx1);
                        tmp.setDY(dy1);
                        
                        //move the balls
                        ball.update();
                        tmp.update();
                    }
                }
                
                //calculate half the dimension
                final double h = ball.getHeight() / 2;
                final double w = ball.getWidth() / 2;

                //get the assigned boundary
                Boundary boundary = game.getBoundaries().getBoundary(ball.getIndex());

                //manage x-velocity
                if (ball.getDX() < 0)
                {
                    if (ball.getX() < boundary.getRect().left + w)
                    {
                        //flip velocity
                        ball.setDX(-ball.getDX());

                        //adjust coordinates
                        ball.setX(boundary.getRect().left + w);
                    }
                }
                else if (ball.getDX() > 0)
                {
                    if (ball.getX() > boundary.getRect().right - w)
                    {
                        //flip velocity
                        ball.setDX(-ball.getDX());

                        //adjust coordinates
                        ball.setX(boundary.getRect().right - w);
                    }
                }

                //manage y-velocity
                if (ball.getDY() < 0)
                {
                    if (ball.getY() < boundary.getRect().top + h)
                    {
                        //flip velocity
                        ball.setDY(-ball.getDY());

                        //adjust coordinates
                        ball.setY(boundary.getRect().top + h);
                    }
                }
                else if (ball.getDY() > 0)
                {
                    if (ball.getY() > boundary.getRect().bottom - h)
                    {
                        //flip velocity
                        ball.setDY(-ball.getDY());

                        //adjust coordinates
                        ball.setY(boundary.getRect().bottom - h);
                    }
                }

                //update the current ball
                ball.update();
            }
        }
    }
    
    /**
     * Get the ball that is in collision.<br>
     * The ball won't have collision with another ball if they are assigned a different boundary index
     * @param ball The ball we want to check if it has collision
     * @return The ball in collision, if none found null is returned
     */
    private Ball getBall(final Ball ball)
    {
        for (Ball tmp : getBalls())
        {
            //don't check self
            if (ball.hasId(tmp))
                continue;
            
            //don't check balls in a different boundary
            if (ball.getIndex() != tmp.getIndex())
                continue;
            
            //if the ball is close enough, we have collision
            if (ball.getDistance(tmp) < ball.getWidth())
                return tmp;
        }
        
        //none in collision, return null
        return null;
    }
    
    /**
     * Get the list of balls
     * @return The complete list of balls
     */
    public List<Ball> getBalls()
    {
        return this.balls;
    }
    
    /**
     * Get the specified ball
     * @param index The index location
     * @return The ball at the specified location
     */
    public Ball getBall(final int index)
    {
        return getBalls().get(index);
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        if (getBalls() != null)
        {
            for (int index = 0; index < getBalls().size(); index++)
            {
                //get the current ball
                Ball ball = getBall(index);

                //don't continue if the ball is null
                if (ball == null)
                    continue;
                
                //assign info
                super.setX(ball.getX() - (ball.getWidth() / 2));
                super.setY(ball.getY() - (ball.getHeight() / 2));
                
                super.setWidth(ball);
                super.setHeight(ball);

                //assign animation key
                super.getSpritesheet().setKey(ball.getType());

                //render ball
                super.render(canvas);
            }
        }
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        if (balls != null)
        {
            for (Ball ball : balls)
            {
                if (ball != null)
                {
                    ball.dispose();
                    ball = null;
                }
            }
            
            balls.clear();
            balls = null;
        }
    }
}