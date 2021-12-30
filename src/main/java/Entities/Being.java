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
import Universe.Inanimate;
import Universe.Resource;
import Universe.Space;
import Universe.World;
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
    private String name;
    private double moveRange;
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
    
    private int happiness = 0;
    private int energy = 0;
    
    private int consumeHappinessModifier;
    private int socialiseHappinessModifier;
    private int breedHappinessModifier;
    
    
    
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
    
    public void move(int yMod, int xMod)
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
            }
        }

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
        Space[][] surroundings = world.getSurroundingSpaces(World_Constants.BEING_SIGHT_RANGE,ylocation,xlocation);
        ArrayList<IWorldObject> objectsInSight = new ArrayList<IWorldObject>();
        
        //get a 1 dimensional array of objects in the surrounding spaces
        for(int r=0;r<(2*World_Constants.BEING_SIGHT_RANGE)+1;r++)
        {
            //increment column
            for(int c=0;c<(2*World_Constants.BEING_SIGHT_RANGE)+1;c++)
            {
                if(surroundings[r][c] != null && surroundings[r][c].getObjectInSpace()!=null)
                {
                    objectsInSight.add(surroundings[r][c].getObjectInSpace());
                }
            }  
        }
        //remove self from this list
        objectsInSight.remove(this);
        
        //IDENTIFY OBJECTS OF INTEREST
        IWorldObject objectOfInterest = null;
        for(IWorldObject o:objectsInSight)
        {
            if(o instanceof Being)
            {
                try
                {              
                    
                    if(o!=this && motivations.contains(Motivation.BREED) & socialNetwork.checkIfNodeExists((Being)o) & socialNetwork.getEdge(this,(Being)o).getWeight()>socialNetwork.getEdge(this,(Being) objectOfInterest).getWeight())
                    {
                        objectOfInterest = (Being) o;
                        //System.out.println("I WANT TO BREED WITH " + o.toString());
                    }
                    if(o!=this && motivations.contains(Motivation.SOCIALISE) & !socialNetwork.checkIfNodeExists((Being)o))
                    {
                        objectOfInterest = (Being) o;
                        //System.out.println("I WANT TO SOCIALIZE WITH " + o.toString());
                    }
                }
                catch(Exception e){
                }
            }
            if(o instanceof Resource)
            {
                if(o!=this && motivations.contains(Motivation.FEED))
                {
                    objectOfInterest = (Resource) o;
                    //System.out.println("I WANT TO EAT " + o.toString());
                }    
            } 
        }
        
        //DETERMINE DIRECTION TO MOVE TO GET TO OBJECT OF INTEREST
        if(objectOfInterest != null && objectOfInterest.getYLocation()<this.ylocation)
        {
            //object is above - move up
            yMove = -1;
            if(objectOfInterest.getXLocation()<this.xlocation)
            {
                //above left - move left
                xMove = -1;
            }
            if (objectOfInterest.getXLocation()>this.xlocation)
            {
                //below right - move right
                xMove = 1;
            }
        }
        else        
        if(objectOfInterest != null && objectOfInterest.getYLocation()>this.ylocation)
        {
            //object is below - move down
            yMove = 1;
            if(objectOfInterest.getXLocation()<this.xlocation)
            {
                //below left - move left;
                xMove = -1;
            }
            if (objectOfInterest.getXLocation()>this.xlocation)
            {
                //below right - move right
                xMove = 1;
            }
        }
        else
        if(objectOfInterest != null && objectOfInterest.getXLocation()<this.xlocation)
        {
            //object is left - move left;
            xMove = -1;
        }
        else
        if(objectOfInterest != null && objectOfInterest.getXLocation()>this.xlocation)
        {
            //object is right - move right
            xMove = 1;
        }
        
        //DETECT ADJACENT OBJECTS
        //loop through objects to detect if object of interest is within 1x or 1y in any direction
        for(IWorldObject o:objectsInSight)
        {
            if(o!=null)
            {
                //get location of object
                int yDelta = o.getYLocation() - ylocation;
                int xDelta = o.getXLocation() - xlocation;

                //if the difference between the object lcoation is 1 coordinate away 
                if(yDelta >=-1 & yDelta<=1 & xDelta >=-1 & xDelta <=1)
                {
                   interact(o);
                }
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
        
        //CHECK FOR NULL SPACE AND PREVENT MOVEMENT IN THAT DIRECTION
        if(surroundings[World_Constants.BEING_SIGHT_RANGE+yMove][World_Constants.BEING_SIGHT_RANGE+xMove] == null)
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
        //FINALLY, MOVE
        //move this being
        this.move(yMove,xMove);
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
