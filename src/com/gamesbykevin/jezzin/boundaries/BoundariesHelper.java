package com.gamesbykevin.jezzin.boundaries;

import android.graphics.Rect;
import com.gamesbykevin.jezzin.balls.Ball;

/**
 * Boundaries helper methods
 * @author GOD
 */
public final class BoundariesHelper 
{
    /**
     * Do we have ball collision with the current progress?<br>
     * This should be checked when creating our wall.
     * @param boundaries The boundaries container object
     * @return true = the line intersects with a ball, false otherwise
     */
    protected static boolean hasProgressCollision(final Boundaries boundaries)
    {
        //store information
        final double x = boundaries.getX();
        final double y = boundaries.getY();
        final double w = boundaries.getWidth();
        final double h = boundaries.getHeight();
        final double dx = boundaries.getDX();
        final double dy = boundaries.getDY();
        
        //check each ball
        for (Ball ball : boundaries.getGame().getBalls().getBalls())
        {
            //if the ball is located in another boundary, we can't have collision
            if (ball.getIndex() != boundaries.getIndex())
                continue;
            
            //get the radius of the ball, will use to detect collison
            final double radius = (ball.getWidth() / 2);
            
            //check collision differently depending on velocity
            if (dx != 0)
            {
                //if are y-coordinate is within the ball
                if (y >= ball.getY() - radius && y <= ball.getY() + radius || 
                    y + h >= ball.getY() - radius && y + h <= ball.getY() + radius)
                {
                    //check all x-coordinates for collision in our progress
                    for (int x1 = (int)x; x1 <= (int)(x + w); x1++)
                    {
                        //check all y-coordinates for collision in our progress
                        for (int y1 = (int)y; y1 <= (int)(y + h); y1++)
                        {
                            //if the distance is within the radius we have collision
                            if (ball.getDistance(x1, y1) <= radius)
                                return true;
                        }
                    }
                }
            }
            else if (dy != 0)
            {
                //make sure x-coordinate is within the ball
                if (x >= ball.getX() - radius && x <= ball.getX() + radius || 
                    x + w >= ball.getX() - radius && x + w <= ball.getX() + radius)
                {
                    //check all y-coordinates for collision in our progress
                    for (int y1 = (int)y; y1 <= (int)(y + h); y1++)
                    {
                        //check all x-coordinates for collision in our progress
                        for (int x1 = (int)x; x1 <= (int)(x + w); x1++)
                        {
                            //if the distance is within the radius we have collision
                            if (ball.getDistance(x1, y1) <= radius)
                                return true;
                        }
                    }
                }
            }
        }
        
        //no collision was found
        return false;
    }
    
    /**
     * Assign each ball to the current boundary it is located within
     * @param boundaries The boundaries container object
     */
    protected static void assignBoundary(final Boundaries boundaries)
    {
        //first we flag
        for (int i = 0; i < boundaries.getBoundaries().size(); i++)
        {
            boundaries.getBoundary(i).setSolid(true);
        }
        
        //assign the balls to their respected boundary
        for (Ball ball : boundaries.getGame().getBalls().getBalls())
        {
            //check each boundary
            for (int i = 0; i < boundaries.getBoundaries().size(); i++)
            {
                //get the current boundary
                Boundary boundary = boundaries.getBoundary(i);

                /**
                 * If the ball is inside the boundary, we assign it to this boundary
                 * We also make the boundary NOT solid
                 */
                if (boundary.contains((int)ball.getX(), (int)ball.getY()))
                {
                    //assign the boundary index
                    ball.setIndex(i);

                    //set flag false, because this can't be solid
                    boundary.setSolid(false);
                }
            }
        }
    }
    
    /**
     * Make sure the progress remains within the current assigned boundary
     * @param boundaries The boundaries container object
     */
    protected static void checkProgress(final Boundaries boundaries)
    {
        if (boundaries.getDX() != 0)
        {
            if (boundaries.getX() + boundaries.getWidth() > boundaries.getBoundary().getRect().right)
                boundaries.setWidth(boundaries.getBoundary().getRect().right - boundaries.getX());
            if (boundaries.getX() < boundaries.getBoundary().getRect().left)
                boundaries.setX(boundaries.getBoundary().getRect().left);
        }
        else if (boundaries.getDY() != 0)
        {
            if (boundaries.getY() + boundaries.getHeight() > boundaries.getBoundary().getRect().bottom)
                boundaries.setHeight(boundaries.getBoundary().getRect().bottom - boundaries.getY());
            if (boundaries.getY() < boundaries.getBoundary().getRect().top)
                boundaries.setY(boundaries.getBoundary().getRect().top);
        }
    }
    
    /**
     * Split the current assigned boundary into 2 smaller boundaries
     * @param boundaries The boundaries container object
     */
    protected static void splitBoundary(final Boundaries boundaries)
    {
        //get this temporary
        Rect tmp = boundaries.getBoundary().getRect();

        //remove the boundary from the list
        boundaries.getBoundaries().remove(boundaries.getIndex());

        //the velocity will determine how the boundary is split
        if (boundaries.getDX() != 0)
        {
            final int y = (int)(boundaries.getY() + (boundaries.getHeight() / 2));
            final int w = tmp.right - tmp.left;
            boundaries.getBoundaries().add(new Boundary(tmp.left, tmp.top, w, y - tmp.top));
            boundaries.getBoundaries().add(new Boundary(tmp.left, y, w, tmp.bottom - y));
        }
        else if (boundaries.getDY() != 0)
        {
            final int x = (int)(boundaries.getX() + (boundaries.getWidth() / 2));
            final int h = tmp.bottom - tmp.top;
            boundaries.getBoundaries().add(new Boundary(tmp.left, tmp.top, x - tmp.left, h));
            boundaries.getBoundaries().add(new Boundary(x, tmp.top, tmp.right - x, h));
        }
    }
}