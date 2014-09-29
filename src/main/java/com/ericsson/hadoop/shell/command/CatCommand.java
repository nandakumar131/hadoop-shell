package com.ericsson.hadoop.shell.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jline.Completor;
import jline.MultiCompletor;
import jline.SimpleCompletor;

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

public class CatCommand extends AbstractHDFSCommand {

	public static final String NAME = "cat";

	private static final String HYPHEN = "-";
	private static final String OPT_LINE_NUMBER = "n";
	private static final String COMMAND_SYNTAX = "cat <file>";

	private static final Log LOG = LogFactory.getLog(CatCommand.class);

	private String fileName;
	private boolean printLineNumber;

	public int execute(String... arguments) throws CommandExecutionException {
		try {
			int exitCode = ExitCode.SUCCESS;
			parseArguments(arguments);
			if (fileName != null) {
				String absolutePath = HDFSPath.getAbsolutePath(fileName);
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

						if (fileExtension.equals(FileType.AVRO)) {
							LOG.info("File type: AVRO, Using AVRO decode to print the file.");
							printAvroFile(absolutePath, printLineNumber);

						} else {
							printFile(absolutePath, printLineNumber);
						}
					} else {
						exitCode = ExitCode.ERROR;
						LOG.error(fileName + ": is a directory");
						System.err.println(NAME + ": " + fileName
								+ ": Is a directory");
					}
				} else {
					exitCode = ExitCode.ERROR;
					LOG.error(fileName + " : no such file or directory");
					System.err.println(NAME + ": " + fileName
							+ ": No such file or directory");
				}
			} else {
				exitCode = ExitCode.ERROR;
				LOG.error("Wrong number of arguments.");
				System.err.println("Wrong number of arguments.");
			}
			resetFlags();
			return exitCode;
		} catch (Exception e) {
			throw new CommandExecutionException(e);
		}
	}

	private void parseArguments(String[] arguments) {
		if (arguments.length != 0) {
			for (String argument : arguments) {
				if (argument.equals(HYPHEN + OPT_LINE_NUMBER)) {
					printLineNumber = true;
				} else {
					fileName = argument;
				}
			}
		}
	}

	private void printFile(String path, boolean printLineNumber)
			throws IOException {
		int lineNumber = 1;
		InputStream is = HDFS.getFileSystem().open(new Path(path));
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = br.readLine()) != null) {
			print(line, printLineNumber, lineNumber++);
		}
	}

	private void printAvroFile(String absolutePath, boolean printLineNumber)
			throws IOException {
		int lineNumber = 1;
		DataFileReader<GenericRecord> dataFileReader = AvroHelper
				.getDataFileReader(absolutePath);
		Schema schema = dataFileReader.getSchema();
		List<Schema.Field> recordFields = schema.getFields();
		int recordSize = recordFields.size();
		GenericRecord record = new GenericData.Record(schema);
		while (dataFileReader.hasNext()) {
			dataFileReader.next(record);
			List<String> line = new ArrayList<String>();
			for (int counter = 0; counter < recordSize; counter++) {
				String key = recordFields.get(counter).name();
				Object object = record.get(recordFields.get(counter).name());
				line.add(key + " = " + object.toString());
			}
			print(line.toString(), printLineNumber, lineNumber++);
		}
	}

	private void print(String line, boolean printLineNumber, int lineNumber) {
		if (printLineNumber) {
			System.out.println("  " + lineNumber + "  " + line);
		} else {
			System.out.println(line);
		}
	}

	private void resetFlags() {
		fileName = null;
		printLineNumber = false;
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
		options.add(HYPHEN + OPT_LINE_NUMBER);
		return options;
	}

	public String help() {
		return new String(String.format("%-10s", COMMAND_SYNTAX)
				+ "  -  concatenate files and print on the standard output");
	}

	@Override
	public String toString() {
		return COMMAND_SYNTAX;
	}

}