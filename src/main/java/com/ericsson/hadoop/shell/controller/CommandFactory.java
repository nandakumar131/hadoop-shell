package com.ericsson.hadoop.shell.controller;

import java.lang.reflect.Constructor;

import com.ericsson.hadoop.shell.command.Command;

public class CommandFactory {

	@SuppressWarnings("unchecked")
	public Command getCommand(String command) {
		if (CommandUtil.getCommandClassName(command) != null) {
			try {
				Constructor<Command> defaulConstructor = null;
				Constructor<Command> constructors[] = (Constructor<Command>[]) Class
						.forName(CommandUtil.getCommandClassName(command))
						.getDeclaredConstructors();
				for (Constructor<Command> constructor : constructors) {
					if (constructor.getGenericParameterTypes().length == 0) {
						defaulConstructor = constructor;
						break;
					}
				}
				return (Command) defaulConstructor.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
