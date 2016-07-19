package crowsofwar.gorecore.tree.test;

import crowsofwar.gorecore.chat.ChatMessage;
import crowsofwar.gorecore.chat.ChatSender;

public class TestChatMessages {
	
	private static final ChatSender chat = new ChatSender();
	public static final ChatMessage MESSAGE_FRUIT = chat.newChatMessage("test.chatSender", "fruit");
	
	
}
