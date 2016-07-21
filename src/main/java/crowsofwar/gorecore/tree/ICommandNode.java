package crowsofwar.gorecore.tree;

import java.util.List;

import crowsofwar.gorecore.chat.ChatMessage;

public interface ICommandNode {
	
	ICommandNode execute(CommandCall call, List<String> options);
	
	boolean needsOpPermission();
	
	String getNodeName();
	
	IArgument<?>[] getArgumentList();
	
	String getHelp();
	
	ChatMessage getInfoMessage();
	
}
