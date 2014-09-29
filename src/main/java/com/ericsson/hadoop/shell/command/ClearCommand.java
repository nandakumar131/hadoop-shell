package com.ericsson.hadoop.shell.command;

import com.ericsson.hadoop.shell.exception.CommandExecutionException;
import com.ericsson.hadoop.shell.util.ExitCode;

import jline.Completor;
import jline.NullCompletor;

public class ClearCommand implements Command {

	public static final String NAME = "clear";

	private final String COMMAND_SYNTAX = "clear";

	public int execute(String... arguments) throws CommandExecutionException {
		System.out.print("\033[H\033[2J");
		System.out.flush();
		/*
		 * try { MyConsole.getConsoleReader().clearScreen(); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */
		return ExitCode.SUCCESS;
	}

	public static Completor getCompletor() {
		return new NullCompletor();
	}

	public String help() {
		return new String(String.format("%-10s", COMMAND_SYNTAX)
				+ "-  Clears the console");
	}

	@Override
	public String toString() {
		return COMMAND_SYNTAX;
	}

}
