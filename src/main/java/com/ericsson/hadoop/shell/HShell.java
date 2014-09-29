package com.ericsson.hadoop.shell;

import jline.ConsoleReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.Path;

import com.ericsson.hadoop.shell.controller.CommandController;
import com.ericsson.hadoop.shell.controller.MyConsole;
import com.ericsson.hadoop.shell.environment.EnvVariables;
import com.ericsson.hadoop.shell.environment.HShellEnv;
import com.ericsson.hadoop.shell.filesystem.HDFS;
import com.ericsson.hadoop.shell.util.ExitCode;

public class HShell {

	private static final String PROMPT = "hdfs:";
	private static final HShellEnv env = HShellEnv.getInstance();
	private static final Log LOG = LogFactory.getLog(HShell.class);

	/**
	 * Main function of HShell.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {

		LOG.info("Starting Hadoop-Shell!");
		addShutdownHook();

		LOG.info("Checking HDFS status.");
		if (!HDFS.isRunning()) {
			System.err.println("Not able to access HDFS.");
			System.err.println("Check log file for more details.");
			LOG.error("Not able to access HDFS.");
			System.exit(1);
		}
		
		ConsoleReader consoleReader = MyConsole.getConsole();
		CommandController commandController = CommandController.getInstance();

		// SplashScreen.render();

		while (true) {
			int exitCode = ExitCode.SUCCESS;
			String[] input = null;
			String command = null;
			String[] arguments = null;
			input = consoleReader
					.readLine(PROMPT + new Path(env.getPWD()) + " > ").trim()
					.split("\\s+");
			command = input[0];
			// TODO: handle "" for arguments
			arguments = new String[input.length - 1];

			System.arraycopy(input, 1, arguments, 0, input.length - 1);

			exitCode = commandController.execute(command, arguments);
			env.put(EnvVariables.EXIT_CODE, String.valueOf(exitCode));

		}
	}

	private static void addShutdownHook() {
		// Register a shutdown hook and call it when the JVM terminates
		Thread closeThread = new Thread("Close Thread") {
			@Override
			public void run() {
				try {
					LOG.debug("Executing ShutdownHook.");
					HDFS.close();
					LOG.info("Bye!");
				} catch (Exception e) {
					LOG.error("Exception while executing shutdownHook", e);
				}
			}
		};
		// Register the shutdown hook
		Runtime.getRuntime().addShutdownHook(closeThread);
		LOG.info("ShutdownHook added.");
	}

}
