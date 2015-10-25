package com.gamesbykevin.jezzin.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.resources.Font;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.text.TimeFormat;

import com.gamesbykevin.jezzin.assets.Assets;
import com.gamesbykevin.jezzin.background.Background;
import com.gamesbykevin.jezzin.balls.Balls;
import com.gamesbykevin.jezzin.boundaries.Boundaries;
import com.gamesbykevin.jezzin.game.controller.Controller;
import com.gamesbykevin.jezzin.player.Player;
import com.gamesbykevin.jezzin.scorecard.Score;
import com.gamesbykevin.jezzin.scorecard.ScoreCard;
import com.gamesbykevin.jezzin.screen.MainScreen;
import com.gamesbykevin.jezzin.screen.MainScreen.State;

/**
 * The main game logic will happen here
 * @author ABRAHAM
 */
public final class Game implements IGame
{
    //our main screen object reference
    private final MainScreen screen;
    
    //paint object to draw text
    private Paint paint;
    
    //our storage object used to save data
    private ScoreCard scorecard;
    
    //our controller objet
    private Controller controller;
    
    //our balls
    private Balls balls;

    //the boundaries in the game
    private Boundaries boundaries;
    
    //the player in the game
    private Player player;
    
    //our game background
    private Background background;
    
    public Game(final MainScreen screen) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //create new paint object
        this.paint = new Paint();
        this.paint.setTypeface(Font.getFont(Assets.FontGameKey.Default));
        this.paint.setTextSize(16f);
        this.paint.setColor(Color.WHITE);
        this.paint.setLinearText(false);
        
        //create ball container
        this.balls = new Balls(this);
        
        //create new controller
        this.controller = new Controller(this);
        
        //create the player
        this.player = new Player(this);
        
        //create the boundaries container
        this.boundaries = new Boundaries(this);
        
        //create our background object
        this.background = new Background(this);
        
        //create score card to track best score
        this.scorecard = new ScoreCard(this, screen.getPanel().getActivity());
    }
    
    /**
     * Get the player
     * @return The current player object
     */
    public Player getPlayer()
    {
        return this.player;
    }
    
    /**
     * Get the boundaries
     * @return The object containing the boundaries in a level
     */
    public Boundaries getBoundaries()
    {
        return this.boundaries;
    }
    
    /**
     * Get balls
     * @return Our balls container, that has the balls in play
     */
    public Balls getBalls()
    {
        return this.balls;
    }
    
    /**
     * Get the controller
     * @return Our controller object reference
     */
    public Controller getController()
    {
        return this.controller;
    }
    
    /**
     * Get the background object
     * @return Object that manages the background
     */
    public Background getBackground()
    {
        return this.background;
    }
    
    /**
     * Get the main screen object reference
     * @return The main screen object reference
     */
    public MainScreen getMainScreen()
    {
        return this.screen;
    }
    
    @Override
    public void reset(final int level) throws Exception
    {
        //assign collision setting
        getBalls().setCollision(getMainScreen().getScreenOptions().hasCollision());        
        
        //reset the balls
        getBalls().reset(level);
        
        //reset player
        getPlayer().reset();
        
        //the number of balls will be the amount of lives + 1
        getPlayer().setLives(level + 1);
        
        //assign the level
        getPlayer().setLevel(level);
        
        //setup game depending on mode
        switch (getMainScreen().getScreenOptions().getModeIndex())
        {
            case Player.MODE_INDEX_CASUAL:
            case Player.MODE_INDEX_SURVIVIAL:
            default:
                break;
                
            case Player.MODE_INDEX_CHALLENGE:
                break;
                
            case Player.MODE_INDEX_TIMED:
                break;
        }
        
        //reset boundaries
        getBoundaries().reset();
        
        //reset background
        getBackground().reset();
        
        //make sure our score card object exists
        if (getScoreCard() != null)
        {
            //get the score reference for the current level and difficulty
            Score score = getScoreCard().getScore(
                getMainScreen().getScreenOptions().getModeIndex(), 
                getMainScreen().getScreenOptions().getDifficultyIndex(), 
                level
            );
            
            //make sure the score object exists
            if (score != null)
            {
                //assign the best score for this level
                getPlayer().setBestDesc(TimeFormat.getDescription(Player.TIME_FORMAT, score.getTime()));
            }
        }
    }
    
    /**
     * Get our score card
     * @return Our score card to track the user personal best score
     */
    public ScoreCard getScoreCard()
    {
        return this.scorecard;
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
        //only update game if no controller buttons were clicked
        if (getController() != null && !getController().updateMotionEvent(event, x, y))
        {
            //makre sure draw isn't in progress
            if (getBoundaries() != null && !getBoundaries().hasDraw())
            {
                if (getPlayer() != null)
                    getPlayer().updateMotionEvent(event, x, y);
            }
        }
    }
    
    /**
     * Update game
     * @throws Exception 
     */
    public void update() throws Exception
    {
        if (getBoundaries() != null)
            getBoundaries().update();
        
        if (getPlayer() != null)
            getPlayer().update();
        
        if (getBalls() != null)
            getBalls().update();
    }
    
    /**
     * Get the paint object
     * @return The paint object used to draw text in the game
     */
    public Paint getPaint()
    {
        return this.paint;
    }
    
    @Override
    public void dispose()
    {
        if (boundaries != null)
        {
            boundaries.dispose();
            boundaries = null;
        }
        
        if (player != null)
        {
            player.dispose();
            player = null;
        }
        
        if (controller != null)
        {
            controller.dispose();
            controller = null;
        }
        
        if (balls != null)
        {
            balls.dispose();
            balls = null;
        }
        
        if (background != null)
        {
            background.dispose();
            background = null;
        }
        
        paint = null;
        
        if (scorecard != null)
        {
            scorecard.dispose();
            scorecard = null;
        }
    }
    
    /**
     * Render game elements
     * @param canvas Where to write the pixel data
     * @throws Exception 
     */
    public void render(final Canvas canvas) throws Exception
    {
        //fill background with black
        canvas.drawColor(Color.BLACK);
        
        if (getBackground() != null)
            getBackground().render(canvas);
        
        if (getBoundaries() != null && getBalls() != null)
        {
            //continue to show the boundaries and balls until goal is met
            if (getBoundaries().getTotalProgress() < Player.PROGRESS_GOAL)
            {
                getBoundaries().render(canvas);
                getBalls().render(canvas);
            }
        }
        
        if (getPlayer() != null)
            getPlayer().render(canvas);
        
        //render the controller for specific states
        if (screen.getState() != MainScreen.State.GameOver && 
            screen.getState() != MainScreen.State.Ready && 
            screen.getState() != MainScreen.State.Options)
        {
            if (getController() != null)
                getController().render(canvas);
        }
    }
}