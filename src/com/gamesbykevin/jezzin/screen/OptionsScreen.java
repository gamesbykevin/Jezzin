package com.gamesbykevin.jezzin.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.SparseArray;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.jezzin.MainActivity;
import com.gamesbykevin.jezzin.screen.ScreenManager;
import com.gamesbykevin.jezzin.panel.GamePanel;
import com.gamesbykevin.jezzin.screen.MenuScreen;
import com.gamesbykevin.jezzin.assets.Assets;
import com.gamesbykevin.jezzin.balls.Balls;
import com.gamesbykevin.jezzin.player.Player;
import com.gamesbykevin.jezzin.storage.settings.Settings;

/**
 * This screen will contain the game options
 * @author GOD
 */
public class OptionsScreen implements Screen, Disposable
{
    //our logo reference
    private final Bitmap logo;
    
    //list of buttons
    private SparseArray<Button> buttons;
    
    //is collision enabled
    private boolean collision = false;
    
    //our main screen reference
    private final ScreenManager screen;
    
    //paint object to draw text
    private Paint paint;
    
    //our storage settings object
    private Settings settings;
    
    //buttons to access each button list
    public static final int INDEX_BUTTON_BACK = 0;
    public static final int INDEX_BUTTON_SOUND = 1;
    public static final int INDEX_BUTTON_VIBRATE = 2;
    public static final int INDEX_BUTTON_MODE = 3;
    public static final int INDEX_BUTTON_LEVEL = 4;
    public static final int INDEX_BUTTON_DIFFICULTY = 5;
    public static final int INDEX_BUTTON_INSTRUCTIONS = 6;
    public static final int INDEX_BUTTON_FACEBOOK = 7;
    public static final int INDEX_BUTTON_TWITTER = 8;
    
    public OptionsScreen(final ScreenManager screen)
    {
        //our logo reference
        this.logo = Images.getImage(Assets.ImageMenuKey.Logo);
        
        //store our screen reference
        this.screen = screen;
        
        //create new paint object
        this.paint = new Paint();
        
        //set the text size
        this.paint.setTextSize(24f);
        
        //set the color
        this.paint.setColor(Color.WHITE);
        
        //start coordinates
        int y = ScreenManager.BUTTON_Y;
        final int x = ScreenManager.BUTTON_X;
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonSound(x, y);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonVibrate(x, y);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonMode(x, y);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonDifficulty(x, y);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonLevels(x, y);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonBack(x, y);
        
        //add social media icons after the above, because the dimensions are different
        addIcons();
        
        //setup each button
        for (int index = 0; index < buttons.size(); index++)
        {
        	final Button button = buttons.get(index);
        	
        	switch (index)
        	{
	        	case INDEX_BUTTON_INSTRUCTIONS:
	        	case INDEX_BUTTON_FACEBOOK:
	        	case INDEX_BUTTON_TWITTER:
	        		button.setWidth(MenuScreen.ICON_DIMENSION);
	        		button.setHeight(MenuScreen.ICON_DIMENSION);
	        		button.updateBounds();
	        		break;
	        		
        		default:
	        		button.setWidth(MenuScreen.BUTTON_WIDTH);
	        		button.setHeight(MenuScreen.BUTTON_HEIGHT);
	        		button.updateBounds();
	        		button.positionText(paint);
        			break;
        	}
        }
        
        //create our settings object, which will load the previous settings
        this.settings = new Settings(this, screen.getPanel().getActivity());
    }
    
