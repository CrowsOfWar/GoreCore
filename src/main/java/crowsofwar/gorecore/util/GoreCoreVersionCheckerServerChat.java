package crowsofwar.gorecore.util;

import net.minecraft.util.ChatComponentTranslation;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

/**
 * <p>A version checker that has the server greet players on
 * login.</p>
 * 
 * @author CrowsOfWar
 */
public class GoreCoreVersionCheckerServerChat extends GoreCoreVersionChecker {
	
	private final String chatKey;
	
	public GoreCoreVersionCheckerServerChat(String chatKey, String currentVersion, String url) {
		this(chatKey, true, currentVersion, url);
	}
	
	public GoreCoreVersionCheckerServerChat(String chatKey, boolean enabled, String currentVersion, String url) {
		super(currentVersion, url);
		this.chatKey = chatKey;
		if (enabled) FMLCommonHandler.instance().bus().register(this);
	}
	
	@SubscribeEvent
	public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
		event.player.addChatMessage(new ChatComponentTranslation(chatKey + (upToDate()
				? ".upToDate" : ".needsUpdate"),
				currentVersion(), latestVersion()));
	}
	
}
