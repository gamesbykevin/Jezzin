package com.gamesbykevin.jezzin.player;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.text.TimeFormat;
import com.gamesbykevin.jezzin.assets.Assets;
import com.gamesbykevin.jezzin.balls.Balls;
import com.gamesbykevin.jezzin.boundaries.Boundaries;
import com.gamesbykevin.jezzin.game.Game;
import com.gamesbykevin.jezzin.panel.GamePanel;

/**
 * The player that plays the game
 * @author GOD
 */
public final class Player implements IPlayer
{
    //did we start to draw the wall
    private boolean begin = false;
    
    /**
     * No velocity
     */
    private static final double VELOCITY_NONE = 0;
    
    /**
     * Velocity of the progress when rendering a wall for each difficulty
     */
    public static final double VELOCITY_NORMAL = (Balls.VELOCITY_MAX * 1.5);
    public static final double VELOCITY_HARD = (Balls.VELOCITY_MAX * 0.9);
    public static final double VELOCITY_EASY = (Balls.VELOCITY_MAX * 3);
    
    //the current velocity when rendering a wall
    private double velocity = 0;
    
    /**
     * The desired time format which will be displayed to the users
     */
    public static final String TIME_FORMAT = "mm:ss.SSS";
    
    //the starting point
    private double startX, startY;

    //our game reference
    private final Game game;
    
    //the players lives
    private int lives;
    
    //the current level of play
    private int level;
    
    //the timer
    private long time;
    
    //the previosu time to track the timer
    private long previous;
    
    //our timer and best timer and difficulty description
    private String timeDesc = "", bestDesc = "", difficultyDesc = "", modeDesc = "";
    
    //different difficulty descriptions
    public static final String DIFFICULTY_DESC_EASY = "Easy";
    public static final String DIFFICULTY_DESC_NORMAL = "Normal";
    public static final String DIFFICULTY_DESC_HARD = "Hard";
    
    //different mode descriptions
    public static final String MODE_DESC_CASUAL = "Casual";
    public static final String MODE_DESC_SURVIVAL = "Survival";
    public static final String MODE_DESC_TIMED = "Timed";
    public static final String MODE_DESC_CHALLENGE = "Challenge";
    
    public static final int MODE_INDEX_CASUAL = 0;
    public static final int MODE_INDEX_SURVIVIAL = 1;
    public static final int MODE_INDEX_TIMED = 2;
    public static final int MODE_INDEX_CHALLENGE = 3;
    
    //do we stop the timer
    private boolean stop = true;
    
    //locations for the stats
    private static final int TIMER_X = Boundaries.DEFAULT_BOUNDS.right - 245;
    private static final int TIMER_Y = GamePanel.HEIGHT - 20;
    private static final int PERSONAL_BEST_X = TIMER_X;
    private static final int PERSONAL_BEST_Y = TIMER_Y - 30;
    private static final int LIVES_X = Boundaries.DEFAULT_BOUNDS.left;
    private static final int LIVES_Y = 30;
    private static final int LEVEL_X = LIVES_X;
    private static final int LEVEL_Y = LIVES_Y + 30;
    private static final int GOAL_X = LIVES_X;
    private static final int GOAL_Y = TIMER_Y - 10;
    private static final int PROGRESS_X = LIVES_X;
    private static final int PROGRESS_Y = PERSONAL_BEST_Y;
    private static final int MODE_X = Boundaries.DEFAULT_BOUNDS.left + 137;
    private static final int MODE_Y = LEVEL_Y;
    
    /**
     * The required progress to complete the level
     */
    public static final int PROGRESS_GOAL = 75;
    
    public Player(final Game game)
    {
        super();
        
        //store our game refernce
        this.game = game;
        
        //reset
        reset();
        
        //set default level
        setLevel(1);
    }
    
    /**
     * Assign the velocity
     * @param velocity The speed at which we can render a wall
     */
    public void setVelocity(final double velocity)
    {
        this.velocity = velocity;
    }
    
    /**
     * Get the velocity
     * @return The speed at which we can render a wall
     */
    private double getVelocity()
    {
        return this.velocity;
    }
    
    /**
     * Stop the timer
     */
    public void stopTimer()
    {
        this.stop = true;
    }
    
    /**
     * Assign the level
     * @param level The level of play
     */
    public void setLevel(final int level)
    {
        this.level = level;
    }
    
    /**
     * Get the level
     * @return The current level of play
     */
    public int getLevel()
    {
        return this.level;
    }
    
    /**
     * Assign the # of lives
     * @param lives The desired amount of lives
     */
    public void setLives(final int lives)
    {
        this.lives = lives;
        
        if (getLives() < 0)
            setLives(0);
    }
    
    /**
     * Get the # of lives
     * @return The number of lives the player has
     */
    public int getLives()
    {
        return this.lives;
    }
    
    /**
     * Get the time
     * @return The total time
     */
    public long getTime()
    {
        return this.time;
    }
    
    /**
     * Set the mode description
     * @param modeDesc The mode that the player will see
     */
    public void setModeDesc(final String modeDesc)
    {
        this.modeDesc = modeDesc;
    }
    
