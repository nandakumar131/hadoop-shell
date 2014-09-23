package com.ericsson.hadoop.shell.filesystem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsShell;
import org.apache.hadoop.util.ToolRunner;

public class HDFS {

	private final static String HSHELL_SITE_FILE = "/hshell-site.xml";

	private static Configuration conf;
	private static FileSystem fileSystem;
	private static FsShell hdfsShell;

	static {
		conf = new Configuration();
		try {
			conf.addResource(new FileInputStream(getHShellSiteFile()));
			fileSystem = FileSystem.get(conf);
		} catch (FileNotFoundException fne) {
			// TODO: log warning
		} catch (NullPointerException npe) {
			// TODO: log warning
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		hdfsShell = new FsShell(conf);
	}

	private static String getHShellSiteFile() {
		URL hshellSiteFileURL = HDFS.class.getResource(HSHELL_SITE_FILE);
		return hshellSiteFileURL == null ? null : hshellSiteFileURL.getPath();
	}

	/**
	 * Executes the command using hadoop's FsShell.
	 * 
	 * @param cmd
	 * @param args
	 * @return exit code
	 * @throws Exception
	 */
	public static int execute(String cmd, List<String> args) throws Exception {
		List<String> arguments = new ArrayList<String>();
		arguments.add(cmd);
		arguments.addAll(args);
		return ToolRunner.run(hdfsShell,
				arguments.toArray(new String[arguments.size()]));
	}

	/**
	 * 
	 * @return hadoop configuration
	 */
	public static Configuration getConfig() {
		return conf;
	}

	public static FileSystem getFileSystem() {
		return fileSystem;
	}

}
