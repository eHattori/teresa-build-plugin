package org.jenkinsci.plugins.teresabuild;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Utils {
	
	public static String executeCommand(String cmd) {
		try {
			
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
			
			StringBuffer output = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			String line = "";
			while ((line = reader.readLine()) != null) {				
				output.append(line + "\n");
			}		
			
			return output.toString();
			
		}catch (Exception e) {			
			e.printStackTrace();
		}
		
		return "";
	}

}
