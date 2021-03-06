package crowsofwar.gorecore;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crowsofwar.gorecore.proxy.GoreCoreCommonProxy;
import crowsofwar.gorecore.settings.GoreCoreModConfig;
import crowsofwar.gorecore.util.GoreCoreIsPlayerWalking;
import crowsofwar.gorecore.util.GoreCorePlayerUUIDs;
import crowsofwar.gorecore.util.GoreCoreVersionCheckerServerChat;

@Mod(modid=GoreCore.MOD_ID, name=GoreCore.MOD_NAME, version=GoreCore.MOD_VERSION)
public class GoreCore {
	
	public static final String MOD_ID = "GoreCore";
	public static final String MOD_NAME = "GoreCore";
	public static final String MOD_VERSION = "1.7.10-0.8.2";
	
	@SidedProxy(clientSide="crowsofwar.gorecore.proxy.GoreCoreClientProxy", serverSide="crowsofwar.gorecore.proxy.GoreCoreCommonProxy")
	public static GoreCoreCommonProxy proxy;
	
	public static GoreCoreModConfig config;
	
	/**
	 * The "is player walking" detector for the client-side.
	 */
	@SideOnly(Side.CLIENT)
	public static GoreCoreIsPlayerWalking walkDetectorClient;
	
	/**
	 * The "is player walking" detector for dedicated or integrated servers.
	 */
	public static GoreCoreIsPlayerWalking walkDetectorServer;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new GoreCoreModConfig(event);
		
		GoreCorePlayerUUIDs.addUUIDsToCacheFromCacheFile();
		walkDetectorClient = proxy.initPlayerWalkingClient();
		walkDetectorServer = new GoreCoreIsPlayerWalking();
		
		new GoreCoreVersionCheckerServerChat("gc.message.versionCheck", MOD_VERSION,
				"https://raw.githubusercontent.com/CrowsOfWar/PhysicalTraits/master/fetch/latest-version-gc.txt");
		
		proxy.sideSpecifics();
		
	}
	
	// Called both on the client and on the dedicated server
	@EventHandler
	public void onShutdown(FMLServerStoppingEvent event) {
		GoreCorePlayerUUIDs.saveUUIDCache();
	}
	
}
