package com.systexpro.defcon.api;

import org.bukkit.entity.Player;

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

	/**
	 * Resets the Defcon Level to 0
	 * @return
	 */
	public boolean resetDefconLevel() {
		plugin.defconLevel = 0;
		return true;
	}

	/**
	 * Has Defcon Permission
	 * @param p
	 * @param arg0
	 * @return
	 */
	public boolean hasDefconPermission(Player p, DefconPermission arg0) {
		if (plugin.UsePermissions) {
			return plugin.permissions.has(p, arg0.toString());
		} else if(plugin.bPerms) {
			return p.hasPermission(arg0.toString());
		} else {
			return p.isOp();
		}
	}
	
	public boolean hasDefconPermission(Player p, String node) {
		if (plugin.UsePermissions) {
			return plugin.permissions.has(p, "mcdefcon." + node);
		} else if(plugin.bPerms) {
			return p.hasPermission("mcdefcon." + node);
		} else {
			return p.isOp();
		}
	}
	
	/**
	 * Returns the current Defcon Level
	 * @return
	 */
	public int getCurrentLevel() {
		return plugin.defconLevel;
	}

	/**
	 * Returns if Defcon is on
	 * Level > 0
	 * @return
	 */
	public boolean isDefconOn() {
		return plugin.defconLevel > 0;
	}

	/**
	 * Returns if Defcon is off
	 * Level < 0
	 * @return
	 */
	public boolean isDefconOff() {
		return plugin.defconLevel < 0;
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
