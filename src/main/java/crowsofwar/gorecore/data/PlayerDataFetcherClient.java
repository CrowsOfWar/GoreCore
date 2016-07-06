package crowsofwar.gorecore.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cpw.mods.fml.common.FMLLog;
import crowsofwar.gorecore.GoreCore;
import crowsofwar.gorecore.util.GoreCorePlayerUUIDs;
import crowsofwar.gorecore.util.GoreCorePlayerUUIDs.ResultOutcome;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PlayerDataFetcherClient<T extends GoreCorePlayerData> implements PlayerDataFetcher<T> {
	
	/**
	 * <p>Keeps track of client-side player data.</p>
	 * 
	 * <p>The outer map keeps track of mod IDs -> inner-maps.</p>
	 * 
	 * <p>Inner maps keep track of player IDs -> player data for that mod.</p>
	 */
	private Map<String, Map<UUID, T>> playerData = new HashMap<String, Map<UUID, T>>();
	
	private Class<T> dataClass;
	
	private GoreCorePlayerDataCreationHandler onCreate;
	
	private String modID;
	
	public PlayerDataFetcherClient(Class<T> dataClass, String modID) {
		this(dataClass, modID, null);
	}
	
	public PlayerDataFetcherClient(Class<T> dataClass, String modID, GoreCorePlayerDataCreationHandler onCreate) {
		this.dataClass = dataClass;
		this.modID = modID;
		if (onCreate == null) onCreate = new GoreCorePlayerDataCreationHandler() {
			@Override public void onClientPlayerDataCreated(GoreCorePlayerData data) {}
		};
		this.onCreate = onCreate;
	}
	
	/**
	 * <p>Get the inner-map for the given mod from {@link #playerData}.</p>
	 * 
	 * @param modID The ID of the mod to get the map for
	 * @return The inner map for the given mod
	 */
	private Map<UUID, T> getInnerMap(String modID) {
		Map<UUID, T> ret = playerData.get(modID);
		if (ret == null) {
			ret = new HashMap<UUID, T>();
			playerData.put(modID, ret);
		}
		return ret;
	}
	
	private T createPlayerData(Class<T> dataClass, UUID playerID) {
		try {
			
			return dataClass
					.getConstructor(GoreCoreDataSaver.class, UUID.class)
					.newInstance(new GoreCoreDataSaverDontSave(), playerID);
			
		} catch (Exception e) {
			GoreCore.LOGGER.warn("Found an error when trying to make new client-side player data!");
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public T fetch(EntityPlayer player, String errorMessage) {
		return fetch(player.worldObj, player.getCommandSenderName(), errorMessage);
	}

	@Override
	public T fetch(World world, String playerName, String errorMessage) {
		T data;
		
		GoreCorePlayerUUIDs.GetUUIDResult getUUID = GoreCorePlayerUUIDs.getUUID(playerName);
		GoreCorePlayerUUIDs.ResultOutcome error = getUUID.getResult();
		if (getUUID.isResultSuccessful()) {
			
			Map<UUID, T> inner = getInnerMap(modID);
			UUID playerID = getUUID.getUUID();
			
			data = inner.get(playerID);
			if (data == null) {
				data = createPlayerData(dataClass, playerID);
				inner.put(playerID, data);
				if (onCreate != null) onCreate.onClientPlayerDataCreated(data);
			}
			
		} else {
			
			getUUID.logError();
			data = null;
			
		}
		
		if (error == ResultOutcome.SUCCESS) {
			return data;
		} else {
			if (errorMessage != null) GoreCore.LOGGER.error("Error while retrieving player data- " + errorMessage);
			String log;
			switch (error) {
			case BAD_HTTP_CODE:
				log = "Unexpected HTTP code";
				break;
			case EXCEPTION_OCCURED:
				log = "Unexpected exception occurred";
				break;
			case USERNAME_DOES_NOT_EXIST:
				log = "Account is not registered";
				break;
			default:
				log = "Unexpected error: " + error;
				break;
			
			}
			
			return null;
			
		}
		
	}

	@Override
	public T fetchPerformance(EntityPlayer player) {
		return fetchPerformance(player.worldObj, player.getCommandSenderName());
	}

	@Override
	public T fetchPerformance(World world, String playerName) {
		T data;
		
		GoreCorePlayerUUIDs.GetUUIDResult getUUID = GoreCorePlayerUUIDs.getUUID(playerName);
		if (getUUID.isResultSuccessful()) {
			Map<UUID, T> inner = getInnerMap(modID);
			UUID playerID = getUUID.getUUID();
			
			data = inner.get(playerID);
			if (data == null) {
				data = createPlayerData(dataClass, playerID);
				inner.put(playerID, data);
				if (onCreate != null) onCreate.onClientPlayerDataCreated(data);
			}
			
		} else {
			data = null;
		}
		
		return data;
	}
	
	
	
}
