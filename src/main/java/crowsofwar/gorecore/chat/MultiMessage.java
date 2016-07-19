package crowsofwar.gorecore.chat;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommandSender;

public class MultiMessage {
	
	private final List<ChatMessage> chatMessages;
	private final ChatSender chat;
	
	MultiMessage(ChatSender chat) {
		this.chatMessages = new ArrayList<ChatMessage>();
		this.chat = chat;
	}
	
	public MultiMessage add(ChatMessage message) {
		this.chatMessages.add(message);
		return this;
	}
	
	public List<ChatMessage> getChatMessages() {
		return chatMessages;
	}
	
	public void send(ICommandSender sender, Object... formattingArgs) {
		chat.send(sender, this, formattingArgs);
	}
	
}
