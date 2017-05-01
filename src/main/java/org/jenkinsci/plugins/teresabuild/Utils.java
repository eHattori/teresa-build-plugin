package org.jenkinsci.plugins.teresabuild;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import hudson.Launcher;
import hudson.Launcher.ProcStarter;
import hudson.Proc;
import hudson.model.Hudson;
import hudson.model.TaskListener;
import hudson.util.ArgumentListBuilder;

public class Utils {

	public static int executeCommand(String cmd, Launcher launcher, Boolean stout)
			throws IOException, InterruptedException {
		try {
			
			if(stout)
				launcher.getListener().getLogger().println(cmd);
			
			String[] cmdE = { "sh", "-c", cmd };
			
			ArgumentListBuilder args = new ArgumentListBuilder(cmdE);			
			ProcStarter ps = Hudson.getInstance().createLauncher(TaskListener.NULL).launch();
			
			ps.cmds(args);
			ps.envs(new String[0]);
			ps.stdout(launcher.getListener());
			
			ps.pwd(Hudson.getInstance().getRootPath());
	        Proc proc = Hudson.getInstance().createLauncher(TaskListener.NULL).launch(ps);

			int retcode = proc.join();

			if (retcode != 0) {
				
				BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getStdout()));
				StringBuffer errorOutput = new StringBuffer();

				String lineError = "";
				while ((lineError = stdError.readLine()) != null) {

					if (stout)
						launcher.getListener().getLogger().println(lineError);
					errorOutput.append(lineError + "\n");
				}

				throw new IOException(errorOutput.toString());
			}

			return retcode;

		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}

	}

}
