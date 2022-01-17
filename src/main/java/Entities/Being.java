/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Tools.Graph;
import java.util.ArrayList;
import Constants.World_Constants;
import Tools.Vertice;
import Universe.IWorldObject;
import Universe.Resource;
import Universe.Space;
import Universe.World;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author chris
 */
public class Being implements IWorldObject, Comparable
{
    private boolean isFocussed = false;
    private Object data;
    private int age;
    private int gender; // 0 = female, 1 = males
    private int yMove;
    private int xMove;
    private double lifeRemaining = World_Constants.BEING_LIFE;
    private double ageRateModifier = 1;
    private Timer metabolicTimer = new Timer();
    private World world;
    private int ylocation = -1;
    private int xlocation = -1;
    private int direction; //integer of 360 degree direction
    private String name;
    private Being parentA;
    private Being parentB;
    private Being partner;
    private ArrayList<Being> children = new ArrayList<Being>();
    private ArrayList<Being> Siblings = new ArrayList<Being>();
    private Graph socialNetwork = new Graph();
    private ArrayList<Trait> traits = new ArrayList<Trait>();
    private ArrayList<Motivation> motivations = new ArrayList<Motivation>();
    private Vertice identity = new Vertice(this,0);
    private Queue<Space> pastMoves = new LinkedList<Space>();
    private HashMap<Motivation,ArrayList> pathMemories = new HashMap<Motivation,ArrayList>();
    private boolean hasVirus = false;
    private boolean hasImmunity = true;
    Space[][] spacialMemory = new Space[(2*World_Constants.BEING_SIGHT_RANGE)+1][(2*World_Constants.BEING_SIGHT_RANGE)+1];
    
    
    
    private int happiness = 0;
    private int energy = 0;
    
    private int consumeHappinessModifier;
    private int socialiseHappinessModifier;
    private int breedHappinessModifier;
    
    //FEATURES TO IMPLEMENT:
    //
    //being keeps memory map of the vector space viewed
    //being is aware of its relative position in vector space
    //when a being is selected its memory map is viewable in the UI
    //being is able to chose to go to desirable locations using it smemory map
    
    //function to optimise increase of happiness
    //being predicts actions to take to increase happiness
    
    
    public Being(int yloc, int xloc, Being parentA, Being ParentB, World w)
    {
        gender = ThreadLocalRandom.current().nextInt(0,1+1); //assign gender randomly
        world = w;
        socialNetwork.addVertice(this,identity);
        motivations.add(Motivation.SOCIALISE);
        motivations.add(Motivation.BREED);
        ylocation = yloc;
        xlocation = xloc;
        born();
    }
        
    public Being(int yloc, int xloc, World w)
    {
        gender = ThreadLocalRandom.current().nextInt(0,1+1); //assign gender randomly
        world = w;
        socialNetwork.addVertice(this,identity);
        motivations.add(Motivation.SOCIALISE);
        motivations.add(Motivation.BREED);
        ylocation = yloc;
        xlocation = xloc;
        born();
    }
    
    public Being(World w)
    {
        gender = ThreadLocalRandom.current().nextInt(0,1+1); //assign gender randomly
        world = w;
        socialNetwork.addVertice(this,identity);
        motivations.add(Motivation.SOCIALISE);
        motivations.add(Motivation.BREED);
        born();
    }
    
    public void setFocussed(Boolean is)
    {
        isFocussed = is;
    }
    
    public boolean isFocussed()
    {
        return isFocussed;
    }
    
    public void maximiseHappiness()
    {
        //TO DO
        //predictive training model to determine what moves maximise happiness
        
    }
    
    public void consume(Resource resource)
    {
        //TO DO
        //being consumes element for use to:
        
        //gain energy
        energy = energy + resource.getEnergy();
        
        //increase happiness
        //happiness = 1 * consumeHappinessModifier;
    }
    
    public int getGender()
    {
        return gender;
    }
    
