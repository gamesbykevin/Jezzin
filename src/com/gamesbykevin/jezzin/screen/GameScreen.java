package com.gamesbykevin.jezzin.screen;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.jezzin.assets.Assets;
import com.gamesbykevin.jezzin.balls.Balls;
import com.gamesbykevin.jezzin.game.Game;
import com.gamesbykevin.jezzin.player.Player;

/**
 * The game screen that contains the game
 * @author GOD
 */
public class GameScreen implements Screen, Disposable
{
    //our object containing the main game functionality
    private Game game;
    
    //our main screen reference
    private final ScreenManager screen;
    
    //are we loading game assets? (image/audio/font etc...)
    private boolean loading = true;
    
    public GameScreen(final ScreenManager screen)
    {
        this.screen = screen;
    }
    
    protected Game getGame()
    {
        return this.game;
    }
    
    /**
     * Create game object
     * @throws Exception
     */
    public void createGame() throws Exception
    {
        if (getGame() == null)
            this.game = new Game(screen);
        
        //set the mode description
        switch (screen.getScreenOptions().getModeIndex())
        {
            case Player.MODE_INDEX_CASUAL:
            default:
                getGame().getPlayer().setModeDesc(Player.MODE_DESC_CASUAL);
                break;
                
            case Player.MODE_INDEX_SURVIVIAL:
                getGame().getPlayer().setModeDesc(Player.MODE_DESC_SURVIVAL);
                break;
                
            case Player.MODE_INDEX_TIMED:
                getGame().getPlayer().setModeDesc(Player.MODE_DESC_TIMED);
                break;
                
            case Player.MODE_INDEX_CHALLENGE:
                getGame().getPlayer().setModeDesc(Player.MODE_DESC_CHALLENGE);
                break;
        }
        
        //reset the game at the user specified level
        getGame().reset(screen.getScreenOptions().getLevelIndex() + 1);
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        //anything need to be reset here
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        if (getGame() != null)
            getGame().updateMotionEvent(event, x, y);
        
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        if (getGame() != null)
            getGame().update();
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //render game if exists
        if (getGame() != null)
            getGame().render(canvas);
    }
    
    @Override
    public void dispose()
    {
        if (game != null)
        {
            game.dispose();
            game = null;
        }
    }
}