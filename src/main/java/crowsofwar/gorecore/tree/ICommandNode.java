package crowsofwar.gorecore.tree;

public interface ICommandNode {
	
	ICommandNode execute(CommandCall call);
	
	boolean needsOpPermission();
	
	String getNodeName();
	
	IArgument<?>[] getArgumentList();
	
	String getHelp();
	
}