    public boolean move(int yMod, int xMod)
    {   
        if(yMod !=0 || xMod !=0)
        {
            if(world.movePresence(yMod, xMod, this))
            {
                //add current space to the end of the past moves queue
                pastMoves.offer(new Space(ylocation,xlocation));
                
                //update being location
                ylocation = ylocation+yMod;
                xlocation = xlocation+xMod;
                return true;
            }
            else
                return false;
        }
        return false;
    }
        
    public void reproduce(Being parentB)
    {
        //if this being has less children than the world limit
        if(children.size()<World_Constants.BEING_CHILD_LIMIT)
        {
            if(gender==0)
            {
                
                TimerTask giveBirth = new TimerTask() {
                @Override
                public void run() 
                {
                    giveBirth(parentB);
                    }
                };
                //start Timer
                metabolicTimer.schedule(giveBirth,World_Constants.BEING_METABOLIC_RATE*World_Constants.BIRTH_AGE);
                
            }
        }
        if(children.size() == World_Constants.BEING_CHILD_LIMIT)
        {
            //if max children have been made, remove the motivation to breed
            //motivations.remove(Motivation.BREED);
        }
    }
    
    public Timer getThread()
    {
        return metabolicTimer;
    }
    
    public void giveBirth(Being parentB)
    {
        Being child = new Being(ylocation-1, xlocation-1, parentA, parentB, world);
        //registerExistence method checks if world pop is full
        //if not full, add the child
        if(world.registerExistence(child, metabolicTimer))
        {
            this.addChild(child);
            parentB.addChild(child);
        }      
        else
        {
            //System.out.println();
            child.die();
        }
    }
    
    public Being getParentA()
    {
        return parentA;
    }
    
    public Being getParentB()
    {
        return parentB;
    }
    
    public void addChild(Being child)
    {
        if(children == null)
        {
           children = new ArrayList<Being>(); 
        }
        children.add(child);
        socialise(child,1,1);
    }
    
    public void born()
    {   
        
        /**
        if(ThreadLocalRandom.current().nextInt(1,World_Constants.BIRTH_CHANCE_OF_VIRUS_MAX+1) < World_Constants.BIRTH_CHANCE_OF_VIRUS)
        {
            setHasVirus(true);
        }
        * **/
        //create task to be repeated by Timer
        TimerTask live = new TimerTask() {
            @Override
            public void run() 
            {
                live();
                //System.out.println("Breath");
            }
        };
        
        //start Timer
        metabolicTimer.scheduleAtFixedRate(live, World_Constants.BEING_METABOLIC_RATE, World_Constants.BEING_METABOLIC_RATE);
    }
    
    public void live()
    {
        lifeRemaining = lifeRemaining - (World_Constants.BEING_AGE_RATE * ageRateModifier);
        age = age + World_Constants.BEING_AGE_RATE;
        if(lifeRemaining > 0)
        {
            this.traverseWorld();
        }
        else
        {
            die();
        }
    }
    
    public void die()
    {
        metabolicTimer.cancel();
        world.removeExistence(this);
        //update world to remove being
        //being still exists in social networks
    } 
    
    public void socialise(Being otherBeing, int mag, int degree)
    {   
        if(otherBeing != null)  
        {
            //if other being is not already in this beings social network
            if(!socialNetwork.checkIfNodeExists(otherBeing) & socialNetwork.getSize()<=World_Constants.DUNBARS_NUMBER)
            {
                //creates a vertice with reference of beings thread reference
                Vertice otherIdentity = new Vertice(otherBeing,mag);
                //add other being to this beings social network
                socialNetwork.addVertice(otherBeing, otherIdentity);
                //add edge between this being and the other
                socialNetwork.AddEdge(identity, otherIdentity, degree);
                //System.out.println("Added to social network");

            }
            //if other being is already in this beings social network
            else if(socialNetwork.checkIfNodeExists(otherBeing))
            {
                //increase the value of the node by 1
                socialNetwork.getVertice(otherBeing.toString()).increaseValue();
                //increase the weight of the connection by 1
                //socialNetwork.getEdge(this, otherBeing).increaseWeight();
                //System.out.println("I already know you " + otherBeing.toString());
            }
        }
    }
    
