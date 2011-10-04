package com.systexpro.defcon.api;

public enum DefconPermission {

	ADMIN_NODE("mcdefcon.admin"),
	ACCEPT_NODE("mcdefcon.accept");

	String node;

	DefconPermission(String s) {
		node = s;
	}

	public String toString() {
		return node;
	}
}
