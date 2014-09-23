package com.ericsson.hadoop.shell.controller;

public class SplashScreen {

	private static StringBuilder screen;
	static {
		screen = new StringBuilder();
		screen.append(String.format("%n")).append(String.format("%n"))
				.append(" _|    _|    _|_|_|  _|                  _|  _|  ")
				.append(String.format("%n"))
				.append(" _|    _|  _|        _|_|_|      _|_|    _|  _|  ")
				.append(String.format("%n"))
				.append(" _|_|_|_|    _|_|    _|    _|  _|_|_|_|  _|  _|  ")
				.append(String.format("%n"))
				.append(" _|    _|        _|  _|    _|  _|        _|  _|  ")
				.append(String.format("%n"))
				.append(" _|    _|  _|_|_|    _|    _|    _|_|_|  _|  _|  ")
				.append(String.format("%n"))
				.append("                            ")
				.append(String.format("%n")).append(String.format("%n"));

	}

	public static void render() {
		// TODO: change system.out.println to console write
		System.out.println(screen);
	}
}
