/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Universe;

/**
 *
 * @author chris
 */
public class Resource extends Inanimate
{
    private int energy = 10;
    
    //default constructor
    public Resource(int y, int x) 
    {
        super(y, x);
    }
    
    public int getEnergy()
    {
        return energy;
    }
    
    
    
}
