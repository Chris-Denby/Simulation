/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Universe;

import Constants.World_Constants;
import Entities.Being;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.util.HashSet;
import java.util.Hashtable;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author chris
 */
public class VisibleWorld extends javax.swing.JFrame
{
    
    World world;
    
    IWorldObject focussedBeing;
        
    
    Dimension dimensions = new Dimension(800,600);
    GridBagLayout worldGrid;
    GridBagConstraints gbConstraints;
    JPanel worldPanel = new JPanel();
    
    GridBagLayout beingViewGrid;
    GridBagConstraints beingViewgbConstraints;
    JPanel beingViewPanel = new JPanel();
    
    //UI Components
    JLabel populationLabel = new JLabel();
    JButton startButton = new JButton("POPULATE");
    
    JSlider beingMetabolicRateSlider;
    JLabel beingMetabolicRateSliderLabel = new JLabel("Metabolic Rate");
    JSlider beingLifeSlider;
    JLabel beingLifeSliderLabel = new JLabel("Lifespan");
    JSlider beingSightRangeSlider;
    JLabel beingSightRangeSliderLabel = new JLabel("Sight Range");
    JSlider beingChildLimitSlider;
    JLabel beingChildLimitSliderLabel = new JLabel("Child Limit");
    //JSlider virusBirthChanceSlider;
    JSlider virusContageonRateSlider;
    JLabel virusContageonRateSliderLabel = new JLabel("Virus Contageon Rate");
    JSlider virusMortalityRateSlider;
    JLabel virusMortalityRateSliderLabel = new JLabel("Virus Mortality Rate");

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
        worldPanel.setLayout(worldGrid);
        
        beingViewGrid = new GridBagLayout();
        beingViewgbConstraints = new GridBagConstraints();
        beingViewgbConstraints.fill = GridBagConstraints.BOTH;
        beingViewPanel.setLayout(beingViewGrid);

        //UI ELEMENTS
        
        Font sliderFont = new Font("Serif",Font.PLAIN, 8);
        JLabel sliderMinLabel = new JLabel("Min");
        JLabel sliderMaxLabel = new JLabel("Max");
        sliderMinLabel.setFont(sliderFont);
        sliderMaxLabel.setFont(sliderFont);
        
        beingMetabolicRateSlider = new JSlider(JSlider.HORIZONTAL,75,1000,200);
        //beingMetabolicRateSlider.setMajorTickSpacing(100);
        beingMetabolicRateSlider.setPaintTicks(true);
        beingMetabolicRateSlider.setPaintLabels(true);
        beingMetabolicRateSlider.setFont(sliderFont);
        Hashtable sliderLabels0 = new Hashtable();
        sliderLabels0.put(1000, sliderMinLabel);
        sliderLabels0.put(75, sliderMaxLabel);
        beingMetabolicRateSlider.setLabelTable(sliderLabels0);
        beingMetabolicRateSliderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        beingMetabolicRateSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        beingLifeSlider = new JSlider(JSlider.HORIZONTAL,World_Constants.BEING_LIFE_MIN, World_Constants.BEING_LIFE_MAX, World_Constants.BEING_LIFE);
        //beingLifeSlider.setMajorTickSpacing(100);
        beingLifeSlider.setPaintTicks(true);
        beingLifeSlider.setPaintLabels(true);
        beingLifeSlider.setFont(sliderFont);
        Hashtable sliderLabels1 = new Hashtable();
        sliderLabels1.put(World_Constants.BEING_LIFE_MIN, sliderMinLabel);
        sliderLabels1.put(World_Constants.BEING_LIFE_MAX, sliderMaxLabel);
        beingLifeSlider.setLabelTable(sliderLabels1);
        beingLifeSliderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        beingLifeSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        beingSightRangeSlider = new JSlider(JSlider.HORIZONTAL,World_Constants.BEING_SIGHT_RANGE_MIN, World_Constants.BEING_SIGHT_RANGE_MAX, World_Constants.BEING_SIGHT_RANGE);
        beingSightRangeSlider.setMajorTickSpacing(100);
        beingSightRangeSlider.setPaintTicks(true);
        beingSightRangeSlider.setPaintLabels(true);
        beingSightRangeSlider.setFont(sliderFont);
        Hashtable sliderLabels2 = new Hashtable();
        sliderLabels2.put(World_Constants.BEING_SIGHT_RANGE_MIN, sliderMinLabel);
        sliderLabels2.put(World_Constants.BEING_SIGHT_RANGE_MAX, sliderMaxLabel);
        beingSightRangeSlider.setLabelTable(sliderLabels2);
        beingSightRangeSliderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        beingSightRangeSlider.setAlignmentX(Component.LEFT_ALIGNMENT);

