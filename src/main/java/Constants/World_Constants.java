/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constants;

/**
 *
 * @author chris
 */
public class World_Constants
{
    public static final int WORLD_HEIGHT = 50;
    public static final int WORLD_WIDTH = 50;
    
    public static final int WORLD_MAX_POPULATION = 100;
    
    public static final int DUNBARS_NUMBER = 20;
    
    public static int BEING_LIFE = 300;
    public static int BEING_LIFE_MIN = 100;
    public static int BEING_LIFE_MAX = 1000;
    
    public static final int BEING_MOVEMENT_RANGE = 1;
    
    public static int BEING_SIGHT_RANGE = 2;
    public static final int BEING_SIGHT_RANGE_MIN = 1;
    public static final int BEING_SIGHT_RANGE_MAX = WORLD_WIDTH/2;    
    
    public static long BEING_METABOLIC_RATE =75L;
    public static final long BEING_METABOLIC_RATE_MIN = 1000L;
    public static final long BEING_METABOLIC_RATE_MAX = 50L;
    
    public static int BEING_CHILD_LIMIT = 1;
    public static final int BEING_CHILD_LIMIT_MIN = 0;
    public static final int BEING_CHILD_LIMIT_MAX = 10;
    
    public static final int BEING_AGE_RATE = 1;
    public static final int BEING_AGE_RATE_MIN = 1;
    public static final int BEING_AGE_RATE_MAX = 10;  
   
    public static int BIRTH_CHANCE_OF_VIRUS = 4; //2-10 scale of liklihood of beig born with the virus (for big bang)
    public static final int BIRTH_CHANCE_OF_VIRUS_MIN = 0;
    public static final int BIRTH_CHANCE_OF_VIRUS_MAX = 10;
    
    public static int VIRUS_CONTAGEON_RATE =4; // 2-10 scale of liklihood of contageon
    public static final int VIRUS_CONTAGEON_RATE_MIN = 0;
    public static final int VIRUS_CONTAGEON_RATE_MAX = 10;
    
    public static double VIRUS_MORTALITY_RATE = 0.9;  // 0-1 scale of instant death to no impact
    public static final double VIRUS_MORTALITY_RATE_MIN = 1;
    public static final double VIRUS_MORTALITY_RATE_MAX = 0;
    
    public static final long BIRTH_AGE = BEING_LIFE - (BEING_LIFE/4);
    public static final long BIRTH_AGE_MIN = BEING_LIFE/10;
    public static final long BIRTH_AGE_MAX = BEING_LIFE;
    
}
