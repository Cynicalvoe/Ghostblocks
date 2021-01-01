package com.hadenwatne.ghostblocks;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class CmdExecutor implements CommandExecutor{
	
	GhostBlocks plugin;
	CmdExecutor(GhostBlocks c){
		plugin=c;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("gb")){
			if(args.length==1){
				if(args[0].equalsIgnoreCase("get")){
					if(sender.hasPermission("ghostblocks.get")){
						Player p = (Player)sender;
						p.getInventory().addItem(plugin.getGhostdrop());
						sender.sendMessage(plugin.getMessages().gotGhostdrop());
					}else{
						sender.sendMessage(plugin.getMessages().noPermission());
					}
				}else{
					if(args[0].equalsIgnoreCase("reset")){
						if(sender.hasPermission("ghostblocks.reset")){
							plugin.getBlocks().clear();
							sender.sendMessage(plugin.getMessages().removedAll());
						}else{
							sender.sendMessage(plugin.getMessages().noPermission());
						}
					}else{
						if(args[0].equalsIgnoreCase("reload")){
							if(sender.hasPermission("ghostblocks.reload")){
								plugin.getUserConfig().loadValues();
								sender.sendMessage(plugin.getMessages().reloaded());
							}else{
								sender.sendMessage(plugin.getMessages().noPermission());
							}
						}else{
							if(args[0].equalsIgnoreCase("truesight")){
								if(sender.hasPermission("ghostblocks.truesight")){
									if(sender instanceof Player){
										Player p = (Player)sender;
										if(p.hasMetadata("GB_ts")){
											p.removeMetadata("GB_ts", plugin);
											sender.sendMessage(plugin.getMessages().truesightOff());
										}else{
											p.setMetadata("GB_ts", new FixedMetadataValue(plugin, true));
											
											for(GBlock b : plugin.getBlocks()) {
												if(b.getLocation().getWorld().getName()==p.getWorld().getName()) {
													b.removeFromView(p, plugin.isProtocolLib());
												}
											}
											
											sender.sendMessage(plugin.getMessages().truesightOn());
										}
									}else{
										sender.sendMessage(plugin.getMessages().mustBePlayer());
									}
								}else{
									sender.sendMessage(plugin.getMessages().noPermission());
								}
							}else{
								sender.sendMessage(plugin.getMessages().wrongCommand());
							}
						}
					}
				}
			}else if(args.length==2 && args[0].equalsIgnoreCase("reset")) {
				if(sender.hasPermission("ghostblocks.reset")){
					if(sender instanceof Player) {
						Player p = (Player)sender;
						
						try {
							int radius = Integer.parseInt(args[1]);
							int removed = 0;
							
							for(GBlock block : new ArrayList<GBlock>(plugin.getBlocks())) {
								if(block.getLocation().getWorld().getName()==p.getWorld().getName()) {
									if(block.getLocation().distance(p.getLocation())<=radius) {
										plugin.getBlocks().remove(block);
										removed++;
									}
								}
							}
							
							sender.sendMessage(plugin.getMessages().removedSome(removed, radius));
						}catch(Exception e) {
							sender.sendMessage("&a[&e&lGhostblocks&a] &cAn error occurred. Please use &e/gb&c for help.");
						}
					}else sender.sendMessage(plugin.getMessages().mustBePlayer());
				}else {
					sender.sendMessage(plugin.getMessages().noPermission());
				}
			}else{
				String version = plugin.getDescription().getVersion().toString(); 
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a==========[ &f&ki&e&l Ghostblocks Help &cv"+version+" &f&ki&a ]=========="));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&e&l> &8/gb get&7 - Get a Ghostdrop"));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&e&l> &8/gb reset [radius] &7 - Remove all Ghostblocks"));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&e&l> &8/gb reload &7 - Reload the configuration"));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&e&l> &8/gb truesight &7 - Toggle Ghostblock visibility"));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&a==========[ &f&ki&e&l Ghostblocks Help &cv"+version+" &f&ki&a ]=========="));
			}
			return true;
		}
		return false;
	}
}
