package com.ericsson.hadoop.shell.command;

import java.io.File;
import java.io.IOException;

import jline.Completor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.Path;

import com.ericsson.hadoop.shell.completor.HDFSDirNameCompletor;
import com.ericsson.hadoop.shell.exception.CommandExecutionException;
import com.ericsson.hadoop.shell.util.ExitCode;
import com.ericsson.hadoop.shell.util.HDFSPath;

public class CDCommand extends AbstractHDFSCommand {

	public static final String NAME = "cd";

	private static final String COMMAND_SYNTAX = "cd <directory>";

	private static final Log LOG = LogFactory.getLog(CDCommand.class);

	public int execute(String... arguments) throws CommandExecutionException {
		try {
			int exitCode = ExitCode.SUCCESS;
			String dir = getDir(arguments);
			String location = HDFSPath.getAbsolutePath(dir);
			Path path = new Path(location);
			try {
				if (hdfs.exists(path)) {
					if (hdfs.isDirectory(path)) {
						location = path.toString() + File.separator;
						env.setPWD(new Path(location).toString());
					} else {
						exitCode = ExitCode.ERROR;
						LOG.error(dir + " Not a direcotry.");
						System.err.println("cd: " + dir + ": Not a directory");
					}
				} else {
					exitCode = ExitCode.ERROR;
					LOG.error(dir + " No such file or direcotry.");
					System.err.println("cd: " + dir
							+ ": No such file or directory");
				}
			} catch (IllegalArgumentException iae) {
				env.setPWD(File.separator);
			}
			return exitCode;
		} catch (IOException e) {
			throw new CommandExecutionException(e);
		}
	}

	private String getDir(String[] arguments) {
		String dir;
		if (arguments.length == 0) {
			dir = File.separator;
		} else {
			dir = arguments[0];
			if (dir.equals("-")) {
				if (env.getOldPWD() == null) {
					dir = env.getPWD();
					System.err.println("cd: OLDPWD not set");
				} else {
					dir = env.getOldPWD();
				}
			}
		}
		return dir;
	}

	public static Completor getCompletor() {
		return new HDFSDirNameCompletor();
	}

	public String help() {
		return new String(String.format("%-10s", COMMAND_SYNTAX)
				+ "  -  Change directory");
	}

	@Override
	public String toString() {
		return COMMAND_SYNTAX;
	}

}
