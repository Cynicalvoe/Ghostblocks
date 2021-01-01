package com.hadenwatne.ghostblocks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class Create implements Listener{
	private Player p;
	private ItemStack c;
	private ItemStack i;
	private Inventory GUI;
	private GhostBlocks plugin;
	private ItemStack bench;
	
	public Create(GhostBlocks plug, Player pl, ItemStack cursor, ItemStack item) {
		p=pl;
		c=cursor;
		i=item;
		plugin=plug;
		bench = this.bench();
		
		GUI = Bukkit.createInventory(null, InventoryType.DISPENSER, "Craft Ghostblocks");
		p.setMetadata("gb_create", new FixedMetadataValue(plugin, true));
		GUI.setItem(4, bench);
		GUI.setItem(3, cursor);
		GUI.setItem(5, item);
		
		p.closeInventory();
		p.openInventory(GUI);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getClickedInventory().equals(GUI)) {
			if(GUI.contains(bench)) {
				if(e.isLeftClick()) {
					if(e.getCurrentItem() != null && e.getCurrentItem().isSimilar(bench)) {
						GUI.clear();
						int difference = c.getAmount() - i.getAmount(); // 0 == same, <0 == more blocks than drops, >0 == more drops than blocks
						int toreturn = Math.abs(difference);
						int lowest = c.getAmount()<=i.getAmount() ? c.getAmount() : i.getAmount();
						
						GUI.setItem(4, plugin.getGhostblockItem(i.getType(), lowest, ((Damageable)i.getItemMeta()).getDamage()));
						
						if(difference > 0) {
							c.setAmount(toreturn);
							GUI.addItem(c);
						}else if(difference < 0) {
							i.setAmount(toreturn);
							GUI.addItem(i);
						}
						
						p.updateInventory();
					}else e.setCancelled(true);
				}else e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		this.unregister();
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		this.unregister();
	}
	
	@EventHandler
	public void onLeave(PlayerKickEvent e) {
		this.unregister();
	}
	
	private ItemStack bench() {
		ItemStack b = new ItemStack(Material.CRAFTING_TABLE);
		ItemMeta im = b.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&l<< &a&lCraft &e&l>>"));
		b.setItemMeta(im);
		return b;
	}
	
	private void unregister() {
		InventoryClickEvent.getHandlerList().unregister(this);
		InventoryCloseEvent.getHandlerList().unregister(this);
		PlayerQuitEvent.getHandlerList().unregister(this);
		PlayerKickEvent.getHandlerList().unregister(this);
		
		for(ItemStack item : GUI.getContents()) {
			if(item != null && item.getType()!=Material.AIR && !item.isSimilar(bench))
				p.getWorld().dropItem(p.getEyeLocation(), item);
		}
		
		p.removeMetadata("gb_create", plugin);
		p.closeInventory();
	}
}
