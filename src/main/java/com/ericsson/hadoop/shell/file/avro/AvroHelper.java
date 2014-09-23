package com.ericsson.hadoop.shell.file.avro;

import java.io.IOException;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.SeekableInput;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.hadoop.fs.Path;

public class AvroHelper {

	public static DataFileReader<GenericRecord> getDataFileReader(
			String filePath) throws IOException {
		SeekableInput avroFile = new AvroFile(new Path(filePath));
		DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>();
		return new DataFileReader<GenericRecord>(avroFile, datumReader);
	}

}
