package com.gamesbykevin.jezzin.storage.scorecard;

/**
 * The score for a level
 * @author GOD
 */
public final class Score 
{
    //the level this score is for
    private int level;
    
    //the time this score is for
    private long time;
    
    //the difficulty this score is for
    private final int difficultyIndex;
    
    //the mode this score is for
    private final int modeIndex;
    
    protected Score(final int modeIndex, final int difficultyIndex, final int level, final long time)
    {
        this.modeIndex = modeIndex;
        this.difficultyIndex = difficultyIndex;
        
        setLevel(level);
        setTime(time);
    }
    
    /**
     * Get the mode index
     * @return The mode this score is for
     */
    public int getModeIndex()
    {
        return this.modeIndex;
    }
    
    /**
     * Get the difficulty index
     * @return The difficulty this score is for
     */
    public int getDifficultyIndex()
    {
        return this.difficultyIndex;
    }
    
    /**
     * Get the level
     * @return The level for this score
     */
    public int getLevel()
    {
        return this.level;
    }
    
    /**
     * Assign the level
     * @param level The level for this score
     */
    public void setLevel(final int level)
    {
        this.level = level;
    }
    
    /**
     * Get the time
     * @return The time to complete the level
     */
    public long getTime()
    {
        return this.time;
    }
    
    /**
     * Set the time
     * @param time The time to complete the level
     */
    public void setTime(final long time)
    {
        this.time = time;
    }
}