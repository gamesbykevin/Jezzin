package com.gamesbykevin.jezzin.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.jezzin.assets.Assets;
import com.gamesbykevin.jezzin.game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * This screen will contain the game options
 * @author GOD
 */
public class OptionsScreen implements Screen, Disposable
{
    //our logo reference
    private final Bitmap logo;
    
    //list of buttons for the sound
    private List<Button> sounds;
    
    //list of buttons for ball collision
    private List<Button> collisions;
    
    //list of buttons for ball size
    private List<Button> sizes;
    
    //is collision enabled
    private boolean collision = false;
    
    //our size selection
    private int size = 0;
    
    //the go back button
    private Button back;
    
    //our main screen reference
    private final MainScreen screen;
    
    //paint object to draw text
    private Paint paint;
    
    public OptionsScreen(final MainScreen screen)
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
        int y = MainScreen.BUTTON_Y;
        
        //add audio option
        this.sounds = new ArrayList<Button>();
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Sound: Disabled");
        button.setX(MainScreen.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.sounds.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Sound: Enabled");
        button.setX(MainScreen.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.sounds.add(button);
        
        //add collision option
        this.collisions = new ArrayList<Button>();
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Ball Collision: Disabled");
        button.setX(MainScreen.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.collisions.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Ball Collision: Enabled");
        button.setX(MainScreen.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.collisions.add(button);
        
        //add collision option
        this.sizes = new ArrayList<Button>();
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Size: Medium");
        button.setX(MainScreen.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.sizes.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Size: Large");
        button.setX(MainScreen.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.sizes.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Size: X-Large");
        button.setX(MainScreen.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.sizes.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Size: Very Small");
        button.setX(MainScreen.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.sizes.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Size: Small");
        button.setX(MainScreen.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.sizes.add(button);
        
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        //the back button
        this.back = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        this.back.setText("Go Back");
        this.back.setX(MainScreen.BUTTON_X);
        this.back.setY(y);
        this.back.updateBounds();
        this.back.positionText(paint);
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
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (back.contains(x, y))
            {
                //set ready state
                screen.setState(MainScreen.State.Ready);
                
                //play sound effect
                //Audio.play(Assets.AudioKey.Selection);
                
                //no need to continue
                return false;
            }
            
            for (Button button : sounds)
            {
                if (button.contains(x, y))
                {
                    //flip setting
                    Audio.setAudioEnabled(!Audio.isAudioEnabled());
                    
                    //play sound effect
                    //Audio.play(Assets.AudioKey.Selection);
                    
                    //exit loop
                    return false;
                }
            }
            
            for (Button button : collisions)
            {
                if (button.contains(x, y))
                {
                    //flip setting
                    collision = !collision;
                    
                    //play sound effect
                    //Audio.play(Assets.AudioKey.Selection);
                    
                    //exit loop
                    return false;
                }
            }
            
            for (Button button : sizes)
            {
                if (button.contains(x, y))
                {
                    //change setting
                    size++;
                    
                    if (size >= sizes.size())
                        size = 0;
                    
                    //play sound effect
                    //Audio.play(Assets.AudioKey.Selection);
                    
                    //exit loop
                    return false;
                }
            }
        }
        
        //return true
        return true;
    }
    
    /**
     * Is ball collision enabled
     * @return true = yes, false = no
     */
    public boolean hasCollision()
    {
        return this.collision;
    }
    
    /**
     * Get the ball size
     * @return The ball size index
     */
    public int getBallSize()
    {
        return this.size;
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
        canvas.drawBitmap(logo, MainScreen.LOGO_X, MainScreen.LOGO_Y, null);
        
        //draw the menu buttons
        sounds.get(Audio.isAudioEnabled() ? 1 : 0).render(canvas, paint);
        collisions.get(collision ? 1 : 0).render(canvas, paint);
        sizes.get(getBallSize()).render(canvas, paint);
        
        //render back button
        back.render(canvas, paint);
    }
    
    @Override
    public void dispose()
    {
        if (back != null)
        {
            back.dispose();
            back = null;
        }
        
        if (collisions != null)
        {
            for (Button button : collisions)
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            collisions.clear();
            collisions = null;
        }
        
        if (sizes != null)
        {
            for (Button button : sizes)
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            sizes.clear();
            sizes = null;
        }
        
        if (sounds != null)
        {
            for (Button button : sounds)
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            sounds.clear();
            sounds = null;
        }
        
        if (paint != null)
            paint = null;
    }
}