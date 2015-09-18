package crowsofwar.gorecore.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import crowsofwar.gorecore.util.GoreCoreNBTInterfaces.MapUser;

public final class GoreCoreNBTUtil {
	
	public static UUID readUUIDFromNBT(NBTTagCompound nbt, String key) {
		return new UUID(nbt.getLong(key + "MostSig"), nbt.getLong(key + "LeastSig"));
	}
	
	public static void writeUUIDToNBT(NBTTagCompound nbt, String key, UUID uuid) {
		nbt.setLong(key + "MostSig", uuid.getMostSignificantBits());
		nbt.setLong(key + "LeastSig", uuid.getLeastSignificantBits());
	}
	
	public static NBTTagCompound getOrCreateNestedCompound(NBTTagCompound nbt, String key) {
		if (nbt.hasKey(key)) {
			return nbt.getCompoundTag(key);
		} else {
			NBTTagCompound result = new NBTTagCompound();
			nbt.setTag(key, result);
			return result;
		}
	}
	
	public static <T> List<T> readListFromNBT(NBTTagCompound nbt, String key, GoreCoreNBTInterfaces.CreateFromNBT<T> creator, Object... extraData) {
		List<T> result = new ArrayList<T>();
		
		NBTTagList nbtList = nbt.getTagList(key, 10);
		for (int i = 0; i < nbtList.tagCount(); i++) {
			result.add(creator.create(nbtList.getCompoundTagAt(i), new Object[0], extraData));
		}
		
		return result;
	}
	
	/**
	 * Writes the list to the NBTTagCompound.
	 * 
	 * @param nbt The NBTTagCompound
	 * @param key The list's identifier
	 * @param writer Used to write items in the list to NBT. methodsExtraData is an empty array.
	 * @param list The list to write
	 * @param extraData Extra data to be passed in to writer#write, is applied for each call to writer#write
	 */
	public static <T> void writeListToNBT(NBTTagCompound nbt, String key, GoreCoreNBTInterfaces.WriteToNBT<T> writer, List<T> list, Object... extraData) {
		NBTTagList nbtList = new NBTTagList();
		
		for (int i = 0; i < list.size(); i++) {
			NBTTagCompound comp = new NBTTagCompound();
			writer.write(comp, list.get(i), new Object[0], extraData);
			nbtList.appendTag(comp);
		}
		
		nbt.setTag(key, nbtList);
	}
	
	/**
	 * Reads the map from NBT.
	 * 
	 * @param nbt The NBTTagCompound to read the map from
	 * @param key The key
	 * @param createKey The creator for objects which will act as keys. methodsExtraData will be an empty array.
	 * @param createVal The creator for objects which will act as values. methodsExtraData will be an array containing only the key.
	 * @param extraDataKey Extra data passed in for createKey, is specific for each implementation of CreateFromNBT
	 * @param extraDataVal Extra data passed in for createVal, is specific for each implementation of CreateFromNBT
	 * @return A HashMap filled with all of the data stored via {@link #writeMapToNBT(NBTTagCompound, String, crowsofwar.gorecore.util.GoreCoreNBTInterfaces.WriteToNBT, crowsofwar.gorecore.util.GoreCoreNBTInterfaces.WriteToNBT, Object[], Object[], Map)}.
	 * The load factor is 0.75.
	 */
	public static <K, V> Map<K, V> readMapFromNBT(NBTTagCompound nbt, MapUser<K, V> mapUser, String key, Object[] constructArgsK, Object[] constructArgsV) {
		Map<K, V> result = new HashMap<K, V>();
		
		NBTTagList nbtList = nbt.getTagList(key, 10);
		for (int i = 0; i < nbtList.tagCount(); i++) {
			NBTTagCompound nbtI = nbtList.getCompoundTagAt(i);
			NBTTagCompound nbtK = nbtI.getCompoundTag("Key");
			NBTTagCompound nbtV = nbtI.getCompoundTag("Val");
			K k = mapUser.createK(nbtK, constructArgsK);
			V v = mapUser.createV(nbtV, k, constructArgsV);
			result.put(k, v);
		}
		
		return result;
	}
	
	/**
	 * Writes the map to NBT.
	 * 
	 * @param nbt
	 * @param key
	 * @param writeKey The writer for the keys. methodsExtraData will be an empty array.
	 * @param writeVal The writer for the values. methodsExtraData will be an empty array.
	 * @param extraDataKey
	 * @param extraDataVal
	 * @param map
	 */
	public static <K, V> void writeMapToNBT(NBTTagCompound nbt, Map<K, V> map, MapUser<K, V> mapUser, String key) {
		NBTTagList nbtList = new NBTTagList();
		
		List<Entry<K, V>> entries = new ArrayList<Entry<K, V>>(map.entrySet());
		for (int i = 0; i < entries.size(); i++) {
			Entry<K, V> entry = entries.get(i);
			NBTTagCompound nbtI = new NBTTagCompound();
			NBTTagCompound nbtK = new NBTTagCompound();
			NBTTagCompound nbtV = new NBTTagCompound();
			mapUser.writeK(nbtK, entry.getKey());
			mapUser.writeV(nbtV, entry.getValue());
			nbtI.setTag("Key", nbtK);
			nbtI.setTag("Val", nbtV);
			nbtList.appendTag(nbtI);
		}
		
		nbt.setTag(key, nbtList);
	}
	
	/**
	 * Gets the NBTTagCompound for that ItemStack, creating it if
	 * necessary.
	 * 
	 * @param stack The ItemStack to get the compound for
	 * @return The stack's data compound
	 */
	public static NBTTagCompound getStackCompound(ItemStack stack) {
		return stack.stackTagCompound == null ? stack.stackTagCompound = new NBTTagCompound() : stack.stackTagCompound;
	}
	
}