        beingChildLimitSlider = new JSlider(JSlider.HORIZONTAL,World_Constants.BEING_CHILD_LIMIT_MIN, World_Constants.BEING_CHILD_LIMIT_MAX, World_Constants.BEING_CHILD_LIMIT);
        beingChildLimitSlider.setMajorTickSpacing(100);
        beingChildLimitSlider.setPaintTicks(true);
        beingChildLimitSlider.setPaintLabels(true);
        beingChildLimitSlider.setFont(sliderFont);
        Hashtable sliderLabels3 = new Hashtable();
        sliderLabels3.put(World_Constants.BEING_CHILD_LIMIT_MIN, sliderMinLabel);
        sliderLabels3.put(World_Constants.BEING_CHILD_LIMIT_MAX, sliderMaxLabel);
        beingChildLimitSlider.setLabelTable(sliderLabels3);
        beingChildLimitSliderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        beingChildLimitSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        //virusBirthChanceSlider = new JSlider(JSlider.HORIZONTAL,World_Constants.BIRTH_CHANCE_OF_VIRUS_MIN, World_Constants.BIRTH_CHANCE_OF_VIRUS_MAX, World_Constants.BIRTH_CHANCE_OF_VIRUS);
        
        virusContageonRateSlider = new JSlider(JSlider.HORIZONTAL,World_Constants.VIRUS_CONTAGEON_RATE_MIN, World_Constants.VIRUS_CONTAGEON_RATE_MAX, World_Constants.VIRUS_CONTAGEON_RATE);
        virusContageonRateSlider.setMajorTickSpacing(100);
        virusContageonRateSlider.setPaintTicks(true);
        virusContageonRateSlider.setPaintLabels(true);
        virusContageonRateSlider.setFont(sliderFont);
        Hashtable sliderLabels4 = new Hashtable();
        sliderLabels4.put(World_Constants.VIRUS_CONTAGEON_RATE_MIN, sliderMinLabel);
        sliderLabels4.put(World_Constants.VIRUS_CONTAGEON_RATE_MAX, sliderMaxLabel);
        virusContageonRateSlider.setLabelTable(sliderLabels4);
        virusContageonRateSliderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        virusContageonRateSlider.setAlignmentX(Component.LEFT_ALIGNMENT);

        virusMortalityRateSlider = new JSlider(JSlider.HORIZONTAL,1, 10, 5);
        virusMortalityRateSlider.setMajorTickSpacing(100);
        virusMortalityRateSlider.setPaintTicks(true);
        virusMortalityRateSlider.setPaintLabels(true);
        virusMortalityRateSlider.setFont(sliderFont);
        Hashtable sliderLabels5 = new Hashtable();
        sliderLabels5.put(10, sliderMinLabel);
        sliderLabels5.put(1, sliderMaxLabel);
        virusMortalityRateSlider.setLabelTable(sliderLabels5);
        virusMortalityRateSliderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        virusMortalityRateSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        //UI LISTENERS
        
        startButton.addActionListener(e ->{
                //world.stopThreads();
                if(world.getWorldPopulation()==0)
                    world.populate(50);
               
        });

        beingMetabolicRateSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                World_Constants.BEING_METABOLIC_RATE = beingMetabolicRateSlider.getValue();
            }
        });
        
        beingLifeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                World_Constants.BEING_LIFE = beingLifeSlider.getValue();
            }
        });
        
        beingSightRangeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                World_Constants.BEING_SIGHT_RANGE = beingSightRangeSlider.getValue();
            }
        });
        
        beingChildLimitSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                World_Constants.BEING_CHILD_LIMIT = beingChildLimitSlider.getValue();
            }
        });      
        
        virusContageonRateSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                World_Constants.VIRUS_CONTAGEON_RATE = virusContageonRateSlider.getValue();
            }
        });
        
        virusMortalityRateSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                World_Constants.VIRUS_CONTAGEON_RATE = virusMortalityRateSlider.getValue()/10;
            }
        });
        
        
        
        //ADD UI ELEMENTS
        
        JPanel controlPanel = new JPanel();
        
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel,BoxLayout.Y_AXIS));
        sliderPanel.setBorder(new EmptyBorder(20,20,20,20));
        sliderPanel.add(beingMetabolicRateSliderLabel);
        sliderPanel.add(beingMetabolicRateSlider);      
        sliderPanel.add(beingLifeSliderLabel);
        sliderPanel.add(beingLifeSlider);
        sliderPanel.add(beingSightRangeSliderLabel);
        sliderPanel.add(beingSightRangeSlider);
        sliderPanel.add(beingChildLimitSliderLabel);
        sliderPanel.add(beingChildLimitSlider);
        sliderPanel.add(virusContageonRateSliderLabel);
        sliderPanel.add(virusContageonRateSlider);
        sliderPanel.add(virusMortalityRateSliderLabel);
        sliderPanel.add(virusMortalityRateSlider);
        sliderPanel.add(startButton);  
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(20,20,20,20));
        //buttonPanel.add(startButton);
        
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(populationLabel);
        controlPanel.add(sliderPanel);
        controlPanel.add(buttonPanel);
        controlPanel.add(beingViewPanel);
        
        //ADD TO WINDOW
        this.add(worldPanel,BorderLayout.WEST);
        this.add(controlPanel,BorderLayout.EAST);
        
        setVisible(true);
    }

    public void drawPresence(int ylocation, int xlocation, IWorldObject object)
    {
        worldPanel.revalidate();
    }
    
    public void drawSpace(int ylocation, int xlocation, Space spaceBlock)
    {
        
        //add mouse listener to each space
        spaceBlock.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(spaceBlock.getObjectInSpace() instanceof Being)
                {
                    //remove focus from current being
                    if(focussedBeing!=null)
                    focussedBeing.setFocussed(false);
                    //add focus to selected being
                    focussedBeing = spaceBlock.getObjectInSpace();
                    focussedBeing.setFocussed(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) 
            {
                if(e.isShiftDown())
                {
                    spaceBlock.setInanimate();                   
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        gbConstraints.gridy = ylocation;
        gbConstraints.gridx = xlocation;
        worldPanel.add(spaceBlock,gbConstraints);
        worldPanel.revalidate();
        
    }
    
    public void clearBeingview()
    {
        beingViewPanel.removeAll();
    }
    
    public void updateBeingView(int ylocation, int xlocation, Space spaceBlock)
    {
        Space space = new Space(ylocation,xlocation);
        space.setPreferredSize(new Dimension(10,10));
        if(spaceBlock!=null)
            space.setBackground(Color.yellow);
                
        try{
        if(spaceBlock!= null & spaceBlock.getObjectInSpace()!=null)
            space.setObject(spaceBlock.getObjectInSpace());
        }
        catch(NullPointerException ex){}
        
        beingViewgbConstraints.gridy = ylocation;
        beingViewgbConstraints.gridx = xlocation;
        beingViewPanel.add(space,beingViewgbConstraints); 
        beingViewPanel.revalidate();  
    }
    
    public void updatePopulationCount(int num)
    {
        populationLabel.setText("Pop: "+num);
        //System.out.println("Theads: " + Thread.activeCount());
        if(num==100)
        {
            Runtime.getRuntime().gc();
        }
    }
    

    public void bind(World world)
    {
        this.world = world;
    }
       
}
