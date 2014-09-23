package com.ericsson.hadoop.shell.command;

import jline.Completor;
import jline.NullCompletor;

import com.ericsson.hadoop.shell.environment.HShellEnv;
import com.ericsson.hadoop.shell.util.ExitCode;

public class PWDCommand implements Command {

	private final String COMMAND_SYNTAX = "pwd";

	public static final String NAME = "pwd";
	private static final HShellEnv env = HShellEnv.getInstance();

	@Override
	public int execute(String... arguments) {
		System.out.println(env.getPWD());
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
				+ "- Print name of current/working directory");
	}

}
