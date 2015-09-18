package crowsofwar.gorecore.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crowsofwar.gorecore.util.GoreCorePlayerUUIDs;
import crowsofwar.gorecore.util.GoreCorePlayerUUIDs.ResultOutcome;
import crowsofwar.physicaltraits.PhysicalTraits;
import crowsofwar.physicaltraits.common.network.PhysicalTraitsPacketSRequestData;

public class GoreCorePlayerDataFetcher {
	
	/**
	 * <p>Use this class to get server-side player data.</p>
	 * 
	 * <p><strong>Methods:</strong></p>
	 * 
	 * <ul>
	 * 	<li>
	 * 		{@link #getServerData(GoreCoreWorldDataPlayers, EntityPlayer) getServerData(worldData, player)} - standard data fetching with error control
	 * 	</li>
	 * 	<li>
	 * 		{@link #getServerData(GoreCoreWorldDataPlayers, String) getServerData(worldData, playerName)} - standard data fetching with error control
	 * 	</li>
	 * 	<li>
	 * 		{@link #getDataQuick(GoreCoreWorldDataPlayers, EntityPlayer, String) getDataQuick(worldData, player, errorMessage)} - minimal player data fetching
	 * 	</li>
	 * 	<li>
	 * 		{@link #getDataQuick(GoreCoreWorldDataPlayers, String, String) getDataQuick(world, playerName, errorMessage)} - minimal player data fetching
	 * 	</li>
	 * 	<li>
	 * 		{@link #getDataPerformance(GoreCoreWorldDataPlayers, EntityPlayer) getDataPerformance(worldData, player)} -
	 * data fetching optimized for performance
	 * 	</li>
	 * 	<li>
	 * 		{@link #getDataPerformance(GoreCoreWorldDataPlayers, String) getDataPerformance(worldData, playerName)} -
	 * data fetching optimized for performance
	 * 	</li>
	 * </ul>
	 * 
	 */
	public static class ServerFetcher {
		
