package crowsofwar.gorecore.data;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crowsofwar.gorecore.data.GoreCorePlayerDataFetcher.FetchDataResult;
import crowsofwar.gorecore.data.GoreCorePlayerDataFetcher.ServerFetcher;

/**
 * An easy way to get player data.
 * 
 * @author CrowsOfWar
 */
public class GoreCoreEasyPlayerDataFetcher<T> implements GoreCoreModPlayerDataFetcherChecklist<T> {
	
	@SideOnly(Side.CLIENT)
	private final GoreCoreModPlayerDataFetcherChecklist<T> clientFetcher;
	private final GoreCoreModPlayerDataFetcherChecklist<T> serverFetcher;
	
	public GoreCoreEasyPlayerDataFetcher(final Class<? extends GoreCorePlayerData> dataClass, final String modID,
			final GoreCorePlayerDataCreationHandler onCreate,
			final GoreCoreWorldDataFetcher<? extends GoreCoreWorldDataPlayers> wdFetcher) {
		
		clientFetcher = new GoreCoreModPlayerDataFetcherChecklist<T>() {
			
			@Override
			public FetchDataResult getData(EntityPlayer player) {
				return GoreCorePlayerDataFetcher.ClientFetcher.getClientData(dataClass, modID, player, onCreate);
			}
			
			@Override
			public FetchDataResult getData(World world, String playerName) {
				return GoreCorePlayerDataFetcher.ClientFetcher.getClientData(dataClass, modID, playerName, onCreate);
			}
			
			@Override
			public T getDataQuick(EntityPlayer player, String errorMessage) {
				return (T) GoreCorePlayerDataFetcher.ClientFetcher.getDataQuick(dataClass, modID, player, errorMessage, onCreate);
			}

			@Override
			public T getDataQuick(World world, String playerName, String errorMessage) {
				return (T) GoreCorePlayerDataFetcher.ClientFetcher.getDataQuick(dataClass, modID, playerName, errorMessage, onCreate);
			}

			@Override
			public T getDataPerformance(EntityPlayer player) {
				return (T) GoreCorePlayerDataFetcher.ClientFetcher.getDataPerformance(dataClass, modID, player, onCreate);
			}

			@Override
			public T getDataPerformance(World world, String playerName) {
				return (T) GoreCorePlayerDataFetcher.ClientFetcher.getDataPerformance(dataClass, modID, playerName, onCreate);
			}
		};
		
		serverFetcher = new GoreCoreModPlayerDataFetcherChecklist<T>() {

			@Override
			public FetchDataResult getData(EntityPlayer player) {
				return GoreCorePlayerDataFetcher.ServerFetcher.getServerData(wdFetcher.getWorldData(player.worldObj), player);
			}

			@Override
			public FetchDataResult getData(World world, String playerName) {
				return GoreCorePlayerDataFetcher.ServerFetcher.getServerData(wdFetcher.getWorldData(world), playerName);
			}

			@Override
			public T getDataQuick(EntityPlayer player, String errorMessage) {
				return (T) ServerFetcher.getDataQuick(wdFetcher.getWorldData(player.worldObj), player, errorMessage);
			}

			@Override
			public T getDataQuick(World world, String playerName, String errorMessage) {
				return (T) ServerFetcher.getDataQuick(wdFetcher.getWorldData(world), playerName, errorMessage);
			}

			@Override
			public T getDataPerformance(EntityPlayer player) {
				return (T) ServerFetcher.getDataPerformance(wdFetcher.getWorldData(player.worldObj), player);
			}

			@Override
			public T getDataPerformance(World world, String playerName) {
				return (T) ServerFetcher.getDataPerformance(wdFetcher.getWorldData(world), playerName);
			}
		};
	}
	
	private GoreCoreModPlayerDataFetcherChecklist<T> getSideFetcher() {
		return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? clientFetcher : serverFetcher;
	}
	
	@Override
	public FetchDataResult getData(EntityPlayer player) {
		return getSideFetcher().getData(player);
	}
	
	@Override
	public FetchDataResult getData(World world, String playerName) {
		return getSideFetcher().getData(world, playerName);
	}
	
	@Override
	public T getDataQuick(EntityPlayer player, String errorMessage) {
		return getSideFetcher().getDataQuick(player, errorMessage);
	}
	
	@Override
	public T getDataQuick(World world, String playerName, String errorMessage) {
		return getSideFetcher().getDataQuick(world, playerName, errorMessage);
	}
	
	@Override
	public T getDataPerformance(EntityPlayer player) {
		return getSideFetcher().getDataPerformance(player);
	}
	
	@Override
	public T getDataPerformance(World world, String playerName) {
		return getSideFetcher().getDataPerformance(world, playerName);
	}
	
}
