package org.jenkinsci.plugins.teresabuild;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;

/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked and a new
 * {@link TeresaBuilder} is created. The created instance is persisted to the
 * project configuration XML by using XStream, so this allows you to use
 * instance fields (like {@link #login}) to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform} method will be invoked.
 *
 * @author Eduardo Hattori
 */
public class TeresaBuilder extends Builder implements SimpleBuildStep {

	private final String login;
	private final String password;
	private final String server;	
	private final String clusterName;
	private final String command;	
	private TaskListener listener;

	/**
	 * We'll use this from the {@code config.jelly}.
	 */
	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public String getServer() {
		return server;
	}

	public String getCommand() {
		return command;
	}
	
	public String getClusterName() {
		return clusterName;
	}	
	
	private void configureTeresa() throws Exception{
		
		Utils.executeCommand("teresa config set-cluster "+ this.getClusterName() +" -s " + this.getServer(), this.listener);
		Utils.executeCommand("teresa config use-cluster " + this.getClusterName(), this.listener);
		
		Utils.executeCommand("echo '"+ this.getPassword() + "' | teresa login --user " + this.getLogin(),  null);	
	}

	// Fields in config.jelly must match the parameter names in the
	// "DataBoundConstructor"
	@DataBoundConstructor
	public TeresaBuilder(String login, String password, String server, String clusterName, String command) {
		this.login = login;
		this.password = password;
		this.server = server;		
		this.clusterName = clusterName;
		this.command = command;
	}

	@Override
	public void perform(Run<?, ?> build, FilePath workspace, Launcher launcher, TaskListener listener) {

		// This is where you 'build' the project.
		// Since this is a dummy, we just say 'hello world' and call that a
		// build.

		// This also shows how you can consult the global configuration of the
		// builder
		this.listener = listener;
		
		listener.getLogger().println("Login:  " + this.login );		
		listener.getLogger().println("Server:  " + this.server );
		listener.getLogger().println("Cluster Name:  " + this.clusterName );		
		listener.getLogger().println("Command :  " + this.command);
		listener.getLogger().println("Client Version : " + this.getDescriptor().getInstalations());
		
		listener.getLogger().println("Configure Teresa Server");
		try {
			this.configureTeresa();
			
			listener.getLogger().println("Execute Command: ");
			Utils.executeCommand(command,this.listener);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Overridden for better type safety.
	// If your plugin doesn't really define any property on Descriptor,
	// you don't have to do this.
	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	/**
	 * Descriptor for {@link TeresaBuilder}. Used as a singleton. The class is
	 * marked as public so that it can be accessed from views.
	 *
	 * <p>
	 * See
	 * {@code src/main/resources/org/jenkinscli/plugins/teresabuild/HelloWorldBuilder/*.jelly}
	 * for the actual HTML fragment for the configuration screen.
	 */
	@Extension // This indicates to Jenkins that this is an implementation of an
				// extension point.
	public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

		private String instalations;

		/**
		 * In order to load the persisted global configuration, you have to call
		 * load() in the constructor.
		 */
		public DescriptorImpl() {
			String output = Utils.executeCommand("teresa version", null);			
			this.instalations = output;
			
			load();
		}
		
		public String getInstalations(){
			return this.instalations;
		}
		
		public FormValidation doCheckVersion(@QueryParameter String value) throws IOException, ServletException {	
			System.out.println(value);
			if (value.length() == 0)
				return FormValidation.error("Teresa-cli is not available see: <https://github.com/luizalabs/teresa-cli> ");
			
			return FormValidation.ok();
		}
		
		public FormValidation doCheckLogin(@QueryParameter String value) throws IOException, ServletException {	
			if (value.length() == 0)
				return FormValidation.error("Please set a Login");
			
			return FormValidation.ok();
		}
		
		public FormValidation doCheckPassword(@QueryParameter String value) throws IOException, ServletException {	
			if (value.length() == 0)
				return FormValidation.error("Please set a Password");
			
			return FormValidation.ok();
		}
		
		public FormValidation doCheckServer(@QueryParameter String value) throws IOException, ServletException {	
			if (value.length() == 0)
				return FormValidation.error("Please set a URI Server");
			
			return FormValidation.ok();
		}
		
		public FormValidation doCheckClusterName(@QueryParameter String value) throws IOException, ServletException {	
			if (value.length() == 0)
				return FormValidation.error("Please set a Cluster Name");
			
			return FormValidation.ok();
		}

		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			// Indicates that this builder can be used with all kinds of project
			// types
			return true;
		}

		/**
		 * This human readable name is used in the configuration screen.
		 */
		public String getDisplayName() {
			return "Teresa Build";
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
			save();
			return super.configure(req, formData);
		}
	}
}
