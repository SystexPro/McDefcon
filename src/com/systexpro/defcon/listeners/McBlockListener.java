package com.systexpro.defcon.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.systexpro.defcon.McDefcon;

public class McBlockListener extends BlockListener {

	public McDefcon plugin;

	public McBlockListener(McDefcon mc) {
		plugin = mc;
	}

	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if(plugin.defconLevel == 5 && !plugin.isAdmin(player, "accept")) {
			player.sendMessage(plugin.level5Message);
			event.setCancelled(true);
		} 
	}
	
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if(plugin.defconLevel == 5 && !plugin.isAdmin(player, "accept")) {
			player.sendMessage(plugin.level5Message);
			event.setCancelled(true);
		} 
	}

}
