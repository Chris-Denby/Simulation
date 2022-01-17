/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Universe;

import Entities.Being;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 *
 * @author chris
 */
public class Space extends JLabel
{
    int ylocation;
    int xlocation;
    int width = 10;
    int height = 10;
    private IWorldObject object;
    Border defaultBorder = BorderFactory.createLineBorder(Color.WHITE, 1);
    
    public Space(int y, int x, String text)
    {
        xlocation = x;
        ylocation = y;
        Font f = new Font("Serif",Font.CENTER_BASELINE, 4);
        this.setFont(f);
        this.setPreferredSize(new Dimension(width,height));
        this.setBorder(defaultBorder);
        this.setOpaque(true);
    }
    
        public Space(int y, int x)
    {
        xlocation = x;
        ylocation = y;
        Font f = new Font("Serif",Font.CENTER_BASELINE, 4);
        this.setFont(f);
        this.setPreferredSize(new Dimension(width,height));
        this.setBorder(defaultBorder);
        this.setOpaque(true);
    }
    
    public boolean setObject(IWorldObject o)
    {        
        object = o;
        Boolean successfull = false;
        if(o != null)
        {
            
            if(o instanceof Being)
            {
                Being b = (Being) o;
                this.setOpaque(true);
                
                
                if(b.getHasVirus())
                {
                    this.setBackground(new Color(66,232,37));
                }
                else
                {
                    if(b.getGender()==0)
                    {
                        this.setBackground(Color.magenta);
                    }
                    else if(b.getGender()==1)
                    {
                        this.setBackground(Color.BLUE);
                    } 
                }
                
                
                if(b.isFocussed())
                    this.setBackground(Color.RED);
                return true;
            } 
            if(o instanceof Inanimate)
            {
                this.setOpaque(true);
                this.setBackground(Color.BLACK);
                return true; 
            }
        }
        else
        {
                this.setBackground(null);
                this.setOpaque(false);
        }
        return successfull;
    }

    public void setInanimate()
    {
        if(object instanceof Being)
        {
            Being b = (Being) object;
            b.die();
        }
        setObject(new Inanimate(ylocation,xlocation));
    }


    public void setLocation(int ylocation, int xlocation) {
        this.ylocation = ylocation;
        this.xlocation = xlocation;
    }

    
    public int getYLocation() {
        return ylocation;
    }


    public int getXLocation() {
        return xlocation;
    }
    
    public IWorldObject getObjectInSpace()
    {
        return object;
    }
    
    @Override
    public String toString()
    {
        return ylocation+","+xlocation;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setForeground(Color.white);
        /** 
         * drawLine method
         * @Param x1 - the first point's x coordinate. 
         * @Param y1 - the first point's y coordinate. 
         * @Param x2 - the second point's x coordinate. 
         * @Param y2 - the second point's y coordinate.
         */
        
        if(object instanceof Being)
        {
            g.drawOval(1, 1, width-2, height-2);
            Being b = (Being) object;

            try{
            switch(b.getDirection())
            {
                case 315:
                    //moving down right
                    g.drawLine(5,5,0,10);
                break;
                case 270: 
                    //moving down only
                    g.drawLine(5,5,5,10);
                break;
                case 225:
                    //moving down left
                    g.drawLine(5,5,0,10);
                break;                    
                case 0:
                    //moving right only
                    g.drawLine(5,5,10,5);
                break;                    
                case -1:
                    //not moving at all
                break;                    
                case 180:
                    //moving left only
                    g.drawLine(5,5,0,5);
                break;                    
                case 45:
                    //moving up right
                    g.drawLine(5,5,10,0);
                break;
                case 90:
                    //moving up only  
                    g.drawLine(5,5,5,0);
                break;
                case 135:
                    //moving up left
                    g.drawLine(5,5,0,0);
                break;                   
            }}
            catch(NullPointerException ex){}
        }
    }
    
}
