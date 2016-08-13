package crowsofwar.gorecore.data;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

/**
 * A base class for WorldSavedData.
 * 
 * @author CrowsOfWar
 */
public abstract class GoreCoreWorldData extends WorldSavedData implements GoreCoreDataSaver {
	
	/**
	 * The world that this data belongs to.
	 * <p>
	 * If the world data was constructed because it was first loaded by vanilla, will still be null
	 * until {@link #getDataForWorld(Class, String, World, boolean)} is called.
	 */
	private World world;
	
	/**
	 * Data stored via the {@link GoreCoreDataSaver} methods. FIXME never saves...?
	 */
	private GoreCoreDataSaverNBT storedData;
	
	public GoreCoreWorldData(String key) {
		super(key);
		this.storedData = new GoreCoreDataSaverNBT();
		System.out.println("constructed from key");
	}
	
	public GoreCoreWorldData(World worldFor, String key) {
		this(key);
		this.world = worldFor;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	/**
	 * Marks the world data dirty so that it will be saved to disk.
	 */
	@Override
	public void saveChanges() {
		markDirty();
	}
	
	/**
	 * Use to make an easy implementation of getDataForWorld:
	 * 
	 * <pre>
	 * public static MyWorldData getDataForWorld(World world) {
	 * 	return getDataForWorld(MyWorldData.class, "MyWorldData", world, true);
	 * }
	 * </pre>
	 * 
	 * @param worldDataClass
	 *            The class object of your world data
	 * @param key
	 *            The key to store the world data under
	 * @param world
	 *            The world to get world data for
	 * @param separatePerDimension
	 *            Whether world data is saved for each dimension or for all dimensions
	 * @return World data, retrieved using the specified options
	 */
	protected static <T extends GoreCoreWorldData> T getDataForWorld(Class<T> worldDataClass, String key, World world,
			boolean separatePerDimension) {
		try {
			MapStorage ms = separatePerDimension ? world.perWorldStorage : world.mapStorage;
			T data = worldDataClass.cast(ms.loadData(worldDataClass, key));
			
			if (data == null) {
				data = worldDataClass.getConstructor(World.class, String.class).newInstance(world, key);
				data.setDirty(true);
				ms.setData(key, data);
			}
			
			if (data.getWorld() == null) {
				data.setWorld(world);
			}
			
			return data;
		} catch (Exception e) {
			FMLLog.bigWarning("GoreCore> Could not create World Data class!");
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public int getInt(String key) {
		return storedData.getInt(key);
	}
	
	@Override
	public void setInt(String key, int value) {
		storedData.setInt(key, value);
	}
	
	@Override
	public String getString(String key) {
		return storedData.getString(key);
	}
	
	@Override
	public void setString(String key, String value) {
		storedData.setString(key, value);
	}
	
	@Override
	public float getFloat(String key) {
		return storedData.getFloat(key);
	}
	
	@Override
	public void setFloat(String key, float value) {
		storedData.setFloat(key, value);
	}
	
	@Override
	public double getDouble(String key) {
		return storedData.getDouble(key);
	}
	
	@Override
	public void setDouble(String key, double value) {
		storedData.setDouble(key, value);
	}
	
	@Override
	public long getLong(String key) {
		return storedData.getLong(key);
	}
	
	@Override
	public void setLong(String key, long value) {
		storedData.setLong(key, value);
	}
	
}
