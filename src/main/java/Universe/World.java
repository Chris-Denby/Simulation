/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Universe;

import Entities.Being;
import Constants.World_Constants;
import static Universe.BigBang.world;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author chris
 */
public class World
{    
    //arraylist to hold objects present in the world - beings only at present
    ArrayList<Being> allBeings = new ArrayList<>();
    ArrayList<Timer> allThreads = new ArrayList<>();

    //the GUI for the visable world layer
    VisibleWorld visibleWorld;  
    HashMap<String,Space> spaces = new HashMap<String,Space>();
    
    public World(VisibleWorld vw)
    {
        visibleWorld = vw;
    }
    
    public synchronized Space[][] getSurroundingSpaces(int range, int ylocation, int xlocation)
    {
        //create a matrix of beings nearby
        //2n+1 is the size of the matrix
        int dimension = (2*range) + 1;
        // = 5
        
        Space[][] localView = new Space[dimension][dimension];
        
        int ymodifier = range*-1;
        int xmodifier = range*-1;
        
        //increment row
        for(int r=0;r<dimension;r++)
        {
            //increment column
            for(int c=0;c<dimension;c++)
            {
                try
                {
                   localView[r][c] = spaces.get((ylocation + ymodifier)+","+(xlocation + xmodifier)); 
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                   localView[r][c] = null;
                }
                xmodifier = xmodifier + 1;
            }
            //move down 1 row
            ymodifier++;
            //restart x back to left hand column
            xmodifier = range*-1;  
        }
        return localView;        
    }
    
