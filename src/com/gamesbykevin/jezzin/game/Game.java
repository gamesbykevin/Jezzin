package com.gamesbykevin.jezzin.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.io.storage.Internal;

import com.gamesbykevin.jezzin.assets.Assets;
import com.gamesbykevin.jezzin.balls.Balls;
import com.gamesbykevin.jezzin.boundaries.Boundaries;
import com.gamesbykevin.jezzin.game.controller.Controller;
import com.gamesbykevin.jezzin.player.Player;
import com.gamesbykevin.jezzin.screen.MainScreen;

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
    private Internal storage;
    
    //our controller objet
    private Controller controller;
    
    //our balls
    private Balls balls;

    //the boundaries in the game
    private Boundaries boundaries;
    
    //the player in the game
    private Player player;
    
    /**
     * Text delimeter used to parse internal storage data for each level
     */
    private static final String STORAGE_DELIMITER_LEVEL = ",";
    
    /**
     * Text delimeter used to parse internal storage data for each attribute in a level
     */
    private static final String STORAGE_DELIMITER_ATTRIBUTE = ";";
    
    public Game(final MainScreen screen) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //create new paint object
        this.paint = new Paint();
        this.paint.setTextSize(24f);
        this.paint.setColor(Color.WHITE);
        
        //create ball container
        this.balls = new Balls(this);
        
        //create new controller
        this.controller = new Controller(this);
        
        //create the player
        this.player = new Player(this);
        
        //create the boundaries container
        this.boundaries = new Boundaries(this);
    }
    
    public Player getPlayer()
    {
        return this.player;
    }
    
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
     * Get the main screen object reference
     * @return The main screen object reference
     */
    public MainScreen getMainScreen()
    {
        return this.screen;
    }
    
    /**
     * Restart game with assigned settings
     * @throws Exception 
     */
    @Override
    public void reset() throws Exception
    {
        //create our storage object
        //this.storage = new Internal("TEST", screen.getPanel().getActivity());
        
        //assign collision setting
        getBalls().setCollision(getMainScreen().getScreenOptions().hasCollision());        
        
        //assign ball size
        switch (getMainScreen().getScreenOptions().getBallSize())
        {
            case 0:
                getBalls().setDimension(Balls.BALL_DIMENSION_MEDIUM);
                break;
            
            case 1:
                getBalls().setDimension(Balls.BALL_DIMENSION_LARGE);
                break;
            
            case 2:
                getBalls().setDimension(Balls.BALL_DIMENSION_XLARGE);
                break;
            
            case 3:
                getBalls().setDimension(Balls.BALL_DIMENSION_XSMALL);
                break;
            
            case 4:
                getBalls().setDimension(Balls.BALL_DIMENSION_SMALL);
                break;
            
            default:
                throw new Exception("Size not accounted here: " + getMainScreen().getScreenOptions().getBallSize());
        }
        
        //reset the ball container with the specified number of balls
        getBalls().reset(5);
        
        //reset player
        getPlayer().reset();
        
        //reset boundaries
        getBoundaries().reset();
    }
    
    /**
     * Update the internal storage.<br>
     * We will update the content of the storage with the current list of completed levels
     */
    public void updateStorage()
    {
        //remove all contents
        //getStorage().getContent().delete(0, getStorage().getContent().length());
        
        //save data to storage
        //getStorage().save();
    }
    
    /**
     * Get storage
     * @return Our internal storage object reference
     */
    private Internal getStorage()
    {
        return this.storage;
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
        else
        {
            
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
        
        
        /*
        //save information to internal storage
        updateStorage();

        //set game over state
        screen.setState(MainScreen.State.GameOver);

        //set display message
        screen.getScreenGameover().setMessage("Level Complete");

        //no need to continue
        return;
        */
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
        
        paint = null;
        storage = null;
    }
    
    /**
     * Render game elements
     * @param canvas Where to write the pixel data
     * @throws Exception 
     */
    public void render(final Canvas canvas) throws Exception
    {
        //fill in black background
        canvas.drawColor(Color.WHITE);
        
        if (getBoundaries() != null)
            getBoundaries().render(canvas);
        
        if (getController() != null)
            getController().render(canvas);
        
        if (getBalls() != null)
            getBalls().render(canvas);
        
        if (getPlayer() != null)
            getPlayer().render(canvas);
    }
}