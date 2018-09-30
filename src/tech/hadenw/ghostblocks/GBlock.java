package tech.hadenw.ghostblocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.packetwrapper.WrapperPlayServerBlockChange;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
public class GBlock extends BukkitRunnable{
	private Material type;
	private Location location;
	private short data;
	
	public GBlock(Material blockType, Location blockLocation, short blockData){
		type=blockType;
		location = blockLocation;
		data = blockData;
	}
	
	public void sendToPlayer(Player p, boolean plib){
		if(!p.hasMetadata("GB_ts")){
			if(p.getWorld().getName()==location.getWorld().getName()) {
				if(!plib)
					p.sendBlockChange(location, location.getBlock().getBlockData());
				else {
					WrapperPlayServerBlockChange packet = new WrapperPlayServerBlockChange();
					packet.setLocation(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
					packet.setBlockData(WrappedBlockData.createData(type));
					packet.sendPacket(p);
				}
			}
		}
	}
	
	public void removeFromView(Player p, boolean plib){
		if(p.getWorld().getName()==location.getWorld().getName()) {
			if(!plib)
				p.sendBlockChange(location, location.getBlock().getBlockData());
			else {
				WrapperPlayServerBlockChange packet = new WrapperPlayServerBlockChange();
				packet.setLocation(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
				packet.setBlockData(WrappedBlockData.createData(location.getBlock().getType()));
				packet.sendPacket(p);
			}
		}
	}
	
	public Location getLocation(){
		return location;
	}
	
	public Material getType(){
		return type;
	}
	
	public short getData(){
		return data;
	}
	
	public void run(){
		location.getBlock().setType(Material.AIR);
	}
}
