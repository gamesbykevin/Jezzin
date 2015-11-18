package com.gamesbykevin.jezzin.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.jezzin.MainActivity;
import com.gamesbykevin.jezzin.assets.Assets;

import java.util.HashMap;

/**
 * Our main menu
 * @author ABRAHAM
 */
public class MenuScreen implements Screen, Disposable
{
    //the logo
    private final Bitmap logo;
    
    //our main screen reference
    private final ScreenManager screen;
    
    //the buttons on the menu screen
    private HashMap<Key, Button> buttons;
    
    /**
     * Button text to display to exit the game
     */
    public static final String BUTTON_TEXT_EXIT_GAME = "Exit Game";
    
    /**
     * Button text to display to rate the game
     */
    public static final String BUTTON_TEXT_RATE_APP = "Rate this App";
    
    /**
     * Button text to display to start a new game
     */
    public static final String BUTTON_TEXT_START_GAME = "Start Game";
    
    /**
     * Button text to display for the options
     */
    public static final String BUTTON_TEXT_OPTIONS = "Options";
    
    /**
     * Button text to display for instructions
     */
    public static final String BUTTON_TEXT_INSTRUCTIONS = "Instructions";
    
    /**
     * Button text to display for more games
     */
    public static final String BUTTON_TEXT_MORE_GAMES = "More Games";
    
    private enum Key
    {
        Start, Exit, Settings, Instructions, More, Rate
    }
    
    //start new game, and did we notify user
    private boolean reset = false, notify = false;
    
    public MenuScreen(final ScreenManager screen)
    {
        //store reference to the logo
        this.logo = Images.getImage(Assets.ImageMenuKey.Logo);
        
        //store our screen reference
        this.screen = screen;
        
        //create a new hashmap
        this.buttons = new HashMap<Key, Button>();
        
        //temp button
        Button tmp;
        
        int y = ScreenManager.BUTTON_Y;
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        tmp.setX(ScreenManager.BUTTON_X);
        tmp.setY(y);
        tmp.addDescription(BUTTON_TEXT_START_GAME);
        this.buttons.put(Key.Start, tmp);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        tmp.setX(ScreenManager.BUTTON_X);
        tmp.setY(y);
        tmp.addDescription(BUTTON_TEXT_OPTIONS);
        this.buttons.put(Key.Settings, tmp);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        tmp.setX(ScreenManager.BUTTON_X);
        tmp.setY(y);
        tmp.addDescription(BUTTON_TEXT_INSTRUCTIONS);
        this.buttons.put(Key.Instructions, tmp);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        tmp.setX(ScreenManager.BUTTON_X);
        tmp.setY(y);
        tmp.addDescription(BUTTON_TEXT_RATE_APP);
        this.buttons.put(Key.Rate, tmp);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        tmp.setX(ScreenManager.BUTTON_X);
        tmp.setY(y);
        tmp.addDescription(BUTTON_TEXT_MORE_GAMES);
        this.buttons.put(Key.More, tmp);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        tmp.setX(ScreenManager.BUTTON_X);
        tmp.setY(y);
        tmp.addDescription(BUTTON_TEXT_EXIT_GAME);
        this.buttons.put(Key.Exit, tmp);
        
        for (Button button : buttons.values())
        {
            button.updateBounds();
            button.positionText(screen.getPaint());
        }
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        //do we need anything here
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        //if the game is to reset, don't continue
        if (reset)
            return false;
        
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (buttons.get(Key.Start).contains(x, y))
            {
                //flag reset
                reset = true;

                //unflag notify
                notify = false;
                
                //we do not request any additional events
                return false;
            }
            else if (buttons.get(Key.Settings).contains(x, y))
            {
                //set the state
                screen.setState(ScreenManager.State.Options);
                
                //play sound effect
                Audio.play(Assets.AudioMenuKey.Selection);
                
                //we do not request any additional events
                return false;
            }
            else if (buttons.get(Key.Instructions).contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioMenuKey.Selection);
                
                //go to instructions
                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_GAME_INSTRUCTIONS_URL);
                
                //we do not request any additional events
                return false;
            }
            else if (buttons.get(Key.Rate).contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioMenuKey.Selection);
                
                //go to web page
                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_RATE_URL);
                
                //we do not request any additional events
                return false;
            }
            else if (buttons.get(Key.More).contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioMenuKey.Selection);
                
                //go to web page
                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_MORE_GAMES_URL);
                
                //we do not request any additional events
                return false;
            }
            else if (buttons.get(Key.Exit).contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioMenuKey.Selection);
                
                //exit game
                this.screen.getPanel().getActivity().finish();
                
                //we do not request any additional events
                return false;
            }
        }
        
        //return true
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        if (reset && notify)
        {
            reset = false;
            
            //load game assets
            Assets.load(screen.getPanel().getActivity());

            //create the game
            screen.getScreenGame().createGame();

            //set running state
            screen.setState(ScreenManager.State.Running);

            //play sound effect
            Audio.play(Assets.AudioMenuKey.Selection);
        }
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        if (reset)
        {
            //render splash scren
            canvas.drawBitmap(Images.getImage(Assets.ImageMenuKey.Splash), 0, 0, null);
            
            //we notified the user
            notify = true;
        }
        else
        {
            //draw main logo
            canvas.drawBitmap(logo, ScreenManager.LOGO_X, ScreenManager.LOGO_Y, null);

            //draw the menu buttons
            if (buttons != null)
            {
                for (Button button : buttons.values())
                {
                    if (button != null)
                        button.render(canvas, screen.getPaint());
                }
            }
        }
    }
    
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
}