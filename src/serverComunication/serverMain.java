package serverComunication;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class serverMain extends JavaPlugin implements Listener{
	
public final Logger logger = Logger.getLogger("Minecraft");
	
	public void onEnable(){
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " is Enable!");
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable(){
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " is Disable!");
		serverLog.tryDisable();
	}
	
	public static void loginToServer(Player player){
		serverLog.onLoginServer(player);
	}
	
}//END Line