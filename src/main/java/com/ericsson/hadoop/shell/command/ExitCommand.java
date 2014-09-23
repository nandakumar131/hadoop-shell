package com.ericsson.hadoop.shell.command;

import com.ericsson.hadoop.shell.util.ExitCode;

import jline.Completor;
import jline.NullCompletor;

public class ExitCommand implements Command {

	public static final String NAME = "exit";

	private final String COMMAND_SYNTAX = "exit";

	@Override
	public int execute(String... arguments) {
		System.exit(0);
		return ExitCode.SUCCESS;
	}

	public static Completor getCompletor() {
		return new NullCompletor();
	}

	@Override
	public String toString() {
		return COMMAND_SYNTAX;
	}

	@Override
	public String help() {
		return new String(String.format("%-10s", COMMAND_SYNTAX)
				+ "-  Exit from shell");
	}

}
