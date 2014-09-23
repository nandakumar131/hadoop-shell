package com.ericsson.hadoop.shell;

import jline.ConsoleReader;

import org.apache.hadoop.fs.Path;

import com.ericsson.hadoop.shell.controller.CommandController;
import com.ericsson.hadoop.shell.controller.MyConsole;
import com.ericsson.hadoop.shell.environment.EnvVariables;
import com.ericsson.hadoop.shell.environment.HShellEnv;
import com.ericsson.hadoop.shell.util.ExitCode;

public class HShell {

	private static final String PROMPT = "hdfs:";
	private static final HShellEnv env = HShellEnv.getInstance();

	/**
	 * Main function of HShell.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {

		ConsoleReader consoleReader = MyConsole.getConsole();
		CommandController commandController = CommandController.getInstance();

		// TODO: check whether HDFS is running or not!

		//SplashScreen.render();

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

}
