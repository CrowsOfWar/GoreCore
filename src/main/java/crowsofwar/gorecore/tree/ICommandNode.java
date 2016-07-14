package crowsofwar.gorecore.tree;

public interface ICommandNode {
	
	ICommandNode execute(CommandCall call);
	
	boolean needsOpPermission();
	
	String getNodeName();
	
}
