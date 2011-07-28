package com.systexpro.defcon;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class MCPlayerListener extends PlayerListener {

	public MCDefcon plugin;

	public MCPlayerListener(MCDefcon df){
		plugin = df;
	}

	public void onPlayerLogin(PlayerLoginEvent event) {
		Player p = event.getPlayer();
		if(plugin.defconLevel == 1 && !plugin.isAdmin(p, "accpet")) {
			if(plugin.mcbansLoaded) {
				plugin.mcbansHandler.kick(p.getDisplayName(), "console", plugin.level1Message);
			} else {
				event.disallow(Result.KICK_OTHER, plugin.level1Message);
			}
		} else if(plugin.defconLevel == 2 && !plugin.isAdmin(p, "accpet")) {
			if(plugin.mcbansLoaded) {
				plugin.mcbansHandler.ban(p.getDisplayName(), "Console", plugin.level2Message, "Local");
			} else {
				event.disallow(Result.KICK_BANNED, plugin.level2Message);
			}
		} else if(plugin.defconLevel == 3 && !plugin.isAdmin(p, "accept")) {
			plugin.mcbansHandler.tempban(p.getDisplayName(), "Console", plugin.mcTempBanTime, null);
		} else if(plugin.defconLevel == 0 && !plugin.isAdmin(p,"accept")) {
			event.allow();
		}
	}


}
