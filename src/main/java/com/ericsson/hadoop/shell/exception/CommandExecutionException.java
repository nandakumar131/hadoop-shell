package com.ericsson.hadoop.shell.exception;

public class CommandExecutionException extends Exception {

	private static final long serialVersionUID = -3451328438702494530L;

	public CommandExecutionException() {
		super();
	}

	public CommandExecutionException(String message) {
		super(message);
	}

	public CommandExecutionException(Throwable cause) {
		super(cause);
	}

	public CommandExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

}
