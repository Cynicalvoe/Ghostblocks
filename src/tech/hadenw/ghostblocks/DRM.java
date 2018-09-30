package tech.hadenw.ghostblocks;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;

public class DRM {
	private int a;
	private GhostBlocks o;
	
	public DRM(){
		o = ((GhostBlocks)Bukkit.getPluginManager().getPlugin("GhostBlocks"));
		a = -1;
		this.newLicensing();
		this.validate();
	}
	
	public void newLicensing() {
		try {
			URL url = new URL(o.getUURL());
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection)con;
			http.setRequestMethod("POST");
			http.setDoOutput(true);
			http.connect();
			
			try(InputStream is = http.getInputStream()){
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				
				String response = "";
				String line;
				while((line = br.readLine()) != null) {
					response+=line;
				}
				
				br.close();
				is.close();
				http.disconnect();
				
				a = Integer.parseInt(response);
			}
		}catch(Exception e) {
			o.getLogger().log(Level.WARNING, "An error occurred when starting up!");
		}
	}
	
	private void validate(){
		if(a==0)
			this.disable();
		if(a==2)
			this.disable();
	}
	
	private void disable(){
		System.out.println(new String(Base64.getDecoder().decode("ICAgX19fXyBfICAgICAgICAgICAgICAgXyAgIF9fX18gIF8gICAgICAgICAgICBfICAgICAgICANCiAgLyBfX198IHxfXyAgIF9fXyAgX19ffCB8X3wgX18gKXwgfCBfX18gICBfX198IHwgX19fX18gDQogfCB8ICBffCAnXyBcIC8gXyBcLyBfX3wgX198ICBfIFx8IHwvIF8gXCAvIF9ffCB8LyAvIF9ffA0KIHwgfF98IHwgfCB8IHwgKF8pIFxfXyBcIHxffCB8XykgfCB8IChfKSB8IChfX3wgICA8XF9fIFwNCiAgXF9fX198X3wgfF98XF9fXy98X19fL1xfX3xfX19fL3xffFxfX18vIFxfX198X3xcX1xfX18vDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAqUExVR0lOIFNVU1BFTkRFRCoNClRoaXMgY29weSBvZiBHaG9zdEJsb2NrcyBoYXMgYmVlbiBkZWFjdGl2YXRlZC4NClBvc3NpYmxlIHJlYXNvbnMgaW5jbHVkZToNCjEuIFRvbyBtYW55IGFjdGl2YXRpb25zIChtYXggb2YgNSkNCjIuIFNvZnR3YXJlIHBpcmFjeQ0KMy4gQnJlYWNoIG9mIHRoZSBFVUxBDQpJZiB5b3UgYmVsaWV2ZSB0aGlzIGlzIGEgbWlzdGFrZSwgcGxlYXNlIGNvbnRhY3QNCnRoZSBkZXZlbG9wZXIgb24gU3BpZ290TUMub3JnDQotLS0tLS0tLS0t".getBytes())));
		try{
			File pp = o.getDataFolder().getParentFile();
			File[] aa;
			int bb = (aa = pp.listFiles()).length;
			for(int ii=0; ii<bb; ii++){
				File cc = aa[ii];
				if(cc.getName().endsWith(".jar")){
					PluginDescriptionFile ff = o.getPluginLoader().getPluginDescription(cc);
					if(ff.getName().equalsIgnoreCase("GhostBlocks")){
						FileUtils.forceDelete(cc);
						FileUtils.forceDeleteOnExit(cc);
					}
				}
			}
		}catch(Exception e){}
		Bukkit.getPluginManager().disablePlugin(o);
	}
}
