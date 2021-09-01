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
public abstract interface IWorldObject 
{
    
    public void setLocation(int ylocation, int xlocation);
    
    public int getYLocation();
    
    public int getXLocation();
    
}
