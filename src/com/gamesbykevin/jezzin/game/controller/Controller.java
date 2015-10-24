package com.gamesbykevin.jezzin.game.controller;

import com.gamesbykevin.androidframework.awt.Button;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Images;

import com.gamesbykevin.jezzin.assets.Assets;
import com.gamesbykevin.jezzin.game.Game;
import com.gamesbykevin.jezzin.panel.GamePanel;
import com.gamesbykevin.jezzin.screen.MainScreen;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will be our game controller
 * @author GOD
 */
public class Controller implements IController
{
    //all of the buttons for the player to control
    private HashMap<Assets.ImageGameKey, Button> buttons;
    
    //our game object reference
    private final Game game;
    
    /**
     * The dimensions of the buttons, to be rendered to the user
     */
    private final static int BUTTON_DIMENSION = 32;

    //location of exit button
    private final static int EXIT_X = GamePanel.WIDTH - (BUTTON_DIMENSION * 4);
    private final static int EXIT_Y = 16;
    
    //location of pause button
    private final static int PAUSE_X = GamePanel.WIDTH - (BUTTON_DIMENSION * 2);
    private final static int PAUSE_Y = EXIT_Y;
    
    /**
     * Default Constructor
     * @param game Object game object reference
     */
    public Controller(final Game game)
    {
        //assign object reference
        this.game = game;
        
        //create temp list
        List<Assets.ImageGameKey> tmp = new ArrayList<Assets.ImageGameKey>();
        
        //add button unique key to list
        tmp.add(Assets.ImageGameKey.Pause);
        tmp.add(Assets.ImageGameKey.Exit);
        
        //create new list of buttons
        this.buttons = new HashMap<Assets.ImageGameKey, Button>();
        
        //add button
        for (Assets.ImageGameKey key : tmp)
        {
            this.buttons.put(key, new Button(Images.getImage(key)));
        }
        
        //update location of our buttons
        this.buttons.get(Assets.ImageGameKey.Pause).setX(PAUSE_X);
        this.buttons.get(Assets.ImageGameKey.Pause).setY(PAUSE_Y);
        this.buttons.get(Assets.ImageGameKey.Exit).setX(EXIT_X);
        this.buttons.get(Assets.ImageGameKey.Exit).setY(EXIT_Y);
        
        for (Assets.ImageGameKey key : tmp)
        {
            //set the dimension of the button
            this.buttons.get(key).setWidth(BUTTON_DIMENSION);
            this.buttons.get(key).setHeight(BUTTON_DIMENSION);
            
            //update the boundary
            this.buttons.get(key).updateBounds();
        }
    }
    
    /**
     * Get our game object reference
     * @return Our game object reference
     */
    private Game getGame()
    {
        return this.game;
    }
    
    /**
     * Update the controller based on the motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     * @return true if motion event was applied, false otherwise
     * @throws Exception
     */
    public boolean updateMotionEvent(final MotionEvent event, final float x, final float y) throws Exception
    {
        //check if the touch screen was released
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            //check if the player hit the controller
            if (buttons.get(Assets.ImageGameKey.Pause).contains(x, y))
            {
                //change the state to paused
                getGame().getMainScreen().setState(MainScreen.State.Paused);
                
                //event was applied
                return true;
            }
            else if (buttons.get(Assets.ImageGameKey.Exit).contains(x, y))
            {
                //change to the exit confirm screen
                getGame().getMainScreen().setState(MainScreen.State.Exit);
                
                //event was applied
                return true;
            }
        }
        
        //no event was applied
        return false;
    }
    
    /**
     * Recycle objects
     */
    @Override
    public void dispose()
    {
        if (buttons != null)
        {
            for (Button button : buttons.values())
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            buttons.clear();
            buttons = null;
        }
    }
    
    /**
     * Render the controller
     * @param canvas Write pixel data to this canvas
     * @throws Exception 
     */
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw the buttons
        if (buttons != null)
        {
            for (Button button : buttons.values())
            {
                if (button != null)
                    button.render(canvas);
            }
        }
    }
}