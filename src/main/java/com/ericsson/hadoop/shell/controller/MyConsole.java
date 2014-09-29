package com.ericsson.hadoop.shell.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import jline.ConsoleReader;
import jline.MultiCompletor;
import jline.SimpleCompletor;

import com.ericsson.hadoop.shell.completor.CommandCompletor;

public class MyConsole {

	private static ConsoleReader myConsole;

	private MyConsole() {
	}

	public static ConsoleReader getConsole() throws IOException {

		getMyConsoleReader();
		return myConsole;
	}

	private static ConsoleReader getMyConsoleReader() throws IOException {
		if (myConsole == null) {
			myConsole = new ConsoleReader();
			myConsole.setBellEnabled(true);

			CommandCompletor cc = new CommandCompletor();
			List<String> commands = CommandUtil.getListOfCommands();

			List<SimpleCompletor> completors = new LinkedList<SimpleCompletor>();
			completors
					.add(new SimpleCompletor(commands.toArray(new String[0])));
			myConsole.addCompletor(new MultiCompletor(completors));

			for (String command : commands) {
				myConsole.addCompletor(cc.getCompletor(command));
			}
		}
		return myConsole;
	}

	public static int getHeight() {
		return myConsole.getTermheight();
	}

	public static int getWidth() {
		return myConsole.getTermwidth();
	}

	public static void print(Collection<?> data, boolean enablePagination)
			throws IOException {
		myConsole.setUsePagination(enablePagination);
		myConsole.printColumns(data);
	}

}
