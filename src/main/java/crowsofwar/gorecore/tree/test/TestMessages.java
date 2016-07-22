package crowsofwar.gorecore.tree.test;

import crowsofwar.gorecore.chat.ChatMessage;
import static crowsofwar.gorecore.chat.ChatSender.newChatMessage;

public class TestMessages {
	
	// TODO Fix: Making a multimessage that uses messages from different ChatSenders doesn't display ALL messages.
	public static final ChatMessage MSG_VIDEOGAME_HELP = newChatMessage("test.buyVideogames.help");
	public static final ChatMessage MSG_CAKE_FROST_HELP = newChatMessage("test.frostCake.help");
	public static final ChatMessage MSG_CAKE_LICK_HELP = newChatMessage("test.lickCake.help");
	public static final ChatMessage MSG_PLAYVIDEOGAMES_HELP = newChatMessage("test.videogames.help");
	public static final ChatMessage MSG_CHATSENDER_HELP = newChatMessage("test.chatSender.help");
	public static final ChatMessage MSG_VIDEOGAME_BRANCH_HELP = newChatMessage("test.videogamesBranch.help");
	
}
