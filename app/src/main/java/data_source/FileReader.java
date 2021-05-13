package data_source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

public class FileReader {

	private String location;

	public FileReader(String filename) {
		this.location = filename;
	}
	
	public HashSet<String> blockEm() {
		HashSet<String> result = new HashSet<String>();
		
		InputStream is;
		try {
			is = new FileInputStream(location);
			@SuppressWarnings("resource")
			BufferedReader buf = new BufferedReader(new InputStreamReader(is));
	        
			String line = buf.readLine();
			StringBuilder sb = new StringBuilder();
			        
			while(line != null){
			   sb.append(line).append("\n");
			   result.add(line);
			   result.add(line.replace('.', '/'));
			   line = buf.readLine();
			}
			

			return result;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
}
