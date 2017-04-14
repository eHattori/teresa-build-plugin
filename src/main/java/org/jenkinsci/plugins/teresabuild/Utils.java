package org.jenkinsci.plugins.teresabuild;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import hudson.model.TaskListener;

public class Utils {
	
	public static String executeCommand(String cmd, TaskListener listener) {
		try {
			
			if(listener != null)
				listener.getLogger().println(cmd);
			
			String[] cmdE = { "sh", "-c", cmd};
			
			Process process = Runtime.getRuntime().exec(cmdE);
			
			
			StringBuffer output = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			String line = "";
			while ((line = reader.readLine()) != null) {
				
				if(listener != null)
						listener.getLogger().println(line);
				output.append(line + "\n");
			}
			
			process.waitFor();
			
			return output.toString();
			
		}catch (Exception e) {
			
			if(listener != null)
				listener.getLogger().println(e.getMessage());
				
			e.printStackTrace();
		}
		
		return "";
	}

}
