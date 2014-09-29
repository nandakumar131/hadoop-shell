package com.ericsson.hadoop.shell.command;

import java.util.List;
import java.util.NoSuchElementException;

import jline.Completor;
import jline.SimpleCompletor;

import com.ericsson.hadoop.shell.configuration.Configuration;
import com.ericsson.hadoop.shell.exception.CommandExecutionException;
import com.ericsson.hadoop.shell.util.ExitCode;

public class SetConfigCommand implements Command {

	private final String COMMAND_SYNTAX = "setConfig <property name> <value>";

	Configuration conf = Configuration.getInstance();

	public int execute(String... arguments) throws CommandExecutionException {
		int exitCode = ExitCode.SUCCESS;
		try {
			String oldValue = conf.getProperty(arguments[0]);
			conf.setProperty(arguments[0], arguments[1]);
			System.out.println("Old Value => " + oldValue);
			System.out.println("New Value => " + arguments[1]);
		} catch (NoSuchElementException e) {
			exitCode = ExitCode.ERROR;
			System.err.println("Property not found!");
		} catch (ArrayIndexOutOfBoundsException e) {
			exitCode = ExitCode.ERROR;
			System.err.println("Wrong number of arguments.");
		}
		return exitCode;
	}

	public static Completor getCompletor() {
		List<String> propertyKeys = Configuration.getInstance().getKeysAsList();
		return new SimpleCompletor(propertyKeys.toArray(new String[0]));
	}

	@Override
	public String toString() {
		return COMMAND_SYNTAX;
	}

	public String help() {
		return new String(String.format("%-10s", COMMAND_SYNTAX)
				+ "  -  Sets the value of given configuration property.");
	}

}
