package crowsofwar.gorecore.chat;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommandSender;

public class MultiMessage {
	
	private final List<ChatMessage> chatMessages;
	private final List<Object[]> formattingArgs;
	private final ChatSender chat;
	
	MultiMessage(ChatSender chat) {
		this.chatMessages = new ArrayList<ChatMessage>();
		this.formattingArgs = new ArrayList<Object[]>();
		this.chat = chat;
	}
	
	public MultiMessage add(ChatMessage message, Object... formattingArgs) {
		this.chatMessages.add(message);
		this.formattingArgs.add(formattingArgs);
		return this;
	}
	
	public List<ChatMessage> getChatMessages() {
		return chatMessages;
	}
	
	public void send(ICommandSender sender) {
		chat.send(sender, this);
	}
	
	public List<Object[]> getFormattingArgs() {
		return formattingArgs;
	}
	
}