    public synchronized Space[][] getSpacesInView(int range, int ylocation, int xlocation, int direction)
    {
        //create a matrix of beings nearby
        //2n+1 is the size of the matrix
        int dimension = (2*range) + 1;
        Space[][] localView = new Space[dimension][dimension];
        //int ymodifier = range*-1;
        //int xmodifier = range*-1;
        int spacesInView = 0;
        
        int startRow;
        int endRow;
        int startColumn;
        int endColumn;
        
        //localView[][] based on range of 2
        //[0,0][0,1][0,2][0,3][0,4]
        //[1,0][1,1][1,2][1,3][1,4]
        //[2,0][2,1][2,2][2,3][2,4]
        //[3,0][3,1][3, 2][3,3][3,4]
        //[4,0][4,1][4,2][4,3][4,4]
        
        int x = 0;
        int y = 0;
        switch(direction)
        {
            case 315:
                //moving down right - FINISHED - **TO TEST**
                startRow = ylocation - range;
                endRow = ylocation + range;
                startColumn = xlocation + range;
                endColumn = xlocation + range;
                //to get diagonal cross section
                /**
                 *      []
                 *    [][]
                 *  ()[][]
                 *[][][][]
                [][][][][]
                **/
                //as we iterate through rows
                //modify loop to reduce the start column by 1 on each iteration 
                for(int r=startRow;r<=endRow;r++)
                {
                    x = range*2; //start populating localview at end column
                    for(int c=endColumn;c>=startColumn;c--)
                    {
                        try
                        { 
                            localView[y][x] = spaces.get(r+","+c);
                            spacesInView++;
                        }
                        catch(ArrayIndexOutOfBoundsException e)
                        {
                            localView[r][c] = null;
                        }
                       x--; 
                    }
                    startColumn--;
                    y++;
                }
            break;
            case 270: 
                //moving down only - FINSISHED - **TO TEST**
                /**
                [][]()[][]
                [][][][][]
                [][][][][]
                **/
                startRow = ylocation; //start at current row
                endRow = ylocation + range; //end at <range> rows down
                startColumn = xlocation - range;
                endColumn = xlocation + range;
                
                y=range; //start populating localview rows at middle row
                for(int r=startRow;r<=endRow;r++)
                {
                    x=0;
                    for(int c=startColumn;c<=endColumn;c++)
                    {
                        try
                        { 
                            localView[y][x] = spaces.get(r+","+c);
                            spacesInView++;
                        }
                        catch(ArrayIndexOutOfBoundsException e)
                        {
                            localView[r][c] = null;
                        }
                        x++; 
                    }
                    y++;
                }
            break;
            case 225:
                //moving down left - FINISHED - ** TO TEST **
                startRow = ylocation - range;
                endRow = ylocation + range;
                startColumn = xlocation - range;
                endColumn = xlocation - range;
                //to get diagonal cross section
                /**
                []
                [][]
                [][]()
                [][][][]
                [][][][][]
                **/
                //as we iterate through rows
                //modify loop to increase the end column by 1 on each iteration 
                for(int r=startRow;r<=endRow;r++)
                {
                    x = 0;
                    for(int c=startColumn;c<=endColumn;c++)
                    {
                        try
                        { 
                            localView[y][x] = spaces.get(r+","+c);
                            spacesInView++;
                        }
                        catch(ArrayIndexOutOfBoundsException e)
                        {
                            localView[r][c] = null;
                        }
                        x++; 
                    }
                    endColumn++;
                    y++;
                }                
            break;                    
            case 0:
                //moving right only - FINISHED - ** TO TEST **
                /**
                [][][]
                [][][]
                ()[][]
                [][][]
                [][][]
                **/
                startColumn = xlocation; //start at current column
                endColumn = xlocation + range; //end at <range> columns right
                startRow = ylocation - range; //start at <range> rows up
                endRow = ylocation + range; //end at <range> rows down
                
                x = range; //start populating at middle column
                for(int r=startRow;r<=endRow;r++)
                {
                    x = range;
                    for(int c=startColumn;c<=endColumn;c++)
                    {
                        try
                        { 
                            localView[y][x] = spaces.get(r+","+c);
                            spacesInView++;                            
                        }
                        catch(ArrayIndexOutOfBoundsException e)
                        {
                            localView[r][c] = null;
                        }
                        x++; 
                    }
                    y++;
                }
            break;                    
            case -1:
                //not moving at all
                //eyes closed?
                
            break;                    
            case 180:
                //moving left only - FINISHED - ** TO TEST **
                /**
                [][][]
                [][][]
                [][]()
                [][][]
                [][][]
                **/
                startColumn = xlocation - range; //start at <range> columns left
                endColumn = xlocation; //end at current column
                startRow = ylocation - range; //start at <range> rows up
                endRow = ylocation + range; //end at <range> rows down
                
                for(int r=startRow;r<=endRow;r++)
                {
                    x = 0;
                    for(int c=startColumn;c<=endColumn;c++)
                    {
                        try
                        { 
                            localView[y][x] = spaces.get(r+","+c);
                            spacesInView++;                            
                        }
                        catch(ArrayIndexOutOfBoundsException e)
                        {
                            localView[r][c] = null;
                        }
                        x++; 
                    }
                    y++;
                }
            break;                    
            case 45:
                //moving up right - FINSIHED - ** TO TEST **
                startRow = ylocation - range;
                endRow = ylocation + range;
                startColumn = xlocation - range;
                endColumn = xlocation + range;
                //to get diagonal cross section
                /**
                [][][][][]
                * [][][][]
                *   ()[][]
                *     [][]
                *       []
                **/
                //as we iterate through rows
                //modify loop to increase the start column by 1 on each iteration 
                int xmodifier = 0; //modifier to increment starting column populated in localview
                for(int r=startRow;r<=endRow;r++)
                {
                    x = 0 + xmodifier;
                    for(int c=startColumn;c<=endColumn;c++)
                    {
                        try
                        { 
                            localView[y][x] = spaces.get(r+","+c);
                            spacesInView++;                            
                        }
                        catch(ArrayIndexOutOfBoundsException e)
                        {
                            localView[r][c] = null;
                        }
                        x++;
                    }
                    startColumn++; //start at the next column on next iteration
                    y++;
                    xmodifier++;
                }
            break;
            case 90:
                //moving up only - FINISHED - ** TO TEST **
                /**
                [][][][][]
                [][][][][]
                [][]()[][]
                **/
                startRow = ylocation - range; //start at <range> rows up
                endRow = ylocation = ylocation; //end at current row
                startColumn = xlocation - range;
                endColumn = xlocation + range;
                
                for(int r=startRow;r<=endRow;r++)
                {
                    x=0;
                    for(int c=startColumn;c<=endColumn;c++)
                    {
                        try
                        { 
                            localView[y][x] = spaces.get(r+","+c);
                            spacesInView++;
                        }
                        catch(ArrayIndexOutOfBoundsException e)
                        {
                            localView[r][c] = null;
                        }
                        x++; 
                    }
                    y++;
                }
            break;
            case 135:
                //moving up left - FINISHED ** TO TEST **
                startRow = ylocation - range;
                endRow = ylocation + range;
                startColumn = xlocation - range;
                endColumn = xlocation + range;
                //to get diagonal cross section
                /**
                [][][][][]
                [][][][]
                [][]()
                [][]
                []
                **/
                //as we iterate through rows
                //modify loop to reduce the endColumn by 1 on each iteration 
                
                for(int r=startRow;r<=endRow;r++)
                {
                    x=0;
                    for(int c=startColumn;c<=endColumn;c++)
                    {
                        try
                        { 
                            localView[y][x] = spaces.get(r+","+c);
                            spacesInView++;
                        }
                        catch(ArrayIndexOutOfBoundsException e)
                        {
                            localView[r][c] = null;
                        }
                        x++; 
                    }
                    y++;
                    endColumn--;
                }
            break;                   
        } 
        //System.out.println("Spaces in view: " + spacesInView + "|| Direction: " + direction);
        return localView;        
    }

