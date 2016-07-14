package crowsofwar.gorecore.tree;

import java.util.Map;

public class NodeBranch implements ICommandNode {
	
	private final ICommandNode[] nodes;
	private final IArgument<String> argName;
	
	public NodeBranch(ICommandNode... nodes) {
		this.nodes = nodes;
		
	}
	
	@Override
	public ICommandNode execute(CommandCall call) {
		ArgumentList args = call.popArguments(arguments);
		
		return null;
	}

	@Override
	public boolean needsOpPermission() {
		return false;
	}

	@Override
	public String getNodeName() {
		return "branch"; // Shouldn't need to be used
	}
	
	
	
}
