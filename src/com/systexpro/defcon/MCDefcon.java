/**
 * MCDefcon 
 * Created by SystexPro
 * Version 1.5
 * Features: Defcon Levels
 * Bugs: Folder is not created
 */
package com.systexpro.defcon;

import java.io.File;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import com.firestar.mcbans.mcbans_handler;
import com.firestar.mcbans.mcbans;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class MCDefcon extends JavaPlugin {

	public MCPlayerListener playerListener = new MCPlayerListener(this);
	public PermissionHandler permissionHandler;
	public mcbans_handler mcbansHandler;
	public String level1Message = "Kicked. Defcon Level 1.";
	public String level2Message = "Banned. Defcon Level 2.";
	public String level3Message = "Temp Banned. Defcon Level 3";
	public String mcBansType = "Local";
	public String mcTempBanTime = "5m";
	public boolean onDefconChangeKickAllPlayers = false;
	public boolean broadcast = true;
	public boolean UsePermissions;
	public boolean mcbansLoaded;
	public boolean useMcBans = false;
	public boolean bPerms = false;
	public int defconLevel = 0;

	@Override
	public void onDisable() {
		loadConfig();
		send("Unloaded McDefcon Version " + this.getDescription().getVersion());
	}

	@Override
	public void onEnable() {
		send("Loading MCDefcon Version " + this.getDescription().getVersion());
		getDataFolder().mkdirs();
		send("Loading Configuration File.");
		loadConfig();
		registerEvents();
		setupPermissions();
		setupMcbans();
		send("Defcon on Level: " + this.defconLevel);
	}

	/**
	 * Load Configuration File
	 */
	public void loadConfig() {
		Configuration config = new Configuration(new File("plugins/McDefcon/config.yml"));
		config.load();
		this.defconLevel = config.getInt("Defcon Level", defconLevel);
		this.bPerms = config.getBoolean("Use Bukkit Permissions", bPerms);
		this.level1Message = config.getString("Level 1 Message", level1Message);
		this.level2Message = config.getString("Level 2 Message", level2Message);
		this.onDefconChangeKickAllPlayers = config.getBoolean("Kick All Players on Defcon Level Change", onDefconChangeKickAllPlayers);
		this.broadcast = config.getBoolean("Broadcast Level Change", broadcast);
		this.useMcBans = config.getBoolean("Use McBans", useMcBans);
		this.mcTempBanTime = config.getString("McBans Temporary Ban Time", mcTempBanTime);
		this.mcBansType = config.getString("McBan Type(G = Global, L = Local)", mcBansType);
		config.save();
	}

	/**
	 * Handle Commands
	 * Player must have Op/Permissins
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player) sender;
		if (commandLabel.equalsIgnoreCase("defcon") || commandLabel.equalsIgnoreCase("dc")) {
			if (args.length >= 1 && sender instanceof Player) {
				if(isAdmin(p, "admin")) {
					handleCommand(sender, args);
					return true;
				} else {
					colorSend(p, "You do not have Permissions for this Command.");
				}
			} else {
				return help(sender);
			}
		}
		return true;
	}

	/**
	 * Handle Commands
	 * @param sender
	 * @param args
	 */
	private void handleCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		String cmd = args[0];
		if(cmd.equalsIgnoreCase("level") || cmd.equalsIgnoreCase("lvl") && args[1] != null) {
			if(args[1].equalsIgnoreCase("0")) {
				this.defconLevel = 0;
				colorSend(player, "Defcon Level set to 0.");
				colorSend(player, "All Players will join without check.");
				colorSendAll("Defcon Level is set to 0.");
				kickAllPlayers(defconLevel);
				//saveDefconLevel();
			} else if(args[1].equalsIgnoreCase("1")) {
				this.defconLevel = 1;
				colorSend(player, "Defcon Level set to 1.");
				colorSend(player, "All Players will be kicked off. (Except Ops)");
				colorSendAll("Defcon Level is set to 1.");
				kickAllPlayers(defconLevel);
				//saveDefconLevel();
			} else if(args[1].equalsIgnoreCase("2")) {
				this.defconLevel = 2;
				colorSend(player, "Defcon level set to 2.");
				colorSend(player, "All Incoming Players will be Banned. (Except Ops)");
				colorSendAll("Defcon Level is set to 2.");
				kickAllPlayers(defconLevel);
				//saveDefconLevel();
			} else if(args[1].equalsIgnoreCase("3")) {
				if(this.useMcBans) {
					this.defconLevel = 3;
					colorSend(player, "Defcon level set to 3.");
					colorSend(player, "All Incoming will be temporary banned for " + this.mcTempBanTime);
					colorSendAll("Defcon Level is set to 3.");
					kickAllPlayers(defconLevel);
				} else {
					colorSend(player, "This Level is for McBans, Please load McBans to use it.");
				}
			} else {
				colorSend(player, "Unknown Defcon Level");
			}
		} else {
			colorSend(player, "Hi");
		}
		if(cmd.equalsIgnoreCase("check")) {
			colorSend(player, "Defcon is on Level: " + defconLevel);
		}
		if(cmd.equalsIgnoreCase("reload")) {
			colorSend(player, "Reloading Configuration File.");
			send("Reloading Configuration File.");
			loadConfig();
		}
		if(cmd.equalsIgnoreCase("version")) {
			colorSend(player, "Version: 1.0. Made by SystexPro");
		} 
	}

	public void kickAllPlayers(int level) {
		if(this.onDefconChangeKickAllPlayers) {
			Player[] online = this.getServer().getOnlinePlayers();
			for(int x = 0; x < online.length; x++) {
				Player p = online[x];
				if(!this.isAdmin(p, "admin") || !this.isAdmin(p, "accept")) {
					p.kickPlayer("Defcon Level set to " + level);
				}
			}
		}
	}


	/**
	 * Register Events
	 */
	private void registerEvents()
	{
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_LOGIN, this.playerListener , Priority.Normal, this);
	}

	/**
	 * Help Menu
	 * @param sender
	 * @return
	 */
	private boolean help(CommandSender sender) {
		Player p = (Player) sender;
		p.sendMessage(ChatColor.DARK_RED + "==MCDefcon v1.0==");
		p.sendMessage(ChatColor.AQUA + "Command Triggers - /dc, /defcon");
		p.sendMessage(ChatColor.DARK_AQUA + "/dc level <level> - Sets Defcon Level: 0, 1, 2");
		p.sendMessage(ChatColor.DARK_AQUA + "/dc check - Checks what level Defcon is on.");
		p.sendMessage(ChatColor.DARK_AQUA + "/dc reload - Reload Coonfiguration File.");
		return true;
	}

	/**
	 * Connect to Permissions
	 */
	private void setupPermissions() {
		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
		if (this.permissionHandler == null) {
			if (permissionsPlugin != null) {
				this.permissionHandler = ((Permissions) permissionsPlugin).getHandler();
				send("Permissions Detected. Hooking into API");
				UsePermissions = true;
			} else {
				if(!bPerms) {
					send("Permission system not detected, defaulting to OP.");	
				} else {
					send("Permission system not detected, defaulting to Bukkit Permissions.");	
				}
				UsePermissions = false;
			}
		}
	}

	private void setupMcbans() {
		Plugin banPlugin = this.getServer().getPluginManager().getPlugin("mcbans");
		if (this.mcbansHandler == null && this.useMcBans) {
			if (banPlugin != null) {
				this.mcbansHandler = ((mcbans) banPlugin).mcb_handler;
				send("McBans Detected. Hooking into API");
				this.mcbansLoaded = true;
			} else {
				send("McBans not detected, change your config.yml to not use McBans");
				this.getServer().getPluginManager().disablePlugin(this);
				this.mcbansLoaded = false;
			}
		}
	}
	/**
	 * Defaults to Op/Permissions
	 * Checks if User has either
	 * @param p
	 * @param node
	 * @return
	 */
	public boolean isAdmin(Player p, String node) {
		if (UsePermissions) {
			return permissionHandler.has(p, "mcdefcon." + node);
		} else if(bPerms) {
			return p.hasPermission("mcdefcon." + node);
		} else {
			return p.isOp();
		}
	}

	/**
	 * Defcon Message
	 * @param p
	 * @param text
	 */
	public void colorSend(Player p, String text) {
		p.sendMessage(ChatColor.AQUA + "[McDefcon] " + ChatColor.YELLOW + text);
	}

	/**
	 * Defcon Message Broadcast
	 * @param p
	 * @param text
	 */
	public void colorSendAll(String text) {
		if(this.broadcast == true) {
			getServer().broadcastMessage(ChatColor.AQUA + "[Global Defcon] " + ChatColor.YELLOW + text);
		} else {
			return;
		}
	}

	/**
	 * Console Message
	 * @param t
	 */
	public void send(String t) {
		System.out.println("[MCDefcon] " + t);
	}

}
