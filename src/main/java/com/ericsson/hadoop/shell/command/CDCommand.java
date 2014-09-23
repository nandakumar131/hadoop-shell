package com.ericsson.hadoop.shell.command;

import java.io.File;
import java.io.IOException;

import jline.Completor;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.ericsson.hadoop.shell.completor.HDFSDirNameCompletor;
import com.ericsson.hadoop.shell.environment.HShellEnv;
import com.ericsson.hadoop.shell.filesystem.HDFS;
import com.ericsson.hadoop.shell.util.ExitCode;
import com.ericsson.hadoop.shell.util.HDFSPath;

public class CDCommand implements Command {

	public static final String NAME = "cd";

	private final String COMMAND_SYNTAX = "cd <directory>";

	private static FileSystem hdfs;
	private static HShellEnv env;

	public CDCommand() {
		try {
			hdfs = FileSystem.get(HDFS.getConfig());
			env = HShellEnv.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int execute(String... arguments) {
		int exitCode = ExitCode.SUCCESS;
		try {
			String dir = getDir(arguments);
			String location = HDFSPath.getAbsolutePath(dir);
			Path path = new Path(location);
			if (hdfs.exists(path)) {
				if (hdfs.isDirectory(path)) {
					location = path.toString() + File.separator;
					env.setPWD(new Path(location).toString());
				} else {
					exitCode = ExitCode.ERROR;
					System.err.println("cd: " + dir + ": Not a directory");
				}
			} else {
				exitCode = ExitCode.ERROR;
				System.err
						.println("cd: " + dir + ": No such file or directory");
			}
		} catch (IllegalArgumentException iae) {
			env.setPWD(File.separator);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exitCode;
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

	@Override
	public String help() {
		return new String(String.format("%-10s", COMMAND_SYNTAX)
				+ "  -  Change directory");
	}

	@Override
	public String toString() {
		return COMMAND_SYNTAX;
	}

}
