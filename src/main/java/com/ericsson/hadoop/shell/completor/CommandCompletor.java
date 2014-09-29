package com.ericsson.hadoop.shell.completor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import jline.ArgumentCompletor;
import jline.Completor;
import jline.SimpleCompletor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ericsson.hadoop.shell.command.Command;
import com.ericsson.hadoop.shell.controller.CommandUtil;

public class CommandCompletor {

	public static final Log LOG = LogFactory.getLog(CommandCompletor.class);

	private static final String COMPLETOR_METHOD = "getCompletor";

	public Completor getCompletor(String command) throws IOException {
		List<Completor> completors = new LinkedList<Completor>();
		completors.add(new SimpleCompletor(command));
		Completor commandCompletor = null;
		try {
			Class<Command> commandClass = CommandUtil.getCommandClass(command);
			Method completor = commandClass.getDeclaredMethod(COMPLETOR_METHOD,
					(Class<?>[]) null);
			commandCompletor = (Completor) completor.invoke(null,
					(Object[]) null);
		} catch (Exception e) {
			// There is no completor for this command, so do nothing.
			LOG.debug("No completor defined for " + command + "command.");
		}
		if (commandCompletor != null) {
			LOG.debug("Adding " + commandCompletor + " completor for " + command
					+ " command.");
			completors.add(commandCompletor);
		}
		return new ArgumentCompletor(completors);
	}

}