		/**
		 * Not to be instantiated
		 */
		private ServerFetcher() {}
		
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
		 * <p>Calls {@link #getDataPerformance(GoreCoreWorldDataPlayers, String)}.
		 * 
		 * @param worldData Your world data that stores player data in
		 * @param player The player to get data for
		 * @return Player data for that player, creating it if necessary. Null if an error occured
		 */
		public static GoreCorePlayerData getDataPerformance(GoreCoreWorldDataPlayers worldData, EntityPlayer player) {
			return getDataPerformance(worldData, player.getCommandSenderName());
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
		 * <p>Calls {@link #getServerData(GoreCoreWorldDataPlayers, String)}.</p>
		 * 
		 * @param worldData The world data which the player data is stored in
		 * @param player The player to get data for
		 * @return Server-type data (GoreCorePlayerDataServer) for the player
		 */
		public static FetchDataResult getServerData(GoreCoreWorldDataPlayers worldData, EntityPlayer player) {
			return getServerData(worldData, player.getCommandSenderName());
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
		
		
		
	}
	
	/**
	 * <p>Use this class to get client-side player data.</p>
	 * 
	 * <p><strong>Methods:</strong></p>
	 * 
	 * <ul>
	 * 	<li>
	 * 		{@link #getClientData(Class, String, EntityPlayer) getClientData(dataClass, modID, player)} - standard data fetching with error control
	 * 	</li>
	 * 	<li>
	 * 		{@link #getClientData(Class, String, String) getClientData(dataClass, modID, playerName)} - standard data fetching with error control
	 * 	</li>
	 * 	<li>
	 * 		{@link #getDataQuick(Class, String, EntityPlayer, String) getDataQuick(dataClass, modID, player, errorMessage)} - minimal player data fetching
	 * 	</li>
	 * 	<li>
	 * 		{@link #getDataQuick(Class, String, String, String) getDataQuick(dataClass, modID, playerName, errorMessage)} - minimal player data fetching
	 * 	</li>
	 * 	<li>
	 * 		{@link #getDataPerformance(Class, String, EntityPlayer) getDataPerformance(dataClass, modID, player)} -
	 * data fetching optimized for performance
	 * 	</li>
	 * 	<li>
	 * 		{@link #getDataPerformance(Class, String, String) getDataPerformance(dataClass, modID, playerName)} -
	 * data fetching optimized for performance
	 * 	</li>
	 * </ul>
	 * 
	 */
	@SideOnly(Side.CLIENT)
	public static class ClientFetcher {
		
		/**
		 * <p>Keeps track of client-side player data.</p>
		 * 
		 * <p>The outer map keeps track of mod IDs -> inner-maps.</p>
		 * 
		 * <p>Inner maps keep track of player IDs -> player data for that mod.</p>
		 */
		private static Map<String, Map<UUID, GoreCorePlayerData>> playerData = new HashMap<String, Map<UUID,GoreCorePlayerData>>();
		
		/**
		 * Not to be instantiated
		 */
		private ClientFetcher() {}
		
		/**
		 * <p>Get the inner-map for the given mod from {@link #playerData}.</p>
		 * 
		 * @param modID The ID of the mod to get the map for
		 * @return The inner map for the given mod
		 */
		private static Map<UUID, GoreCorePlayerData> getInnerMap(String modID) {
			Map<UUID, GoreCorePlayerData> ret = playerData.get(modID);
			if (ret == null) {
				ret = new HashMap<UUID, GoreCorePlayerData>();
				playerData.put(modID, ret);
			}
			return ret;
		}
		
		/**
		 * <p>A quick way to get player data. Errors are automatically logged.</p>
		 * 
		 * <p>Calls {@link #getDataQuick(Class, String, String, String)}.</p>
		 * 
		 * @param dataClass The class of your client-type data: <code>MyModsPlayerDataClient.class</code>
		 * @param modID The ID of your mod
		 * @param player The player to get data for
		 * @param errorMessage The message to be logged if an error occurs to tell the user
		 * what caused the error,  e.g. "Error trying to get player data for tick handler"
		 * @return Player data for the player, null if an error occured
		 */
		public static GoreCorePlayerData getDataQuick(Class<? extends GoreCorePlayerData> dataClass, String modID, EntityPlayer player, String errorMessage) {
			return getDataQuick(dataClass, modID, player.getCommandSenderName(), errorMessage);
		}
		
		/**
		 * <p>A quick way to get player data. Errors are automatically logged.</p>
		 * 
		 * @param dataClass The class of your client-type data: <code>MyModsPlayerDataClient.class</code>
		 * @param modID The ID of your mod
		 * @param playerName The username of the player
		 * @param errorMessage The message to be logged if an error occurs to tell the user
		 * what caused the error,  e.g. "Error trying to get player data for tick handler"
		 * @return Player data for the player, null if an error occured
		 */
		public static GoreCorePlayerData getDataQuick(Class<? extends GoreCorePlayerData> dataClass, String modID, String playerName, String errorMessage) {
			FetchDataResult result = getClientData(dataClass, modID, playerName);
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
		 * <p>Calls {@link #getDataPerformance(Class, String, String)}.
		 * 
		 * @param dataClass The class of your client-type data: <code>MyModsPlayerDataClient.class</code>
		 * @param modID The ID of your mod
		 * @param player The player to get data for
		 * @return Player data for that player, creating it if necessary. Null if an error occured
		 */
		public static GoreCorePlayerData getDataPerformance(Class<? extends GoreCorePlayerData> dataClass, String modID, EntityPlayer player) {
			return getDataPerformance(dataClass, modID, player.getCommandSenderName());
		}
		
		/**
		 * <p>Gets player data for that person, but does not log any errors. Less
		 * extra objects are created, but no errors can be logged.</p>
		 * 
		 * @param dataClass The class of your client-type data: <code>MyModsPlayerDataClient.class</code>
		 * @param modID The ID of your mod
		 * @param playerName The name of the player to get data for
		 * @return Player data for that player, creating it if necessary. Null if an error occured
		 */
		public static GoreCorePlayerData getDataPerformance(Class<? extends GoreCorePlayerData> dataClass, String modID, String playerName) {
			GoreCorePlayerData data;
			
			GoreCorePlayerUUIDs.GetUUIDResult getUUID = GoreCorePlayerUUIDs.getUUID(playerName);
			if (getUUID.isResultSuccessful()) {
				Map<UUID, GoreCorePlayerData> inner = getInnerMap(modID);
				UUID playerID = getUUID.getUUID();
				
				data = inner.get(playerID);
				if (data == null) {
					data = createPlayerData(dataClass, playerID);
					inner.put(playerID, data);
					PhysicalTraits.network.sendToServer(new PhysicalTraitsPacketSRequestData(playerID));
				}
				
			} else {
				data = null;
			}
			
			return data;
		}
		
		/**
		 * <p>Returns client-type data for that player, stored in a HashMap.</p>
		 * 
		 * <p>Calls {@link #getClientData(Class, String, String)}.
		 * 
		 * @param dataClass The class of your client-type data: <code>MyModsPlayerDataClient.class</code>
		 * @param modID The ID of your mod
		 * @param player The player to get data for
		 * @return Client-type data for the player
		 */
		public static FetchDataResult getClientData(Class<? extends GoreCorePlayerData> dataClass, String modID, EntityPlayer player) {
			return getClientData(dataClass, modID, player.getCommandSenderName());
		}
		
		/**
		 * <p>Returns client-type data for that player, stored in a HashMap.</p>
		 * 
		 * @param dataClass The class of your client-type data: <code>MyModsPlayerDataClient.class</code>
		 * @param modID The ID of your mod
		 * @param playerName The name of the player to get the data for
		 * @return Client-type data for the player
		 */
		public static FetchDataResult getClientData(Class<? extends GoreCorePlayerData> dataClass, String modID, String playerName) {
			GoreCorePlayerData data;
			
			GoreCorePlayerUUIDs.GetUUIDResult getUUID = GoreCorePlayerUUIDs.getUUID(playerName);
			GoreCorePlayerUUIDs.ResultOutcome error = getUUID.getResult();
			if (getUUID.isResultSuccessful()) {
				
				Map<UUID, GoreCorePlayerData> inner = getInnerMap(modID);
				UUID playerID = getUUID.getUUID();
				
				data = inner.get(playerID);
				if (data == null) {
					data = createPlayerData(dataClass, playerID);
					inner.put(playerID, data);
					PhysicalTraits.network.sendToServer(new PhysicalTraitsPacketSRequestData(playerID));
				}
				
			} else {
				
				getUUID.logError();
				data = null;
				
			}
			
			return new FetchDataResult(data, error);
		}
		
		private static GoreCorePlayerData createPlayerData(Class<? extends GoreCorePlayerData> dataClass, UUID playerID) {
			try {
				
				GoreCorePlayerData data = dataClass
						.getConstructor(GoreCoreDataSaver.class, UUID.class)
						.newInstance(new GoreCoreDataSaverDontSave(), playerID);
				return data;
				
			} catch (Exception e) {
				FMLLog.warning("GoreCore> Found an error when trying to make new client-side player data!");
				e.printStackTrace();
				return null;
			}
		}
		
	}
	
	public static final class FetchDataResult {
		/**
		 * The data retrieved, null if there were errors
		 */
		private final GoreCorePlayerData data;
		
		/**
		 * The error that happened while retrieving data
		 */
		private final GoreCorePlayerUUIDs.ResultOutcome error;
		
		public FetchDataResult(GoreCorePlayerData data, ResultOutcome error) {
			this.data = data;
			this.error = error;
		}

		public GoreCorePlayerData getData() {
			return data;
		}

		public GoreCorePlayerUUIDs.ResultOutcome getError() {
			return error;
		}
		
		public boolean hadError() {
			return error != ResultOutcome.SUCCESS;
		}
		
		/**
		 * Logs an error to the console if there was a problem.
		 */
		public void logError() {
			if (hadError()) {
				String text = "There's a bug with the error warning code! o_o";
				if (error == ResultOutcome.USERNAME_DOES_NOT_EXIST)
					text = "The player was not registered on Minecraft.net - are you using a cracked launcher?";
				if (error == ResultOutcome.EXCEPTION_OCCURED)
					text = "An unexpected error (specifically, an exception) occured while getting the player's UUID";
				if (error == ResultOutcome.BAD_HTTP_CODE)
					text = "Got an unexpected HTTP code";
				FMLLog.warning("GoreCore> Attempted to get a player's UUID for getting player data but failed: " + text);
			}
		}
	}
	
}
