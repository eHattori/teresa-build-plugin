package org.jenkinsci.plugins.teresabuild;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import hudson.model.TaskListener;

public class Utils {
	
	public static String executeCommand(String cmd, TaskListener listener) throws IOException, InterruptedException {
		try {
			
			if(listener != null)
				listener.getLogger().println(cmd);
			
			String[] cmdE = { "sh", "-c", cmd};
			
			Process process = Runtime.getRuntime().exec(cmdE);
			
			
			StringBuffer output = new StringBuffer();
			StringBuffer errorOutput = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
			String line = "";
			while ((line = reader.readLine()) != null) {
				
				if(listener != null)
						listener.getLogger().println(line);
				output.append(line + "\n");
			}
			
			process.waitFor();			
			if(process.exitValue()!= 0){
				
				String lineError = "";
				while ((lineError = stdError.readLine()) != null) {
					
					if(listener != null){
						listener.getLogger().println(lineError);
					}
					
					errorOutput.append(lineError + "\n");					
				}
				
				throw new IOException(errorOutput.toString());
			}
			
			return output.toString();
			
		}catch (IOException e) {
			
			if(listener != null)
				listener.getLogger().println("ERRO: " + e.getMessage());
				
			throw new IOException(e.getMessage());
		}	
		
	}

}
