package tech.hadenw.ghostblocks;

import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import tech.hadenw.ghostblocks.files.Configuration;
import tech.hadenw.ghostblocks.files.Messages;
import tech.hadenw.ghostblocks.files.Tempfile;

public class GhostBlocks extends JavaPlugin{
	private Tempfile tempfile;
	private Configuration config;
	private List<GBlock> ghostblocks;
	private Messages m;
	private boolean isUpdate;
	private boolean isPLib;
	private String version;
	private String uURL;
	
	public void onEnable(){
		version = this.getServer().getBukkitVersion();
		
		// Moving to ProtocolLib, this is here for a sort of "pre check"
		Plugin plib = Bukkit.getPluginManager().getPlugin("ProtocolLib");
		if(plib != null && plib.isEnabled()) {
			isPLib = true;
			this.getLogger().log(Level.INFO, "GhostBlocks will be moving to use ProtocolLib in a future update. You're good to go!");
		} else {
			isPLib = false;
			this.getLogger().log(Level.WARNING, "GhostBlocks will be moving to use ProtocolLib in a future update, and it doesn't seem to be installed. GhostBlocks will still function, but it"
					+ " is recommended to install ProtocolLib soon.");
		}
		
		config = new Configuration(this);
		isUpdate = this.checkUpdate();
		tempfile = new Tempfile(this);
		ghostblocks = new ArrayList<GBlock>();
		m = new Messages(this);
		tempfile.loadBlocks();
		
		if(isUpdate) 
			this.getLogger().log(Level.INFO, "An update is available! Download it on SpigotMC.org to keep up with the latest features and bug fixes.");
		
		Bukkit.getServer().getPluginManager().registerEvents(new BlockListener(this), this);
		new RefreshBlocks(this).runTaskTimer(this, config.getBlockRefresh()*20, config.getBlockRefresh()*20);
		this.getCommand("gb").setExecutor(new CmdExecutor(this));
		this.getCommand("gb").setTabCompleter(new CommandTabComplete());
		
		if(config.getAllowCrafting())
			this.registerRecipes();
		
		new DRM();
	}
	
	public String getVersion() {
		return version;
	}
	
	public void onDisable(){
		tempfile.saveBlocks();
	}
	
	public boolean isProtocolLib() {
		return isPLib;
	}
	
	public boolean isUpdateAvailable() {
		return isUpdate;
	}
	
	public Messages getMessages() {
		return m;
	}
	
	public List<GBlock> getBlocks(){
		return ghostblocks;
	}
	
	public Configuration getUserConfig(){
		return config;
	}
	
	public void setUURL(String a) {
		uURL = uURL+a;
	}
	
	public String getUURL() {
		return uURL;
	}
	
	private void registerRecipes(){
		{
			NamespacedKey key = new NamespacedKey(this, "gb-gd1");
			ShapedRecipe sr = new ShapedRecipe(key, this.getGhostdrop());
		    sr.shape("GGG","GDG","GGG");
		    sr.setIngredient('G', Material.GOLD_INGOT);
		    sr.setIngredient('D', Material.GHAST_TEAR);
		    Bukkit.addRecipe(sr);
		}
		{
			NamespacedKey key = new NamespacedKey(this, "gb-gd2");
			ShapedRecipe sr = new ShapedRecipe(key, this.getGhostdrop());
		    sr.shape("III","IDI","III");
		    sr.setIngredient('I', Material.IRON_INGOT);
		    sr.setIngredient('D', Material.GHAST_TEAR);
		    Bukkit.addRecipe(sr);
		}
	}
	
	public ItemStack getGhostdrop(){
		ItemStack item = new ItemStack(Material.GHAST_TEAR);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(this.getUserConfig().getGhostDropName());
		im.setLore(this.getUserConfig().getGhostDropLore());
		item.setItemMeta(im);
		item = EnchantGlow.addGlow(item);
		
		return item;
	}
	
	public ItemStack getGhostblockItem(Material type, int amt, short data){
		ItemStack item = new ItemStack(type, amt, data);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(this.getUserConfig().getGhostBlockName());
		
		List<String> lore = new ArrayList<String>(this.getUserConfig().getGhostBlockLore());
		for(int i=0; i<lore.size(); i++) {
			lore.set(i, lore.get(i).replaceAll("%TYPE%", type.toString()).replaceAll("%DATA%", Short.toString(data)));
		}
		
		im.setLore(lore);
		item.setItemMeta(im);
		item = EnchantGlow.addGlow(item);
		
		return item;
	}
	
	private boolean checkUpdate() {
		uURL = new String(Base64.getDecoder().decode("aHR0cDovL2RybS5oYWRlbncudGVjaC9wbHVnaW4vY2hlY2svZ2IvdmFsaWRhdGU=".getBytes()));
		if(this.getUserConfig().getCheckForUpdates()) {
			this.getLogger().log(Level.INFO, "Checking for updates...");
			
			try {
				URL url;
				List<String> str = new ArrayList<String>();
				url = new URL("https://api.spigotmc.org/legacy/update.php?resource=24546");
				
				Scanner s = new Scanner(url.openStream());
				while(true){
					if(s.hasNextLine())
						str.add(s.nextLine());
					else break;
				}
				
				s.close();
				
				int v = Integer.parseInt(this.getDescription().getVersion().replaceAll("\\.", ""));
				int nv = Integer.parseInt(str.get(0).replaceAll("\\.", "").substring(1));
				
				if(nv > v) return true;
			}catch(Exception e) {
			}
			
			this.getLogger().log(Level.INFO, "Finished checking for updates!");
		}
		
		return false;
	}
}
