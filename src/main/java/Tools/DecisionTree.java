/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.util.ArrayList;

/**
 *
 * @author chris
 */
public class DecisionTree 
{
    //the way to get from one node to the next is via the pointer to the left or right child
    //if no child on a pointer, pointer value is null
    //each node consists of [pointer left][pointer right][data on node]

    //root node of tree
    BinaryNode root;
    
    public DecisionTree(int value)
    {
        //sets value for root note
        root = new BinaryNode(value);
    }

    public boolean constructTreeAddYesNode(BinaryNode currentNode, int existingID, int newID, String questionAnswer)
    {
        //adds a new node to the next available yes branch
        
        //if yes branch is empty
        if(currentNode.yesBranch==null)
        {
            //add the new node to the yes branch
            currentNode.yesBranch = new BinaryNode(newID, questionAnswer);
            return true;
        }
        //else, if the yes branch is occupied try yes branch of the yes branch
        else if(currentNode.yesBranch != null)
        {
            return constructTreeAddYesNode(currentNode.yesBranch, existingID, newID, questionAnswer);
        }
        else return false;
    }
    
    public boolean constructTreeAddNoNode(BinaryNode currentNode, int existingID, int newID, String questionAnswer)
    {
        //adds a new node to the next available no branch
        
        //if no branch is empty
        if(currentNode.noBranch==null)
        {
            //add the new node to the no branch
            currentNode.noBranch = new BinaryNode(newID, questionAnswer);
            return true;
        }
        //else, if the no branch is occupied try no branch of the no branch
        else if(currentNode.noBranch != null)
        {
            return constructTreeAddNoNode(currentNode.noBranch, existingID, newID, questionAnswer);
        }
        else return false;
    }
    
    public BinaryNode askQuestion(BinaryNode currentNode, Object queryObject)
    {
        //if matching node is found
        if(queryObject == currentNode)
        {
            return currentNode;
        }
        else
        //else try the yes branch
        if(currentNode.yesBranch != null)
        {
            askQuestion(currentNode.yesBranch, queryObject);
        }
        //else true the no branch
        else if(currentNode.noBranch != null)
        {
            askQuestion(currentNode.noBranch, queryObject); 
        }
        return null;
    }
       
    public class BinaryNode 
    {
        //nodes data value
        int value;
        int nodeID;
        String questionAnswer;
        //reference to child nodes (pass by reference)
        BinaryNode yesBranch;
        BinaryNode noBranch;

        public BinaryNode(int value)
        {
            this.value = value;
            yesBranch = null;
            noBranch = null;
        }
        
        public BinaryNode(int nodeID, String questionAnswer)
        {
            this.nodeID = nodeID;
            this.questionAnswer = questionAnswer;
        }
        
        @Override
        public String toString()
        {
            return ""+value;
        }
    } 
}


