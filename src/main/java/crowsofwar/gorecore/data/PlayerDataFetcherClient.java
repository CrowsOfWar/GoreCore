package crowsofwar.gorecore.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import crowsofwar.gorecore.data.GoreCorePlayerDataFetcher.FetchDataResult;
import crowsofwar.gorecore.util.GoreCorePlayerUUIDs;
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
	private Map<String, Map<UUID, GoreCorePlayerData>> playerData = new HashMap<String, Map<UUID, GoreCorePlayerData>>();
	
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
	private Map<UUID, GoreCorePlayerData> getInnerMap(String modID) {
		Map<UUID, GoreCorePlayerData> ret = playerData.get(modID);
		if (ret == null) {
			ret = new HashMap<UUID, GoreCorePlayerData>();
			playerData.put(modID, ret);
		}
		return ret;
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
	
	@Override
	public T fetch(EntityPlayer player, String errorMessage) {
		return fetch(player.worldObj, player.getCommandSenderName(), errorMessage);
	}

	@Override
	public T fetch(World world, String playerName, String errorMessage) {
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
				if (onCreate != null) onCreate.onClientPlayerDataCreated(data);
			}
			
		} else {
			
			getUUID.logError();
			data = null;
			
		}
		
		FetchDataResult result = new FetchDataResult(data, error);
		if (result.hadError()) {
			if (errorMessage != null) FMLLog.warning("GoreCore> " + errorMessage);
			result.logError();
			return null;
		} else {
			return (T) result.getData();
		}
	}

	@Override
	public T fetchPerformance(EntityPlayer player) {
		return fetchPerformance(player.worldObj, player.getCommandSenderName());
	}

	@Override
	public T fetchPerformance(World world, String playerName) {
		GoreCorePlayerData data;
		
		GoreCorePlayerUUIDs.GetUUIDResult getUUID = GoreCorePlayerUUIDs.getUUID(playerName);
		if (getUUID.isResultSuccessful()) {
			Map<UUID, GoreCorePlayerData> inner = getInnerMap(modID);
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
		
		return (T) data;
	}
	
	
	
}
