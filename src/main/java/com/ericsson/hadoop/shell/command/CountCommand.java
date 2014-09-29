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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.Path;

import com.ericsson.hadoop.shell.completor.HDFSFileNameCompletor;
import com.ericsson.hadoop.shell.exception.CommandExecutionException;
import com.ericsson.hadoop.shell.file.FileType;
import com.ericsson.hadoop.shell.file.avro.AvroHelper;
import com.ericsson.hadoop.shell.filesystem.HDFS;
import com.ericsson.hadoop.shell.util.ExitCode;
import com.ericsson.hadoop.shell.util.HDFSPath;

public class CountCommand extends AbstractHDFSCommand {

	public static final String NAME = "count";

	private static final String COMMAND_SYNTAX = "count <file>";

	private static final Log LOG = LogFactory.getLog(CountCommand.class);

	public int execute(String... arguments) throws CommandExecutionException {
		if (!(arguments.length > 0)) {
			LOG.error("File Name Missing.");
			System.err.println("File Name Missing.");
			System.err.println("Usage:");
			System.err.println("\t" + toString());
			return ExitCode.ERROR;
		}
		try {
			int exitCode = ExitCode.SUCCESS;
			// TODO: validate argument. print usage
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
						LOG.debug("No extension found for the file "
								+ absolutePath + ", using default decoder.");
						// File doesn't have any extension, do nothing.
					}
					int count;

					if (fileExtension.equals(FileType.AVRO)) {
						LOG.info("The file type is AVRO, so using avro decoder.");
						count = getAvroFileRecordCount(absolutePath);
					} else {
						count = getFileRecordCount(absolutePath);
					}
					System.out.println(count + " : " + absolutePath);
				} else {
					exitCode = ExitCode.ERROR;
					LOG.error(argument + ": is a directory");
					System.err.println(NAME + ": " + argument
							+ ": Is a directory");
				}
			} else {
				exitCode = ExitCode.ERROR;
				LOG.error(argument + " : no such file or directory");
				System.err.println(NAME + ": " + argument
						+ ": No such file or directory");
			}
			return exitCode;
		} catch (Exception e) {
			throw new CommandExecutionException(e);
		}
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

	public String help() {
		return new String(String.format("%-10s", COMMAND_SYNTAX)
				+ "  -  prints the line count of file");
	}

	@Override
	public String toString() {
		return COMMAND_SYNTAX;
	}

}