    public synchronized boolean registerExistence(Being being, Timer timer)
    {
        if(allBeings.size()<World_Constants.WORLD_MAX_POPULATION)
        {
            //System.out.println("pop not maxed");
            //if being has been given a location
            if(being.getXLocation()>-1 & being.getYLocation()>-1)
            {
                spaces.get(being.getYLocation()+","+being.getXLocation()).setObject(being);
                allBeings.add(being);
                allThreads.add(timer);
                visibleWorld.updatePopulationCount(allBeings.size());
            }
            else
            {
                //iterate through spaces to find an empty one
                /**
                Iterator iterator = spaces.entrySet().iterator();
                while(iterator.hasNext())
                {
                    //do only if space is not null and does not contain an inanimate object
                    Map.Entry keyValue = (Map.Entry) iterator.next();
                    Space space = (Space) keyValue.getValue();
                    if(space!=null & !(space.getObjectInSpace()instanceof Inanimate))
                    {
                        space.setObject(being);
                        visibleWorld.drawPresence(being.getYLocation(),being.getXLocation(),being);
                        allBeings.add(being);
                        visibleWorld.updatePopulationCount(allBeings.size());
                        return true;

                    }
                }
                **/
            }
            return true;
        }
        else
        {
            //System.out.println("POP MAXED!");
            return false;
        }
        
    }
    
    public synchronized void removeExistence(Being being)
    {
        try{
        //set currently occupied space as null and remove being from the list of all beings
        spaces.get(being.getYLocation()+","+being.getXLocation()).setObject(null);
        allBeings.remove(being);
        visibleWorld.updatePopulationCount(allBeings.size());
        }
        catch(NullPointerException ex){}
    }
      
    public void populateWithSpace()
    {
        //move through rows (height)
        for(int r=0;r<World_Constants.WORLD_HEIGHT;r++)
        {
            //move through columns (width)
            for(int c=0;c<World_Constants.WORLD_WIDTH;c++)
            {
                Space spaceBlock = new Space(r,c,"");
                spaces.put(r+","+c,spaceBlock);
                visibleWorld.drawSpace(r,c,spaceBlock);
            }  
        }  
    }
    
    public void populateBeingView(Space[][] surroundings)
    {
        int height = surroundings.length; //rows
        int width = surroundings[0].length; //columns
        
        //move through rows (height)
        for(int r=0;r<height;r++)
        {
            //move through columns (width)
            for(int c=0;c<width;c++)
            {
                visibleWorld.updateBeingView(r,c,surroundings[r][c]);
            }  
        } 
    }
        
    public synchronized boolean movePresence(int yMod, int xMod, IWorldObject obj)
    {
        try
        {
            //check if space to move to is empty
            if(spaces.get((obj.getYLocation()+yMod)+","+(obj.getXLocation()+xMod)).getObjectInSpace() == null)
            {
                //put object in new space
                spaces.get((obj.getYLocation()+yMod)+","+(obj.getXLocation()+xMod)).setObject(obj);
                
                
                //if object in old space is this object, remove from old space
                if(spaces.get(obj.getYLocation()+","+obj.getXLocation()).getObjectInSpace()== obj)
                {
                    spaces.get(obj.getYLocation()+","+obj.getXLocation()).setObject(null);
                }
                return true;
            }
            else
            {
                return false;
            }          
        }
        catch(Exception e)
        {
            //System.out.println("Move failed @ " + obj.getYLocation() + ","+obj.getXLocation());
            return false;
        }       
    }
    
    public void placeObjectInSpace(int ylocation, int xlocation, IWorldObject object)
    {
        spaces.get(ylocation+","+xlocation).setObject(new Inanimate(ylocation,xlocation));
    }
    
    public int getWorldPopulation()
    {
        return allBeings.size();
    }
    
    public synchronized void stopThreads()
    {
        allThreads.forEach((timer) ->
        timer.cancel());
    }
    
    public void populate(int startingPop)
    {
       Being being;
       for(int x=0;x<startingPop;x++)
       {    
            Being newBeing = new Being(ThreadLocalRandom.current().nextInt(1,World_Constants.WORLD_HEIGHT),ThreadLocalRandom.current().nextInt(1,World_Constants.WORLD_WIDTH),world);
            
            if(ThreadLocalRandom.current().nextInt(1,World_Constants.BIRTH_CHANCE_OF_VIRUS_MAX+1) < World_Constants.BIRTH_CHANCE_OF_VIRUS)
            {
                newBeing.setHasVirus(true);
            }
            world.registerExistence(newBeing,newBeing.getThread());
       } 
    }
}