package com.ericsson.hadoop.shell;

import com.ericsson.hadoop.shell.configuration.Configuration;

public class Scratchpad {

	public static void main(String[] args) {

		System.out.println(Configuration.getInstance().getKeysAsList());
	}

}
