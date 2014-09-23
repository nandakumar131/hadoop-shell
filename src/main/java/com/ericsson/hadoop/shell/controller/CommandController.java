package com.ericsson.hadoop.shell.controller;

import java.util.HashMap;
import java.util.Map;

import com.ericsson.hadoop.shell.command.Command;
import com.ericsson.hadoop.shell.util.ExitCode;

public class CommandController {

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
					exitCode = myCommand.execute(arguments);
				} else {
					exitCode = ExitCode.COMMAND_NOT_FOUND;
					System.err.println("Invalid Command.");
				}
			}
		} catch (Exception e) {
			System.err.println("Error.");
			e.printStackTrace();
		}
		return exitCode;
	}
}
