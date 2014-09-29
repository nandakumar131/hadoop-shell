package com.ericsson.hadoop.shell.environment;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HShellEnv implements Map<String, String> {

	private static final HShellEnv hshellEnv;

	private Map<String, String> internalMap = new HashMap<String, String>();

	static {
		hshellEnv = new HShellEnv();
	}

	private HShellEnv() {
		internalMap.put(EnvVariables.PWD, File.separator);
		internalMap.put(EnvVariables.EXIT_CODE, "0");
	}

	public static HShellEnv getInstance() {
		return hshellEnv;
	}

	public int size() {
		return internalMap.size();
	}

	public boolean isEmpty() {
		return internalMap.isEmpty();
	}

	public boolean containsKey(Object key) {
		return internalMap.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return internalMap.containsValue(value);
	}

	public String get(Object key) {
		return internalMap.get(key);
	}

	public String put(String key, String value) {
		return internalMap.put(key, value);
	}

	public String remove(Object key) {
		return internalMap.remove(key);
	}

	public void putAll(Map<? extends String, ? extends String> m) {
		internalMap.putAll(m);
	}

	public void clear() {
		internalMap.clear();
	}

	public Set<String> keySet() {
		return internalMap.keySet();
	}

	public Collection<String> values() {
		return internalMap.values();
	}

	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return internalMap.entrySet();
	}

	public String getPWD() {
		return internalMap.get(EnvVariables.PWD);
	}

	public void setPWD(String pwd) {
		internalMap
				.put(EnvVariables.OLD_PWD, internalMap.get(EnvVariables.PWD));
		internalMap.put(EnvVariables.PWD, pwd);
	}

	public String getOldPWD() {
		return internalMap.get(EnvVariables.OLD_PWD);
	}
}
