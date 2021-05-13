package data_source;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class LogWriter {
	
	private String location;

	public LogWriter(String location) {
		this.location = location;
	}
	
	public void writeText() {
		writeText("test");
	}
	
	public void writeText(String text) {
		try {
			@SuppressWarnings("resource")
			PrintStream out=null;
			try {
				out = new PrintStream(location, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.println(text);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
