package com.systexpro.defcon;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor  {

	public McDefcon plugin;

	public CommandHandler(McDefcon mcDefcon) {
		plugin = mcDefcon;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		// TODO Auto-generated method stub
		return false;
	}


}
