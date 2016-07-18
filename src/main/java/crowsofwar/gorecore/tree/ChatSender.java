package crowsofwar.gorecore.tree;

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
						for (int i = 0; i < cm.translateArgs.length; i++) {
							text = text.replace("${" + cm.translateArgs[i] + "}", translate.getFormatArgs()[i].toString());
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
	
	public void send(ICommandSender sender, String referenceName, Object... args) {
		ChatMessage cm = referenceToChatMessage.get(referenceName);
		if (cm != null) {
			sender.addChatMessage(cm.getChatMessage(args));
		} else {
			GoreCore.LOGGER.warn("ChatSender- attempted to access unknown message " + referenceName);
		}
	}
	
	public void send(ICommandSender sender, List<String> references, List<Object[]> args) {
		if (references.size() != args.size()) throw new IllegalArgumentException("References/args do not match");
		if (references.size() == 0) throw new IllegalArgumentException("Cannot send 0 messages");
		IChatComponent comp = null;
		for (int i = 0; i < references.size(); i++) {
			ChatMessage cm = referenceToChatMessage.get(references.get(i));
			comp = comp == null ? cm.getChatMessage(args.get(i)) : comp.appendSibling(cm.getChatMessage(args.get(i)));
		}
		sender.addChatMessage(comp);
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
		
		public IChatComponent getChatMessage(Object... formattingArgs) {
			return new ChatComponentTranslation(translateKey, formattingArgs);
		}
		
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
