/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Universe;

import Entities.Being;
import java.util.Timer;
import junit.framework.TestCase;

/**
 *
 * @author chris
 */
public class WorldTest extends TestCase {
    
    public WorldTest(String testName) {
        super(testName);
    }

    /**
     * Test of getSurroundingSpaces method, of class World.
     */
    public void testGetSurroundingSpaces() {
        System.out.println("getSurroundingSpaces");
        int range = 0;
        int ylocation = 0;
        int xlocation = 0;
        World instance = null;
        Space[][] expResult = null;
        Space[][] result = instance.getSurroundingSpaces(range, ylocation, xlocation);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSpacesInView method, of class World.
     */
    public void testGetSpacesInView() {
        System.out.println("getSpacesInView");
        int range = 0;
        int ylocation = 0;
        int xlocation = 0;
        int direction = 0;
        World instance = null;
        Space[][] expResult = null;
        Space[][] result = instance.getSpacesInView(range, ylocation, xlocation, direction);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of registerExistence method, of class World.
     */
    public void testRegisterExistence() {
        System.out.println("registerExistence");
        Being being = null;
        Timer timer = null;
        World instance = null;
        boolean expResult = false;
        boolean result = instance.registerExistence(being, timer);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeExistence method, of class World.
     */
    public void testRemoveExistence() {
        System.out.println("removeExistence");
        Being being = null;
        World instance = null;
        instance.removeExistence(being);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of populateWithSpace method, of class World.
     */
    public void testPopulateWithSpace() {
        System.out.println("populateWithSpace");
        World instance = null;
        instance.populateWithSpace();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of populateBeingView method, of class World.
     */
    public void testPopulateBeingView() {
        System.out.println("populateBeingView");
        Space[][] surroundings = null;
        World instance = null;
        instance.populateBeingView(surroundings);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of movePresence method, of class World.
     */
    public void testMovePresence() {
        System.out.println("movePresence");
        int yMod = 0;
        int xMod = 0;
        IWorldObject obj = null;
        World instance = null;
        boolean expResult = false;
        boolean result = instance.movePresence(yMod, xMod, obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of placeObjectInSpace method, of class World.
     */
    public void testPlaceObjectInSpace() {
        System.out.println("placeObjectInSpace");
        int ylocation = 0;
        int xlocation = 0;
        IWorldObject object = null;
        World instance = null;
        instance.placeObjectInSpace(ylocation, xlocation, object);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWorldPopulation method, of class World.
     */
    public void testGetWorldPopulation() {
        System.out.println("getWorldPopulation");
        World instance = null;
        int expResult = 0;
        int result = instance.getWorldPopulation();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stopThreads method, of class World.
     */
    public void testStopThreads() {
        System.out.println("stopThreads");
        World instance = null;
        instance.stopThreads();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of populate method, of class World.
     */
    public void testPopulate() {
        System.out.println("populate");
        int startingPop = 0;
        World instance = null;
        instance.populate(startingPop);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