    public void traverseWorld()
    {          
        
        //BEGIN WITH DEFAULT RANDOM MOVE DIRECTION
        yMove = ThreadLocalRandom.current().nextInt(-1,1+1);
        xMove = ThreadLocalRandom.current().nextInt(-1,1+1);
        
        direction = getMoveDirection(yMove, xMove);        

        //array width & height = (2*range)+1
        //but because index starts at 0, dimensions = 2* range        
        
        //if something is above
        //y<currentY
        //x=currentX
        
        //if something is above left
        //y<currentY
        //x<currentX
        
        //if something is above left
        //y=currentY
        //x<currentX
        
        //if someting is below left
        //y>currentY
        //x<currentX
        
        //if something is below
        //y>currentY
        //x=currentX
        
        //if something is below right
        //y>currentY
        //x>currentX
        
        //if something is right
        //y=currentY
        //x>currentX
        
        //if something is above right
        //y<currentY
        //x>currentX 
        
        //GET SURROUNDINGS
        //Space[][] surroundings = world.getSurroundingSpaces(World_Constants.BEING_SIGHT_RANGE,ylocation,xlocation);
        Space[][] surroundings = world.getSpacesInView(World_Constants.BEING_SIGHT_RANGE,ylocation,xlocation,direction);
        ArrayList<IWorldObject> objectsInSight = new ArrayList<IWorldObject>();       
        
        //get a 1 dimensional array of objects in the surrounding spaces
        for(int r=0;r<(2*World_Constants.BEING_SIGHT_RANGE)+1;r++)
        {
            //increment column
            for(int c=0;c<(2*World_Constants.BEING_SIGHT_RANGE)+1;c++)
            {
                if(surroundings[r][c] != null && surroundings[r][c].getObjectInSpace()!=null)
                {
                    //interrogate objects in surroudings that arent null
                    objectsInSight.add(surroundings[r][c].getObjectInSpace());
                    //System.out.println("I see object");
                }
            }  
        }
        //remove self from this list
        objectsInSight.remove(this);        
        
        
        //IDENTIFY OBJECTS OF INTEREST
        IWorldObject objectOfInterest = null;
        
        //SORT OBJECTS IN SIGHT BY DISTANCE
        Comparator<IWorldObject> distanceOrder =  new Comparator<IWorldObject>() {
        public int compare(IWorldObject o1, IWorldObject o2) {
            int o1diff = Math.abs(ylocation-o1.getYLocation()) + Math.abs(xlocation-o1.getXLocation());
            int o2diff = Math.abs(ylocation-o2.getYLocation()) + Math.abs(xlocation-o2.getXLocation());
            return Integer.compare(o1diff, o2diff);
        }};
           
        //SORT OBJECTS IN SIGHT BY FAMILIARITY
        Comparator<IWorldObject> familiarityOrder =  new Comparator<IWorldObject>() {
        public int compare(IWorldObject o1, IWorldObject o2) {
            if(o1 instanceof Being & o2 instanceof Being & socialNetwork.getVertice(o1.toString())!=null & socialNetwork.getVertice(o2.toString())!=null)
                return Integer.compare(socialNetwork.getVertice(o1.toString()).getValue(),socialNetwork.getVertice(o2.toString()).getValue());
            else
                return 0;
        }};
        
        int objectsInSightIndex = 0;
        //if looking for beings, chose object of interest based on familiarity
        //pick first being from the list, if not a being move to next in list.
        Collections.sort(objectsInSight, familiarityOrder);
        if(motivations.contains(Motivation.BREED) | motivations.contains(Motivation.SOCIALISE) & objectsInSight.size()>0)
        {
            objectsInSightIndex = 0;
            boolean repeat = true;
            //go to closes object
            while(repeat & objectsInSightIndex<objectsInSight.size())
            {
                if(objectsInSight.get(objectsInSightIndex) instanceof Being){
                objectOfInterest = objectsInSight.get(objectsInSightIndex);
                repeat = false;
                }
                else
                    objectsInSightIndex++;
            }           
        }
        //if looking for inanimate objects, chose object of interest based on proximity
        //pick first resource from the list, if not a resource move to next in list.
        Collections.sort(objectsInSight, distanceOrder);
        if(motivations.contains(Motivation.FEED) & objectsInSight.size()>0)
        {
            objectsInSightIndex = 0;
            boolean repeat = true;
            //go to closes object
            while(repeat & objectsInSightIndex<objectsInSight.size())
            {
                if(objectsInSight.get(objectsInSightIndex) instanceof Resource){
                objectOfInterest = objectsInSight.get(objectsInSightIndex);
                repeat = false;
                }
                else
                    objectsInSightIndex++;
            }
        }
        
        //DETERMINE DIRECTION TO MOVE TO GET TO OBJECT OF INTEREST
        if(objectOfInterest != null && objectOfInterest.getYLocation()<this.ylocation)
        {
            //object is above - move up
            yMove = -1;
        }    
        if(objectOfInterest != null && objectOfInterest.getYLocation()>this.ylocation)
        {
            //object is below - move down
            yMove = 1;
        }
        if(objectOfInterest != null && objectOfInterest.getXLocation()<this.xlocation)
        {
            //object is left - move left;
            xMove = -1;
        }
        if(objectOfInterest != null && objectOfInterest.getXLocation()>this.xlocation)
        {
            //object is right - move right
            xMove = 1;
        }
        
        //DETECT ADJACENT OBJECTS
        //if the difference between the object location is 1 coordinate away 
        if(objectOfInterest!=null)
        {
            //System.out.println("Check adjacent");
            int yDelta = objectOfInterest.getYLocation() - ylocation;
            int xDelta = objectOfInterest.getXLocation() - xlocation;
            
            if(Integer.compareUnsigned(yDelta, 1)==0 | Integer.compareUnsigned(xDelta, 1)==0)
            {
                interact(objectOfInterest);
            }
        }
        
        //CHECK IF SPACE TO MOVE TO IS A NULL SPACE AND IF SO, MOVE AWAY
        //current space is
        //y = sight range
        //x = sight range
        
        //[0,0][0,1][0,2][0,3][0,4]
        //[1,0][1,1][1,2][1,3][1,4];
        //[2,0][2,1][2,2][2,3][2,4];
        //[3,0][3,1][3,2][3,3][3,4];
        //[4,0][4,1][4,2][4,3][4,4];
        
        boolean avoidSpace = false;
        if(surroundings[World_Constants.BEING_SIGHT_RANGE+yMove][World_Constants.BEING_SIGHT_RANGE+xMove] == null)
            avoidSpace = true;
        if(surroundings[World_Constants.BEING_SIGHT_RANGE+yMove][World_Constants.BEING_SIGHT_RANGE+xMove]!= null && surroundings[World_Constants.BEING_SIGHT_RANGE+yMove][World_Constants.BEING_SIGHT_RANGE+xMove].getObjectInSpace()!=null)
            avoidSpace = true;
        //CHECK THAT THE SPACE BEING IS MOVING TO IS NULL OR ALREADY OCCUPIED AND PREVENT MOVEMENT IN THAT DIRECTION
        if(avoidSpace)
        {
            if(yMove == -1)
            {
                //if moving up - move down
                yMove = 1;              
            }
            else if (yMove == 1)
            {
                //if moving down - move up
                yMove = -1;
            }
            
            if(xMove == -1)
            {
                //if moving left - move right
                xMove = 1;
            }
            else if(xMove == 1)
            {
                //if moving right - move left
                xMove = -1;
            }
        }
              
        //SET MOVE DIRECTION
        direction = getMoveDirection(yMove,xMove);        
        
        //UPDATE BEING VIEW IF CURRENT BEING IS IN FOCUS IN THE UI
        if(isFocussed)
        {
            world.populateBeingView(surroundings);           
        }
        
        //FINALLY, MOVE
        //move this being
        //if movement failed (blocked) then move randomly
        if(this.move(yMove,xMove))
        {
            
        }
        else
        {
            //if(isFocussed)
            //IF MOVEMENT FAILS / BLOCKED
            //System.out.println("Most failed @ " + yMove+","+xMove);
        }
    }
    