    /**
     * Add icons, including links to social media
     */
    private void addIcons()
    {
        Button tmp = new Button(Images.getImage(Assets.ImageMenuKey.Instructions));
        tmp.setX(GamePanel.WIDTH - (MenuScreen.ICON_DIMENSION * 4.5));
        tmp.setY(GamePanel.HEIGHT - (MenuScreen.ICON_DIMENSION * 1.25));
        getButtons().put(INDEX_BUTTON_INSTRUCTIONS, tmp);
        
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Facebook));
        tmp.setX(GamePanel.WIDTH - (MenuScreen.ICON_DIMENSION * 3));
        tmp.setY(GamePanel.HEIGHT - (MenuScreen.ICON_DIMENSION * 1.25));
        getButtons().put(INDEX_BUTTON_FACEBOOK, tmp);
        
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Twitter));
        tmp.setX(GamePanel.WIDTH - (MenuScreen.ICON_DIMENSION * 1.5));
        tmp.setY(GamePanel.HEIGHT - (MenuScreen.ICON_DIMENSION * 1.25));
        getButtons().put(INDEX_BUTTON_TWITTER, tmp);
    }
    
    /**
     * Get the list of buttons.<br>
     * We typically use this list to help load/set the settings based on the index of each button.
     * @return The list of buttons on the options screen
     */
    public SparseArray<Button> getButtons()
    {
    	if (this.buttons == null)
    		this.buttons = new SparseArray<Button>();
    	
    	return this.buttons;
    }
    
    private void addButtonBack(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Back");
        button.setX(x);
        button.setY(y);
        getButtons().put(INDEX_BUTTON_BACK, button);
    }
    
    private void addButtonSound(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Sound: On");
        button.addDescription("Sound: Off");
        button.setX(x);
        button.setY(y);
        getButtons().put(INDEX_BUTTON_SOUND, button);
    }

    private void addButtonVibrate(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Vibrate: On");
        button.addDescription("Vibrate: Off");
        button.setX(x);
        button.setY(y);
        getButtons().put(INDEX_BUTTON_VIBRATE, button);
    }
    
    private void addButtonMode(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Mode: " + Player.MODE_DESC_CASUAL);
        button.addDescription("Mode: " + Player.MODE_DESC_SURVIVAL);
        button.addDescription("Mode: " + Player.MODE_DESC_CHALLENGE);
        button.setX(x);
        button.setY(y);
        getButtons().put(INDEX_BUTTON_MODE, button);
    }
    
    private void addButtonLevels(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        
        //add each level description
        for (int i = 1; i <= Balls.BALL_MAX; i++)
        {
        	button.addDescription("Level: " + i);
        }
        
        button.setX(x);
        button.setY(y);
        getButtons().put(INDEX_BUTTON_LEVEL, button);
    }
    
    private void addButtonDifficulty(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Difficulty: " + Player.DIFFICULTY_DESC_NORMAL);
        button.addDescription("Difficulty: " + Player.DIFFICULTY_DESC_HARD);
        button.addDescription("Difficulty: " + Player.DIFFICULTY_DESC_EASY);
        button.setX(x);
        button.setY(y);
        getButtons().put(INDEX_BUTTON_DIFFICULTY, button);
    }
    
    /**
     * Assign the index.
     * @param key The key of the button we want to change
     * @param index The desired index
     */
    public void setIndex(final int key, final int index)
    {
    	buttons.get(key).setIndex(index);
    }
    
    /**
     * Get the index selection of the specified button
     * @param key The key of the button we want to check
     * @return The current selection for the specified button key
     */
    public int getIndex(final int key)
    {
    	return getButtons().get(key).getIndex();
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        if (getButtons() != null)
        {
        	for (int key = 0; key < getButtons().size(); key++)
        	{
        		//get the current button
        		Button button = getButtons().get(key);
        		
        		try
        		{
	        		switch (key)
	        		{
						case INDEX_BUTTON_BACK:
						case INDEX_BUTTON_SOUND:
						case INDEX_BUTTON_MODE:
						case INDEX_BUTTON_DIFFICULTY:
						case INDEX_BUTTON_LEVEL:
						case INDEX_BUTTON_VIBRATE:
							button.positionText(paint);
							break;
							
						//do nothing for these
						case INDEX_BUTTON_INSTRUCTIONS:
						case INDEX_BUTTON_FACEBOOK:
						case INDEX_BUTTON_TWITTER:
							break;
							
						default:
							throw new Exception("Key not handled here: " + key);
	        		}
        		}
        		catch (Exception e)
        		{
        			e.printStackTrace();
        		}
        	}
        }
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
    	//we only want motion event up
    	if (event.getAction() != MotionEvent.ACTION_UP)
    		return true;
    	
    	//don't continue if the buttons don't exist
    	if (getButtons() == null)
    		return true;
    	
    	for (int key = 0; key < getButtons().size(); key++)
    	{
    		//get the current button
    		Button button = getButtons().get(key);
    		
    		//if the button does not exist skip to the next
    		if (button == null)
    			continue;
    		
			//if we did not select this button, skip to the next
			if (!button.contains(x, y))
				continue;
			
			//determine which button
			switch (key)
			{
				case INDEX_BUTTON_BACK:
					
					//change index
					button.setIndex(button.getIndex() + 1);
					
	                //store our settings
	                settings.save();
	                
	                //set ready state
	                screen.setState(ScreenManager.State.Ready);
	                
	                //play sound effect
	                Audio.play(Assets.AudioMenuKey.Selection);
	                
	                //no need to continue
	                return false;
	                
				case INDEX_BUTTON_VIBRATE:
					//change index
					button.setIndex(button.getIndex() + 1);
					
					//position the text
			        button.positionText(paint);
					
	                //play sound effect
	                Audio.play(Assets.AudioMenuKey.Selection);
	                
                    //no need to continue
                    return false;
					
				case INDEX_BUTTON_SOUND:
	    			
					//change index
					button.setIndex(button.getIndex() + 1);
					
					//position the text
			        button.positionText(paint);
			        
                    //flip setting
                    Audio.setAudioEnabled(!Audio.isAudioEnabled());
                    
                    //we also want to update the audio button in the controller so the correct is displayed
                    if (screen.getScreenGame() != null && screen.getScreenGame().getGame() != null)
                    {
                    	//make sure the controller exists
                		if (screen.getScreenGame().getGame().getController() != null)
                			screen.getScreenGame().getGame().getController().reset();
                    }
                    
                    //play sound effect
                    Audio.play(Assets.AudioMenuKey.Selection);
                    
                    //exit loop
                    return false;
                    
				case INDEX_BUTTON_MODE:
				case INDEX_BUTTON_DIFFICULTY:
				case INDEX_BUTTON_LEVEL:
					
					//change index
					button.setIndex(button.getIndex() + 1);
					
					//position the text
			        button.positionText(paint);
					
                    //play sound effect
                    Audio.play(Assets.AudioMenuKey.Selection);
                    
                    //no need to continue
					return false;
                    
				case INDEX_BUTTON_INSTRUCTIONS:
					
	                //play sound effect
	                Audio.play(Assets.AudioMenuKey.Selection);
	                
	                //go to instructions
	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_GAME_INSTRUCTIONS_URL);
	                
	                //we do not request any additional events
	                return false;
					
				case INDEX_BUTTON_FACEBOOK:
					
	                //play sound effect
	                Audio.play(Assets.AudioMenuKey.Selection);
	                
	                //go to instructions
	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_FACEBOOK_URL);
	                
	                //we do not request any additional events
	                return false;
					
				case INDEX_BUTTON_TWITTER:
					
	                //play sound effect
	                Audio.play(Assets.AudioMenuKey.Selection);
	                
	                //go to instructions
	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_TWITTER_URL);
	                
	                //we do not request any additional events
	                return false;
				
				default:
                	throw new Exception("Key not setup here: " + key);
			}
    	}
    	
        //return true
        return true;
    }
    
    /**
     * Assign collision
     * @param collision true if collision enabled, false otherwise 
     */
    public void setCollision(final boolean collision)
    {
        this.collision = collision;
    }
    
    /**
     * Is ball collision enabled
     * @return true = yes, false = no
     */
    public boolean hasCollision()
    {
        return this.collision;
    }
    
    @Override
    public void update() throws Exception
    {
        //no updates needed here
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw main logo
        canvas.drawBitmap(logo, ScreenManager.LOGO_X, ScreenManager.LOGO_Y, null);
        
        //draw the menu buttons
    	for (int index = 0; index < getButtons().size(); index++)
    	{
    		if (getButtons().get(index) != null)
    		{
    			switch (index)
    			{
	    			case INDEX_BUTTON_BACK:
	    			case INDEX_BUTTON_SOUND:
	    			case INDEX_BUTTON_DIFFICULTY:
	    			case INDEX_BUTTON_MODE:
	    			case INDEX_BUTTON_LEVEL:
	    			case INDEX_BUTTON_VIBRATE:
	    				getButtons().get(index).render(canvas, paint);
	    				break;
	    				
	    			case INDEX_BUTTON_INSTRUCTIONS:
	    			case INDEX_BUTTON_FACEBOOK:
	    			case INDEX_BUTTON_TWITTER:
	    				getButtons().get(index).render(canvas);
	    				break;
	    				
	    			default:
	    				throw new Exception("Button with index not setup here: " + index);
    			}
    		}
    	}
    }
    
    @Override
    public void dispose()
    {
        if (paint != null)
            paint = null;
        
        if (settings != null)
        {
            settings.dispose();
            settings = null;
        }
        
        if (buttons != null)
        {
        	for (int i = 0; i < buttons.size(); i++)
        	{
        		if (buttons.get(i) != null)
        		{
        			buttons.get(i).dispose();
        			buttons.put(i, null);
        		}
        	}
        	
        	buttons.clear();
        	buttons = null;
        }
    }
}