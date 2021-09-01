/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Universe;

import Constants.World_Constants;
import Entities.Being;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author chris
 */
public class BigBang
{
    static World world;
    static VisibleWorld visibleWorld;
    
    public static void main(String[] args)
    {
       visibleWorld = new VisibleWorld();
       world = new World(visibleWorld);
       world.populateWithSpace();
       
       int startingPop = 20;
       Being being;
       for(int x=0;x<startingPop;x++)
       {
            world.registerExistence(new Being(ThreadLocalRandom.current().nextInt(1,World_Constants.WORLD_HEIGHT),ThreadLocalRandom.current().nextInt(1,World_Constants.WORLD_WIDTH),world));
       } 
    }
}