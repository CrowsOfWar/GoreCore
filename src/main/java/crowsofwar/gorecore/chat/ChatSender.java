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
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;

public class ChatSender {
	
	private final Map<String, ChatMessage> referenceToChatMessage;
	private final Map<String, ChatMessage> translateKeyToChatMessage;
	
	public ChatSender() {
		MinecraftForge.EVENT_BUS.register(this);
		this.referenceToChatMessage = new HashMap<String, ChatMessage>();
		this.translateKeyToChatMessage = new HashMap<String, ChatMessage>();
	}
	
	public ChatMessage newChatMessage(String translateKey, String... translateArgs) {
		ChatMessage cm = new ChatMessage(this, translateKey, translateArgs);
		translateKeyToChatMessage.put(translateKey, cm);
		return cm;
	}
	
	public void cleanup() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	
	private Object getField(ChatComponentTranslation obj, String field) {
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
				if (chat instanceof ChatComponentTranslation) {
					ChatComponentTranslation translate = (ChatComponentTranslation) chat;
					String key = (String) getField(translate, "key");
					ChatMessage cm = translateKeyToChatMessage.get(key);
					
					if (cm != null) {
						changed = true;
						String text = translate.getUnformattedText();
						String[] translateArgs = cm.getTranslationArgs();
						for (int i = 0; i < translateArgs.length; i++) {
							text = text.replace("${" + translateArgs[i] + "}", translate.getFormatArgs()[i].toString());
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
								
							} else if (item.equals("/color")){
								
								recievedFormatInstruction = format.setColor(item);
								
							} else if (item.startsWith("color=")) {
								
								recievedFormatInstruction = format.setColor(item.substring("color=".length()));
								
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
						
						result += (newText);
						
					}
					
				}
			}
			if (changed)
			e.message = new ChatComponentText(result);
		}
		
	}
	
	void send(ICommandSender sender, ChatMessage message, Object... args) {
		sender.addChatMessage(message.getChatMessage(args));
	}
	
	void send(ICommandSender sender, MultiMessage multi) {
		List<ChatMessage> messages = multi.getChatMessages();
		if (messages.isEmpty()) throw new IllegalArgumentException("Cannot send empty MultiMessage");
		IChatComponent send = null;
		for (int i = 0; i < messages.size(); i++) {
			ChatMessage message = messages.get(i);
			if (send == null) {
				send = message.getChatMessage(multi.getFormattingArgs().get(i));
			} else {
				send.appendSibling(message.getChatMessage(multi.getFormattingArgs().get(i)));
			}
		}
		sender.addChatMessage(send);
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
			EnumChatFormatting set = null;
			if (colorStr.equals("/color")) {
				set = EnumChatFormatting.WHITE;
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
