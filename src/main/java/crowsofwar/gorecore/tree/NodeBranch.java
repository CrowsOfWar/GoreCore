package crowsofwar.gorecore.tree;

import java.util.Map;

import crowsofwar.gorecore.tree.TreeCommandException.Reason;

public class NodeBranch implements ICommandNode {
	
	private final ICommandNode[] nodes;
	private final IArgument<String> argName;
	private final IArgument<?>[] args;
	
	public NodeBranch(ICommandNode... nodes) {
		this.nodes = nodes;
		this.argName = new ArgumentDirect<String>("node-name", ITypeConverter.CONVERTER_STRING);
		this.args = new IArgument<?>[] { argName };
	}
	
	@Override
	public ICommandNode execute(CommandCall call) {
		ArgumentList args = call.popArguments(argName);
		String name = args.get(argName);
		for (int i = 0; i < nodes.length; i++) {
			System.out.println(nodes[i].getNodeName() + "/" + name);
			if (nodes[i].getNodeName().equals(name)) return nodes[i];
		}
		throw new TreeCommandException(Reason.NO_BRANCH_NODE, name);
	}

	@Override
	public boolean needsOpPermission() {
		return false;
	}

	@Override
	public String getNodeName() {
		return "branch"; // Shouldn't need to be used
	}

	@Override
	public IArgument<?>[] getArgumentList() {
		return args;
	}
	
}
