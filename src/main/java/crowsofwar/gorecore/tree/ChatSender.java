package crowsofwar.gorecore.tree;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crowsofwar.gorecore.GoreCore;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;

public class ChatSender {
	
	private final Map<String, ChatMessage> referenceToChatMessage;
	private final Map<String, ChatMessage> translateKeyToChatMessage;
	
	public ChatSender() {
		MinecraftForge.EVENT_BUS.register(this);
		this.referenceToChatMessage = new HashMap<String, ChatSender.ChatMessage>();
		this.translateKeyToChatMessage = new HashMap<String, ChatSender.ChatMessage>();
	}
	
	public void registerChatMessage(String reference, String translateKey, String... translateArgs) {
		ChatMessage cm = new ChatMessage(reference, translateKey, translateArgs);
		referenceToChatMessage.put(reference, cm);
		translateKeyToChatMessage.put(translateKey, cm);
	}
	
	public void cleanup() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void processClientChat(ClientChatReceivedEvent e) {
		IChatComponent chat = e.message;
		if (chat instanceof ChatComponentTranslation && chat.getUnformattedText().startsWith("[format-exp]")) {
			ChatComponentTranslation translate = (ChatComponentTranslation) chat;
			String key = getChatKey(translate);
			ChatMessage cm = translateKeyToChatMessage.get(key);
			
			if (cm != null) {
				
				String text = translate.getUnformattedText();
				text = text.substring("[format-exp]".length());
				for (int i = 0; i < cm.translateArgs.length; i++) {
					text = text.replace("$" + cm.translateArgs[i], translate.getFormatArgs()[i].toString());
				}
				
				e.message = new ChatComponentText(text);
				
			}
			
		}
	}
	
	public void send(ICommandSender sender, String referenceName, Object... args) {
		ChatMessage cm = referenceToChatMessage.get(referenceName);
		if (cm != null) {
			cm.send(sender, args);
		} else {
			GoreCore.LOGGER.warn("ChatSender- attempted to access unknown message " + referenceName);
		}
	}
	
	private String getChatKey(ChatComponentTranslation translate) {
		try {
			
			Field f = ChatComponentTranslation.class.getDeclaredField("key");
			f.setAccessible(true);
			return (String) f.get(translate);
			
		} catch (ReflectiveOperationException e) {
			GoreCore.LOGGER.error("Caught error while trying to retrieve ChatComponentTranslation key via reflection");
			e.printStackTrace();
			return "";
		}
		
	}
	
	private class ChatMessage {
		
		private final String reference;
		private final String translateKey;
		private final String[] translateArgs;
		
		public ChatMessage(String reference, String translateKey, String[] translateArgs) {
			this.reference = reference;
			this.translateKey = translateKey;
			this.translateArgs = translateArgs;
		}
		
		public void send(ICommandSender sender, Object... formattingArgs) {
			sender.addChatMessage(new ChatComponentTranslation(translateKey, formattingArgs));
		}
		
	}
	
}
