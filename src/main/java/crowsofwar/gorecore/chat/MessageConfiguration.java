package crowsofwar.gorecore.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.EnumChatFormatting;

public class MessageConfiguration {
	
	public static final MessageConfiguration DEFAULT = new MessageConfiguration();
	
	private final Map<String, String> colors;
	
	public MessageConfiguration() {
		this.colors = new HashMap<String, String>();
	}
	
	public MessageConfiguration addColor(String reference, EnumChatFormatting color) {
		if (!color.isColor()) throw new IllegalArgumentException("The chat formatting must be a color");
		this.colors.put(reference, color.name().toLowerCase());
		return this;
	}
	
	public String getColor(String reference) {
		return colors.get(reference);
	}
	
}
