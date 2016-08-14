package crowsofwar.gorecore.chat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mojang.realmsclient.gui.ChatFormatting;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crowsofwar.gorecore.GoreCore;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;

public class ChatSender {
	
	private static final ChatSender instance;
	
	private static final Map<String, ChatMessage> referenceToChatMessage;
	private static final Map<String, ChatMessage> translateKeyToChatMessage;
	
	static {
		instance = new ChatSender();
		MinecraftForge.EVENT_BUS.register(instance);
		referenceToChatMessage = new HashMap<String, ChatMessage>();
		translateKeyToChatMessage = new HashMap<String, ChatMessage>();
	}
	
	private ChatSender() {}
	
	public static ChatMessage newChatMessage(String translateKey, String... translateArgs) {
		return newChatMessage(MessageConfiguration.DEFAULT, translateKey, translateArgs);
	}
	
	public static ChatMessage newChatMessage(MessageConfiguration config, String translateKey, String... translateArgs) {
		ChatMessage cm = new ChatMessage(config, translateKey, translateArgs);
		translateKeyToChatMessage.put(translateKey, cm);
		return cm;
	}
	
	private static Object getField(ChatComponentTranslation obj, String field) {
		try {
			
			Field fieldObj = obj.getClass().getDeclaredField(field);
			fieldObj.setAccessible(true);
			return fieldObj.get(obj);
			
		} catch (ReflectiveOperationException e) {
			GoreCore.LOGGER.error("Couldn't get ChatComponentTranslation field '" + field + "' via reflection");
			e.printStackTrace();
			return null;
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void processClientChat(ClientChatReceivedEvent e) {
		if (e.message instanceof ChatComponentTranslation) {
			ChatComponentTranslation message = (ChatComponentTranslation) e.message;
			
			String result = "";
			
			List<IChatComponent> comps = new ArrayList();
			
			Object[] cloneFormatArgs = (Object[]) getField(message, "formatArgs");
			comps.add(new ChatComponentTranslation((String) getField(message, "key"), cloneFormatArgs));
			
			comps.addAll(e.message.getSiblings());
			boolean changed = false;
			
			for (IChatComponent chat : comps) {
				String processed = processChatComponent(chat);
				if (processed != null) {
					changed = true;
					result += processed;
				}
			}
			if (changed) e.message = new ChatComponentText(result);
		}
		
	}
	
	private String processChatComponent(IChatComponent chat) {
		String result = null;
		if (chat instanceof ChatComponentTranslation) {
			ChatComponentTranslation translate = (ChatComponentTranslation) chat;
			String key = (String) getField(translate, "key");
			ChatMessage cm = translateKeyToChatMessage.get(key);
			
			System.out.println("recieved " + key);
			System.out.println("chat message is " + cm);
			
			if (cm != null) {
				
				result = processText(translate.getUnformattedText(), cm, translate.getFormatArgs());
				
			}
			
		}
		
		return result;
		
	}
	
	private String processText(String text, ChatMessage cm, Object... formatArgs) {
		MessageConfiguration cfg = cm.getConfig();
		System.out.println("processing " + text);
		
		String[] translateArgs = cm.getTranslationArgs();
		// System.out.println("Translate args length: " + translateArgs.length);
		for (int i = 0; i < translateArgs.length; i++) {
			System.out.println("Translate arg " + translateArgs[i]);
			text = text.replace("${" + translateArgs[i] + "}", formatArgs[i].toString());
		}
		
		ChatFormat format = new ChatFormat();
		
		String newText = "";
		String[] split = text.split("[\\[\\]]");
		for (int i = 0; i < split.length; i++) {
			boolean recievedFormatInstruction = false;
			String item = split[i];
			if (item.equals("")) continue;
			if (item.equals("bold")) {
				
				format.setBold(true);
				recievedFormatInstruction = true;
				
			} else if (item.equals("/bold")) {
				
				format.setBold(false);
				recievedFormatInstruction = true;
				
			} else if (item.equals("italic")) {
				
				format.setItalic(true);
				recievedFormatInstruction = true;
				
			} else if (item.equals("/italic")) {
				
				format.setItalic(false);
				recievedFormatInstruction = true;
				
			} else if (item.equals("/color")) {
				
				recievedFormatInstruction = format.setColor(item);
				
			} else if (item.startsWith("color=")) {
				
				recievedFormatInstruction = format.setColor(cfg, item.substring("color=".length()));
				
			} else if (item.startsWith("translate=")) {
				
				String key = item.substring("translate=".length());
				item = processText(I18n.format(key), cm, formatArgs);
				
			}
			
			// If any formats changed, must re add all chat formats
			if (recievedFormatInstruction) {
				newText += EnumChatFormatting.RESET;
				newText += format.getColor(); // For some reason, color must come before bold
				newText += format.isBold() ? EnumChatFormatting.BOLD : "";
				newText += format.isItalic() ? EnumChatFormatting.ITALIC : "";
			} else {
				newText += item;
			}
			
		}
		text = newText;
		
		return newText;
	}
	
	static void send(ICommandSender sender, ChatMessage message, Object... args) {
		sender.addChatMessage(message.getChatMessage(args));
	}
	
	private class ChatFormat {
		
		private boolean isBold;
		private boolean isItalic;
		private EnumChatFormatting color;
		
		public ChatFormat() {
			isBold = false;
			isItalic = false;
			setColor("white");
		}
		
		public boolean setColor(String colorStr) {
			return setColor(MessageConfiguration.DEFAULT, colorStr);
		}
		
		public boolean setColor(MessageConfiguration config, String colorStr) {
			EnumChatFormatting set = null;
			if (colorStr.equals("/color")) {
				set = EnumChatFormatting.WHITE;
			} else if (config.hasColor(colorStr)) {
				set = config.getColor(colorStr);
			} else {
				EnumChatFormatting[] allFormats = EnumChatFormatting.values();
				for (int i = 0; i < allFormats.length; i++) {
					EnumChatFormatting format = allFormats[i];
					if (format.isColor() && format.name().toLowerCase().equals(colorStr.toLowerCase())) {
						set = format;
						break;
					}
				}
			}
			
			if (set == null) {
				return false;
			} else {
				this.color = set;
				return true;
			}
			
		}
		
		public void setBold(boolean bold) {
			this.isBold = bold;
		}
		
		public void setItalic(boolean italic) {
			this.isItalic = italic;
		}
		
		public boolean isBold() {
			return isBold;
		}
		
		public boolean isItalic() {
			return isItalic;
		}
		
		public EnumChatFormatting getColor() {
			return color;
		}
		
	}
	
}
