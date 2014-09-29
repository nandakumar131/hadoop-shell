package com.ericsson.hadoop.shell.command;

import org.apache.hadoop.fs.FileSystem;

import com.ericsson.hadoop.shell.environment.HShellEnv;
import com.ericsson.hadoop.shell.filesystem.HDFS;

public abstract class AbstractHDFSCommand implements Command {

	protected static FileSystem hdfs = HDFS.getFileSystem();
	protected static HShellEnv env = HShellEnv.getInstance();
	
}
