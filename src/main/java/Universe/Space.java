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
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 *
 * @author chris
 */
public class Space extends JLabel
{
    boolean viewed = false;
    int ylocation;
    int xlocation;
    private IWorldObject object;
    
    public Space(int y, int x, String text)
    {
        xlocation = x;
        ylocation = y;
        this.setText(text);
        Font f = new Font("Serif",Font.CENTER_BASELINE, 4);
        this.setFont(f);
        this.setPreferredSize(new Dimension(5,5));
        Border border = BorderFactory.createLineBorder(Color.WHITE, 1);
        this.setBorder(border);
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
                if(b.getGender()==0)
                {
                    this.setBackground(Color.magenta);
                }
                else if(b.getGender()==1)
                {
                    this.setBackground(Color.BLUE);
                }
                return true;
            } 
            if(o instanceof Inanimate)
            {
                this.setBackground(Color.BLACK);
                this.setText("I");
                this.setOpaque(true);
                return true; 
            }
        }
        else
        {
                this.setBackground(null);
                this.setText("");
                this.setOpaque(false);
        }
        return successfull;
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
    
}