    public int getMoveDirection(int yMove, int xMove)
    {
        int direction = 0;
        if(yMove>0)
        {
            if(xMove>0){
                //moving down right
                direction = 315;}
            else if(xMove==0){
                //moving down only
                direction = 270;}
            else if(xMove<0){
                //moving down left
                direction = 225;}
        }
        else if( yMove==0)
        {
            if(xMove>0){
                //moving right only
                direction = 0;}
            else if(xMove==0){
                //not moving at all
                direction = -1;}
            else if(xMove<0){
                //moving left only
                direction = 180;}
        }
        else if(yMove<0)
        {
            if(xMove>0){
                //moving up right
                direction = 45;}
            else if(xMove==0){
                //moving up only
                direction = 90;}
            else if(xMove<0){
                //moving up left
                direction = 135;}
        } 
        return direction;
    }
    
    public void updateSpacialMemory(char direction)
    {
        int height = spacialMemory.length; //rows
        int width = spacialMemory[0].length; //columns
        
        //if height increase
        spacialMemory = Arrays.copyOf(spacialMemory, width+1);
        
        //if width increase
        for(Space[] sa:spacialMemory)
            sa = Arrays.copyOf(sa, width+1);  
    }

    public boolean checkMotivations(Motivation motivation)
    {
        return motivations.contains(motivation);
    }
    
