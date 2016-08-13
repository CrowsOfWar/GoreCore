package crowsofwar.gorecore.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cpw.mods.fml.common.FMLLog;
import crowsofwar.gorecore.util.GoreCoreNBTUtil;
import crowsofwar.gorecore.util.GoreCorePlayerUUIDs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * A world data class which comes equipped with the ability to save and load player data.
 * 
 * @param T
 *            The type of your player data
 * 
 * @author CrowsOfWar
 */
public abstract class GoreCoreWorldDataPlayers<T extends GoreCorePlayerData> extends GoreCoreWorldData {
	
	private Map<UUID, GoreCorePlayerData> players;
	
	public GoreCoreWorldDataPlayers(String key) {
		super(key);
		this.players = new HashMap<UUID, GoreCorePlayerData>();
	}
	
	public GoreCoreWorldDataPlayers(World worldFor, String key) {
		super(worldFor, key);
		this.players = new HashMap<UUID, GoreCorePlayerData>();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.players = GoreCoreNBTUtil.readMapFromNBT(nbt, GoreCorePlayerData.MAP_USER, "PlayerData", new Object[] {},
				new Object[] { playerDataClass(), this, getWorld() });
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		GoreCoreNBTUtil.writeMapToNBT(nbt, players, GoreCorePlayerData.MAP_USER, "PlayerData");
	}
	
	/**
	 * Gets the player data for that player, creating it if necessary.
	 * 
	 * @param player
	 *            The UUID of the player to get data for
	 * @return Player data for that player
	 */
	public T getPlayerData(UUID player) {
		if (players.containsKey(player)) {
			return (T) players.get(player);
		} else {
			T data = createNewPlayerData(player);
			players.put(player, data);
			saveChanges();
			return data;
		}
	}
	
	/**
	 * Gets the player data for the player. If the player data has not been created, then this will
	 * return null.
	 * 
	 * @param player
	 *            The UUID of the player to get data for
	 * @return Player data for the player, or null if it does not exist
	 */
	public GoreCorePlayerData getPlayerDataWithoutCreate(UUID player) {
		return players.get(player);
	}
	
	public abstract Class<? extends GoreCorePlayerData> playerDataClass();
	
	private T createNewPlayerData(UUID player) {
		try {
			
			EntityPlayer playerEntity = GoreCorePlayerUUIDs.findPlayerInWorldFromUUID(getWorld(), player);
			GoreCorePlayerData data = playerDataClass().getConstructor(GoreCoreDataSaver.class, UUID.class, EntityPlayer.class)
					.newInstance(this, player, playerEntity);
			return (T) data;
			
		} catch (Exception e) {
			FMLLog.warning("GoreCore> Found an error when trying to make new player data!");
			e.printStackTrace();
			return null;
		}
	}
	
}
