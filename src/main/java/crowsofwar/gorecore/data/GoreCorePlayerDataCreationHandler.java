package crowsofwar.gorecore.data;

import java.util.UUID;

/**
 * Handles new player data being created on the client side.
 * 
 * @author CrowsOfWar
 */
public interface GoreCorePlayerDataCreationHandler {
	
	/**
	 * Called when client player data is created
	 * 
	 * @param data The player data that just was created
	 */
	public void onClientPlayerDataCreated(GoreCorePlayerData data);
	
}
