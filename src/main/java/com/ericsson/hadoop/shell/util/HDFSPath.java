package com.ericsson.hadoop.shell.util;

import java.io.File;

import com.ericsson.hadoop.shell.environment.HShellEnv;

public class HDFSPath {

	private static final HShellEnv env = HShellEnv.getInstance();

	public static String getAbsolutePath(String path) {
		// TODO: remove double quotes if present from the path string
		if (!(isAbsolutePath(path))) {
			if (env.getPWD().equals(File.separator)) {
				return env.getPWD() + path;
			}
			return env.getPWD() + File.separator + path;
		}
		return path;
	}

	public static boolean isAbsolutePath(String path) {
		return path.startsWith(File.separator);
	}

}
