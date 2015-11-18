package com.gamesbykevin.jezzin.screen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Font;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;

import com.gamesbykevin.jezzin.MainActivity;
import com.gamesbykevin.jezzin.assets.Assets;
import com.gamesbykevin.jezzin.panel.GamePanel;
import com.gamesbykevin.jezzin.player.Player;

/**
 * The game over screen
 * @author GOD
 */
public class GameoverScreen implements Screen, Disposable
{
    //our main screen reference
    private final ScreenManager screen;
    
    //object to paint background
    private Paint paintMessage;
    
    //the message to display
    private String message = "";
    
    //the dimensions of the text message
    private int pixelW;
    
    //buttons
    private Button next, replay, menu, rate;
    
    //time we have displayed text
    private long time;
    
    /**
     * The amount of time to wait until we render the game over menu
     */
    private static final long DELAY_MENU_DISPLAY = 3000;
    
    //do we display the menu
    private boolean display = false;
    
    /**
     * The text to display for the new game
     */
    private static final String BUTTON_TEXT_NEW_GAME = "Next";
    
    /**
     * The text to display to retry
     */
    private static final String BUTTON_TEXT_REPLAY = "Retry";
    
    /**
     * The text to display for the menu
     */
    private static final String BUTTON_TEXT_MENU = "Menu";
    
    public GameoverScreen(final ScreenManager screen)
    {
        //store our parent reference
        this.screen = screen;
        
        //the start location of the button
        int y = ScreenManager.BUTTON_Y;

        //create our buttons
        y += ScreenManager.BUTTON_Y_INCREMENT;
        this.next = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        this.next.setX(ScreenManager.BUTTON_X);
        this.next.setY(y);
        this.next.updateBounds();
        this.next.addDescription(BUTTON_TEXT_NEW_GAME);
        this.next.positionText(screen.getPaint());
        
        //will be in same position as next
        y += ScreenManager.BUTTON_Y_INCREMENT;
        this.replay = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        this.replay.setX(ScreenManager.BUTTON_X);
        this.replay.setY(y);
        this.replay.updateBounds();
        this.replay.addDescription(BUTTON_TEXT_REPLAY);
        this.replay.positionText(screen.getPaint());
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        this.menu = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        this.menu.setX(ScreenManager.BUTTON_X);
        this.menu.setY(y);
        this.menu.updateBounds();
        this.menu.addDescription(BUTTON_TEXT_MENU);
        this.menu.positionText(screen.getPaint());
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        this.rate = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        this.rate.setX(ScreenManager.BUTTON_X);
        this.rate.setY(y);
        this.rate.updateBounds();
        this.rate.addDescription(MenuScreen.BUTTON_TEXT_RATE_APP);
        this.rate.positionText(screen.getPaint());
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        //reset timer
        time = System.currentTimeMillis();
        
        //do we display the menu
        display = false;
    }
    