    public void interact(IWorldObject o)
    {        
        if(o instanceof Being)
        {
            Being otherBeing = (Being) o;
            //being beings are of different gender, is not a child of this being, or a parent of this being - reproduce
            if(motivations.contains(Motivation.BREED) && otherBeing.checkMotivations(Motivation.BREED))
            {
                if(this.gender != otherBeing.gender)
                {   
                    if(parentA != otherBeing | parentB != otherBeing)
                    {
                        if(!children.contains(otherBeing))
                        {
                            reproduce(otherBeing);                        
                        }  
                    }
                }   
            }
            //if another motivation
            if(motivations.contains(Motivation.SOCIALISE)) 
            {
                if(otherBeing.checkMotivations(Motivation.SOCIALISE))
                {  
                    socialise(otherBeing,1,1);
                }
            }
            
            //if being has a/the virus
            if(hasVirus == true)
            {
                //determine if it passes it on
                if(ThreadLocalRandom.current().nextInt(1,11) < World_Constants.VIRUS_CONTAGEON_RATE)
                {
                    otherBeing.setHasVirus(true);                    
                }
                else
                {
                    otherBeing.setHasImmunity(true);
                }
            }
        }
    }
    
    public void setHasVirus(boolean result)
    {
        //if if being current does have the virus
        if(hasVirus)
        {   //and is cured
            if(!result)
            {   //give them their remaining life back
                lifeRemaining = World_Constants.BEING_LIFE - age;
                hasVirus = false;
            }
        }
        //if if being current does not have the virus
        else if (!hasVirus)
        {   //and they contract the virus
            if(result)
            {   //reduce their life expetancy
                lifeRemaining = lifeRemaining * World_Constants.VIRUS_MORTALITY_RATE;
                hasVirus = true;
            }            
        }
    }
    
    public boolean getHasVirus()
    {
        return hasVirus;
    }
    
    public void setHasImmunity(boolean hasImmunity)    
    {
        this.hasImmunity = hasImmunity;
    }
    
    @Override
    public void setLocation(int ylocation, int xlocation) {
    }

    @Override
    public int getYLocation() {
        return ylocation;
    }

    @Override
    public int getXLocation() {
        return xlocation;
    }
    
    public int getDirection()
    {
        return direction;
    }

    @Override
    public int compareTo(Object o) 
    {
        if(this==o)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
    
    enum Motivation
    {
        HUNGER,
        BREED,
        SOCIALISE,
        FEED
    }
    
}
