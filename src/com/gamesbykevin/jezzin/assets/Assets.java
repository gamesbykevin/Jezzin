package com.gamesbykevin.jezzin.assets;

import android.app.Activity;

import com.gamesbykevin.androidframework.resources.*;

/**
 * This class will contain all of our assets
 * @author GOD
 */
public class Assets 
{
    /**
     * The directory where audio sound effect resources are kept
     */
    private static final String DIRECTORY_MENU_AUDIO = "audio/menu";
    
    /**
     * The directory where audio sound effect resources are kept
     */
    private static final String DIRECTORY_GAME_AUDIO = "audio/game";
    
    /**
     * The directory where image resources are kept for the menu
     */
    private static final String DIRECTORY_MENU_IMAGE = "image/menu";
    
    /**
     * The directory where image resources are kept for the game
     */
    private static final String DIRECTORY_GAME_IMAGE_MAIN = "image/game/other";
    
    /**
     * The directory where image resources are kept for the backgrounds in the game
     */
    public static final String DIRECTORY_GAME_IMAGE_BACKGROUNDS = "image/game/backgrounds";
    
    /**
     * The directory where font resources are kept
     */
    private static final String DIRECTORY_MENU_FONT = "font/menu";
    
    /**
     * The directory where font resources are kept
     */
    private static final String DIRECTORY_GAME_FONT = "font/game";
    
    /**
     * The directory where our text files are kept
     */
    private static final String DIRECTORY_TEXT = "text";
    
    /**
     * The different fonts used in our game.<br>
     * Order these according to the file name in the "font" assets folder.
     */
    public enum FontMenuKey
    {
        Default
    }
    
    /**
     * The different fonts used in our game.<br>
     * Order these according to the file name in the "font" assets folder.
     */
    public enum FontGameKey
    {
        Default
    }
    
    /**
     * The different images for our menu items
     */
    public enum ImageMenuKey
    {
        Background,
        Button,
        Cancel,
        Confirm,
        Logo
    }
    
    
    /**
     * The different images in our game.<br>
     * Order these according to the file name in the "image" assets folder.
     */
    public enum ImageGameKey
    {
        Balls,
        Exit,
        Pause,
        Player,
        SoundOff,
        SoundOn
    }
    
    /**
     * The different images in our game.<br>
     * Order these according to the file name in the "image" assets folder.
     */
    public enum ImageGameBackgroundKey
    {
        Image1, Image2;/*, Image3, Image4, Image5, Image6, Image7, Image8, Image9, Image10,
        Image11, Image12, Image13, Image14, Image15, Image16, Image17, Image18, Image19, Image20,
        Image21, Image22, Image23, Image24, Image25, Image26, Image27, Image28, Image29, Image30,
        Image31, Image32, Image33, Image34, Image35, Image36, Image37, Image38, Image39, Image40,
        Image41, Image42, Image43, Image44, Image45, Image46, Image47, Image48, Image49, Image50,
        Image51, Image52, Image53, Image54, Image55, Image56, Image57, Image58, Image59, Image60,
        Image61, Image62, Image63, Image64, Image65, Image66, Image67, Image68, Image69, Image70,
        Image71, Image72, Image73, Image74, Image75;
        */
        private final String filename;
        
        private ImageGameBackgroundKey()
        {
            //store the filename
            this.filename = this.toString() + ".jpg";
        }
        
        public String getFilename()
        {
            return this.filename;
        }
    }
    
    /**
     * The key of each text file.<br>
     * Order these according to the file name in the "text" assets folder.
     */
    public enum TextKey
    {
        
    }
    
    /**
     * The key of each sound in our game.<br>
     * Order these according to the file name in the "audio" assets folder.
     */
    public enum AudioMenuKey
    {
        Selection
    }
    
    /**
     * The key of each sound in our game.<br>
     * Order these according to the file name in the "audio" assets folder.
     */
    public enum AudioGameKey
    {
        LoseLife, 
    }
    
    /**
     * Load the specified background image.<br>
     * Since there are many background images, we only load 1 at a time
     * @param activity Object containing AssetManager needed to load assets
     * @param key Unique key to access this image asset
     * @throws Exception 
     */
    public static final void loadBackgroundImage(final Activity activity, final ImageGameBackgroundKey key) throws Exception
    {
        Images.loadImage(activity, key, DIRECTORY_GAME_IMAGE_BACKGROUNDS);
    }
    
    /**
     * Load all assets.<br>
     * If an asset already exists, it won't be loaded again
     * @param activity Object containing AssetManager needed to load assets
     * @throws Exception 
     */
    public static final void load(final Activity activity) throws Exception
    {
        //load all images for the menu
        Images.load(activity, ImageMenuKey.values(), DIRECTORY_MENU_IMAGE, true);
        
        //load all fonts for the menu
        Font.load(activity, FontMenuKey.values(), DIRECTORY_MENU_FONT, true);
        
        //load all audio for the menu
        Audio.load(activity, AudioMenuKey.values(), DIRECTORY_MENU_AUDIO, true);
        
        //load images for the game
        Images.load(activity, ImageGameKey.values(), DIRECTORY_GAME_IMAGE_MAIN, true);
        
        //load all audio for the game
        Audio.load(activity, AudioGameKey.values(), DIRECTORY_GAME_AUDIO, true);
        
        //load all fonts for the game
        Font.load(activity, FontGameKey.values(), DIRECTORY_GAME_FONT, true);
        
        //load all text files
        Files.load(activity, TextKey.values(), DIRECTORY_TEXT, true);
    }
    
    /**
     * Recycle all assets
     */
    public static void recycle()
    {
        Images.dispose();
        Font.dispose();
        Audio.dispose();
        Files.dispose();
    }
}