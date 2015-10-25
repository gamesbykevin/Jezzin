package com.gamesbykevin.jezzin.background;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.jezzin.assets.Assets;
import java.util.ArrayList;
import java.util.List;

import com.gamesbykevin.jezzin.assets.Assets.ImageGameBackgroundKey;
import com.gamesbykevin.jezzin.boundaries.Boundaries;
import com.gamesbykevin.jezzin.game.Game;
import com.gamesbykevin.jezzin.panel.GamePanel;

/**
 * This will be the hidden background for a given level
 * @author GOD
 */
public final class Background implements IBackground
{
    //list of optional backgrounds
    private List<ImageGameBackgroundKey> options;
    
    //store the previous image reference
    private ImageGameBackgroundKey previous = null;
    
    //our game reference
    private final Game game;
    
    //the source coordinates of the image
    private Rect source;
    
    public Background(final Game game)
    {
        //our game reference
        this.game = game;
        
        //create new list
        this.options = new ArrayList<ImageGameBackgroundKey>();
        
        //create source coordinate rectangle
        this.source = new Rect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
    }
    
    @Override
    public void reset() throws Exception
    {
        /**
         * If there is a previous reference we will recycle it
         */
        if (previous != null)
        {
            //make sure the resource is available before we recycle it
            if (Images.getImage(previous) != null)
                Images.getImage(previous).recycle();
        }
        
        //if the list is empty, fill it
        if (options.isEmpty())
        {
            //add all backgrounds as options
            for (ImageGameBackgroundKey key : ImageGameBackgroundKey.values())
            {
                options.add(key);
            }
        }
        
        //pick a random background
        final int index = GamePanel.RANDOM.nextInt(options.size());
        
        //store the reference
        previous = ImageGameBackgroundKey.values()[index];
        
        //remove that option from the list
        options.remove(index);
        
        //load the asset
        Images.loadImage(
            game.getMainScreen().getPanel().getActivity(), 
            previous, 
            Assets.DIRECTORY_GAME_IMAGE_BACKGROUNDS + "/" + previous.getFilename()
        );
    }
    
    @Override
    public void dispose()
    {
        previous = null;
        
        if (options != null)
        {
            options.clear();
            options = null;
        }
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //makes sure image exists before rendering
        if (previous != null && Images.getImage(previous) != null)
        {
            //make sure object hasn't been recycled before rendering
            if (!Images.getImage(previous).isRecycled())
                canvas.drawBitmap(Images.getImage(previous), source, Boundaries.DEFAULT_BOUNDS, null);
        }
    }
}