    /**
     * Assign the message
     * @param message The message we want displayed
     */
    public void setMessage(final String message)
    {
        //assign the message
        this.message = message;
        
        //create temporary rectangle
        Rect tmp = new Rect();
        
        //create paint text object for the message
        if (paintMessage == null)
            paintMessage = new Paint();
        
        //assign metrics
        paintMessage.setColor(Color.WHITE);
        paintMessage.setTextSize(30f);
        paintMessage.setTypeface(Font.getFont(Assets.FontMenuKey.Default));
        
        //get the rectangle around the message
        paintMessage.getTextBounds(message, 0, message.length(), tmp);
        
        //store the dimensions
        pixelW = tmp.width();
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        //if we aren't displaying the menu, return false
        if (!display)
            return false;
        
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (next.contains(x, y) && next.isVisible())
            {
                //remove message
                setMessage("");
                
                //reset for the next level
                screen.getScreenGame().getGame().reset(screen.getScreenGame().getGame().getBalls().getBalls().size() + 1);
                
                //move back to the game
                screen.setState(ScreenManager.State.Running);
                
                //play sound effect
                Audio.play(Assets.AudioMenuKey.Selection);
                
                //we don't request additional motion events
                return false;
            }
            else if (replay.contains(x, y))
            {
                //remove message
                setMessage("");
                
                //reset the same level
                screen.getScreenGame().getGame().reset(screen.getScreenGame().getGame().getBalls().getBalls().size());
                
                //move back to the game
                screen.setState(ScreenManager.State.Running);
                
                //play sound effect
                Audio.play(Assets.AudioMenuKey.Selection);
                
                //we don't request additional motion events
                return false;
            }
            else if (menu.contains(x, y))
            {
                //remove message
                setMessage("");
                
                //move to the main menu
                screen.setState(ScreenManager.State.Ready);
                
                //play sound effect
                Audio.play(Assets.AudioMenuKey.Selection);
                
                //we don't request additional motion events
                return false;
            }
            else if (rate.contains(x, y))
            {
                //remove message
                setMessage("");
                
                //play sound effect
                Audio.play(Assets.AudioMenuKey.Selection);
                
                //go to rate game page
                screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_RATE_URL);
                
                //we don't request additional motion events
                return false;
            }
        }
        
        //no action was taken here
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        //if not displaying the menu, track timer
        if (!display)
        {
            //if time has passed display menu
            if (System.currentTimeMillis() - time >= DELAY_MENU_DISPLAY)
            {
                display = true;
                
                int y = ScreenManager.BUTTON_Y;
                
                //if we did not meet the progress, then we lost
                if (screen.getScreenGame().getGame().getBoundaries().getTotalProgress() < Player.PROGRESS_GOAL)
                {
                    next.setVisible(false);
                    
                    //will be in same position as next
                    y += ScreenManager.BUTTON_Y_INCREMENT;
                    this.replay.setY(y);
                    this.replay.updateBounds();
                    this.replay.positionText(screen.getPaint());
                    
                    y += ScreenManager.BUTTON_Y_INCREMENT;
                    this.menu.setY(y);
                    this.menu.updateBounds();
                    this.menu.positionText(screen.getPaint());
                    
                    y += ScreenManager.BUTTON_Y_INCREMENT;
                    this.rate.setY(y);
                    this.rate.updateBounds();
                    this.rate.positionText(screen.getPaint());
                }
                else
                {
                    next.setVisible(true);

                    //create our buttons
                    y += ScreenManager.BUTTON_Y_INCREMENT;
                    this.next.setY(y);
                    this.next.updateBounds();
                    this.next.addDescription(BUTTON_TEXT_NEW_GAME);
                    this.next.positionText(screen.getPaint());
                    
                    //will be in same position as next
                    y += ScreenManager.BUTTON_Y_INCREMENT;
                    this.replay.setY(y);
                    this.replay.updateBounds();
                    this.replay.positionText(screen.getPaint());
                    
                    y += ScreenManager.BUTTON_Y_INCREMENT;
                    this.menu.setY(y);
                    this.menu.updateBounds();
                    this.menu.positionText(screen.getPaint());
                    
                    y += ScreenManager.BUTTON_Y_INCREMENT;
                    this.rate.setY(y);
                    this.rate.updateBounds();
                    this.rate.positionText(screen.getPaint());
                }
            }
        }
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        if (display)
        {
            //only darken the background when the menu is displayed
            ScreenManager.darkenBackground(canvas);
            
            if (paintMessage != null)
            {
                //calculate middle
                final int x = (GamePanel.WIDTH / 2) - (pixelW / 2);
                final int y = (int)(GamePanel.HEIGHT * .1);

                //draw text
                canvas.drawText(this.message, x, y, paintMessage);
            }
        
            //render buttons
            next.render(canvas, screen.getPaint());
            replay.render(canvas, screen.getPaint());
            rate.render(canvas, screen.getPaint());
            menu.render(canvas, screen.getPaint());
        }
    }
    
    @Override
    public void dispose()
    {
        if (paintMessage != null)
            paintMessage = null;
        
        if (next != null)
        {
            next.dispose();
            next = null;
        }
        
        if (menu != null)
        {
            menu.dispose();
            menu = null;
        }
        
        if (replay != null)
        {
            replay.dispose();
            replay = null;
        }
        
        if (rate != null)
        {
            rate.dispose();
            rate = null;
        }
    }
}