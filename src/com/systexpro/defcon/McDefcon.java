/**
 * MCDefcon 
 * Created by SystexPro
 * Version 1.5
 * Features: Defcon Levels
 * Bugs: Folder is not created
 */
package com.systexpro.defcon;

import java.io.File;

import org.bukkit.Bukkit;
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

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.firestar.mcbans.mcbans_handler;
import com.firestar.mcbans.mcbans;
import com.systexpro.defcon.api.DefconLevel;
import com.systexpro.defcon.api.McDefconApi;
import com.systexpro.defcon.listeners.McBlockListener;
import com.systexpro.defcon.listeners.McPlayerListener;
import com.systexpro.defcon.update.McDefconUpdater;

public class McDefcon extends JavaPlugin {

	public McPlayerListener playerListener;
	public McBlockListener blockListener;
	public PermissionManager permissions;
	public mcbans_handler mcbansHandler;
	public CommandHandler commands = new CommandHandler(this);
	public Configuration config = new Configuration(new File("plugins/McDefcon/config.yml"));
	public McDefconUpdater update;
	public McDefconApi defconAPI;
	public String level1Message = "Kicked. Defcon Level 1.";
	public String level2Message = "Banned. Defcon Level 2.";
	public String level3Message = "Temp Banned. Defcon Level 3";
	public String level4Message = "Defcon Level 4. All Players Muted.";
	public String level5Message = "Defcon Level 5. Building is off.";
	public String level6Message = "Defcon Level 6. All players are frozen.";
//	public String level7Message = "Change me!!! I'm a custom Level!";
//	public String level7Command = "/say you should chnage this to do a command, /say yea lol";
	public String mcTempBanTime = "5m";
	public final String buildVersion = "#1240";
	public boolean onDefconChangeKickAllPlayers = false;
	public boolean broadcast = true;
	public boolean UsePermissions = false;
	public boolean autoUpdate = false;
	public boolean mcbansLoaded = false;
	public boolean useMcBans = false;
	public boolean bPerms = false;
	public boolean showDefconLevelOnJoin = true;
	public int defconLevel = 0;
	public int maxLevel = 7;
	public int[] levelList;

	public void onDisable() {
		loadConfig();
		send("Unloaded McDefcon Version " + this.getDescription().getVersion());
	}

	@Override
	public void onEnable() {
		send("Loading MCDefcon Version " + getDescription().getVersion());
		getDataFolder().mkdirs();
		send("Loading Configuration File.");
		loadConfig();
		playerListener = new McPlayerListener(this);
		blockListener = new McBlockListener(this);
		defconAPI = new McDefconApi(this);
		send("Starting McDefconAPI");
		if(bPerms) {
			UsePermissions = false;
		} else {
			setupPermissions();
		}
		setupMcbans();
		registerEvents();
		send("Defcon on Level: " + this.defconLevel);
//		this.getCustomCommand();
	}

