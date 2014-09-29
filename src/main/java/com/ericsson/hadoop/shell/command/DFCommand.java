package com.ericsson.hadoop.shell.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jline.Completor;
import jline.SimpleCompletor;

import org.apache.hadoop.fs.FsStatus;

import com.ericsson.hadoop.shell.exception.CommandExecutionException;
import com.ericsson.hadoop.shell.util.ExitCode;

public class DFCommand extends AbstractHDFSCommand {

	public static final String NAME = "df";

	private static final String COMMAND_SYNTAX = "df";

	private static final String FILESYSTEM_NAME = "HDFS";
	private static final String HYPHEN = "-";
	private static final String OPT_HUMAN_REDABLE = "h";

	public int execute(String... arguments) throws CommandExecutionException {
		try {
			boolean printInHumanRedableFormat = false;
			if (arguments.length != 0
					&& arguments[0].equals(HYPHEN + OPT_HUMAN_REDABLE)) {
				printInHumanRedableFormat = true;
			}
			FsStatus fsStatus = hdfs.getStatus();
			printHeader();

			long capacity = fsStatus.getCapacity();
			long dfsUsed = fsStatus.getUsed();
			long remaining = fsStatus.getRemaining();

			long nonDfsUsed = capacity - (dfsUsed + remaining);
			float usedPercent = ((float) dfsUsed / capacity) * 100;
			if (printInHumanRedableFormat) {
				printValues(getSizeinHumanReadableFormat(capacity),
						getSizeinHumanReadableFormat(dfsUsed),
						getSizeinHumanReadableFormat(nonDfsUsed),
						getSizeinHumanReadableFormat(remaining), usedPercent);
			} else {
				printValues(String.valueOf(capacity), String.valueOf(dfsUsed),
						String.valueOf(nonDfsUsed), String.valueOf(remaining),
						usedPercent);
			}

		} catch (IOException e) {
			throw new CommandExecutionException(e);
		}
		return ExitCode.SUCCESS;
	}

	private String getSizeinHumanReadableFormat(long bytes) {
		// returns sizes in human readable format (e.g., 1K 234M 2G)
		String returnValue;
		long conversionValue = 1024l;
		float inKB = (float) bytes / conversionValue;
		if (inKB >= conversionValue) {
			float inMB = (float) inKB / conversionValue;
			if (inMB >= conversionValue) {
				returnValue = String.format("%1.2f", (float) inMB
						/ conversionValue)
						+ " G";
			} else {
				returnValue = String.format("%1.2f", (float) inMB) + " M";
			}
		} else {
			returnValue = String.format("%1.2f", (float) inKB) + " K";
		}
		return returnValue;
	}

	private void printHeader() {
		System.out.println(String.format("%-12s", "Filesystem")
				+ String.format("%-20s", "Conf Capacity")
				+ String.format("%-15s", "DFS Used")
				+ String.format("%-15s", "Non DFS Used")
				+ String.format("%-15s", "Avail")
				+ String.format("%-10s", "DFS Use%"));
		// print delimiter
		for (int i = 0; i < 88; i++) {
			System.out.print("-");
		}
		// print new line
		System.out.println();
	}

	private void printValues(String capacity, String dfsUsed,
			String nonDfsUsed, String remaining, float usedPercent) {
		System.out.println(String.format("%-12s", FILESYSTEM_NAME)
				+ String.format("%-20s", capacity)
				+ String.format("%-15s", dfsUsed)
				+ String.format("%-15s", nonDfsUsed)
				+ String.format("%-15s", remaining)
				+ String.format("%-10s", String.format("%-3.2f", usedPercent)
						+ "%"));
	}

	public String help() {
		return new String(String.format("%-10s", COMMAND_SYNTAX)
				+ "  - report file system disk space usage");
	}

	public static Completor getCompletor() {
		return new SimpleCompletor(getAllAvailableOptions().toArray(
				new String[0]));
	}

	private static List<String> getAllAvailableOptions() {
		List<String> options = new ArrayList<String>();
		options.add(HYPHEN + OPT_HUMAN_REDABLE);
		return options;
	}

}
