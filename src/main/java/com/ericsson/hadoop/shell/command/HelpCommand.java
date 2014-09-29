package com.ericsson.hadoop.shell.command;

import java.util.Collections;
import java.util.List;

import jline.Completor;
import jline.SimpleCompletor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ericsson.hadoop.shell.controller.CommandFactory;
import com.ericsson.hadoop.shell.controller.CommandUtil;
import com.ericsson.hadoop.shell.exception.CommandExecutionException;
import com.ericsson.hadoop.shell.util.ExitCode;

public class HelpCommand implements Command {

	public static final String NAME = "help";

	private final String COMMAND_SYNTAX = "help";

	private static final Log LOG = LogFactory.getLog(HelpCommand.class);

	private CommandFactory commandFactory = new CommandFactory();

	public int execute(String... arguments) throws CommandExecutionException {
		int exitCode = ExitCode.SUCCESS;
		if (arguments.length == 0) {
			listCommands();
		} else {
			String commandName = arguments[0];
			String commandHelp = getHelp(commandName);
			if (commandHelp != null) {
				System.out.println(commandHelp);
			} else {

				exitCode = ExitCode.ERROR;
				System.out.println("No help found for " + commandName + ".");
				LOG.warn("No help found for " + commandName + ".");
			}
		}
		return exitCode;
	}

	private void listCommands() {
		System.out.println("Commands");
		System.out.println("--------------------");
		List<String> commandList = CommandUtil.getListOfCommands();
		Collections.sort(commandList);
		for (String command : commandList) {
			System.out.println(commandFactory.getCommand(command));
		}
	}

	private String getHelp(String command) {
		Command theCommand = commandFactory.getCommand(command);
		if (theCommand != null) {
			return theCommand.help();
		} else {
			return null;
		}
	}

	public static Completor getCompletor() {
		return new SimpleCompletor(CommandUtil.getListOfCommands().toArray(
				new String[0]));
	}

	public String help() {
		return new String(String.format("%-10s", COMMAND_SYNTAX)
				+ "-  Prints the help message");
	}

	@Override
	public String toString() {
		return COMMAND_SYNTAX;
	}

}
