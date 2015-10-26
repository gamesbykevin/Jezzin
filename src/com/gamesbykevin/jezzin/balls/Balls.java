package com.gamesbykevin.jezzin.balls;

import android.graphics.Canvas;
import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.jezzin.assets.Assets;
import com.gamesbykevin.jezzin.boundaries.Boundaries;
import com.gamesbykevin.jezzin.game.Game;
import com.gamesbykevin.jezzin.panel.GamePanel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will manage the balls in the game
 * @author GOD
 */
public final class Balls extends Entity implements IBalls
{
    /**
     * The default size of a ball animation
     */
    private static final int DEFAULT_ANIMATION_DIMENSION = 64;
    
    /**
     * The size of the balls
     */
    public static final int BALL_DIMENSION = 32;
    
    /**
     * The maximum velocity for the balls
     */
    public static final double VELOCITY_MAX = (double)BALL_DIMENSION / 4.0;
    
    /**
     * The minimum velocity for the balls
     */
    public static final double VELOCITY_MIN = (double)BALL_DIMENSION / 20.0;
    
    //the current velocity restriction
    private double velocityMax = VELOCITY_MAX;
    
    //the current velocity restriction
    private double velocityMin = VELOCITY_MIN;
    
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
    protected final Game game;
    
    /**
     * The maximum number of balls allowed
     */
    public static final int BALL_MAX = 20;
    
    /**
     * The minimum number of balls allowed
     */
    public static final int BALL_MIN = 1;
    
    public Balls(final Game game)
    {
        //store our reference object
        this.game = game;
        
        //create new list
        this.balls = new ArrayList<Ball>();
        
        //set default dimension
        setDimension(BALL_DIMENSION);
        
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
        
        //reset
        reset(BALL_MIN);
    }
    
    /**
     * Assign the maximum velocity of the balls
     * @param velocityMax The desired speed
     */
    public void setVelocityMax(final double velocityMax)
    {
        this.velocityMax = velocityMax;
        
        if (getVelocityMax() < VELOCITY_MAX)
            setVelocityMax(VELOCITY_MAX);
    }
    
    /**
     * Assign the minimum velocity of the balls
     * @param velocityMin The desired speed
     */
    public void setVelocityMin(final double velocityMin)
    {
        this.velocityMin = velocityMin;
        
        if (getVelocityMin() < VELOCITY_MIN)
            setVelocityMin(VELOCITY_MIN);
    }
    
    /**
     * Get velocity minimum
     * @return The minimum amount of pixels the balls are allowed to move
     */
    private double getVelocityMin()
    {
        return this.velocityMin;
    }
    
    /**
     * Get velocity maximum
     * @return The maximum amount of pixels the balls are allowed to move
     */
    private double getVelocityMax()
    {
        return this.velocityMax;
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
     * @param count The number of balls to be created
     */
    public final void reset(int count)
    {
        if (count > BALL_MAX)
            count = BALL_MAX;
        if (count < BALL_MIN)
            count = BALL_MIN;
        
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
            ball.setX(GamePanel.RANDOM.nextInt(Boundaries.DEFAULT_BOUNDS.width() - getDimension()) + Boundaries.DEFAULT_BOUNDS.left);
            ball.setY(GamePanel.RANDOM.nextInt(Boundaries.DEFAULT_BOUNDS.height() - getDimension()) + Boundaries.DEFAULT_BOUNDS.top);
            
            //if we want to apply collision
            if (hasCollision())
            {
                //continue until this ball does not collide with another
                while (BallsHelper.getCollisionBall(ball, getBalls()) != null)
                {
                    //pick random location
                    ball.setX(GamePanel.RANDOM.nextInt(Boundaries.DEFAULT_BOUNDS.width() - getDimension()) + Boundaries.DEFAULT_BOUNDS.left);
                    ball.setY(GamePanel.RANDOM.nextInt(Boundaries.DEFAULT_BOUNDS.height() - getDimension()) + Boundaries.DEFAULT_BOUNDS.top);
                }
            }
            
            //pick random velocity
            ball.setDX((GamePanel.RANDOM.nextDouble() * (getVelocityMax() - getVelocityMin())) + getVelocityMin());
            ball.setDY((GamePanel.RANDOM.nextDouble() * (getVelocityMax() - getVelocityMin())) + getVelocityMin());
            
            //remove option from our list
            options.remove(index);
            
            //if our list is empty, fill list again
            if (options.isEmpty())
            {
                //add all types to the list
                for (Type type : Type.values())
                {
                    options.add(type);
                }
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
                    BallsHelper.checkBallCollision(ball, getBalls());

                //manage the ball velocity with its assigned boundary
                BallsHelper.checkBallVelocity(ball, game.getBoundaries().getBoundary(ball.getIndex()));

                //update the current ball
                ball.update();
            }
        }
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