/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import Tools.Graph.Edge;
import java.util.HashMap;

/**
 *
 * @author chris
 */
public class Vertice
{
    //name of object the Vertice represents
    Object thing; 
    int value = 0;

    public Vertice(Object thing, int value)
    {
        //key for vertice hashmap of edges is the combined string of origin and destination vertices
        //verticeEdges = new HashMap<String,Graph.Edge>();
        this.thing = thing;
        this.value = value; 
    }
        
    public int getValue()
    {
        return value;
    }
    
    public void setValue(int value)
    {
        this.value = value;
    }
    
    public void increaseValue()
    {
        value++;
    }
}
