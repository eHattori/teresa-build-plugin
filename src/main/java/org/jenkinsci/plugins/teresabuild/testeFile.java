package org.jenkinsci.plugins.teresabuild;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class testeFile {

	public static void main(String[] args) throws IOException {

		Runtime run = Runtime.getRuntime();

		String[] cmd = { "sh", "-c", "top" };

		try {

			Process p = run.exec(cmd);
			int count = 0;
			String s = "";
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			while (!procDone(p)) {

				while ((s = stdInput.readLine()) != null) {
					count++;
					System.out.println("result:" + count + " --- " + s + "\n");
				}
			}

			stdInput.close();
			p.waitFor();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean procDone(Process p) {
		try {
			@SuppressWarnings("unused")
			final int v = p.exitValue();
			return true;
		} catch (IllegalThreadStateException e) {
			return false;
		}
	}

}
