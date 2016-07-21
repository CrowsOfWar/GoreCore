package crowsofwar.gorecore.tree.test;

import crowsofwar.gorecore.chat.ChatMessage;
import crowsofwar.gorecore.chat.ChatSender;

public class TestMessages {
	
	// TODO Fix: Making a multimessage that uses messages from different ChatSenders doesn't display ALL messages.
	private static final ChatSender SENDER = new ChatSender();
	public static final ChatMessage MSG_VIDEOGAME_HELP = SENDER.newChatMessage("test.buyVideogames.help");
	public static final ChatMessage MSG_CAKE_FROST_HELP = SENDER.newChatMessage("test.frostCake.help");
	public static final ChatMessage MSG_CAKE_LICK_HELP = SENDER.newChatMessage("test.lickCake.help");
	public static final ChatMessage MSG_PLAYVIDEOGAMES_HELP = SENDER.newChatMessage("test.videogames.help");
	public static final ChatMessage MSG_CHATSENDER_HELP = SENDER.newChatMessage("test.chatSender.help");
	public static final ChatMessage MSG_VIDEOGAME_BRANCH_HELP = SENDER.newChatMessage("test.videogamesBranch.help");
	
}
