package com.hadenwatne.ghostblocks.files;

import java.io.File;
import java.util.logging.Level;

import com.hadenwatne.ghostblocks.GBlock;
import com.hadenwatne.ghostblocks.GhostBlocks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Tempfile {
	private FileConfiguration temp;
	private File tempFile;
	private GhostBlocks plugin;
	
	public Tempfile(GhostBlocks c){
		plugin=c;
		this.temp = null;
	    this.tempFile = null;
	    reloadTemp();
	}
	
	public void loadBlocks(){
		if(getTemp().contains("info")){
			plugin.getLogger().log(Level.INFO, "==[ GhostBlocks: Loading blocks... ]==");
			getTemp().set("info", null);
			
			for(String blockKey : this.getTemp().getKeys(false)){
				Location l = new Location(Bukkit.getWorld(this.getTemp().getString(blockKey+".w")),this.getTemp().getDouble(blockKey+".x"),this.getTemp().getDouble(blockKey+".y"),this.getTemp().getDouble(blockKey+".z"));
				String t = this.getTemp().getString(blockKey+".t");
				int d = this.getTemp().getInt(blockKey+".d");
				
				plugin.getBlocks().add(new GBlock(Material.valueOf(t), l, (byte)d));
				
				getTemp().set(blockKey, null);
			}
			
			saveTemp();
			plugin.getLogger().log(Level.INFO, "==[ GhostBlocks: Load Complete! ]==");
		}
	}
	
	public void saveBlocks(){
		if(plugin.getBlocks().size() > 0){
			plugin.getLogger().log(Level.INFO, "==[ GhostBlocks: Saving blocks... ]==");
			
			for(GBlock gb : plugin.getBlocks()){
				getTemp().set(plugin.getBlocks().indexOf(gb)+".x", gb.getLocation().getX());
				getTemp().set(plugin.getBlocks().indexOf(gb)+".y", gb.getLocation().getY());
				getTemp().set(plugin.getBlocks().indexOf(gb)+".z", gb.getLocation().getZ());
				getTemp().set(plugin.getBlocks().indexOf(gb)+".w", gb.getLocation().getWorld().getName());
				getTemp().set(plugin.getBlocks().indexOf(gb)+".t", gb.getType().toString());
				getTemp().set(plugin.getBlocks().indexOf(gb)+".d", gb.getData());
			}
			
			getTemp().set("info", true);
			saveTemp();
			plugin.getBlocks().clear();
			plugin.getLogger().log(Level.INFO, "==[ GhostBlocks: Save Complete! ]==");
		}
	}

	public void reloadTemp(){
		if(this.tempFile == null){
			this.tempFile = new File(plugin.getDataFolder(), "temp.tmp");
		    this.temp = YamlConfiguration.loadConfiguration(this.tempFile);
		}
	}
		 
	public FileConfiguration getTemp(){
		if(this.temp == null){
			reloadTemp();
		}
	   return this.temp;
	}
		 
	public void saveTemp(){
		if ((this.temp == null) || (this.tempFile == null)) {
			return;
		}
		try{
			getTemp().save(this.tempFile);
		}catch(Exception ex){
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.tempFile, ex);
		}
	}
}
