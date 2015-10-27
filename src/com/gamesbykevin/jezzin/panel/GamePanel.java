package com.gamesbykevin.jezzin.panel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;

import com.gamesbykevin.jezzin.MainActivity;
import com.gamesbykevin.jezzin.R;
import com.gamesbykevin.jezzin.assets.Assets;
import com.gamesbykevin.jezzin.screen.ScreenManager;
import com.gamesbykevin.jezzin.thread.MainThread;

import java.util.Random;

/**
 * Game Panel class
 * @author GOD
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, Disposable
{
    /**
     * Our random object used to make random decisions
     */
    public static Random RANDOM = new Random(System.nanoTime());
    
    //default dimensions of window for this game
    public static final int WIDTH = 480;
    public static final int HEIGHT = 800;
    
    //the reference to our activity
    private final MainActivity activity;
    
    //the object containing our game screens
    private ScreenManager screen;
    
    //our main game thread
    private MainThread thread;
    
    //splash loading screen
    private Bitmap splash;
    
    //did we notify the user of loading
    private boolean notify = false;
    
    //the source and destination used to render the splash image
    private Rect source, destination;
    
    /**
     * Create a new game panel
     * @param activity Our main activity reference
     */
    public GamePanel(final MainActivity activity)
    {
        //call to parent constructor
        super(activity);
        
        //store context
        this.activity = activity;
            
        //make game panel focusable = true so it can handle events
        super.setFocusable(true);
        
        //load splash image to be displayed to the user
        splash = BitmapFactory.decodeResource(activity.getResources(), R.drawable.splash);
    }
    
    @Override
    public void dispose()
    {
        //it could take several attempts to stop the thread
        boolean retry = true;
        
        //count number of attempts to complete thread
        int count = 0;
        
        while (retry && count <= MainThread.COMPLETE_THREAD_ATTEMPTS)
        {
            try
            {
                //increase count
                count++;
                
                if (thread != null)
                {
                    //set running false, to stop the infinite loop
                    thread.setRunning(false);

                    //wait for thread to finish
                    thread.join();
                }
                
                //if we made it here, we were successful
                retry = false;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        //make thread null
        this.thread = null;
        
        //assign null
        RANDOM = null;
        
        if (screen != null)
        {
            screen.dispose();
            screen = null;
        }
        
        if (splash != null)
            splash = null;
        
        //recycle asset objects
        Assets.recycle();
    }
    
    /**
     * Get the activity
     * @return The activity reference
     */
    public final MainActivity getActivity()
    {
        return this.activity;
    }
    
    /**
     * Now that the surface has been created we can create our game objects
     * @param holder Object used to track events
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        try
        {
            //flag assets as not loaded
            Assets.LOADED = false;
            
            //flag we did not notify
            notify = false;
            
            //create if null
            if (RANDOM == null)
                RANDOM = new Random(System.nanoTime());
            
            //if the thread does not exist, create it
            if (this.thread == null)
                this.thread = new MainThread(getHolder(), this);

            //if the thread hasn't been started yet
            if (!this.thread.isRunning())
            {
                //start the thread
                this.thread.setRunning(true);
                this.thread.start();
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent event)
    {
        try
        {
            if (this.screen != null)
            {
                //calculate the coordinate offset
                final float scaleFactorX = (float)WIDTH / getWidth();
                final float scaleFactorY = (float)HEIGHT / getHeight();

                //adjust the coordinates
                final float x = event.getRawX() * scaleFactorX;
                final float y = event.getRawY() * scaleFactorY;

                //update the events
                return this.screen.update(event, x, y);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return super.onTouchEvent(event);
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        //flag assets as not loaded
        Assets.LOADED = false;
        
        //flag we did not notify
        notify = false;
        
        //pause the game
        if (screen != null)
        {
            //stop all audio while paused
            Audio.stop();
            
            //set the state
            screen.setState(ScreenManager.State.Paused);
        }
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        //does anything need to be done here?
    }
    
    /**
     * Update the game state
     */
    public void update()
    {
        try
        {
            if (notify)
            {
                //make sure the screen is created first before the thread starts
                if (this.screen == null)
                {
                    if (!Assets.LOADED)
                    {
                        //load all assets
                        Assets.load(getActivity());

                        //flag assets loaded
                        Assets.LOADED = true;
                    }
                    else
                    {
                        this.screen = new ScreenManager(this);
                    }
                }
                else
                {
                    screen.update();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onDraw(Canvas canvas)
    {
        if (canvas != null)
        {
            //store the canvas state
            final int savedState = canvas.save();
            
            try
            {
                final float scaleFactorX = getWidth() / (float)GamePanel.WIDTH;
                final float scaleFactorY = getHeight() / (float)GamePanel.HEIGHT;

                //scale to the screen size
                canvas.scale(scaleFactorX, scaleFactorY);
                
                //render splash image
                if (splash != null)
                {
                    if (screen == null)
                    {
                        if (source == null)
                            source = new Rect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
                        if (destination == null)
                            destination = new Rect(0, 0, getWidth(), getHeight());
                        
                        //render image
                        canvas.drawBitmap(splash, source, destination, null);
                    }

                    //flag notify
                    notify = true;
                }
                
                //make sure the screen object exists
                if (screen != null)
                {
                    //render the main sreen containing the game and other screens
                    screen.render(canvas);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
            //restore previous canvas state
            canvas.restoreToCount(savedState);
        }
    }
}