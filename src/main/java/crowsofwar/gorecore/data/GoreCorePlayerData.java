package crowsofwar.gorecore.data;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.FMLLog;
import crowsofwar.gorecore.util.GoreCoreNBTInterfaces;
import crowsofwar.gorecore.util.GoreCoreNBTInterfaces.MapUser;
import crowsofwar.gorecore.util.GoreCoreNBTUtil;

public abstract class GoreCorePlayerData implements GoreCoreNBTInterfaces.ReadableWritable {
	
	public static final MapUser<UUID, GoreCorePlayerData> MAP_USER = new MapUser<UUID, GoreCorePlayerData>() {
		@Override
		public UUID createK(NBTTagCompound nbt, Object[] constructArgsK) {
			return GoreCoreNBTUtil.readUUIDFromNBT(nbt, "KeyUUID");
		}
		
		@Override
		public GoreCorePlayerData createV(NBTTagCompound nbt, UUID key, Object[] constructArgsV) {
			try {
				GoreCorePlayerData data = ((Class<? extends GoreCorePlayerData>) constructArgsV[0])
						.getConstructor(GoreCoreDataSaver.class, UUID.class)
						.newInstance(constructArgsV[1], key);
				data.readFromNBT(nbt);
				return data;
			} catch (Exception e) {
				FMLLog.severe("GoreCore> An error occured while creating new player data!");
				e.printStackTrace();
				return null;
			}
		}
		
		@Override
		public void writeK(NBTTagCompound nbt, UUID obj) {
			GoreCoreNBTUtil.writeUUIDToNBT(nbt, "KeyUUID", obj);
		}
		
		@Override
		public void writeV(NBTTagCompound nbt, GoreCorePlayerData obj) {
			obj.writeToNBT(nbt);
		}
	};
	
	protected UUID playerID;
	protected GoreCoreDataSaver dataSaver;
	
	public GoreCorePlayerData(GoreCoreDataSaver dataSaver, UUID playerID) {
		construct(dataSaver, playerID);
	}
	
	/**
	 * Called from constructor to initialize data. Override to change
	 * constructor.
	 */
	protected void construct(GoreCoreDataSaver dataSaver, UUID playerID) {
		if (dataSaver == null) FMLLog.severe("GoreCore> Player data was created with a null dataSaver - this is a bug! Debug:");
		if (playerID == null) FMLLog.severe("GoreCore> Player data was created with a null playerID - this is a bug! Debug:");
		if (dataSaver == null || playerID == null) Thread.dumpStack();
		
		this.dataSaver = dataSaver;
		this.playerID = playerID;
	}
	
	protected void saveChanges() {
		dataSaver.saveChanges();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.playerID = GoreCoreNBTUtil.readUUIDFromNBT(nbt, "PlayerID");
		readPlayerDataFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		GoreCoreNBTUtil.writeUUIDToNBT(nbt, "PlayerID", playerID);
		writePlayerDataToNBT(nbt);
	}
	
	protected abstract void readPlayerDataFromNBT(NBTTagCompound nbt);
	
	protected abstract void writePlayerDataToNBT(NBTTagCompound nbt);
	
	public UUID getPlayerID() {
		return playerID;
	}
	
}