    /**
     * Set the best time description
     * @param bestDesc The personal best time to beat the current level
     */
    public void setBestDesc(final String bestDesc)
    {
        this.bestDesc = bestDesc;
    }
    
    /**
     * Set the time description
     * @param timeDesc The desired time description to display to the user
     */
    public void setTimeDesc(final String timeDesc)
    {
        this.timeDesc = timeDesc;
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
        //don't continue if the coordinates are outside the playable bounds
        if (x <= Boundaries.DEFAULT_BOUNDS.left || x >= Boundaries.DEFAULT_BOUNDS.right)
            return;
        if (y <= Boundaries.DEFAULT_BOUNDS.top || y >= Boundaries.DEFAULT_BOUNDS.bottom)
            return;
        
        switch (event.getAction())
        {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                //if offscreen
                setBegin(false);
                break;
                
            case MotionEvent.ACTION_DOWN:
                //flag start
                setBegin(true);

                //set location
                startX = x;
                startY = y;
                break;
                
            case MotionEvent.ACTION_UP:
                //don't continue if we did not begin
                if (!hasBegin())
                    return;

                //identify the difference between user start and finish
                final double xDiff = (startX > x) ? startX - x : x - startX;
                final double yDiff = (startY > y) ? startY - y : y - startY;

                //result after starting draw
                boolean result = false;

                //determine which direction the wall is generated
                if (xDiff > yDiff)
                {
                    result = game.getBoundaries().startDraw((int)startX, (int)startY, (int)x, (int)y, getVelocity(), VELOCITY_NONE);
                }
                else if (yDiff > xDiff)
                {
                    result = game.getBoundaries().startDraw((int)startX, (int)startY, (int)x, (int)y, VELOCITY_NONE, getVelocity());
                }

                //if not successful we can try again
                if (!result)
                    setBegin(false);
                break;
        }
    }
    
    @Override
    public void update()
    {
        //if we stopped the timer, record the previous time
        if (this.stop)
        {
            this.stop = false;
            this.previous = System.currentTimeMillis();
        }
        
        //get the current time
        final long current = System.currentTimeMillis();
        
        //add the difference to the total time
        this.time += (current - previous);
        
        //update the time description
        this.setTimeDesc(TimeFormat.getDescription(TIME_FORMAT, getTime()));
        
        //update the previous
        this.previous = current;
    }
    
    @Override
    public void reset()
    {
        //flag false
        setBegin(false);
        
        //reset time stats
        this.time = 0;
        this.previous = 0;
        this.stop = true;
        this.setTimeDesc(TimeFormat.getDescription(TIME_FORMAT, getTime()));
        this.setBestDesc("");
        
        switch (game.getMainScreen().getScreenOptions().getDifficultyIndex())
        {
            case 0:
                setVelocity(VELOCITY_NORMAL);
                this.difficultyDesc = DIFFICULTY_DESC_NORMAL;
                break;
                
            case 1:
                setVelocity(VELOCITY_HARD);
                this.difficultyDesc = DIFFICULTY_DESC_HARD;
                break;
                
            case 2:
            default:
                setVelocity(VELOCITY_EASY);
                this.difficultyDesc = DIFFICULTY_DESC_EASY;
                break;
        }
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw the timer description
        canvas.drawText("Time: " + timeDesc, TIMER_X, TIMER_Y, game.getPaint());
        
        //draw the personal best description
        canvas.drawText("Best: " + bestDesc, PERSONAL_BEST_X, PERSONAL_BEST_Y, game.getPaint());
        
        //draw the lives
        canvas.drawText("Lives: " + getLives(), LIVES_X, LIVES_Y, game.getPaint());
        
        //draw the level
        canvas.drawText("Level: " + getLevel(), LEVEL_X, LEVEL_Y, game.getPaint());
        
        //draw the mode
        canvas.drawText("Mode", MODE_X, LIVES_Y, game.getPaint());
        canvas.drawText(modeDesc, MODE_X, MODE_Y, game.getPaint());
        
        //get the progress
        final int progress = game.getBoundaries().getTotalProgress();
        
        //draw the progress description along with the difficulty
        canvas.drawText(difficultyDesc + ": " + progress + "%", PROGRESS_X, PROGRESS_Y, game.getPaint());
        
        //draw the goal progress outline and fill it
        game.getPaint().setStyle(Paint.Style.STROKE);
        canvas.drawRect(GOAL_X, GOAL_Y, GOAL_X + (Player.PROGRESS_GOAL * 2), GOAL_Y + 20, game.getPaint());
        game.getPaint().setStyle(Paint.Style.FILL);
        
        if (progress >= Player.PROGRESS_GOAL)
        {
            canvas.drawRect(GOAL_X, GOAL_Y, GOAL_X + (Player.PROGRESS_GOAL * 2), GOAL_Y + 20, game.getPaint());
        }
        else
        {
            canvas.drawRect(GOAL_X, GOAL_Y, GOAL_X + (progress * 2), GOAL_Y + 20, game.getPaint());
        }
    }
    
    @Override
    public void dispose()
    {
        //nothing to recycle here
    }
}