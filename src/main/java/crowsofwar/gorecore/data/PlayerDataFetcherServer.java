package crowsofwar.gorecore.data;

import java.util.UUID;

import cpw.mods.fml.common.FMLLog;
import crowsofwar.gorecore.data.GoreCorePlayerDataFetcher.FetchDataResult;
import crowsofwar.gorecore.util.GoreCorePlayerUUIDs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PlayerDataFetcherServer<T extends GoreCorePlayerData> implements PlayerDataFetcher<T> {
	
	private final WorldDataFetcher<? extends GoreCoreWorldDataPlayers<T>> worldDataFetcher;
	
	public PlayerDataFetcherServer(WorldDataFetcher<? extends GoreCoreWorldDataPlayers<T>> worldDataFetcher) {
		this.worldDataFetcher = worldDataFetcher;
	}
	
	@Override
	public T fetch(EntityPlayer player, String errorMessage) {
		return fetch(player.worldObj, player.getCommandSenderName(), errorMessage);
	}

	@Override
	public T fetch(World world, String playerName, String errorMessage) {
		GoreCorePlayerData pd;
		GoreCorePlayerUUIDs.ResultOutcome error;
		
		GoreCorePlayerUUIDs.GetUUIDResult getUUID = GoreCorePlayerUUIDs.getUUID(playerName);
		if (getUUID.isResultSuccessful()) {
			
			pd = (GoreCorePlayerData) worldDataFetcher.fetch(world).getPlayerData(getUUID.getUUID());
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
	public T fetchPerformance(EntityPlayer player) {
		return fetchPerformance(player.worldObj, player.getCommandSenderName());
	}

	@Override
	public T fetchPerformance(World world, String playerName) {
		UUID res = GoreCorePlayerUUIDs.getUUIDPerformance(playerName);
		return res == null ? null : worldDataFetcher.fetch(world).getPlayerData(res);
	}
	
	public static interface WorldDataFetcher<T extends GoreCoreWorldData> {
		
		T fetch(World world);
		
	}
	
}
