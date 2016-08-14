package crowsofwar.gorecore.proxy;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import crowsofwar.gorecore.client.GoreCoreClientEvents;
import crowsofwar.gorecore.client.GoreCoreRenderTickEvent;
import crowsofwar.gorecore.util.GoreCoreIsPlayerWalking;

public class GoreCoreClientProxy extends GoreCoreCommonProxy {
	
	@Override
	protected File createUUIDCacheFile() {
		return new File(Minecraft.getMinecraft().mcDataDir, "GoreCore_ClientUUIDCache.txt");
	}
	
	@Override
	protected File createMinecraftDir() {
		return new File(Minecraft.getMinecraft().mcDataDir, ".");
	}
	
	@Override
	public boolean isPlayerWalking(EntityPlayer player) {
		if (player == Minecraft.getMinecraft().thePlayer) {
			GameSettings gc = Minecraft.getMinecraft().gameSettings;
			return gc.keyBindForward.getIsKeyPressed() || gc.keyBindBack.getIsKeyPressed() || gc.keyBindLeft.getIsKeyPressed()
					|| gc.keyBindRight.getIsKeyPressed();
		}
		
		return false;
	}
	
	@Override
	public GoreCoreIsPlayerWalking initPlayerWalkingClient() {
		return new GoreCoreIsPlayerWalking();
	}
	
	@Override
	public void sideSpecifics() {
		FMLCommonHandler.instance().bus().register(new GoreCoreRenderTickEvent());
		GoreCoreClientEvents eventHandler = new GoreCoreClientEvents();
		MinecraftForge.EVENT_BUS.register(eventHandler);
		FMLCommonHandler.instance().bus().register(eventHandler);
	}
	
	@Override
	public String translate(String key, Object... args) {
		return I18n.format(key, args);
	}
	
	@Override
	public EntityPlayer getClientSidePlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
	
}
