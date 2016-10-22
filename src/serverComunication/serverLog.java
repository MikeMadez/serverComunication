package serverComunication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

public class serverLog {
	public final Logger logger = Logger.getLogger("Minecraft");
	private static Connection connection;
	
	public static void tryDisable(){
		try {
			if(connection != null && connection.isClosed()){
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return;
	}
	
	public synchronized static void openConnection(){
		try{
			connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/minecraft_server", "root", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return;
	}
	
	public synchronized static void closeConnection(){
		try{
			connection.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return;
	}
	
	public synchronized static boolean playerDataContainsPlayer(Player player){
		try{
			PreparedStatement sql = connection.prepareStatement("SELECT * FROM `users` WHERE idUser=?;");
			sql.setString(1, player.getUniqueId().toString());
			ResultSet resultSet = sql.executeQuery();
			boolean containsPlayer = resultSet.next();
			
			sql.close();
			resultSet.close();
			
			return containsPlayer;		
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static void onLoginServer(Player player){
		openConnection();
		String playerUUID = player.getUniqueId().toString();
		
		try{
			long time = System.currentTimeMillis();
			Timestamp timestampUpdate = new Timestamp(time);
			
			if(playerDataContainsPlayer(player)){
				
				PreparedStatement loginsUpdate = connection.prepareStatement("UPDATE `users` SET last_login=?, username=? WHERE idUser=?;");
				loginsUpdate.setTimestamp(1, timestampUpdate);
				loginsUpdate.setString(2, player.getName());
				loginsUpdate.setString(3, playerUUID);
				
				loginsUpdate.execute();
				loginsUpdate.close();
		}else{
			
			PreparedStatement newPlayer = connection.prepareStatement("INSERT INTO `users` values(?, ?, ?, ?, 1, 3);");
			newPlayer.setString(1, playerUUID);
			newPlayer.setString(2, player.getName());
			newPlayer.setTimestamp(3, timestampUpdate);
			newPlayer.setTimestamp(4, timestampUpdate);
			newPlayer.execute();
			
			newPlayer = connection.prepareStatement("INSERT INTO `pvp` values(?, 0, 0, 0);");
			newPlayer.setString(1, playerUUID);
			newPlayer.execute();
			
			newPlayer = connection.prepareStatement("INSERT INTO `bank` values(?, 200, 10);");
			newPlayer.setString(1, playerUUID);
			newPlayer.execute();
			
			newPlayer.close();
		}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeConnection();
		}
		
		return;
	}
	
}//END END END