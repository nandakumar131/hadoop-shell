package com.ericsson.hadoop.shell.command;

import jline.Completor;
import jline.NullCompletor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ericsson.hadoop.shell.exception.CommandExecutionException;
import com.ericsson.hadoop.shell.util.ExitCode;

public class ExitCommand implements Command {

	public static final String NAME = "exit";

	private final String COMMAND_SYNTAX = "exit";

	private static final Log LOG = LogFactory.getLog(ExitCommand.class);

	public int execute(String... arguments) throws CommandExecutionException {
		LOG.info("Exiting Hadoop-Shell.");
		System.exit(ExitCode.SUCCESS);
		return ExitCode.SUCCESS;
	}

	public static Completor getCompletor() {
		return new NullCompletor();
	}

	@Override
	public String toString() {
		return COMMAND_SYNTAX;
	}

	public String help() {
		return new String(String.format("%-10s", COMMAND_SYNTAX)
				+ "-  Exit from shell");
	}

}
