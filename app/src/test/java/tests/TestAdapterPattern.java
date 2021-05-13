package tests;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

import data_source.Argument;
import data_source.Class;
import data_source.Field;
import data_source.Interface;
import data_source.MegaContainer;
import data_source.Method;
import data_source.MethodCalled;
import domain.AdapterPattern;

public class TestAdapterPattern {
	MegaContainer mc;

	public void InstantiateMEGA() {	
		mc = new MegaContainer();
		
		Interface intf = new Interface();
		intf.name = "method_adapter";
		Method methd = new Method();
		methd.name = "method_adapter";
		intf.methods.add(methd);
		
		Class adap = new Class();
		adap.name = "method_adapter";
		adap.inherited.add("method_adapter");
		
		Field fld = new Field();
		fld.packageType = "method_adapter";
		adap.fields.add(fld);
		
		Method mtd = new Method();
		mtd.name = "method_adapter";
		
		MethodCalled metcc = new MethodCalled("method_adapter", "method_adapter");
		mtd.methodsCalled.add(metcc);
		
		adap.methods.add(mtd);
		
		adap.inherited.add("method_adapter");
		
		mc.classes.put("method_adapter", adap);
		mc.interfaces.put("method_adapter", intf);
	}
	
	@Test
	public void IdentifiesAdapterPattern() {
		this.InstantiateMEGA();
		
		AdapterPattern ap = new AdapterPattern(mc, "red");
		assertEquals(true, ap.check("method_adapter"));
	}
}
