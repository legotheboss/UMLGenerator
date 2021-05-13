package tests;

import data_source.Class;
import data_source.Interface;
import data_source.MegaContainer;
import data_source.Relation;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class TestDataSource {
	
	MegaContainer mc = new MegaContainer();

    @Test
    public void initializeMEGA() {
    	mc.resetContainer();
        assertTrue (mc.classes.isEmpty());
        assertTrue (mc.relations.isEmpty());
        assertTrue (mc.interfaces.isEmpty());
        assertTrue (mc.inheritance.isEmpty());
    }

    @Test
    public void addClassesToMEGA() {
    	mc.resetContainer();
        
        Class x = new Class();
        x.name = "testclass";

        Class y =  new Class();
        y.name = "test2class";
        
        mc.classes.put(x.name, x);
        assertEquals(1, mc.classes.size());

        mc.classes.put(x.name, x);
        assertNotEquals(2, mc.classes.size());

        mc.classes.remove(x.name);
        assertEquals(0, mc.classes.size());
        
        mc.classes.put(y.name, y);
        mc.classes.put(x.name, x);
        
        assertEquals(2, mc.classes.size());
    }
    
    @Test
    public void addInterfacesToMEGA() {
    	mc.resetContainer();
        
        Interface i1 = new Interface();
        i1.name = "testInterface";

        Interface i2 =  new Interface();
        i2.name = "test2Interface";
        
        mc.interfaces.put("int1", i1);
        assertEquals(1, mc.interfaces.size());

        mc.interfaces.put("int1", i1);
        assertNotEquals(2, mc.interfaces.size());

        mc.interfaces.remove("int1");
        assertEquals(0, mc.interfaces.size());
        
        mc.interfaces.put("int1", i1);
        mc.interfaces.put("int2", i2);
        
        assertEquals(2, mc.interfaces.size());
    }
    
    @Test
    public void addRelationsToMEGA() {
    	mc.resetContainer();
        populateRelationMap(mc.relations);
        assertEquals(2, mc.relations.size());
    }
    
    @Test
    public void addInheritanceToMEGA() {
    	mc.resetContainer();
        populateRelationMap(mc.inheritance);
        assertEquals(2, mc.inheritance.size());
    }
    
    @Test
    public void resetMEGA() {
    	mc.resetContainer();
    	populateRelationMap(mc.relations);
    	populateRelationMap(mc.inheritance);
    	mc.classes.put("c1", new Class());
    	mc.interfaces.put("i1", new Interface());
    	mc.resetContainer();   	
    	int objSize = mc.classes.size() + mc.inheritance.size() + mc.interfaces.size() + mc.relations.size();
    	assertEquals(0, objSize);
    }
    
    @Test
    public void checkName() {
    	assertEquals("MegaContainer", new MegaContainer().getName());
    }
    
    private void populateRelationMap(HashMap<String, Relation> r)  {
        Relation r1 = new Relation(0, "test1", null, null, "->", "<-");
        Relation r2 = new Relation(0, "test2", null, null, "<-", "->");
        r.put("r1", r1);
        r.put("r2", r2);   
    }


}
