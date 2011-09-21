package com.systexpro.defcon.api;

import com.systexpro.defcon.McDefcon;

public class McDefconApi {

	public McDefcon plugin;

	public McDefconApi(McDefcon df) {
		plugin = df;
	}

	/**
	 * Set Defcon Level
	 * @param level
	 */
	public void setDefconLevel(int level) {
		if(level < plugin.maxLevel)
			plugin.defconLevel = level;
	}

	public String getLevel1Message() {
		return plugin.level1Message;
	}

	public String getLevel2Message() {
		return plugin.level2Message;
	}

	public String getLevel3Message() {
		return plugin.level3Message;
	}

	public String getLevel4Message() {
		return plugin.level4Message;
	}
	
	public String getLevel5Message() {
		return plugin.level5Message;
	}
	
	public String getLevel6Message() {
		return plugin.level6Message;
	}



}
