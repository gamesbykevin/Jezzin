package com.gamesbykevin.jezzin.storage.settings;

import android.app.Activity;
import com.gamesbykevin.androidframework.io.storage.Internal;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.jezzin.screen.OptionsScreen;

/**
 * Save the settings to the internal storage
 * @author GOD
 */
public final class Settings extends Internal
{
    //our options screen reference object
    private final OptionsScreen screen;
    
    /**
     * This string will separate each setting
     */
    private static final String SEPARATOR = ";";
    
    public Settings(final OptionsScreen screen, final Activity activity)
    {
        super("Settings", activity);
        
        //store our screen reference object
        this.screen = screen;
        
        //if content exists load it
        if (super.getContent().toString().trim().length() > 0)
        {
            try
            {
                //split the content into an array
                final String[] data = super.getContent().toString().split(SEPARATOR);

                //parse settings
                final int difficultyIndex = Integer.parseInt(data[0]);
                final int modeIndex = Integer.parseInt(data[1]);
                final int levelIndex = Integer.parseInt(data[2]);
                final boolean audioEnabled = Boolean.parseBoolean(data[3]);
                final boolean collisionEnabled = Boolean.parseBoolean(data[4]);

                //assign settings
                screen.setDifficultyIndex(difficultyIndex);
                screen.setModeIndex(modeIndex);
                screen.setLevelIndex(levelIndex);
                screen.setCollision(collisionEnabled);
                Audio.setAudioEnabled(audioEnabled);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Save the settings to the internal storage
     */
    @Override
    public void save()
    {
        try
        {
            //remove all existing content
            super.getContent().delete(0, super.getContent().length());

            //option settings
            final int difficultyIndex = screen.getDifficultyIndex();
            final int modeIndex = screen.getModeIndex();
            final int levelIndex = screen.getLevelIndex();
            final boolean audioEnabled = Audio.isAudioEnabled();
            final boolean collisionEnabled = screen.hasCollision();

            //add the settings to the string builder
            super.getContent().append(difficultyIndex);
            super.getContent().append(SEPARATOR);
            super.getContent().append(modeIndex);
            super.getContent().append(SEPARATOR);
            super.getContent().append(levelIndex);
            super.getContent().append(SEPARATOR);
            super.getContent().append(audioEnabled);
            super.getContent().append(SEPARATOR);
            super.getContent().append(collisionEnabled);

            //save data
            super.save();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
}