package com.ericsson.hadoop.shell.command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jline.Completor;
import jline.MultiCompletor;
import jline.SimpleCompletor;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.shell.PathData;

import com.ericsson.hadoop.shell.completor.HDFSFileNameCompletor;
import com.ericsson.hadoop.shell.controller.MyConsole;
import com.ericsson.hadoop.shell.environment.HShellEnv;
import com.ericsson.hadoop.shell.filesystem.HDFS;
import com.ericsson.hadoop.shell.util.ExitCode;
import com.ericsson.hadoop.shell.util.HDFSPath;

public class LSCommand implements Command {

	public static final String NAME = "ls";

	private final String COMMAND_SYNTAX = "ls [option] <file>";

	private static final String HYPHEN = "-";
	private static final String OPT_LONG_LISTING = "l";
	private static final String OPT_RECURSIVE = "R";
	private static final String OPT_DIR_LIST = "d";
	private static final String OPT_HUMAN_REDABLE = "h";

	private static final HShellEnv env = HShellEnv.getInstance();

	private List<String> paths = new ArrayList<String>();

	private boolean longListing;
	private boolean recursive;
	private boolean dirList;
	private boolean humanReadable;

	@Override
	public int execute(String... arguments) {
		int exitCode = ExitCode.SUCCESS;
		parseArguments(arguments);
		try {
			for (String path : paths) {
				String absolutePath = HDFSPath.getAbsolutePath(path);
				try {
					PathData[] items = PathData.expandAsGlob(absolutePath,
							HDFS.getConfig());
					if (items.length == 0) {
						printNoSuchFileORDir(absolutePath);
					}
					for (PathData item : items) {
						if ((paths.size() > 1 || items.length > 1)
								&& HDFS.getFileSystem().isDirectory(item.path)) {
							System.out.println();
							System.out.println(item.path.toUri().getPath()
									+ ":");
						}
						// Other options are not applicable if longListing or
						// recursive is not enabled.
						if (!longListing && !recursive) {
							listFiles(item.path);
						} else {
							List<String> args = getOptions();
							args.add(item.path.toUri().getPath());
							exitCode = HDFS.execute(HYPHEN + NAME, args);
						}

					}
				} catch (FileNotFoundException | IllegalArgumentException e) {
					// TODO: log exception message
					exitCode = ExitCode.ERROR;
					printNoSuchFileORDir(absolutePath);
				}
			}
		} catch (Exception e) {
			exitCode = ExitCode.ERROR;
			e.printStackTrace();
		}
		resetFlags();
		return exitCode;
	}

	private void parseArguments(String[] arguments) {
		if (arguments.length == 0) {
			paths.add(env.getPWD());
			return;
		}

		for (String argument : arguments) {
			if (argument.startsWith(HYPHEN)) {
				if (argument.endsWith(OPT_DIR_LIST)
						|| argument.endsWith(OPT_HUMAN_REDABLE)
						|| argument.endsWith(OPT_LONG_LISTING)
						|| argument.endsWith(OPT_RECURSIVE)) {
					setOptions(argument);
					continue;
				}
			}
			paths.add(argument);
		}
		if (paths.size() == 0) {
			paths.add(env.getPWD());
		}
	}

	private void setOptions(String options) {
		if (options.isEmpty()) {
			return;
		}
		for (String opt : options.split("")) {
			switch (opt) {
			case OPT_LONG_LISTING:
				longListing = true;
				break;
			case OPT_RECURSIVE:
				recursive = true;
				break;
			case OPT_DIR_LIST:
				dirList = true;
				break;
			case OPT_HUMAN_REDABLE:
				humanReadable = true;
				break;
			}
		}

	}

	private List<String> getOptions() {
		List<String> arguments = new ArrayList<String>();
		if (recursive) {
			arguments.add(HYPHEN + OPT_RECURSIVE);
		}
		if (dirList) {
			arguments.add(HYPHEN + OPT_DIR_LIST);
		}
		if (humanReadable) {
			arguments.add(HYPHEN + OPT_HUMAN_REDABLE);
		}
		return arguments;
	}

	private void listFiles(Path path) throws IOException {
		List<String> listOfFiles = new ArrayList<String>();
		FileStatus[] hdfsFileSystemFiles = HDFS.getFileSystem()
				.listStatus(path);

		for (int i = 0; i < hdfsFileSystemFiles.length; i++) {
			String name = hdfsFileSystemFiles[i].getPath().toUri().getPath()
					.toString();
			if (hdfsFileSystemFiles[i].isDirectory()) {
				name = name.substring(name.lastIndexOf(File.separator) + 1)
						.trim() + "/";
			} else {
				name = name.substring(name.lastIndexOf(File.separator) + 1)
						.trim();
			}
			listOfFiles.add(name);
		}
		MyConsole.print(listOfFiles, false);
	}

	private void resetFlags() {
		paths.clear();
		longListing = false;
		recursive = false;
		dirList = false;
		humanReadable = false;
	}

	public static Completor getCompletor() {
		List<Completor> listOfCompletors = new ArrayList<Completor>();
		listOfCompletors.add(new HDFSFileNameCompletor());
		listOfCompletors.add(new SimpleCompletor(getAllAvailableOptions()
				.toArray(new String[0])));
		Completor multiCompletor = new MultiCompletor(listOfCompletors);
		return multiCompletor;
	}

	private static List<String> getAllAvailableOptions() {
		List<String> options = new ArrayList<String>();
		options.add(HYPHEN + OPT_LONG_LISTING);
		options.add(HYPHEN + OPT_DIR_LIST);
		options.add(HYPHEN + OPT_HUMAN_REDABLE);
		options.add(HYPHEN + OPT_RECURSIVE);
		options.add(HYPHEN + OPT_LONG_LISTING + OPT_RECURSIVE
				+ OPT_HUMAN_REDABLE);
		return options;
	}

	private void printNoSuchFileORDir(String fileName) {
		System.err.println("ls: " + fileName + " : No such file or directory");
	}

	@Override
	public String help() {
		return new String(String.format("%-10s", COMMAND_SYNTAX)
				+ "  -  List directory contents");
	}

	@Override
	public String toString() {
		return COMMAND_SYNTAX;
	}

}
