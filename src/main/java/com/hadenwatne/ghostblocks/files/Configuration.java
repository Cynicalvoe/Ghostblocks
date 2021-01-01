package com.hadenwatne.ghostblocks.files;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.hadenwatne.ghostblocks.GhostBlocks;
import org.bukkit.ChatColor;

public class Configuration {
	private boolean allowCrafting;
	private int blockRefresh;
	private boolean doDisappear;
	private int disappearDistance;
	private boolean doExplode;
	private int explosionRadius;
	private boolean theSecretSetting;
	private boolean checkForUpdates;
	
	private String ghostDropName;
	private List<String> ghostDropLore;
	private String ghostBlockName;
	private List<String> ghostBlockLore;

	private final String colorChar = Character.toString(ChatColor.COLOR_CHAR);
	
	GhostBlocks pl;
	public Configuration(GhostBlocks plugin){
		pl=plugin;
		
		plugin.getLogger().log(Level.INFO, "Loading configuration...");
		pl.saveDefaultConfig();
		this.loadValues();
		plugin.getLogger().log(Level.INFO, "Loaded configuration!");
	}
	
	public void loadValues(){
		pl.reloadConfig();
		
		try {
			allowCrafting = pl.getConfig().getBoolean("allowCrafting");
		}catch(Exception e){
			allowCrafting = true;
			pl.getLogger().log(Level.WARNING, "Error loading \"allowCrafting\"! Using default value.");
		}
		
		try {
			blockRefresh = pl.getConfig().getInt("blockRefresh");
			blockRefresh = blockRefresh <= 0 ? 30 : blockRefresh;
		}catch(Exception e){
			blockRefresh = 30;
			pl.getLogger().log(Level.WARNING, "Error loading \"blockRefresh\"! Using default value.");
		}
		
		try {
			doDisappear = pl.getConfig().getBoolean("doDisappear");
		}catch(Exception e){
			doDisappear = true;
			pl.getLogger().log(Level.WARNING, "Error loading \"doDisappear\"! Using default value.");
		}
		
		try {
			disappearDistance = pl.getConfig().getInt("disappearDistance");
			disappearDistance = disappearDistance <= 0 ? 5 : disappearDistance;
		}catch(Exception e){
			disappearDistance = 5;
			pl.getLogger().log(Level.WARNING, "Error loading \"disappearDistance\"! Using default value.");
		}
		
		try {
			doExplode = pl.getConfig().getBoolean("doExplode");
		}catch(Exception e){
			doExplode = true;
			pl.getLogger().log(Level.WARNING, "Error loading \"doExplode\"! Using default value.");
		}
		
		try {
			explosionRadius = pl.getConfig().getInt("explosionRadius");
			explosionRadius = explosionRadius <= 0 ? 5 : explosionRadius;
		}catch(Exception e){
			explosionRadius = 5;
			pl.getLogger().log(Level.WARNING, "Error loading \"explosionRadius\"! Using default value.");
		}
		
		try {
			theSecretSetting = pl.getConfig().getBoolean("theSecretSetting");
		}catch(Exception e){
			theSecretSetting = true;
			pl.getLogger().log(Level.WARNING, "Error loading \"theSecretSetting\"! Using default value.");
		}
		
		try {
			checkForUpdates = pl.getConfig().getBoolean("checkForUpdates");
		}catch(Exception e){
			checkForUpdates = true;
			pl.getConfig().set("checkForUpdates", true);
			pl.saveConfig();
			pl.getLogger().log(Level.WARNING, "Error loading \"checkForUpdates\"! Using default value.");
		}
		
		// Added later
		try {
			ghostDropName = pl.getConfig().getString("ghostDrop.name").replaceAll("&", colorChar);
			ghostBlockName = pl.getConfig().getString("ghostBlock.name").replaceAll("&", colorChar);
			
			ghostDropLore =  pl.getConfig().getStringList("ghostDrop.lore");
			for(int i=0; i<ghostDropLore.size(); i++) {
				ghostDropLore.set(i, ghostDropLore.get(i).replaceAll("&", colorChar));
			}
			
			ghostBlockLore =  pl.getConfig().getStringList("ghostBlock.lore");
			for(int i=0; i<ghostBlockLore.size(); i++) {
				ghostBlockLore.set(i, ghostBlockLore.get(i).replaceAll("&", colorChar));
			}
		}catch(Exception e){
			ghostDropName = ChatColor.translateAlternateColorCodes('&', "&f&ki&6&lGhostdrop&f&ki");
			ghostBlockName = ChatColor.translateAlternateColorCodes('&',"&f&ki&e&lGhostblock&f&ki");
			ghostDropLore = new ArrayList<String>();
			ghostDropLore.add(ChatColor.translateAlternateColorCodes('&',"&aDrag onto any block"));
			ghostBlockLore = new ArrayList<String>();
			ghostBlockLore.add(ChatColor.translateAlternateColorCodes('&',"&dType: &7%TYPE%:%DATA%"));
			
			pl.getConfig().set("ghostDrop.name", "&f&ki&6&lGhostdrop&f&ki");
			pl.getConfig().set("ghostDrop.lore", ghostDropLore);
			pl.getConfig().set("ghostBlock.name", "&f&ki&e&lGhostblock&f&ki");
			pl.getConfig().set("ghostBlock.lore", ghostBlockLore);
			pl.saveConfig();
			pl.getLogger().log(Level.WARNING, "Error loading \"Item Configuration\" settings! Using default values.");
		}
	}
	
	public String getGhostDropName() {
		return this.ghostDropName;
	}
	
	public List<String> getGhostDropLore() {
		return this.ghostDropLore;
	}
	
	public String getGhostBlockName() {
		return this.ghostBlockName;
	}
	
	public List<String> getGhostBlockLore() {
		return this.ghostBlockLore;
	}
	
	public boolean getAllowCrafting(){
		return this.allowCrafting;
	}
	
	public int getBlockRefresh(){
		return this.blockRefresh;
	}
	
	public boolean getDoDisappear(){
		return this.doDisappear;
	}
	
	public int getDisappearDistance(){
		return this.disappearDistance;
	}
	
	public boolean getDoExplode(){
		return this.doExplode;
	}
	
	public int getExplosionRadius(){
		return this.explosionRadius;
	}
	
	public boolean getTheSecretSetting(){
		return this.theSecretSetting;
	}
	
	public boolean getCheckForUpdates(){
		return this.checkForUpdates;
	}
}
