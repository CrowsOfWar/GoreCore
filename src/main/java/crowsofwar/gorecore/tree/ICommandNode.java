package crowsofwar.gorecore.tree;

import java.util.List;

public interface ICommandNode {
	
	ICommandNode execute(CommandCall call, List<String> options);
	
	boolean needsOpPermission();
	
	String getNodeName();
	
	IArgument<?>[] getArgumentList();
	
	String getHelp();
	
}
