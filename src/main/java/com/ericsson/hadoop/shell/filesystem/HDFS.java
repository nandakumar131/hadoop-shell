package com.ericsson.hadoop.shell.filesystem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsShell;
import org.apache.hadoop.util.ToolRunner;

import com.ericsson.hadoop.shell.util.ExitCode;

public class HDFS {

	private final static String HSHELL_SITE_FILE = "/hshell-site.xml";

	private static final Log LOG = LogFactory.getLog(HDFS.class);

	private static Configuration conf;
	private static FileSystem fileSystem;
	private static FsShell hdfsShell;

	static {
		conf = new Configuration();
		try {
			conf.addResource(new FileInputStream(getHShellSiteFile()));
			fileSystem = FileSystem.get(conf);
		} catch (FileNotFoundException fne) {
			LOG.error(HSHELL_SITE_FILE + " file not found!", fne);
		} catch (NullPointerException npe) {
			LOG.error("Exception while loading configuration", npe);
		} catch (IOException ioe) {
			LOG.error("Exception while loading" + HSHELL_SITE_FILE + " file");
			System.exit(ExitCode.ERROR);
		}
		hdfsShell = new FsShell(conf);
	}

	private static String getHShellSiteFile() {
		URL hshellSiteFileURL = HDFS.class.getResource(HSHELL_SITE_FILE);
		return hshellSiteFileURL == null ? null : hshellSiteFileURL.getPath();
	}

	public static boolean isRunning() {
		try {
			fileSystem.getStatus();
		} catch (IOException e) {
			LOG.error("Exception while accessing hadoop filesystem.", e);
			return false;
		}
		return true;
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

	public static void close() throws IOException {
		fileSystem.close();
	}

}
