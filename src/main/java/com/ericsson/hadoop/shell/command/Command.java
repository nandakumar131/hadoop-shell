package com.ericsson.hadoop.shell.command;

public interface Command {

	public int execute(String... arguments);

	public String help();

}
