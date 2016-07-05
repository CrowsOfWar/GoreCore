package crowsofwar.gorecore.data;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface PlayerDataFetcher<T extends GoreCorePlayerData> {
	
	T fetch(GoreCoreWorldDataPlayers<T> data, EntityPlayer player, String errorMessage);
	T fetch(GoreCoreWorldDataPlayers<T> data, String playerName, String errorMessage);
	
	T fetchPerformance(GoreCoreWorldDataPlayers<T> data, EntityPlayer player);
	T fetchPerformance(GoreCoreWorldDataPlayers<T> data, String playerName);
	
}
