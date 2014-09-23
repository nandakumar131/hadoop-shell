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

	@Override
	public int size() {
		return internalMap.size();
	}

	@Override
	public boolean isEmpty() {
		return internalMap.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return internalMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return internalMap.containsValue(value);
	}

	@Override
	public String get(Object key) {
		return internalMap.get(key);
	}

	@Override
	public String put(String key, String value) {
		return internalMap.put(key, value);
	}

	@Override
	public String remove(Object key) {
		return internalMap.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		internalMap.putAll(m);
	}

	@Override
	public void clear() {
		internalMap.clear();
	}

	@Override
	public Set<String> keySet() {
		return internalMap.keySet();
	}

	@Override
	public Collection<String> values() {
		return internalMap.values();
	}

	@Override
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
