package crowsofwar.gorecore.data;

import java.util.UUID;

import cpw.mods.fml.common.FMLLog;
import crowsofwar.gorecore.data.GoreCorePlayerDataFetcher.FetchDataResult;
import crowsofwar.gorecore.util.GoreCorePlayerUUIDs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PlayerDataFetcherServer<T extends GoreCorePlayerData> implements PlayerDataFetcher<T> {
	
	/**
	 * <p>A quick way to get player data. Errors are automatically logged.</p>
	 * 
	 * <p>Calls {@link #getDataQuick(GoreCoreWorldDataPlayers, String, String)}.</p>
	 * 
	 * @param player The player to get data for
	 * @param errorMessage The message to be logged if an error occurs to tell the user
	 * what caused the error,  e.g. "Error trying to get player data for tick handler"
	 * @return Player data for the player, null if an error occured
	 */
	public static GoreCorePlayerData getDataQuick(GoreCoreWorldDataPlayers worldData, EntityPlayer player, String errorMessage) {
		return getDataQuick(worldData, player.getCommandSenderName(), errorMessage);
	}
	
	/**
	 * <p>A quick way to get player data. Errors are automatically logged.</p>
	 * 
	 * @param world The world to get player data for
	 * @param playerName The username of the player
	 * @param errorMessage The message to be logged if an error occurs to tell the user
	 * what caused the error,  e.g. "Error trying to get player data for tick handler"
	 * @return Player data for the player, null if an error occured
	 */
	public static GoreCorePlayerData getDataQuick(GoreCoreWorldDataPlayers worldData, String playerName, String errorMessage) {
		FetchDataResult result = getServerData(worldData, playerName);
		if (result.hadError()) {
			if (errorMessage != null) FMLLog.warning("GoreCore> " + errorMessage);
			result.logError();
			return null;
		} else {
			return result.getData();
		}
	}
	
	/**
	 * <p>Gets player data for that person, but does not log any errors. Less
	 * extra objects are created, but no errors can be logged.</p>
	 * 
	 * @param worldData Your world data that stores player data in
	 * @param playerName The name of the player to get data for
	 * @return Player data for that player, creating it if necessary. Null if an error occured
	 */
	public static GoreCorePlayerData getDataPerformance(GoreCoreWorldDataPlayers worldData, String playerName) {
		UUID res = GoreCorePlayerUUIDs.getUUIDPerformance(playerName);
		return res == null ? null : (GoreCorePlayerData) worldData.getPlayerData(res);
	}
	
	/**
	 * <p>Returns server-stored data for that player (player data
	 * stored in world data).</p>
	 * 
	 * @param worldData The world data which the player data is stored in
	 * @param playerName The name of the player to get data for
	 * @return Server-type data (GoreCorePlayerDataServer) for the player
	 */
	public static FetchDataResult getServerData(GoreCoreWorldDataPlayers worldData, String playerName) {
		
		GoreCorePlayerData pd;
		GoreCorePlayerUUIDs.ResultOutcome error;
		
		GoreCorePlayerUUIDs.GetUUIDResult getUUID = GoreCorePlayerUUIDs.getUUID(playerName);
		if (getUUID.isResultSuccessful()) {
			
			pd = (GoreCorePlayerData) worldData.getPlayerData(getUUID.getUUID());
			error = getUUID.getResult();
			
		} else {
			
			getUUID.logError();
			pd = null;
			error = getUUID.getResult();
			
		}
		
		return new FetchDataResult(pd, error);
		
	}

	@Override
	public T fetch(GoreCoreWorldDataPlayers<T> data, EntityPlayer player, String errorMessage) {
		return fetch(data, player.getCommandSenderName(), errorMessage);
	}

	@Override
	public T fetch(GoreCoreWorldDataPlayers<T> data, String playerName, String errorMessage) {
		GoreCorePlayerData pd;
		GoreCorePlayerUUIDs.ResultOutcome error;
		
		GoreCorePlayerUUIDs.GetUUIDResult getUUID = GoreCorePlayerUUIDs.getUUID(playerName);
		if (getUUID.isResultSuccessful()) {
			
			pd = (GoreCorePlayerData) data.getPlayerData(getUUID.getUUID());
			error = getUUID.getResult();
			
		} else {
			
			getUUID.logError();
			pd = null;
			error = getUUID.getResult();
			
		}
		
		FetchDataResult result = new FetchDataResult(pd, error);
		if (result.hadError()) {
			if (errorMessage != null) FMLLog.warning("GoreCore> " + errorMessage);
			result.logError();
			return null;
		} else {
			return (T) result.getData();
		}
	}

	@Override
	public T fetchPerformance(GoreCoreWorldDataPlayers<T> data, EntityPlayer player) {
		return fetchPerformance(data, player.getCommandSenderName());
	}

	@Override
	public T fetchPerformance(GoreCoreWorldDataPlayers<T> data, String playerName) {
		UUID res = GoreCorePlayerUUIDs.getUUIDPerformance(playerName);
		return res == null ? null : (T) data.getPlayerData(res);
	}
	
}
