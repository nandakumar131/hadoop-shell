package com.ericsson.hadoop.shell.controller;

import java.lang.reflect.Constructor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ericsson.hadoop.shell.command.Command;

public class CommandFactory {

	public static final Log LOG = LogFactory.getLog(CommandFactory.class);

	@SuppressWarnings("unchecked")
	public Command getCommand(String command) {
		String commandClass = CommandUtil.getCommandClassName(command);
		if (commandClass != null) {
			try {
				LOG.info("Loading command '" + command + "' from class "
						+ commandClass);
				Constructor<Command> defaulConstructor = null;
				Constructor<Command> constructors[] = (Constructor<Command>[]) Class
						.forName(commandClass).getDeclaredConstructors();
				for (Constructor<Command> constructor : constructors) {
					if (constructor.getGenericParameterTypes().length == 0) {
						defaulConstructor = constructor;
						break;
					}
				}
				return (Command) defaulConstructor.newInstance();
			} catch (Exception e) {
				LOG.error("Exception while loading command " + command, e);
			}
		}
		LOG.warn("No mapping class found for the command '" + command
				+ "' in commands.xml file.");
		return null;
	}

}
