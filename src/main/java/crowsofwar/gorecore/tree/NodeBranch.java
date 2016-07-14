package crowsofwar.gorecore.tree;

import java.util.Map;

public class NodeBranch implements ICommandNode {
	
	private final ICommandNode[] nodes;
	private final IArgument<String> argName;
	
	public NodeBranch(ICommandNode... nodes) {
		this.nodes = nodes;
		this.argName = new ArgumentDirect<String>("branch-e", ITypeConverter.CONVERTER_STRING);
	}
	
	@Override
	public ICommandNode execute(CommandCall call) {
		ArgumentList args = call.popArguments(argName);
		String name = args.get(argName);
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i].getNodeName().equals(name)) return nodes[i];
		}
		throw new TreeCommandException("No node found with name", name);
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
