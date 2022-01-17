/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author chris
 */
public class Graph 
{     
    //adjacency list
    //contains vertices and edges
    
    //weighted direct graph
    //HashSet of Vertices
    //Each Verticie has a name and a list of all its outgoing edges
    //Each edge has a weight and a destination vertice
    
    //contains the nodes in the graph
    HashMap<String,Vertice> nodes;
    HashMap<String, Edge> edges;
    
    public Graph()
    {
        nodes = new HashMap<String,Vertice>();
        edges = new HashMap<String,Edge>();
    }
    
    public void AddEdge(Vertice originVertice, Vertice destinationVertice, int weight)
    {
        //adds and edge between a the specified vertice and the given vertice
        //key for vertice hashmap of edges is the combined string of origin and destination vertices
        edges.put(originVertice.thing.toString()+destinationVertice.thing.toString(),new Edge(originVertice, destinationVertice, weight));
    }
    
    public void addVertice(Object o, Vertice newVertice)
    {
        //adds the specified vertice to the graph
        nodes.put(o.toString(), newVertice); 
    }
    
    public void removeVertice(Object o)
    {
        //remove vertice from list hashset of vertice's
        nodes.remove(o.toString());        
    }
        
    public boolean checkIfNodeExists(Object o)
    {
        return nodes.containsKey(o.toString());
    }
    
    public Object getObject(Object o)
    {
        return nodes.get(o.toString()).thing;
    }
    
    public Vertice getVertice(String key)
    {
        return nodes.get(key);
    }
    
    public Edge getEdge(Object origin, Object destination)
    {
        return edges.get(origin.toString()+destination.toString());
    }
    
    public int getSize()
    {
        return nodes.size();
    }
    
    public class Edge
    {
        //class variables
        
        int weight;
        Vertice origin;
        Vertice destination;
        
        public Edge(Vertice origin, Vertice destination, int weight)
        {
            this.weight = weight;
            this.origin = origin;
            this.destination = destination;
        }
        
        //contructor to use for unweighted graph
        public Edge(Vertice origin, Vertice destination)
        {
            this.origin = origin;
            this.destination = destination;
            this.weight = 1; //all values are 1 for unweighted graph
        }
        
        public int getWeight()
        {
            return weight;
        }
        
        public void setWeight(int weight)
        {
            this.weight = weight;
        }
        
        public void increaseWeight()
        {
            weight++;
        }
        
    }
}
