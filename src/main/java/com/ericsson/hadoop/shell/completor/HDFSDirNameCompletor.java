package com.ericsson.hadoop.shell.completor;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import jline.Completor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.ericsson.hadoop.shell.environment.HShellEnv;
import com.ericsson.hadoop.shell.filesystem.HDFS;

public class HDFSDirNameCompletor implements Completor {

	private static FileSystem hdfs = HDFS.getFileSystem();
	private static final HShellEnv env = HShellEnv.getInstance();
	
	private static final Log LOG = LogFactory.getLog(HDFSDirNameCompletor.class);

	@SuppressWarnings("rawtypes")
	public int complete(final String buf, final int cursor,
			final List candidates) {

		String buffer;
		String translated;

		if (buf == null) {
			String pwd = env.getPWD();
			buffer = (pwd.equals(File.separator)) ? pwd : pwd + File.separator;
		} else {
			buffer = buf;
		}

		translated = buffer;

		if (!(translated.startsWith(File.separator))) {
			if (new Path(env.getPWD()).isRoot()) {
				translated = new Path(env.getPWD()) + translated;
			}
			translated = new Path(env.getPWD()) + File.separator + translated;
		}

		Path f = new Path(translated);
		Path dir;

		if (translated.endsWith(File.separator)) {
			dir = (f.getParent() == null) ? new Path(File.separator) : f;
			translated = dir.toString();
		} else {
			dir = (f.getParent() == null) ? new Path(File.separator) : f
					.getParent();
			String parent;
			if (dir.isRoot()) {
				parent = dir.toString();
			} else {
				parent = dir.toString() + File.separator;
			}
			translated = parent
					+ translated.substring(
							translated.lastIndexOf(File.separator) + 1).trim();
		}

		try {
			FileStatus[] hdfsFileSystemFiles;
			try {
				hdfsFileSystemFiles = hdfs.listStatus(dir);
			} catch (IllegalArgumentException iae) {
				return buffer.lastIndexOf(File.separator)
						+ File.separator.length();
			}
			final FileStatus[] entries = (dir == null) ? new FileStatus[0]
					: hdfsFileSystemFiles;
			int returnValue = matchFiles(buffer, translated, entries,
					candidates);
			sortFileNames(candidates);
			return returnValue;
		} catch (IOException e) {
			LOG.error("Exception while accessing HDFS filesystem ", e);
			return -1;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void sortFileNames(final List fileNames) {
		Collections.sort(fileNames);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int matchFiles(String buffer, String translated,
			FileStatus[] entries, List candidates) throws IOException {
		if (entries == null) {
			return -1;
		}

		addCurrentAndParentDir(buffer, candidates);

		for (int i = 0; i < entries.length; i++) {
			if (entries[i].getPath().toUri().getPath().toString()
					.startsWith(translated)) {
				String name = entries[i].getPath().toUri().getPath().toString();
				if (entries[i].isDirectory()) {
					name = name.substring(name.lastIndexOf(File.separator) + 1)
							.trim() + "/";
					candidates.add(name);
				}
			}
		}
		return buffer.lastIndexOf(File.separator) + File.separator.length();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addCurrentAndParentDir(String buffer, List candidates)
			throws IOException {

		// TODO: Use constant for current and parent dir, and use file seperator
		String parentDir;
		String currentValue;

		if (buffer.contains(File.separator)) {
			parentDir = env.getPWD()
					+ buffer.substring(0, buffer.lastIndexOf(File.separator));

			currentValue = buffer.substring(
					buffer.lastIndexOf(File.separator) + 1).trim();
		} else {
			parentDir = env.getPWD();
			currentValue = buffer;
		}

		if (!(currentValue.endsWith("."))) {
			return;
		}

		if (!(hdfs.isDirectory(new Path(parentDir)))) {
			return;
		}

		if (!currentValue.equals(".") && !currentValue.equals("..")) {
			return;
		}
		if (!(currentValue.endsWith(".."))) {
			candidates.add("./");
		}
		candidates.add("../");
	}

}
