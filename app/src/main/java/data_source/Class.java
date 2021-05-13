package data_source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Class extends IContainer{
	public String superclass;
	public boolean isAbstract = false;
	public HashSet<Field> fields = new HashSet<Field>();
	public ArrayList<Method> methods = new ArrayList<Method>();
	public HashSet<String> inherited = new HashSet<String>();
}
