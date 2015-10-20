package com.gamesbykevin.jezzin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import com.gamesbykevin.jezzin.assets.Assets;

/**
 * Our loading splash screen
 * @author GOD
 */
public class SplashScreen extends Activity implements Runnable
{
    //class reference
    public static SplashScreen SPLASH_SCREEN;
    
    /**
     * Time to sleep thread when loading assets
     */
    private static final long SLEEP_TIME = 100;
    
    //our main thread
    private Thread thread;
    
    //our progress bar to display to the user
    private ProgressDialog progressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        //turn the title off
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set the screen to full screen
        super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        //set the content view
        setContentView(R.layout.splash);
        
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
        //store splash screen reference
        SPLASH_SCREEN = this;
        
        //Create a new progress dialog.
        progressDialog = new ProgressDialog(SplashScreen.this);
        
        //Set the progress dialog to display a horizontal bar .
        getProgressDialog().setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        
        //Set the dialog title to 'Loading...'.
        getProgressDialog().setTitle("Loading Menu");
        
        //Set the dialog message 
        getProgressDialog().setMessage("In progress, please wait...");
        
        //This dialog can't be canceled by pressing the back key.
        getProgressDialog().setCancelable(false);
        
        //This dialog isn't indeterminate.
        getProgressDialog().setIndeterminate(false);
        
        //The maximum number of progress items.
        getProgressDialog().setMax(Assets.getMenuAssetsCount());
        
        //Set the current progress to zero.
        getProgressDialog().setProgress(0);
        
        //Display the progress dialog.
        getProgressDialog().show();

        //Initialize the thread
        thread = new Thread(this, "ProgressDialogThread");
        
        //start the thread
        thread.start();
    }
    
    public ProgressDialog getProgressDialog()
    {
        return progressDialog;
    }
    
    @Override
    public void run()
    {
        try
        {
            //are we done loading
            boolean complete = false;
            
            while (true)
            {
                //if we are complete, exit loop
                if (complete)
                    break;
                
                //load the assets and get the count
                final int count = Assets.loadMenuAssets(this);
                
                //we are complete if all assets are loaded
                complete = (count == Assets.getMenuAssetsCount());
                
                //update the progress
                getProgressDialog().setProgress(count);
                
                //sleep the thread for a short time
                thread.sleep(SLEEP_TIME);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //create our operation to be performed, which is to switch activities
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);

            //switch to our activity
            startActivity(intent);
        }
    }
}