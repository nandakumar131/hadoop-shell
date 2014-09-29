package com.ericsson.hadoop.shell.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ericsson.hadoop.shell.command.Command;
import com.ericsson.hadoop.shell.exception.CommandExecutionException;
import com.ericsson.hadoop.shell.util.ExitCode;

public class CommandController {

	public static final Log LOG = LogFactory.getLog(CommandController.class);

	private static CommandController myCommandController;
	private static CommandFactory commandFactory = new CommandFactory();
	private static Map<String, Command> myCommands = new HashMap<String, Command>();

	private CommandController() {

	}

	public static synchronized CommandController getInstance() {
		if (myCommandController == null)
			myCommandController = new CommandController();
		return myCommandController;
	}

	public int execute(String command, String... arguments) {
		int exitCode = ExitCode.SUCCESS;
		try {
			if (!command.equals("")) {
				if (!myCommands.containsKey(command)) {
					myCommands.put(command, commandFactory.getCommand(command));
				}
				Command myCommand = myCommands.get(command);
				if (myCommand != null) {
					LOG.info("Executing '" + command
							+ "' command with argument(s) "
							+ Arrays.asList(arguments));
					exitCode = myCommand.execute(arguments);
					LOG.info("Command '" + command + "' exited with code: "
							+ exitCode);
				} else {
					LOG.warn("Invalid Command: " + command);
					System.err.println("Invalid Command.");
					exitCode = ExitCode.COMMAND_NOT_FOUND;
				}
			}
		} catch (CommandExecutionException e) {
			LOG.error("Exception while executing '" + command + "' command", e);
			System.err.println("Exception while executing command.");
			System.out.println("Check log file for more details.");
		}
		return exitCode;
	}
}
