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
import com.gamesbykevin.jezzin.game.Game;
import com.gamesbykevin.jezzin.player.Player;

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
    private final MainScreen screen;
    
    //paint object to draw text
    private Paint paint;
    
    //difficulty selection
    private int difficultyIndex = 0;
    
    //level selection
    private int levelIndex = 0;
    
    //mode selection
    private int modeIndex = 0;
    
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
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButtonsSound(y);
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButtonsMode(y);
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButtonsDifficulty(y);
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButtonsLevels(y);
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButtonsCollision(y);
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButtonsBack(y);
    }
    
    private void addButtonsMode(int y)
    {
        //add collision option
        this.modes = new ArrayList<Button>();
        
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Mode: " + Player.MODE_DESC_CASUAL);
        button.setX(MainScreen.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.modes.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Mode: " + Player.MODE_DESC_SURVIVAL);
        button.setX(MainScreen.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.modes.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Mode: " + Player.MODE_DESC_TIMED);
        button.setX(MainScreen.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.modes.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Mode: " + Player.MODE_DESC_CHALLENGE);
        button.setX(MainScreen.BUTTON_X);
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
            button.setX(MainScreen.BUTTON_X);
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
        this.back.setX(MainScreen.BUTTON_X);
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
        button.setX(MainScreen.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.difficulties.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Difficulty: " + Player.DIFFICULTY_DESC_HARD);
        button.setX(MainScreen.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.difficulties.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Difficulty: " + Player.DIFFICULTY_DESC_EASY);
        button.setX(MainScreen.BUTTON_X);
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
    }
    
    private void addButtonsSound(int y)
    {
        //add audio option
        this.sounds = new ArrayList<Button>();
        
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
                Audio.play(Assets.AudioMenuKey.Selection);
                
                //no need to continue
                return false;
            }
            
            for (Button button : modes)
            {
                if (button.contains(x, y))
                {
                    //increase mode selection
                    modeIndex++;
                    
                    if (modeIndex >= modes.size())
                        modeIndex = 0;
                    
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
                    levelIndex++;
                    
                    if (levelIndex >= levels.size())
                        levelIndex = 0;
                    
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
                    this.difficultyIndex++;
                    
                    //play sound effect
                    Audio.play(Assets.AudioMenuKey.Selection);
                    
                    //make sure index is within bounds
                    if (difficultyIndex >= difficulties.size())
                        difficultyIndex = 0;
                    
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
     * Get the mode index
     * @return The user selection for mode
     */
    public int getModeIndex()
    {
        return this.modeIndex;
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
     * Get the difficulty index
     * @return The user selection for difficulty
     */
    public int getDifficultyIndex()
    {
        return this.difficultyIndex;
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
        canvas.drawBitmap(logo, MainScreen.LOGO_X, MainScreen.LOGO_Y, null);
        
        //draw the menu buttons
        sounds.get(Audio.isAudioEnabled() ? 1 : 0).render(canvas, paint);
        collisions.get(collision ? 1 : 0).render(canvas, paint);
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