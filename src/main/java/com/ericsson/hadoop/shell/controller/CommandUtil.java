package com.ericsson.hadoop.shell.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ericsson.hadoop.shell.command.Command;

public class CommandUtil {

	private static final String COMMAND_FILE = "/commands.xml";

	private static final String PROP_COMMAND_NAME = "name";
	private static final String PROP_COMMAND_CLASS = "class";

	private static final Log LOG = LogFactory.getLog(CommandUtil.class);

	private static List<String> commands = new ArrayList<String>();
	private static Map<String, String> commandClasseNames = new HashMap<String, String>();

	static {
		try {
			loadCommands();
		} catch (Exception e) {
			LOG.error("Exception while parsing " + COMMAND_FILE, e);
		}
	}

	private static void loadCommands() throws Exception {
		File commandXML = new File(CommandUtil.class.getResource(COMMAND_FILE)
				.getPath());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		dBuilder = dbFactory.newDocumentBuilder();
		Document xmlDoc = dBuilder.parse(commandXML);
		xmlDoc.getDocumentElement().normalize();

		NodeList commandList = xmlDoc.getDocumentElement().getChildNodes();

		for (int i = 0; i < commandList.getLength(); i++) {

			if (commandList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				NodeList commandDetails = ((Element) commandList.item(i))
						.getChildNodes();
				String command = null;
				String commandClass = null;

				for (int j = 0; j < commandDetails.getLength(); j++) {

					if (commandDetails.item(j).getNodeName()
							.equals(PROP_COMMAND_NAME)) {
						command = ((Element) commandDetails.item(j))
								.getChildNodes().item(0).getNodeValue().trim();
					}
					if (commandDetails.item(j).getNodeName()
							.equals(PROP_COMMAND_CLASS)) {
						commandClass = ((Element) commandDetails.item(j))
								.getChildNodes().item(0).getNodeValue().trim();
					}
				}
				LOG.info("Found '" + command + "' command, Command Class - "
						+ commandClass);
				commands.add(command);
				commandClasseNames.put(command, commandClass);
			}
		}
	}

	public static List<String> getListOfCommands() {
		return commands;
	}

	public static String getCommandClassName(String command) {
		return commandClasseNames.get(command);
	}

	@SuppressWarnings("unchecked")
	public static Class<Command> getCommandClass(String command)
			throws ClassNotFoundException {
		return (Class<Command>) Class.forName(commandClasseNames.get(command));
	}

}
