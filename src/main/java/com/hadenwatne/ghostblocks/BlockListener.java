package com.hadenwatne.ghostblocks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockListener implements Listener{
	GhostBlocks plugin;
	BlockListener(GhostBlocks c){
		plugin=c;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlace(BlockPlaceEvent e){
		ItemStack hand = e.getPlayer().getInventory().getItemInMainHand();
		
		if(((Damageable)hand.getItemMeta()) != null) {
			if(hand.isSimilar(plugin.getGhostblockItem(e.getBlock().getType(), hand.getAmount(), ((Damageable)hand.getItemMeta()).getDamage()))){
				if(e.canBuild() && e.isCancelled()==false && e.getPlayer().hasPermission("ghostblocks.place")){
					GBlock toRemove = null;
					for(GBlock gb : plugin.getBlocks()){
						if(gb.getLocation().getX()==e.getBlock().getLocation().getX() && gb.getLocation().getY()==e.getBlock().getLocation().getY() && gb.getLocation().getZ()==e.getBlock().getLocation().getZ() && gb.getLocation().getWorld().getName()==e.getBlock().getLocation().getWorld().getName()){
							toRemove = gb;
							break;
						}
					}
					
					plugin.getBlocks().remove(toRemove);
					
					GBlock block = new GBlock(e.getBlock().getType(), e.getBlock().getLocation(), ((Damageable)hand.getItemMeta()).getDamage());
					plugin.getBlocks().add(block);
					block.runTaskLater(plugin, 100);
				}else{
					e.setCancelled(true);
					e.getPlayer().sendMessage(plugin.getMessages().cannotPlace());
				}
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		class Test extends BukkitRunnable{
			Player pl;
			Test(Player pla){pl=pla;}
			public void run(){
				if(pl.isOnline()){
					for(GBlock gb : plugin.getBlocks()){
						gb.sendToPlayer(pl, plugin.isProtocolLib());
						this.cancel();
					}
				}else{
					this.cancel();
				}
			}
		}
		new Test(p).runTaskLater(plugin, 20);
		
		if(p.hasPermission("ghostblocks.admin") && plugin.isUpdateAvailable())
			p.sendMessage(plugin.getMessages().updateAvailable());
	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent e){
		if(plugin.getUserConfig().getDoExplode()){
			List<GBlock> toRemove = new ArrayList<GBlock>();
			for(GBlock gb : plugin.getBlocks()){
				if(gb.getLocation().getWorld().getName().equals(e.getEntity().getWorld().getName()))
					if(gb.getLocation().distance(e.getEntity().getLocation()) <= plugin.getUserConfig().getExplosionRadius()){
						toRemove.add(gb);}}
			
			for(GBlock gb: toRemove){
				plugin.getBlocks().remove(gb);
				for(Player p : Bukkit.getOnlinePlayers()){
					gb.removeFromView(p, plugin.isProtocolLib());}}
		}
	}
	
	@EventHandler
	public void onUseDrop(InventoryClickEvent e){
		if(e.getCursor() != null && e.getCurrentItem() != null){
			ItemStack cursor = e.getCursor().clone();
			ItemStack block = e.getCurrentItem().clone();
			
			if(cursor.isSimilar(plugin.getGhostdrop())){
				if(block.getType().isBlock() && block.getType() != Material.AIR && block.getType() != Material.CHEST && !block.isSimilar(plugin.getGhostblockItem(block.getType(), 1, ((Damageable)block.getItemMeta()).getDamage()))){			
					if(e.isLeftClick()){
						
						int difference = cursor.getAmount() - block.getAmount(); // 0 == same, <0 == more blocks than drops, >0 == more drops than blocks
						int toreturn = Math.abs(difference);
						int lowest = cursor.getAmount()<=block.getAmount() ? cursor.getAmount() : block.getAmount();
						
						e.getClickedInventory().setItem(e.getSlot(), new ItemStack(Material.AIR));
						e.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
						
						e.getClickedInventory().setItem(e.getSlot(),plugin.getGhostblockItem(block.getType(), lowest, ((Damageable)block.getItemMeta()).getDamage()));
						
						if(difference > 0) { // More drops than blocks, return drops in difference
							ItemStack gd = plugin.getGhostdrop();
							gd.setAmount(toreturn);
							e.getWhoClicked().setItemOnCursor(gd);
						}else if(difference < 0) { // More blocks than drops, return blocks in difference
							block.setAmount(toreturn);
							e.getWhoClicked().setItemOnCursor(block);
						}
						
						((Player)e.getWhoClicked()).updateInventory();
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if(e.getFrom().getWorld().getName().equals(e.getTo().getWorld().getName())){
			Location l = e.getTo();
			
			if(plugin.getUserConfig().getDoDisappear()){
				for(GBlock gb : plugin.getBlocks()){
					if(l.getWorld().getName().equals(gb.getLocation().getWorld().getName())) {
						if(l.distance(gb.getLocation())<=plugin.getUserConfig().getDisappearDistance()){
							if(plugin.getUserConfig().getTheSecretSetting()){
								if(gb.getLocation().getBlock().getType()==Material.AIR){
									gb.removeFromView(e.getPlayer(), plugin.isProtocolLib());
								}
							}else{
								gb.removeFromView(e.getPlayer(), plugin.isProtocolLib());
							}
						}
					}
				}
			}
		}
	}
}
