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
       visibleWorld.bind(world);
       world.populateWithSpace();
    }
    
    
}