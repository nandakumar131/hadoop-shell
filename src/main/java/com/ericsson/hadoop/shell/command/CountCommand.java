package com.ericsson.hadoop.shell.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jline.Completor;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.ericsson.hadoop.shell.completor.HDFSFileNameCompletor;
import com.ericsson.hadoop.shell.file.FileType;
import com.ericsson.hadoop.shell.file.avro.AvroHelper;
import com.ericsson.hadoop.shell.filesystem.HDFS;
import com.ericsson.hadoop.shell.util.ExitCode;
import com.ericsson.hadoop.shell.util.HDFSPath;

public class CountCommand implements Command {

	public static final String NAME = "count";

	private final String COMMAND_SYNTAX = "count <file>";

	private static FileSystem hdfs = HDFS.getFileSystem();

	@Override
	public int execute(String... arguments) {
		int exitCode = ExitCode.SUCCESS;
		try {
			String argument = arguments[0];
			String absolutePath = HDFSPath.getAbsolutePath(argument);
			Path path = new Path(absolutePath);
			if (hdfs.exists(path)) {
				if (!hdfs.isDirectory(path)) {
					String fileExtension = "";
					try {
						fileExtension = absolutePath.substring(
								absolutePath.lastIndexOf('.') + 1,
								absolutePath.length()).trim();
					} catch (Exception ex) {
						// File doesn't have any extension, do nothing.
					}
					int count;

					if (fileExtension.equals(FileType.AVRO)) {
						count = getAvroFileRecordCount(absolutePath);
					} else {
						count = getFileRecordCount(absolutePath);
					}
					System.out.println(count + " : " + absolutePath);
				} else {
					System.err.println(NAME + ": " + argument
							+ ": Is a directory");
				}
			} else {
				System.err.println(NAME + ": " + argument
						+ ": No such file or directory");
			}
		} catch (Exception e) {
			// TODO: do logging and remove p.printStackTrace()
			e.printStackTrace();
			exitCode = ExitCode.ERROR;
		}
		return exitCode;
	}

	private int getFileRecordCount(String path) throws IOException {
		int count = 0;
		InputStream is = HDFS.getFileSystem().open(new Path(path));
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		while (br.readLine() != null) {
			count++;
		}
		return count;
	}

	private int getAvroFileRecordCount(String path) throws IOException {
		int count = 0;
		DataFileReader<GenericRecord> dataFileReader = AvroHelper
				.getDataFileReader(path);
		Schema schema = dataFileReader.getSchema();
		GenericRecord record = new GenericData.Record(schema);
		while (dataFileReader.hasNext()) {
			dataFileReader.next(record);
			count++;
		}
		return count;
	}

	public static Completor getCompletor() {
		return new HDFSFileNameCompletor();
	}

	@Override
	public String help() {
		return new String(String.format("%-10s", COMMAND_SYNTAX)
				+ "  -  prints the line count of file");
	}

	@Override
	public String toString() {
		return COMMAND_SYNTAX;
	}

}