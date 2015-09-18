package crowsofwar.gorecore.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;

public class GoreCoreRenderTickEvent {
	
	private EntityRenderer gcRenderer;
	
	@SubscribeEvent
	public void renderTick(RenderTickEvent event) {
		if (event.phase == Phase.START) {
			Minecraft mc = Minecraft.getMinecraft();
			if (gcRenderer == null) gcRenderer = new GoreCoreEntityRenderer(mc, mc.getResourceManager());
			if (mc.entityRenderer != gcRenderer) mc.entityRenderer = gcRenderer;
		}
	}
	
}
