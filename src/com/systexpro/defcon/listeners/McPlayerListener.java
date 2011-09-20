package com.systexpro.defcon.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import com.systexpro.defcon.McDefcon;

public class McPlayerListener extends PlayerListener {

	public McDefcon plugin;

	public McPlayerListener(McDefcon df){
		plugin = df;
	}

	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		int messageSent = 0;
		if(plugin.defconLevel == 6) {
			if(!plugin.isAdmin(p, "admin") || !plugin.isAdmin(p, "accept")) {
				messageSent++;
				p.sendMessage(plugin.level6Message);
				event.setCancelled(true);
			} else {
				event.setCancelled(false);
			}
		}
	}

	public void onPlayerLogin(PlayerLoginEvent event) {
		Player p = event.getPlayer();
		if(plugin.defconLevel == 1 && !plugin.isAdmin(p, "accept")) {
			if(plugin.mcbansLoaded) {
				plugin.mcbansHandler.kick(p.getDisplayName(), "console", plugin.level1Message);
			} else {
				event.disallow(Result.KICK_OTHER, plugin.level1Message);
			}
		} else if(plugin.defconLevel == 2 && !plugin.isAdmin(p, "accept")) {
			if(plugin.mcbansLoaded) {
				plugin.mcbansHandler.ban(p.getDisplayName(), "Console", plugin.level2Message, "Local");
			} else {
				event.disallow(Result.KICK_BANNED, plugin.level2Message);
			}
		} else if(plugin.defconLevel == 3 && !plugin.isAdmin(p, "accept")) {
			plugin.mcbansHandler.tempban(p.getDisplayName(), "Console", plugin.mcTempBanTime, null);
		} else if(plugin.defconLevel == 0 && !plugin.isAdmin(p, "accept")) {
			event.allow();
		}
	}

	public void onPlayerChat(PlayerChatEvent event) {
		Player p = event.getPlayer();
		if(plugin.defconLevel == 4) {
			if(!plugin.isAdmin(p, "admin") || !plugin.isAdmin(p, "accept")) {
				p.sendMessage(plugin.level4Message);
				event.setCancelled(true);
			} else {
				event.setCancelled(false);
			}
		}
	}
}
