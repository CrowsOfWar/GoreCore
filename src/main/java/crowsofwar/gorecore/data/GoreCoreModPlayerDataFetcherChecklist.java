package crowsofwar.gorecore.data;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import crowsofwar.gorecore.data.GoreCorePlayerDataFetcher.FetchDataResult;

/**
 * A checklist of methods for mods who want to make their
 * own "shortcut methods" for {@link GoreCorePlayerDataFetcher}.
 *
 * @author CrowsOfWar
 *
 * @param <T> The world data base class
 */
public interface GoreCoreModPlayerDataFetcherChecklist<T> {
	
	FetchDataResult getData(EntityPlayer player);
	FetchDataResult getData(World world, String playerName);
	T getDataQuick(EntityPlayer player, String errorMessage);
	T getDataQuick(World world, String playerName, String errorMessage);
	T getDataPerformance(EntityPlayer player);
	T getDataPerformance(World world, String playerName);
	
}
