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
import com.gamesbykevin.jezzin.balls.Balls;
import com.gamesbykevin.jezzin.player.Player;
import com.gamesbykevin.jezzin.storage.settings.Settings;

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
    
    //list of buttons for the difficulties
    private List<Button> difficulties;
    
    //list of levels to start at
    private List<Button> levels;
    
    //list of game modes
    private List<Button> modes;
    
    //is collision enabled
    private boolean collision = false;
    
    //the go back button
    private Button back;
    
    //our main screen reference
    private final ScreenManager screen;
    
    //paint object to draw text
    private Paint paint;
    
    //difficulty selection
    private int difficultyIndex = 0;
    
    //level selection
    private int levelIndex = 0;
    
    //mode selection
    private int modeIndex = 0;
    
    //our storage settings object
    private Settings settings;
    
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
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonsSound(y);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonsMode(y);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonsDifficulty(y);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonsLevels(y);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonsCollision(y);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonsBack(y);
        
        //create our settings object, which will load the previous settings
        this.settings = new Settings(this, screen.getPanel().getActivity());
    }
    
    private void addButtonsMode(int y)
    {
        //add collision option
        this.modes = new ArrayList<Button>();
        
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Mode: " + Player.MODE_DESC_CASUAL);
        button.setX(ScreenManager.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.modes.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Mode: " + Player.MODE_DESC_SURVIVAL);
        button.setX(ScreenManager.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.modes.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Mode: " + Player.MODE_DESC_TIMED);
        button.setX(ScreenManager.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.modes.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Mode: " + Player.MODE_DESC_CHALLENGE);
        button.setX(ScreenManager.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.modes.add(button);
    }
    
    private void addButtonsLevels(int y)
    {
        //add level option
        this.levels = new ArrayList<Button>();
        
        for (int i = 1; i <= Balls.BALL_MAX; i++)
        {
            Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
            button.setText("Level: " + i);
            button.setX(ScreenManager.BUTTON_X);
            button.setY(y);
            button.updateBounds();
            button.positionText(paint);
            this.levels.add(button);
        }
    }
    
    private void addButtonsBack(int y)
    {
        //the back button
        this.back = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        this.back.setText("Go Back");
        this.back.setX(ScreenManager.BUTTON_X);
        this.back.setY(y);
        this.back.updateBounds();
        this.back.positionText(paint);
    }
    
    private void addButtonsDifficulty(int y)
    {
        //the difficulty options
        this.difficulties = new ArrayList<Button>();
        
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Difficulty: " + Player.DIFFICULTY_DESC_NORMAL);
        button.setX(ScreenManager.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.difficulties.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Difficulty: " + Player.DIFFICULTY_DESC_HARD);
        button.setX(ScreenManager.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.difficulties.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Difficulty: " + Player.DIFFICULTY_DESC_EASY);
        button.setX(ScreenManager.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.difficulties.add(button);
    }
    
    private void addButtonsCollision(int y)
    {
        //add collision option
        this.collisions = new ArrayList<Button>();
        
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Ball Collision: Disabled");
        button.setX(ScreenManager.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.collisions.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Ball Collision: Enabled");
        button.setX(ScreenManager.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.collisions.add(button);
    }
    
    private void addButtonsSound(int y)
    {
        //add audio option
        this.sounds = new ArrayList<Button>();
        
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Sound: Disabled");
        button.setX(ScreenManager.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.sounds.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Sound: Enabled");
        button.setX(ScreenManager.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.sounds.add(button);
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
                //store our settings
                settings.save();
                
                //set ready state
                screen.setState(ScreenManager.State.Ready);
                
                //play sound effect
                Audio.play(Assets.AudioMenuKey.Selection);
                
                //no need to continue
                return false;
            }
            
            for (Button button : modes)
            {
                if (button.contains(x, y))
                {
                    //increase mode selection
                    setModeIndex(getModeIndex() + 1);
                    
                    //play sound effect
                    Audio.play(Assets.AudioMenuKey.Selection);
                    
                    //exit loop
                    return false;
                }
            }
            
            for (Button button : levels)
            {
                if (button.contains(x, y))
                {
                    //increase level
                    setLevelIndex(getLevelIndex() + 1);
                    
                    //play sound effect
                    Audio.play(Assets.AudioMenuKey.Selection);
                    
                    //exit loop
                    return false;
                }
            }

            for (Button button : sounds)
            {
                if (button.contains(x, y))
                {
                    //flip setting
                    Audio.setAudioEnabled(!Audio.isAudioEnabled());
                    
                    //play sound effect
                    Audio.play(Assets.AudioMenuKey.Selection);
                    
                    //exit loop
                    return false;
                }
            }
            
            for (Button button : difficulties)
            {
                if (button.contains(x, y))
                {
                    //move to the next seletion
                    setDifficultyIndex(getDifficultyIndex() + 1);
                    
                    //play sound effect
                    Audio.play(Assets.AudioMenuKey.Selection);
                    
                    //exit loop
                    return false;
                }
            }
            
            for (Button button : collisions)
            {
                if (button.contains(x, y))
                {
                    //flip setting
                    setCollision(!hasCollision());
                    
                    //play sound effect
                    Audio.play(Assets.AudioMenuKey.Selection);
                    
                    //exit loop
                    return false;
                }
            }
        }
        
        //return true
        return true;
    }
    
    /**
     * Assign the mode index
     * @param modeIndex The desired mode index
     */
    public void setModeIndex(final int modeIndex)
    {
        this.modeIndex = modeIndex;
        
        if (getModeIndex() >= modes.size())
            setModeIndex(0);
    }
    
    /**
     * Get the mode index
     * @return The user selection for mode
     */
    public int getModeIndex()
    {
        return this.modeIndex;
    }
    
    /**
     * Assign the level index
     * @param levelIndex The desired level index
     */
    public void setLevelIndex(final int levelIndex)
    {
        this.levelIndex = levelIndex;
        
        if (getLevelIndex() >= levels.size())
            setLevelIndex(0);
    }
    
    /**
     * Get the level index
     * @return The user selection for level
     */
    public int getLevelIndex()
    {
        return this.levelIndex;
    }
    
    /**
     * Assign the difficulty index
     * @param difficultyIndex The desired difficulty index
     */
    public void setDifficultyIndex(final int difficultyIndex)
    {
        this.difficultyIndex = difficultyIndex;
        
        if (getDifficultyIndex() >= difficulties.size())
            setDifficultyIndex(0);
    }
    
    /**
     * Get the difficulty index
     * @return The user selection for difficulty
     */
    public int getDifficultyIndex()
    {
        return this.difficultyIndex;
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
        sounds.get(Audio.isAudioEnabled() ? 1 : 0).render(canvas, paint);
        collisions.get(hasCollision() ? 1 : 0).render(canvas, paint);
        difficulties.get(getDifficultyIndex()).render(canvas, paint);
        levels.get(getLevelIndex()).render(canvas, paint);
        modes.get(getModeIndex()).render(canvas, paint);
        
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
        
        if (settings != null)
        {
            settings.dispose();
            settings = null;
        }
        
        if (levels != null)
        {
            for (Button button : levels)
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            levels.clear();
            levels = null;
        }
        
        if (difficulties != null)
        {
            for (Button button : difficulties)
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            difficulties.clear();
            difficulties = null;
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