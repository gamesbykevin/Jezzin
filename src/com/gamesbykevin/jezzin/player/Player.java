package com.gamesbykevin.jezzin.player;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.text.TimeFormat;
import com.gamesbykevin.jezzin.assets.Assets;
import com.gamesbykevin.jezzin.balls.Balls;
import com.gamesbykevin.jezzin.boundaries.Boundaries;
import com.gamesbykevin.jezzin.game.Game;
import com.gamesbykevin.jezzin.panel.GamePanel;
import com.gamesbykevin.jezzin.screen.OptionsScreen;
import com.gamesbykevin.jezzin.screen.ScreenManager;

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
    private float startX, startY;

    //our game reference
    private final Game game;
    
    //the players lives
    private int lives;
    
    //the current level of play
    private int level;
    
    //the timer
    private long elapsed;
    
    //if we are counting down the time
    private long timeLeft;
    
    //the previous time to track the timer
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
    public static final String MODE_DESC_CHALLENGE = "Challenge";
    
    public static final int MODE_INDEX_CASUAL = 0;
    public static final int MODE_INDEX_SURVIVIAL = 1;
    public static final int MODE_INDEX_CHALLENGE = 2;
    
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
    private static final int MODE_X = Boundaries.DEFAULT_BOUNDS.left + 135;
    private static final int MODE_Y = LEVEL_Y;
    
    /**
     * The required progress to complete the level
     */
    public static final int PROGRESS_GOAL = 75;
    
    //do we count down the timer
    private boolean countdown = false;
    
    public Player(final Game game)
    {
        super();
        
        //store our game reference
        this.game = game;
        
        //reset
        reset();
        
        //set default level
        setLevel(1);
    }
    
    /**
     * Flag the count down
     * @param countdown true = we will count down the timer, false = we will count up
     */
    /**
     * Flag the count down
     * @param countdown true = we will count down the timer, false otherwise
     * @param timeLeft The time to count down from
     */
    public void setCountdown(final boolean countdown, final long timeLeft)
    {
    	//assign flag
        this.countdown = countdown;
        
        //assign count down time
        this.timeLeft = timeLeft;
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
            this.lives = 0;
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
     * Get the elapsed time
     * @return The total time
     */
    public long getTime()
    {
        return this.elapsed;
    }
    
    /**
     * Set the elapsed time
     * @param time The time milliseconds of the timer
     */
    public void setTime(final long elapsed)
    {
        this.elapsed = elapsed;
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
    public void update(final MotionEvent event, final float x, final float y) throws Exception
    {
        //don't continue if the coordinates are outside the playable bounds
        if (x <= Boundaries.DEFAULT_BOUNDS.left || x >= Boundaries.DEFAULT_BOUNDS.right)
            return;
        if (y <= Boundaries.DEFAULT_BOUNDS.top || y >= Boundaries.DEFAULT_BOUNDS.bottom)
            return;
        
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            	//if we already began, don't continue
            	if (hasBegin())
            		return;
            	
                //flag start
                setBegin(true);

                //set location
                this.startX = x;
                this.startY = y;
                break;
                
            case MotionEvent.ACTION_MOVE:
            	//don't continue if we did not begin
            	if (!hasBegin())
            		return;
            	
            	//start the swipe
            	startSwipe(x, y);
            	break;
                
            /*
            case MotionEvent.ACTION_UP:
                //don't continue if we did not begin
                if (!hasBegin())
                    return;

                break;
           	*/
        }
    }
    
    /**
     * Start the swipe
     * @param x The new x-coordinate
     * @param y The new y-coordinate
     */
    private void startSwipe(final float x, final float y)
    {
        //identify the difference between user start and finish
        final float xDiff = (startX > x) ? startX - x : x - startX;
        final float yDiff = (startY > y) ? startY - y : y - startY;

        //result after starting draw
        boolean result = false;

        //determine which direction the wall is generated
        if (xDiff > yDiff)
        {
            result = game.getBoundaries().startDraw((int)startX, (int)startY, getVelocity(), VELOCITY_NONE);
        }
        else if (yDiff > xDiff)
        {
            result = game.getBoundaries().startDraw((int)startX, (int)startY, VELOCITY_NONE, getVelocity());
        }

        //if not successful we can try again
        if (!result)
            setBegin(false);
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
        setTime(getTime() + (current - previous));
        
        if (countdown)
        {
            //update the time description with the difference since we are counting down
            this.setTimeDesc(TimeFormat.getDescription(TIME_FORMAT, timeLeft - getTime()));
            
            if (timeLeft - getTime() < 0)
            {
            	//set the time description
            	this.setTimeDesc("00:00.000");
                
                //change the state to game over
                game.getScreen().setState(ScreenManager.State.GameOver);
                
                //assign message to display to user
                game.getScreen().getScreenGameover().setMessage("Time up");
                
                //play sound effect
                Audio.play(Assets.AudioGameKey.TimeUp);
                
                //no need to continue
                return;
            }
        }
        else
        {
            //update the time description
            this.setTimeDesc(TimeFormat.getDescription(TIME_FORMAT, getTime()));
        }
        
        //update the previous
        this.previous = current;
    }
    
    @Override
    public void reset()
    {
        //flag false
        setBegin(false);
        
        //reset time stats
        this.elapsed = 0;
        this.previous = 0;
        this.stop = true;
        this.setTimeDesc(TimeFormat.getDescription(TIME_FORMAT, getTime()));
        this.setBestDesc("");
        
        //set the speed and description of the chosen difficulty
        switch (game.getScreen().getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_DIFFICULTY))
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
        
        //draw the progress bar
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