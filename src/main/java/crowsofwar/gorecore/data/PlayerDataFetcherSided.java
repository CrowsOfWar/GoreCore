package crowsofwar.gorecore.data;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Player data fetcher class which hands off
 * the functionality to a delegate. (One for
 * each side)
 * 
 * @author CrowsOfWar
 */
public class PlayerDataFetcherSided<T extends GoreCorePlayerData> implements PlayerDataFetcher<T> {
	
	private PlayerDataFetcher<T> clientDelegate, serverDelegate;
	
	public PlayerDataFetcherSided(PlayerDataFetcher<T> client, PlayerDataFetcher<T> server) {
		clientDelegate = client;
		serverDelegate = server;
	}
	
	private PlayerDataFetcher<T> getDelegate() {
		return FMLCommonHandler.instance().getEffectiveSide().isClient() ? clientDelegate : serverDelegate;
	}
	
	@Override
	public T fetch(GoreCoreWorldDataPlayers<T> data, EntityPlayer player, String errorMessage) {
		return getDelegate().fetch(data, player, errorMessage);
	}

	@Override
	public T fetch(GoreCoreWorldDataPlayers<T> data, String playerName, String errorMessage) {
		return getDelegate().fetch(data, playerName, errorMessage);
	}

	@Override
	public T fetchPerformance(GoreCoreWorldDataPlayers<T> data, EntityPlayer player) {
		return getDelegate().fetchPerformance(data, player);
	}

	@Override
	public T fetchPerformance(GoreCoreWorldDataPlayers<T> data, String playerName) {
		return getDelegate().fetchPerformance(data, playerName);
	}
	
	
	
}
