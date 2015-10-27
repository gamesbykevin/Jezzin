package com.gamesbykevin.jezzin.storage.scorecard;

import android.app.Activity;

import com.gamesbykevin.androidframework.io.storage.Internal;

import com.gamesbykevin.jezzin.game.Game;
import com.gamesbykevin.jezzin.player.Player;
import com.gamesbykevin.jezzin.thread.MainThread;

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
                final int modeIndex = Integer.parseInt(data[0]);
                final int difficultyIndex = Integer.parseInt(data[1]);
                final int level = Integer.parseInt(data[2]);
                final long time = Long.parseLong(data[3]);

                //load the score to our list
                updateScore(modeIndex, difficultyIndex, level, time);
            }
        }
    }
    
    /**
     * Update the level with the specified score.<br>
     * If the specified level does not exist for the difficulty, it will be added
     * @param modeIndex The mode index
     * @param difficultyIndex The difficulty index
     * @param level The specified level
     * @param time The time duration
     * @return true if updating the score was successful, false otherwise
     */
    public boolean updateScore(final int modeIndex, final int difficultyIndex, final int level, final long time)
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
            if (tmp.getModeIndex() != modeIndex)
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
            scores.add(new Score(modeIndex, difficultyIndex, level, time));

            //save to internal storage
            this.save();

            //score was updated
            return true;
        }
        else
        {
            //score object exists, so we have to check if new record depending on game mode
            switch (modeIndex)
            {
                //casual, survival, challenge we want to shortest time
                case Player.MODE_INDEX_CASUAL:
                case Player.MODE_INDEX_SURVIVIAL:
                default:
                    
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

                //timed we want the most time left
                case Player.MODE_INDEX_TIMED:
                    
                    //if the time has more remaining, new record
                    if (time > score.getTime())
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
                
                //if we made it here, we have a new record
                case Player.MODE_INDEX_CHALLENGE:
                    
                    //set the time to beat, the previous record minus remaining time
                    score.setTime(score.getTime() - time);
                    
                    //save data
                    this.save();

                    //score was updated
                    return true;
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
            super.getContent().append(score.getModeIndex());
            super.getContent().append(SEPARATOR);
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
     * @return The score object for the current level, mode, difficulty, if not found return null
     */
    public Score getScore()
    {
        return getScore(
            game.getMainScreen().getScreenOptions().getModeIndex(), 
            game.getMainScreen().getScreenOptions().getDifficultyIndex(), 
            game.getPlayer().getLevel());
    }
    
    /**
     * Get the score object reference for the specified level
     * @param modeIndex The mode index
     * @param difficultyIndex The difficulty index
     * @param level The level we want the score for
     * @return The score of the specified level, if not found null is returned
     */
    public Score getScore(final int modeIndex, final int difficultyIndex, final int level)
    {
        for (Score score : scores)
        {
            //if the mode, difficulty, level match, return the score object
            if (
                score.getModeIndex() == modeIndex && 
                score.getDifficultyIndex() == difficultyIndex && 
                score.getLevel() == level
            )
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