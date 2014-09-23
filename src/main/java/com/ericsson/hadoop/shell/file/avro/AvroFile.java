package com.ericsson.hadoop.shell.file.avro;

import java.io.Closeable;
import java.io.IOException;

import org.apache.avro.file.SeekableInput;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;

import com.ericsson.hadoop.shell.filesystem.HDFS;

public class AvroFile implements Closeable, SeekableInput {

	private final FSDataInputStream stream;
	private final long len;

	/**
	 * Construct given a path
	 **/
	public AvroFile(Path path) throws IOException {
		Configuration conf = HDFS.getConfig();
		this.stream = path.getFileSystem(conf).open(path);
		this.len = path.getFileSystem(conf).getFileStatus(path).getLen();
	}

	public long length() {
		return len;
	}

	public int read(byte[] b, int off, int len) throws IOException {
		return stream.read(b, off, len);
	}

	public void seek(long p) throws IOException {
		stream.seek(p);
	}

	public long tell() throws IOException {
		return stream.getPos();
	}

	public void close() throws IOException {
		stream.close();
	}
}