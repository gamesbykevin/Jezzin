package com.gamesbykevin.jezzin.balls;

import com.gamesbykevin.jezzin.boundaries.Boundary;

import java.util.List;

/**
 * Ball helper methods
 * @author GOD
 */
public final class BallsHelper 
{
    /**
     * Get the ball that is in collision with the specified ball.<br>
     * The ball won't have collision with another ball if they are assigned a different boundary index
     * @param ball The ball we want to check if it has collision
     * @param balls The list of balls we need to check
     * @return The ball that has collision, if none found null will be returned
     */
    protected static Ball getCollisionBall(final Ball ball, final List<Ball> balls)
    {
        for (Ball tmp : balls)
        {
            //don't check self
            if (ball.hasId(tmp))
                continue;
            
            //don't check balls in a different boundary
            if (ball.getIndex() != tmp.getIndex())
                continue;
            
            //if the ball is close enough, we have collision
            if (ball.getDistance(tmp) < ball.getWidth())
                return tmp;
        }
        
        //none in collision, return null
        return null;
    }
    
    
    /**
     * Check for ball collision
     * @param ball The current ball we want to check for collision
     * @param balls The List of balls in play
     */
    protected static void checkBallCollision(final Ball ball, final List<Ball> balls)
    {
        //check if there is another ball that has collision
        Ball tmp = getCollisionBall(ball, balls);

        //if ball exists, there was collision
        if (tmp != null)
        {
            //store velocity
            final double dx1 = ball.getDX();
            final double dy1 = ball.getDY();
            final double dx2 = tmp.getDX();
            final double dy2 = tmp.getDY();

            //switch velocity
            ball.setDX(dx2);
            ball.setDY(dy2);
            tmp.setDX(dx1);
            tmp.setDY(dy1);

            //move the balls
            ball.setX(ball.getX() + ball.getDX());
            ball.setY(ball.getY() + ball.getDY());
            tmp.setX(tmp.getX() + tmp.getDX());
            tmp.setY(tmp.getY() + tmp.getDY());
        }
    }
    
    /**
     * Manage the balls velocity.<br>
     * Here we will make sure the balls stay within their assigned boundary
     * @param ball The ball we want to check
     * @param boundary The boundary containing the ball
     */
    protected static void checkBallVelocity(final Ball ball, final Boundary boundary)
    {
        //calculate half the dimension
        final double h = ball.getHeight() / 2;
        final double w = ball.getWidth() / 2;
        
        //manage x-velocity
        if (ball.getDX() < 0)
        {
            if (ball.getX() < boundary.getRect().left + w)
            {
                //flip velocity
                ball.setDX(-ball.getDX());

                //adjust coordinates
                ball.setX(boundary.getRect().left + w);
            }
        }
        else if (ball.getDX() > 0)
        {
            if (ball.getX() > boundary.getRect().right - w)
            {
                //flip velocity
                ball.setDX(-ball.getDX());

                //adjust coordinates
                ball.setX(boundary.getRect().right - w);
            }
        }

        //manage y-velocity
        if (ball.getDY() < 0)
        {
            if (ball.getY() < boundary.getRect().top + h)
            {
                //flip velocity
                ball.setDY(-ball.getDY());

                //adjust coordinates
                ball.setY(boundary.getRect().top + h);
            }
        }
        else if (ball.getDY() > 0)
        {
            if (ball.getY() > boundary.getRect().bottom - h)
            {
                //flip velocity
                ball.setDY(-ball.getDY());

                //adjust coordinates
                ball.setY(boundary.getRect().bottom - h);
            }
        }
    }
}