	/**
	 * Load Configuration File
	 */
	public void loadConfig() {
		config.load();
		config.setHeader(
				"#====================================\n" +
				"#McBans by SystexPro\n" +
				"#Build: " + buildVersion + "\n" +
				"#Version: " + getDescription().getVersion() + "\n" +
				"#====================================\n" 
		);
		//defcon
		this.defconLevel = config.getInt("main.Defcon Level", defconLevel);
		//messages
		this.level1Message = config.getString("messages.Level 1 Message", this.level1Message);
		this.level2Message = config.getString("messages.Level 2 Message", this.level2Message);
		this.level4Message = config.getString("messages.Level 4 Message", this.level4Message);
		this.level5Message = config.getString("messages.Level 5 Message", this.level5Message);
		this.level6Message = config.getString("messages.Level 6 Message", this.level6Message);
//		this.level7Message = config.getString("messages.Level 7 Message(Change Me)", this.level7Message);
//		this.level7Command = config.getString("messages.Level 7 Command(Change Me)", this.level7Command);
		//options
		//this.autoUpdate = config.getBoolean("options.Auto Update Plugin", autoUpdate);
		this.bPerms = config.getBoolean("options.Use Bukkit Permissions", bPerms);
		this.broadcast = config.getBoolean("options.Broadcast Level Change", broadcast);
		this.useMcBans = config.getBoolean("options.Use McBans", useMcBans);
		this.showDefconLevelOnJoin = config.getBoolean("options.Show Defcon Level On Join", showDefconLevelOnJoin);
		this.onDefconChangeKickAllPlayers = config.getBoolean("options.Kick All Players on Defcon Level Change", onDefconChangeKickAllPlayers);
		this.mcTempBanTime = config.getString("options.McBans Temporary Ban Time", mcTempBanTime);
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
				if(this.defconAPI.hasDefconPermission(p, "admin")) {
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
				defconAPI.setDefconLevel(DefconLevel.LEVEL_0.getLevel());
				colorSend(player, "Defcon Level set to 0.");
				colorSend(player, "All Players will join without check.");
				colorSendAll("Defcon Level is set to 0.");
			} else if(args[1].equalsIgnoreCase("1")) {
				defconAPI.setDefconLevel(DefconLevel.LEVEL_1.getLevel());
				colorSend(player, "Defcon Level set to 1.");
				colorSend(player, "All Players will be kicked off. (Except Ops)");
				colorSendAll("Defcon Level is set to 1.");
				kickAllPlayers(defconLevel);
			} else if(args[1].equalsIgnoreCase("2")) {
				if(useMcBans) {
					defconAPI.setDefconLevel(DefconLevel.LEVEL_2.getLevel());
					colorSend(player, "Defcon level set to 2.");
					colorSend(player, "All Incoming will be temporary banned for " + this.mcTempBanTime);
					colorSendAll("Defcon Level is set to 2.");
					kickAllPlayers(defconLevel);
				} else {
					colorSend(player, "This Level is for McBans, Please load McBans to use it.");
				}
			} else if(args[1].equalsIgnoreCase("3")) {
				defconAPI.setDefconLevel(DefconLevel.LEVEL_3.getLevel());
				colorSend(player, "Defcon level set to 3.");
				colorSend(player, "All Incoming Players will be Banned. (Except Ops)");
				colorSendAll("Defcon Level is set to 3.");
			} else if(args[1].equalsIgnoreCase("4")) {
				defconAPI.setDefconLevel(DefconLevel.LEVEL_4.getLevel());
				colorSend(player, "Defcon level set to 4.");
				colorSend(player, "All Players are Muted");
				colorSendAll("Defcon Level is set to 4.");
			} else if(args[1].equalsIgnoreCase("5")) {
				defconAPI.setDefconLevel(DefconLevel.LEVEL_5.getLevel());
				colorSend(player, "Defcon level set to 5.");
				colorSend(player, "Building is off.");
				colorSendAll("Defcon Level is set to 5.");
			} else if(args[1].equalsIgnoreCase("6")) {
				defconAPI.setDefconLevel(DefconLevel.LEVEL_6.getLevel());
				colorSend(player, "Defcon level set to 6.");
				colorSend(player, "All players are Frozen.");
				colorSendAll("Defcon Level is set to 6.");
//			} else if(args[1].equalsIgnoreCase("7")) {
//				defconAPI.setDefconLevel(DefconLevel.LEVEL_7.getLevel());
//				colorSend(player, "Defcon level set to 7.");
//				colorSend(player, "Using Custom Command.");
//				colorSendAll("Defcon Level is set to 7.");
			} else if(args[1].equalsIgnoreCase("view")) {
				colorSend(player, ChatColor.AQUA + "Defcon 1 " + ChatColor.GRAY + ": Kicks all Incoming Players.");
				colorSend(player, ChatColor.AQUA + "Defcon 2 " + ChatColor.GRAY + ": Temporary Bans all Incoming Players(McBans Only).");
				colorSend(player, ChatColor.AQUA + "Defcon 3 " + ChatColor.GRAY + ": Bans all Incoming Players.");
				colorSend(player, ChatColor.AQUA + "Defcon 4 " + ChatColor.GRAY + ": Mutes all Players.");
				colorSend(player, ChatColor.AQUA + "Defcon 5 " + ChatColor.GRAY + ": Turns off Building.");
				colorSend(player, ChatColor.AQUA + "Defcon 6 " + ChatColor.GRAY + ": Players are Frozen.");
//				colorSend(player, ChatColor.AQUA + "Defcon 7 " + ChatColor.GRAY + ": Custom Command.");
			} else {
				colorSend(player, "Unknown Defcon Level");
			}
		} 
		if(cmd.equalsIgnoreCase("check")) {
			colorSend(player, "Defcon is on Level: " + defconLevel);
		}
		if(cmd.equalsIgnoreCase("cs")) {
			colorSend(player, "Kicking all Players.");
			kickAllPlayersFromServer(player);
		}
		if(cmd.equalsIgnoreCase("reload") || cmd.equalsIgnoreCase("r")) {
			colorSend(player, "Reloading Configuration File.");
			send("Reloading Configuration File.");
			loadConfig();
		}
		if(cmd.equalsIgnoreCase("version") || cmd.equalsIgnoreCase("v")) {
			colorSend(player, "Version: " + getDescription().getVersion() + ".");
			colorSend(player, "Bukkit Build: " + buildVersion + ".");
			colorSend(player, "Made by SystexPro");
		} 
		if(cmd.equalsIgnoreCase("?") || cmd.equalsIgnoreCase("help")) {
			help(player);
		}
	}
	
