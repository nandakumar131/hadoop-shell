package com.ericsson.hadoop.shell.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ericsson.hadoop.shell.command.Command;

public class CommandUtil {

	private static final String COMMAND_FILE = "/commands.xml";

	private static final String PROP_COMMAND_NAME = "name";
	private static final String PROP_COMMAND_CLASS = "class";

	private static List<String> commands = new ArrayList<String>();
	private static Map<String, String> commandClasseNames = new HashMap<String, String>();

	static {
		//TODO: move the code to loadCommands() method, from static block.
		File commandXML = new File(CommandUtil.class.getResource(COMMAND_FILE)
				.getPath());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document xmlDoc = dBuilder.parse(commandXML);
			xmlDoc.getDocumentElement().normalize();

			NodeList commandList = xmlDoc.getDocumentElement().getChildNodes();

			for (int i = 0; i < commandList.getLength(); i++) {

				if (commandList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					NodeList commandDetails = ((Element) commandList.item(i))
							.getChildNodes();
					String key = null;
					String value = null;

					for (int j = 0; j < commandDetails.getLength(); j++) {

						if (commandDetails.item(j).getNodeName()
								.equals(PROP_COMMAND_NAME)) {
							key = ((Element) commandDetails.item(j))
									.getChildNodes().item(0).getNodeValue()
									.trim();
						}
						if (commandDetails.item(j).getNodeName()
								.equals(PROP_COMMAND_CLASS)) {
							value = ((Element) commandDetails.item(j))
									.getChildNodes().item(0).getNodeValue()
									.trim();
						}
					}
					commands.add(key);
					commandClasseNames.put(key, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
