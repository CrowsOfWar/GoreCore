package crowsofwar.gorecore.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.EnumChatFormatting;

public class MessageConfiguration {
	
	public static final MessageConfiguration DEFAULT = new MessageConfiguration();
	
	private final Map<String, EnumChatFormatting> colors;
	
	public MessageConfiguration() {
		this.colors = new HashMap<String, EnumChatFormatting>();
	}
	
	public MessageConfiguration addColor(String reference, EnumChatFormatting color) {
		if (!color.isColor()) throw new IllegalArgumentException("The chat formatting must be a color");
		this.colors.put(reference, color);
		return this;
	}
	
	public EnumChatFormatting getColor(String reference) {
		return colors.get(reference);
	}
	
	public String getColorName(String reference) {
		if (hasColor(reference))
			return getColor(reference).name().toLowerCase();
		else
			return null;
	}
	
	public boolean hasColor(String reference) {
		return colors.containsKey(reference);
	}
	
}