//	public void getCustomCommand() {
//		String[] args = this.level7Command.split(", ");
//		for(int x = 0; x < args.length; x++) {
//			send(args[x]);
//		}
//	}

	public void kickAllPlayers(int level) {
		if(onDefconChangeKickAllPlayers) {
			Player[] online = getServer().getOnlinePlayers();
			for(int x = 0; x < online.length; x++) {
				Player p = online[x];
				if(!this.defconAPI.hasDefconPermission(p, "admin") || !this.defconAPI.hasDefconPermission(p, "accept")) {
					p.kickPlayer("Defcon Level set to " + level);
				}
			}
		}
	}

	public void kickAllPlayersFromServer(Player p1) {
		Player[] online = getServer().getOnlinePlayers();
		for(int x = 0; x < online.length; x++) {
			Player p = online[x];
			if(!this.defconAPI.hasDefconPermission(p, "admin") || !this.defconAPI.hasDefconPermission(p, "accept")) {
				p.kickPlayer("Kicking all Players - " + p1.getDisplayName());
			}
		}
	}

	/**
	 * Register Events
	 */
	private void registerEvents()
	{
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_LOGIN, playerListener , Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener , Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener , Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener , Priority.Normal, this);
	}


	/**
	 * Help Menu
	 * @param sender
	 * @return
	 */
	private boolean help(CommandSender sender) {
		Player p = (Player) sender;
		p.sendMessage(ChatColor.DARK_RED + "==MCDefcon " + getDescription().getVersion() + "==");
		p.sendMessage(ChatColor.AQUA + "Command Triggers - /dc, /defcon");
		p.sendMessage(ChatColor.DARK_AQUA + "/dc level <level> - Sets Defcon Level: 0, 1, 2, 3, 4, 5, and 6.");
		p.sendMessage(ChatColor.DARK_AQUA + "/dc level view - Shows the current Defcon Levels.");
		p.sendMessage(ChatColor.DARK_AQUA + "/dc check - Checks what level Defcon is on.");
		p.sendMessage(ChatColor.DARK_AQUA + "/dc reload - Reload Configuration File.");
		p.sendMessage(ChatColor.DARK_AQUA + "/dc cs - (Clear Server) Kicks every from the server.");
		p.sendMessage(ChatColor.DARK_AQUA + "/dc ? - Help Menu.");
		return true;
	}

	/**
	 * Connect to Permissions
	 */
	private void setupPermissions() {
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")){
			if (permissions == null) {
				permissions = PermissionsEx.getPermissionManager();
				send("Permissions Detected. Hooking into API");
				UsePermissions = true;
			}
		} else {
			send("McBans not detected, change your config.yml to not use McBans");
		} 
	}

	/**
	 * Setup McBans
	 */
	private void setupMcbans() {
		Plugin banPlugin = getServer().getPluginManager().getPlugin("mcbans");
		if (mcbansHandler == null && useMcBans) {
			if (banPlugin != null) {
				mcbansHandler = ((mcbans) banPlugin).mcb_handler;
				send("McBans Detected. Hooking into API");
				mcbansLoaded = true;
			} else {
				send("McBans not detected, change your config.yml to not use McBans");
				getServer().getPluginManager().disablePlugin(this);
				mcbansLoaded = false;
			}
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
		if(broadcast) {
			getServer().broadcastMessage(ChatColor.AQUA + "[Global Defcon] " + ChatColor.YELLOW + text);
		} else {
			return;
		}
	}

	/**
	 * Console Message
	 * @param object
	 */
	public void send(Object object) {
		System.out.println("[MCDefcon] " + object);
	}

}
