package com.gamesbykevin.jezzin.storage.scorecard;

import android.app.Activity;

import com.gamesbykevin.androidframework.io.storage.Internal;

import com.gamesbykevin.jezzin.game.Game;
import com.gamesbykevin.jezzin.screen.OptionsScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Here we will track the best time and save it to the internal storage
 * @author GOD
 */
public final class ScoreCard extends Internal
{
    //list of scores
    private List<Score> scores;
    
    /**
     * New level separator string
     */
    private static final String NEW_LEVEL = ";";
    
    /**
     * This string will separate the level from the time
     */
    private static final String SEPARATOR = "-";
    
    //our game reference object
    private final Game game;
    
    public ScoreCard(final Game game, final Activity activity)
    {
        super("ScoreCard", activity);
        
        //store our game reference object
        this.game = game;
        
        //create new score
        this.scores = new ArrayList<Score>();
        
        //make sure content exists before we try to load it
        if (super.getContent().toString().trim().length() > 0)
        {
            //load file with each level on a new line
            final String[] levels = super.getContent().toString().split(NEW_LEVEL);

            //load each level into our array
            for (int index = 0; index < levels.length; index++)
            {
                //split level data
                String[] data = levels[index].split(SEPARATOR);

                //get the information
                final int difficultyIndex = Integer.parseInt(data[0]);
                final int level = Integer.parseInt(data[1]);
                final long time = Long.parseLong(data[2]);

                //load the score to our list
                updateScore(difficultyIndex, level, time);
            }
        }
    }
    
    /**
     * Update the level with the specified score.<br>
     * If the specified level does not exist for the difficulty, it will be added
     * @param difficultyIndex The difficulty index
     * @param level The specified level
     * @param time The time duration
     * @return true if updating the score was successful, false otherwise
     */
    public boolean updateScore(final int difficultyIndex, final int level, final long time)
    {
        //our score object reference
        Score score = null;
        
        //check each score in our list
        for (Score tmp : scores)
        {
            //all have to match to locate our object
            if (tmp.getLevel() != level)
                continue;
            if (tmp.getDifficultyIndex() != difficultyIndex)
                continue;
            
            //store our reference
            score = tmp;

            //we found our object exit loop
            break;
        }
        
        //if our score object does not exist, this will be a new record
        if (score == null)
        {
            //score was not found, so add it
            scores.add(new Score(difficultyIndex, level, time));

            //save to internal storage
            this.save();

            //score was updated
            return true;
        }
        else
        {
            //if the time is less, new record
            if (time < score.getTime())
            {
                //update record
                score.setTime(time);
                
                //save data
                this.save();
                
                //score was updated
                return true;
            }
            else
            {
                //score was not updated
                return false;
            }
        }
    }
    
    /**
     * Save the scores to the internal storage
     */
    @Override
    public void save()
    {
        //remove all existing content
        super.getContent().delete(0, super.getContent().length());
        
        for (Score score : scores)
        {
            //if content exists, add new line, to separate each level
            if (super.getContent().length() > 0)
                super.getContent().append(NEW_LEVEL);
            
            //write difficulty, level, time
            super.getContent().append(score.getDifficultyIndex());
            super.getContent().append(SEPARATOR);
            super.getContent().append(score.getLevel());
            super.getContent().append(SEPARATOR);
            super.getContent().append(score.getTime());
        }
        
        //save the content to physical internal storage location
        super.save();
    }
    
    /**
     * Get our score reference object
     * @return The score object for the current level and difficulty, if not found return null
     */
    public Score getScore()
    {
        return getScore(
            game.getScreen().getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_DIFFICULTY), 
            game.getPlayer().getLevel());
    }
    
    /**
     * Get the score object reference for the specified level and difficulty
     * @param difficultyIndex The difficulty index
     * @param level The level we want the score for
     * @return The score of the specified level, if not found null is returned
     */
    public Score getScore(final int difficultyIndex, final int level)
    {
        for (Score score : scores)
        {
            //if the difficulty and level match, return the score object
            if (score.getDifficultyIndex() == difficultyIndex && score.getLevel() == level)
                return score;
        }
        
        return null;
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        if (scores != null)
        {
            scores.clear();
            scores = null;
        }
    }
}