package crowsofwar.gorecore.data;

import java.util.UUID;

import crowsofwar.gorecore.GoreCore;
import crowsofwar.gorecore.util.GoreCorePlayerUUIDs;
import crowsofwar.gorecore.util.GoreCorePlayerUUIDs.ResultOutcome;
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
		T pd;
		GoreCorePlayerUUIDs.ResultOutcome error;
		
		GoreCorePlayerUUIDs.GetUUIDResult getUUID = GoreCorePlayerUUIDs.getUUID(playerName);
		if (getUUID.isResultSuccessful()) {
			
			pd = worldDataFetcher.fetch(world).getPlayerData(getUUID.getUUID());
			error = getUUID.getResult();
			
		} else {
			
			getUUID.logError();
			pd = null;
			error = getUUID.getResult();
			
		}
		
		if (error == ResultOutcome.SUCCESS) {
			return pd;
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
		UUID res = GoreCorePlayerUUIDs.getUUIDPerformance(playerName);
		return res == null ? null : worldDataFetcher.fetch(world).getPlayerData(res);
	}
	
	public static interface WorldDataFetcher<T extends GoreCoreWorldData> {
		
		T fetch(World world);
		
	}
	
}
