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
import Universe.Space;
import Universe.World;
import java.util.Arrays;
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
    int gender; // 0 = female, 1 = males
    Space previousSpace;
    Space currentSpace;
    int yMove;
    int xMove;
    double lifeRemaining = World_Constants.BEING_LIFE;
    double ageRateModifier = 1;
    Timer metabolicTimer;
    World world;
    int ylocation = -1;
    int xlocation = -1;
    String name;
    double moveRange;
    Being parentA;
    Being parentB;
    Being partner;
    ArrayList<Being> children = new ArrayList<Being>();
    ArrayList<Being> Siblings = new ArrayList<Being>();
    Graph socialNetwork = new Graph();
    ArrayList<Trait> traits = new ArrayList<Trait>();
    ArrayList<Motivation> motivations = new ArrayList<Motivation>();
    Vertice identity = new Vertice(this,0);
    Queue<Space> pastMoves = new LinkedList<Space>();
    HashMap<Motivation,ArrayList> pathMemories = new HashMap<Motivation,ArrayList>();
    
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
        //this.start();
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
        //this.start();
    }
    
    public Being(World w)
    {
        gender = ThreadLocalRandom.current().nextInt(0,1+1); //assign gender randomly
        world = w;
        socialNetwork.addVertice(this,identity);
        motivations.add(Motivation.SOCIALISE);
        motivations.add(Motivation.BREED);
        born();
        //this.start();
    }
    
    public int getGender()
    {
        return gender;
    }
    
    public void move(int yMod, int xMod)
    {   
        //move only to a new space
        if(yMod !=0 & xMod !=0)
        {
            if(world.movePresence(yMod, xMod, this))
            {
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
                Being child = new Being(ylocation, xlocation, parentA, parentB, world);
                this.addChild(child);
                parentB.addChild(child);
                world.registerExistence(child);     
            }
        }
        if(children.size() == World_Constants.BEING_CHILD_LIMIT)
        {
            //if max children have been made, remove the motivation to breed
            motivations.remove(Motivation.BREED);
            die();
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
        
        //initialize Timer
        metabolicTimer = new Timer();
        
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
        //if being is not already in this beings social network
        if(!socialNetwork.checkIfNodeExists(otherBeing))
        {
            //creates a vertice with reference of beings thread reference
            Vertice otherIdentity = new Vertice(otherBeing,mag);
            //add other being to this beings social network
            socialNetwork.addVertice(otherBeing, otherIdentity);
            //add edge between this being and the other
            socialNetwork.AddEdge(identity, otherIdentity, degree);
            //System.out.println("Added to social network");
            
        }
        //if being is already in this beings social network
        else if(socialNetwork.checkIfNodeExists(otherBeing))
        {
            //increase the value of the node by 1
            socialNetwork.getVertice(otherBeing.toString()).increaseValue();
            //increase the weight of the connection by 1
            socialNetwork.getEdge(this, otherBeing).increaseWeight();
            //System.out.println("I already know you " + otherBeing.toString());
            
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
                    if(o!=this && motivations.contains(Motivation.BREED) && socialNetwork.checkIfNodeExists((Being)o) && socialNetwork.getEdge(this,(Being)o).getWeight()>socialNetwork.getEdge(this,(Being) objectOfInterest).getWeight())
                    {
                        objectOfInterest = (Being) o;
                        //System.out.println("I WANT TO BREED WITH " + o.toString());
                    }
                    if(o!=this && motivations.contains(Motivation.SOCIALISE) && !socialNetwork.checkIfNodeExists((Being)o))
                    {
                        objectOfInterest = (Being) o;
                        //System.out.println("I WANT TO SOCIALIZE WITH " + o.toString());
                    }
                }
                catch(Exception e){
                }
            }
            if(o instanceof Inanimate)
            {
                
            } 
        }
        
        //DETERMINE DIRECTION TO MOVE
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
        //loop through objects to detect if object if interest is within 1x or 1y in any direction z
        for(IWorldObject o:objectsInSight)
        {
            int yDelta = o.getYLocation() - ylocation;
            int xDelta = o.getXLocation() - xlocation;
            
            //if the difference between the object lcoation is 1 coordinate away 
            if(yDelta >=-1 & yDelta<=1 & xDelta >=-1 & xDelta <=1)
            {
               interact(o);
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
        this.move(yMove,xMove);
    }
    
    public void traverseWorldOriginal()
    {        
        //get sub matrix
        Space[][] surroundings = world.getSurroundingSpaces(World_Constants.BEING_SIGHT_RANGE,ylocation,xlocation);
        Space[] immediateArea = new Space[8];
        Space[] sightBoundary = new Space[16];

        //array width & height = (2*range)+1
        //but because index starts at 0, dimensions = 2* range

        //if sight range is 1
        //current space is
        //y = sight range
        //x = sight range
        
        //Interpret surroundings###############
        //[0,0][0,1][0,2][0,3][0,4]
        //[1,0][1,1][1,2][1,3][1,4];
        //[2,0][2,1][2,2][2,3][2,4];
        //[3,0][3,1][3,2][3,3][3,4];
        //[4,0][4,1][4,2][4,3][4,4];
        
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
        
        
        //get boundary of surroundings
        sightBoundary[0] = surroundings[0][2];  //up-up
        sightBoundary[1] = surroundings[0][1]; //up-up-left
        sightBoundary[2] = surroundings[0][0]; //up-up-left-left
        sightBoundary[3] = surroundings[0][3]; //up-up-right
        sightBoundary[4] = surroundings[0][4]; //up-up-right-right
        sightBoundary[5] = surroundings[2][0]; //left-left 
        sightBoundary[6] = surroundings[1][0]; //left-left-up
        sightBoundary[7] = surroundings[3][0]; //left-left-down
        sightBoundary[8] = surroundings[2][4]; //right-right
        sightBoundary[9] = surroundings[1][4]; //right-right-up
        sightBoundary[10] = surroundings[3][4]; //right-right-down
        sightBoundary[11] = surroundings[4][2]; //down-down
        sightBoundary[12] = surroundings[4][1]; //down-down-left
        sightBoundary[13] = surroundings[4][0]; //down-down-left-left
        sightBoundary[14] = surroundings[4][3]; //down-down-right
        sightBoundary[15] = surroundings[4][4]; //down-down-right-right
        
        //get immediate area of surroundings
        currentSpace = surroundings[2][2]; //currentSpace
        immediateArea[0] = surroundings[1][2]; // up
        immediateArea[1] = surroundings[1][1]; //upleft
        immediateArea[2] = surroundings[1][3]; //upright
        immediateArea[3] = surroundings[2][1]; //left
        immediateArea[4] = surroundings[2][3]; //right
        immediateArea[5] = surroundings[3][2]; //down
        immediateArea[6] = surroundings[3][1]; //downleft
        immediateArea[7] = surroundings[3][3]; //downright

        
        
        
        
        //keep a record of the previous space just left
        //if previous space has not yet been set, set it to the current space
        if(previousSpace==null)
            previousSpace = surroundings[2][2];
        else
            previousSpace = currentSpace; 
        
        //SET RANDOM MOVE DIRECTION
        yMove = ThreadLocalRandom.current().nextInt(-1,1+1);
        xMove = ThreadLocalRandom.current().nextInt(-1,1+1);
        
        //determine move direction
        
        //CHECK FOR BOUNDARY
        //if boundary is up
        if(immediateArea[0] == null)
        {
            //move down
            yMove = 1;
            //System.out.println("a");
        }
        //if an boundary is left
        if(immediateArea[3] == null)
        {
            //move right
            xMove = 1;
            //System.out.println("b");
        }
        //if an boundary is right
        if(immediateArea[4] == null)
        {
            //move left
            xMove = -1;
            //System.out.println("c");
        }
        //if boundary is down
        if(immediateArea[5] == null)
        {
            //move up
            yMove = -1;
            //System.out.println("f");
        }
        
        
        //CHECK SURROUNDING SITE RANGE
        for(Space s:sightBoundary)
        {
            if(s != null && s.getObjectInSpace() instanceof Being)
            {
                Being b = (Being) s.getObjectInSpace();
                if(motivations.contains(Motivation.BREED))
                {
                    //if a being is in sight range - move towards it
                    if(b.getYLocation()>this.ylocation)
                        //move down
                        yMove = 1;
                    if(b.getYLocation()<this.ylocation)
                        //move up
                        yMove = -1;
                    if(b.getXLocation()>this.xlocation) 
                        //move right
                        xMove = 1;
                    if(b.getXLocation()<this.xlocation)
                        //move left
                        xMove = -1;
                }
                if(motivations.contains(Motivation.SOCIALISE))
                {


                }
            }
            if(s != null && s.getObjectInSpace() instanceof Inanimate)
            {
                if(motivations.contains(Motivation.HUNGER))
                {
                    //if its food, move towards it
                }
                
            }
        }
        
        
        //CHECK IMMEDIATE AREA
        //for each space in the immediate area
        for(Space s:immediateArea)
        {
            //if space is not null and contains a being, stop for a cycle
            //unless - the object is already stationary, start moving again
            if(s!= null && s.getObjectInSpace() != null)
            {
                //if the being is not stationary - stop
                if(previousSpace != currentSpace)
                {
                    yMove = 0;
                    xMove = 0; 
                }
                //interact with the object
                interact(s.getObjectInSpace());
            }
        }
        this.move(yMove,xMove);

        //USE IF NEEDED##############
        //loop through surroundings 
        //increment row
        /**
        for(int r=0;r<(2*World_Constants.BEING_SIGHT_RANGE)+1;r++)
        {
            //increment column
            for(int c=0;c<(2*World_Constants.BEING_SIGHT_RANGE)+1;c++)
            {
                
                if(surroundings[r][c] != null)
                {
                    //Space space = surroundings[r][c];
                    //System.out.println(space.getYLocation()+","+space.getXLocation());
                    
                }
            }  
        }
        **/
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
        }
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
        SOCIALISE
    }
    
}
