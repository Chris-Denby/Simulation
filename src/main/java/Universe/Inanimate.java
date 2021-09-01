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
public class Inanimate implements IWorldObject{

    int ylocation;
    int xlocation;
    
    public Inanimate(int y, int x)
    {
        this.ylocation = y;
        this.xlocation = x;
    }
    
    @Override
    public void setLocation(int ylocation, int xlocation) {
    this.ylocation = ylocation;    
    }

    @Override
    public int getYLocation() {
    return ylocation;
    }

    @Override
    public int getXLocation() {
    return xlocation;
    }
    
}
