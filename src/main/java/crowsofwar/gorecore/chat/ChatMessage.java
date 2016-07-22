package crowsofwar.gorecore.chat;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class ChatMessage {
	
	private final String translateKey;
	private final String[] translateArgs;
	
	ChatMessage(String translateKey, String... translateArgs) {
		this.translateKey = translateKey;
		this.translateArgs = translateArgs;
	}
	
	public IChatComponent getChatMessage(Object... formattingArgs) {
		return new ChatComponentTranslation(translateKey, formattingArgs);
	}
	
	public void send(ICommandSender sender, Object... formattingArgs) {
		ChatSender.send(sender, this, formattingArgs);
	}
	
	public String[] getTranslationArgs() {
		return translateArgs;
	}
	
	public MultiMessage chain() {
		return new MultiMessage().add(this);
	}
	
}