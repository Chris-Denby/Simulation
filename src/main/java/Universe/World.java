/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Universe;

import Entities.Being;
import Constants.World_Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author chris
 */
public class World
{
    //arraylist to hold objects present in the world - beings only at present
    ArrayList<Being> allBeings = new ArrayList<>();

    //the GUI for the visable world layer
    VisibleWorld visibleWorld;  
    HashMap<String,Space> spaces = new HashMap<String,Space>();
    
    public World(VisibleWorld vw)
    {
        visibleWorld = vw;
    }
    
    public Space[][] getSurroundingSpaces(int range, int ylocation, int xlocation)
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

    public boolean registerExistence(Being being)
    {
        //if being has been given a location
        if(being.getXLocation()>-1 & being.getYLocation()>-1)
        {
            spaces.get(being.getYLocation()+","+being.getXLocation()).setObject(being);
            allBeings.add(being);
            visibleWorld.updatePopulationCount(allBeings.size());
            return true;
        }
        else
        {
            //iterate through spaces to find an empty one
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
        }
        return false;
    }
    
    public boolean removeExistence(Being being)
    {
        
        if(spaces.get(being.getYLocation()+","+being.getXLocation()).setObject(null))
        {
            allBeings.remove(being);
            visibleWorld.updatePopulationCount(allBeings.size());
            return true;   
        }

        else
            return false;

    }
        
    public void populateWithSpace()
    {
        int cell = 0;
        //move through rows (height)
        for(int r=0;r<World_Constants.WORLD_HEIGHT;r++)
        {
            //move through columns (width)
            for(int c=0;c<World_Constants.WORLD_WIDTH;c++)
            {
                Space spaceBlock = new Space(r,c,"");
                spaces.put(r+","+c,spaceBlock);
                visibleWorld.drawSpace(r,c,spaceBlock);
                cell++;
            }  
        } 
        System.out.println(cell+" spaces created");
        
        //ADD INANIMATE OBJECTS
        placeObjectInSpace(25,24,new Inanimate(25,24));
        placeObjectInSpace(25,25,new Inanimate(25,25));
        placeObjectInSpace(25,26,new Inanimate(25,46));
        placeObjectInSpace(24,25,new Inanimate(24,25));
        placeObjectInSpace(26,25,new Inanimate(26,25));
 
    }
        
    public boolean movePresence(int yMod, int xMod, IWorldObject obj)
    {
        try
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
        catch(Exception e)
        {
            return false;
        }       
    }
    
    public void placeObjectInSpace(int ylocation, int xlocation, IWorldObject object)
    {
        spaces.get(ylocation+","+xlocation).setObject(new Inanimate(ylocation,xlocation));
    }
}