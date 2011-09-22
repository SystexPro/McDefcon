package com.systexpro.defcon.api;

public enum DefconLevel {

	LEVEL_0(0),
	LEVEL_1(1),
	LEVEL_2(2),
	LEVEL_3(3),
	LEVEL_4(4),
	LEVEL_5(5),
	LEVEL_6(6);
	
	int level;
	
	DefconLevel(int i) {
		level = i;
	}
	
	public int getLevel() {
		return level;
	}
	
	
}
