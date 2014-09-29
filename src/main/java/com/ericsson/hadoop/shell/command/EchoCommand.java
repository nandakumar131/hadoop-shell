package com.ericsson.hadoop.shell.command;

import jline.Completor;
import jline.NullCompletor;

import com.ericsson.hadoop.shell.exception.CommandExecutionException;
import com.ericsson.hadoop.shell.util.ExitCode;

public class EchoCommand extends AbstractHDFSCommand {

	public static final String NAME = "echo";

	private final String COMMAND_SYNTAX = "echo <string>";

	public int execute(String... arguments) throws CommandExecutionException {
		StringBuffer stringBuffer = new StringBuffer();
		for (String argument : arguments) {
			stringBuffer.append(getValue(argument));
			stringBuffer.append(" ");
		}
		System.out.println(stringBuffer);
		return ExitCode.SUCCESS;
	}

	private String getValue(String argument) {
		String result;
		if (argument.startsWith("$")) {
			String key = argument.replace("$", "");
			String value = env.get(key);
			result = value == null ? "" : value;
		} else {
			result = argument;
		}
		return result;
	}

	public static Completor getCompletor() {
		return new NullCompletor();
	}

	public String help() {
		return new String(String.format("%-10s", COMMAND_SYNTAX)
				+ "-  Display a line of text");
	}

	@Override
	public String toString() {
		return COMMAND_SYNTAX;
	}

}
