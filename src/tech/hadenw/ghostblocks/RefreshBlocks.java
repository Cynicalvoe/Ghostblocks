package tech.hadenw.ghostblocks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RefreshBlocks extends BukkitRunnable{
	
	GhostBlocks plugin;
	RefreshBlocks(GhostBlocks c){
		plugin=c;
	}
	
	public void run(){
		for(Player p : Bukkit.getOnlinePlayers()){
			for(GBlock gb : plugin.getBlocks()){
				gb.sendToPlayer(p, plugin.isProtocolLib());
			}
		}
	}
}
