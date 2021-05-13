package domain;

import java.util.HashMap;

public class OptionsSingleton {

	 private static OptionsSingleton single_instance = null;
	  
	    public HashMap<String, String> options;
	  
	    private OptionsSingleton()
	    {
	        options = new HashMap<String, String>();
	    }
	  
	    public static OptionsSingleton getInstance()
	    {
	        if (single_instance == null)
	            single_instance = new OptionsSingleton();
	  
	        return single_instance;
	    }
	    
}

