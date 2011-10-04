package com.systexpro.defcon.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
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
		Location loc1 = event.getFrom();
		if(plugin.defconLevel == 6) {
			if(!plugin.defconAPI.hasDefconPermission(p, "accept")) {
				p.teleport(loc1);
			} else {
				event.setCancelled(false);
			}
		}
	}

	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if(plugin.showDefconLevelOnJoin) {
			plugin.colorSend(p, "Server is on Defcon Level: " + plugin.defconLevel);
		}
	}

	public void onPlayerLogin(PlayerLoginEvent event) {
		Player p = event.getPlayer();
		if(plugin.defconLevel == 1 && !plugin.defconAPI.hasDefconPermission(p, "accept")) {
			if(plugin.mcbansLoaded) {
				plugin.mcbansHandler.kick(p.getDisplayName(), "console", plugin.defconAPI.getLevel1Message());
			} else {
				event.disallow(Result.KICK_OTHER, plugin.level1Message);
			}
		} else if(plugin.defconLevel == 2 && !plugin.defconAPI.hasDefconPermission(p, "accept")) {
			if(plugin.mcbansLoaded) {
				plugin.mcbansHandler.ban(p.getDisplayName(), "Console", plugin.defconAPI.getLevel2Message(), "Local");
			} else {
				event.disallow(Result.KICK_BANNED, plugin.level2Message);
			}
		} else if(plugin.defconLevel == 3 && !plugin.defconAPI.hasDefconPermission(p, "accept")) {
			plugin.mcbansHandler.tempban(p.getDisplayName(), "Console", plugin.mcTempBanTime, null);
		} else if(plugin.defconLevel == 0 && !plugin.defconAPI.hasDefconPermission(p, "accept")) {
			event.allow();
		}
	}

	public void onPlayerChat(PlayerChatEvent event) {
		Player p = event.getPlayer();
		if(plugin.defconLevel == 4) {
			if(!plugin.defconAPI.hasDefconPermission(p, "accept")) {
				plugin.colorSend(p, plugin.defconAPI.getLevel4Message());
				event.setCancelled(true);
			} else {
				event.setCancelled(false);
			}
		}
	}
}
