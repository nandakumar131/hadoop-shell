package com.ericsson.hadoop.shell.command;

import com.ericsson.hadoop.shell.exception.CommandExecutionException;

public interface Command {

	public int execute(String... arguments) throws CommandExecutionException;

	public String help();

}
