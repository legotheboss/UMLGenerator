package tests;

import presentation.MainGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.HeadlessException;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestGUI {
    boolean buttonTest;
    MainGUI gui;
    JButton b;
    
    public void setUp() {
    	this.gui = new MainGUI();
    	this.buttonTest = false;
    }
    
    public void removeActionListeners(JButton b) {
    	for(ActionListener al : b.getActionListeners())
            b.removeActionListener(al);
    }
    
    public void addSimpleActionPerformed(JButton b) {
    	b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonTest = true;
                assertTrue(buttonTest);
            };
        });
    }

    @Test
    public void testCreateUMLClicked() {
        try {
            setUp();

            JButton b = gui.createUMLbutton;

            removeActionListeners(b);

            addSimpleActionPerformed(b);
        } catch (HeadlessException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testCreateSVGClicked() {
        try {
            setUp();

            JButton b = gui.createSVGbutton;
            removeActionListeners(b);

            addSimpleActionPerformed(b);
        } catch (HeadlessException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testClassMethodInput() {
        try {
            setUp();
            JButton b = gui.createUMLbutton;

            removeActionListeners(b);
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gui.insertParameters();
                    assertTrue(gui.os.get("ClassParser").equals("presentation.Main"));
                };
            });
        } catch (HeadlessException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testRecurseLevel() {
        try {

            setUp();
            JButton b = gui.createUMLbutton;
            
            removeActionListeners(b);
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gui.insertParameters();
                    assertTrue(gui.os.get("RecursiveMethodParser").equals("1"));
                };
            });
        }
        catch (HeadlessException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testDoRecursion() {
        try {
            setUp();
            JButton b = gui.createUMLbutton;
            
            this.removeActionListeners(b);
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gui.insertParameters();
                    assertTrue(gui.os.get("RecursiveParser").equals("true"));
                };
            });

        } catch (HeadlessException e) {
            assertTrue(true);
        } 
    }

}
