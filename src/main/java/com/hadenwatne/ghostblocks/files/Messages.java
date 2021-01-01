package com.hadenwatne.ghostblocks.files;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;

import com.hadenwatne.ghostblocks.GhostBlocks;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Messages {
	GhostBlocks plugin;
	private FileConfiguration messages;
	private File messagesFile;
	private HashMap<String, String> vals;
	private final String colorChar = Character.toString(ChatColor.COLOR_CHAR);
	
	public Messages(GhostBlocks c){
		plugin=c;
		this.messages = null;
	    this.messagesFile = null;
	    
	    vals = new HashMap<String, String>();
	    this.reloadMessages();
	    
	    if(this.getMessages().contains("plugin-title")){
	    	this.loadValues();
	    }else{
	    	this.saveDefaultValues();
	    }
	    
	    plugin.getLogger().log(Level.INFO, "Loaded Custom Messages");
	}
	
	public void saveDefaultValues(){
		this.getMessages().set("plugin-title", "&a[&e&lGhostblocks&a]");
		this.getMessages().set("cannot-place", "&cYou can't place that here!");
		this.getMessages().set("got-ghostdrop", "&aGot a Ghostdrop!");
		this.getMessages().set("no-permission", "&cYou don't have permission!");
		this.getMessages().set("removed-all", "&aRemoved all Ghostblocks from the system!");
		this.getMessages().set("removed-some", "&aRemoved %BLOCKS% Ghostblocks within &e%RADIUS% &ablocks!");
		this.getMessages().set("plugin-reloaded", "&aConfiguration was reloaded!");
		this.getMessages().set("truesight-on", "&aTruesight activated!");
		this.getMessages().set("truesight-off", "&eTruesight deactivated.");
		this.getMessages().set("must-be-player", "&cThat must be run by a player!");
		this.getMessages().set("wrong-command", "&cInvalid command! Try &e/gb&c for help.");
		this.getMessages().set("update-available", "&aAn update is available! Download it now on SpigotMC.org");

		this.saveMessages();
		this.loadValues();
	}
	
	public void loadValues(){
		vals.clear();
		
		vals.put("plugin-title", getMessages().getString("plugin-title").replaceAll("&", colorChar)+" ");
		vals.put("cannot-place", getMessages().getString("cannot-place").replaceAll("&", colorChar));
		vals.put("got-ghostdrop", getMessages().getString("got-ghostdrop").replaceAll("&", colorChar));
		vals.put("no-permission", getMessages().getString("no-permission").replaceAll("&", colorChar));
		vals.put("removed-all", getMessages().getString("removed-all").replaceAll("&", colorChar));
		vals.put("removed-some", getMessages().getString("removed-some").replaceAll("&", colorChar));
		vals.put("plugin-reloaded", getMessages().getString("plugin-reloaded").replaceAll("&", colorChar));
		vals.put("truesight-on", getMessages().getString("truesight-on").replaceAll("&", colorChar));
		vals.put("truesight-off", getMessages().getString("truesight-off").replaceAll("&", colorChar));
		vals.put("must-be-player", getMessages().getString("must-be-player").replaceAll("&", colorChar));
		vals.put("wrong-command", getMessages().getString("wrong-command").replaceAll("&", colorChar));
		vals.put("update-available", getMessages().getString("update-available").replaceAll("&", colorChar));
	}
	
	public String getTitle(){
		return vals.get("plugin-title");
	}
	
	public String cannotPlace(){
		return vals.get("plugin-title")+vals.get("cannot-place");
	}
	public String gotGhostdrop(){
		return vals.get("plugin-title")+vals.get("got-ghostdrop");
	}
	public String noPermission(){
		return vals.get("plugin-title")+vals.get("no-permission");
	}
	public String removedAll(){
		return vals.get("plugin-title")+vals.get("removed-all");
	}
	public String removedSome(int blocks, int radius){
		return vals.get("plugin-title")+vals.get("removed-some").replaceAll("%BLOCKS%", Integer.toString(blocks)).replaceAll("%RADIUS%", Integer.toString(radius));
	}
	public String reloaded(){
		return vals.get("plugin-title")+vals.get("plugin-reloaded");
	}
	public String truesightOn(){
		return vals.get("plugin-title")+vals.get("truesight-on");
	}
	public String truesightOff(){
		return vals.get("plugin-title")+vals.get("truesight-off");
	}
	public String mustBePlayer(){
		return vals.get("plugin-title")+vals.get("must-be-player");
	}
	public String wrongCommand(){
		return vals.get("plugin-title")+vals.get("wrong-command");
	}
	public String updateAvailable(){
		return vals.get("plugin-title")+vals.get("update-available");
	}
	
	public void reloadMessages(){
		if (this.messagesFile == null){
			this.messagesFile = new File(plugin.getDataFolder()+"/messages.yml");
			this.messages = YamlConfiguration.loadConfiguration(this.messagesFile);
		}
	}
	 
	public FileConfiguration getMessages(){
		if (this.messages == null) {
			reloadMessages();
		}
		return this.messages;
	}
	 
	public void saveMessages(){
		if ((this.messages == null) || (this.messagesFile == null)) {
			return;
		}
		
		try{
			getMessages().save(this.messagesFile);
		} catch (Exception ex){
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.messagesFile, ex);
		}
	}
}
