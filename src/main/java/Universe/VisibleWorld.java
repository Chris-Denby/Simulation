/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Universe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author chris
 */
public class VisibleWorld extends javax.swing.JFrame
{
    Dimension dimensions = new Dimension(800,600);
    GridBagLayout worldGrid;
    GridBagConstraints gbConstraints;
    JPanel worldPanel = new JPanel();
    JLabel populationLabel = new JLabel();
    
    public VisibleWorld()
    {
        //FRAME SETUP
        setTitle("Simulation");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(dimensions);
        
        //GRID SETUP
        worldGrid = new GridBagLayout();
        gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.BOTH;
        //setLayout(worldGrid);
        worldPanel.setLayout(worldGrid);
        this.add(worldPanel,BorderLayout.CENTER);
        this.add(populationLabel,BorderLayout.PAGE_END);
        setVisible(true);
    }

    public void drawPresence(int ylocation, int xlocation, IWorldObject object)
    {
        worldPanel.revalidate();
    }
    
    public void drawSpace(int ylocation, int xlocation, Space spaceBlock)
    {
        gbConstraints.gridy = ylocation;
        gbConstraints.gridx = xlocation;
        worldPanel.add(spaceBlock,gbConstraints);
        worldPanel.revalidate();
    }
    
    public void updatePopulationCount(int num)
    {
        populationLabel.setText("Pop: "+num);
    }

   
}
