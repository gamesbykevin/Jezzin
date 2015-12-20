package com.gamesbykevin.jezzin.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.jezzin.screen.ScreenManager;
import com.gamesbykevin.jezzin.panel.GamePanel;
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
    
    /**
     * Dimension of the standard menu button
     */
    public static final int BUTTON_WIDTH = 300;
    
    /**
     * Dimension of the standard menu button
     */
    public static final int BUTTON_HEIGHT = 75;
    
    /**
     * The size of our icon buttons
     */
    public static final int ICON_DIMENSION = 75;
    
    private enum Key
    {
        Start, Exit, Settings, Instructions, More, Rate, Twitter, Facebook
    }
    
    //start new game, and did we notify user
    private boolean reset = false, notify = false;
    
    public MenuScreen(final ScreenManager screen)
    {
        //store reference to the logo
        this.logo = Images.getImage(Assets.ImageMenuKey.Logo);
        
        //store our screen reference
        this.screen = screen;
        
        //create a new hash map
        this.buttons = new HashMap<Key, Button>();
        
        int y = ScreenManager.BUTTON_Y;
        final int x = ScreenManager.BUTTON_X;
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButton(x, y, BUTTON_TEXT_START_GAME, Key.Start);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButton(x, y, BUTTON_TEXT_OPTIONS, Key.Settings);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButton(x, y, BUTTON_TEXT_RATE_APP, Key.Rate);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButton(x, y, BUTTON_TEXT_MORE_GAMES, Key.More);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButton(x, y, BUTTON_TEXT_EXIT_GAME, Key.Exit);
        
        //add social media icons
        addIcons();
        
        //set the size and bounds of the buttons
        for (Key key : Key.values())
        {
        	//get the current button
        	Button button = buttons.get(key);
        	
        	switch (key)
        	{
	        	case Twitter:
	        	case Facebook:
	        	case Instructions:
                	button.setWidth(ICON_DIMENSION);
                	button.setHeight(ICON_DIMENSION);
                	button.updateBounds();
	        		break;
        		
        		default:
                	button.setWidth(BUTTON_WIDTH);
                	button.setHeight(BUTTON_HEIGHT);
                    button.updateBounds();
                    button.positionText(screen.getPaint());
        			break;
        	}
        }
    }

    /**
     * Add icons, including links to social media
     */
    private void addIcons()
    {
        Button tmp = new Button(Images.getImage(Assets.ImageMenuKey.Instructions));
        tmp.setX(GamePanel.WIDTH - (ICON_DIMENSION * 4.5));
        tmp.setY(GamePanel.HEIGHT - (ICON_DIMENSION * 1.25));
        this.buttons.put(Key.Instructions, tmp);
        
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Facebook));
        tmp.setX(GamePanel.WIDTH - (ICON_DIMENSION * 3));
        tmp.setY(GamePanel.HEIGHT - (ICON_DIMENSION * 1.25));
        this.buttons.put(Key.Facebook, tmp);
        
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Twitter));
        tmp.setX(GamePanel.WIDTH - (ICON_DIMENSION * 1.5));
        tmp.setY(GamePanel.HEIGHT - (ICON_DIMENSION * 1.25));
        this.buttons.put(Key.Twitter, tmp);
    }
    
    private void addButton(final int x, final int y, final String description, final Key key)
    {
    	Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
    	button.setX(x);
    	button.setY(y);
    	button.addDescription(description);
    	
    	//add button to the list
    	this.buttons.put(key, button);
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
    	//does anything need to be done here
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        //if the game is to reset, don't continue
        if (reset)
            return false;
        
        //we only want action up
        if (event.getAction() != MotionEvent.ACTION_UP)
        	return true;
        
    	//check every button
    	for (Key key : Key.values())
    	{
    		//get the current button
    		Button button = buttons.get(key);
    		
    		//if the coordinates are contained in the button
    		if (button.contains(x, y))
    		{
    			//do something different depending on the key
    			switch (key)
    			{
	        		case Instructions:
	                    //play sound effect
	                    Audio.play(Assets.AudioMenuKey.Selection);
	                    
	                    //go to instructions
	                    this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_GAME_INSTRUCTIONS_URL);
	                    
	                    //we do not request any additional events
	                    return false;
	                    
	        		case Facebook:
	                    //play sound effect
	                    Audio.play(Assets.AudioMenuKey.Selection);
	                    
	                    //go to instructions
	                    this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_FACEBOOK_URL);
	                    
	                    //we do not request any additional events
	                    return false;
	                    
	        		case Twitter:
	                    //play sound effect
	                    Audio.play(Assets.AudioMenuKey.Selection);
	                    
	                    //go to instructions
	                    this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_TWITTER_URL);
	                    
	                    //we do not request any additional events
	                    return false;
	        			
	        		case Start:
	                    //flag reset
	                    reset = true;
	                    
	                    //flag notify false
	                    notify = false;
	                    
	                    //play sound effect
	                    Audio.play(Assets.AudioMenuKey.Selection);
	                    
	                    //we do not request any additional events
	                    return false;
	                    
        			case Exit:
                        //play sound effect
                        Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //exit game
                        this.screen.getPanel().getActivity().finish();
                        
                        //we do not request any additional events
                        return false;
                        
        			case Settings: 
                        //set the state
                        screen.setState(ScreenManager.State.Options);
                        
                        //play sound effect
                        Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //we do not request any additional events
                        return false;
                        
    				case More: 
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //go to web page
    	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_MORE_GAMES_URL);
    	                
    	                //we do not request any additional events
    	                return false;
    	                
					case Rate:
		                //play sound effect
		                Audio.play(Assets.AudioMenuKey.Selection);
		                
		                //go to web page
		                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_RATE_URL);
		                
		                //we do not request any additional events
		                return false;
		                
	                default:
	                	throw new Exception("Key not handled here: " + key);
    			}
    		}
    	}
        
        //return true
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
    	//only reset if we notified the user by displaying the splash screen
        if (reset && notify)
        {
            //load game assets
            Assets.load(screen.getPanel().getActivity());

            //create the game
            screen.getScreenGame().createGame();

            //set running state
            screen.setState(ScreenManager.State.Running);
            
            //we are done resetting
            reset = false;
        }
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        if (reset)
        {
            //render splash screen
            canvas.drawBitmap(Images.getImage(Assets.ImageMenuKey.Splash), 0, 0, null);
            
            //we notified the user
            notify = true;
        }
        else
        {
            //draw main logo
            canvas.drawBitmap(logo, ScreenManager.LOGO_X, ScreenManager.LOGO_Y, null);

            //we can't draw the buttons if they don't exist
            if (buttons == null)
            	return;
            
        	for (Key key : Key.values())
        	{
        		//get the current button
        		Button button = buttons.get(key);
        		
        		//render the button accordingly
        		switch (key)
        		{
	        		case Instructions:
	        		case Facebook:
	        		case Twitter:
	        			button.render(canvas);
	        			break;
	        			
	        		case Start:
        			case Exit:
        			case Settings: 
    				case More: 
					case Rate:
        				button.render(canvas, screen.getPaint());
        				break;
        				
    				default:
    					throw new Exception("Key is not handled here: " + key);
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