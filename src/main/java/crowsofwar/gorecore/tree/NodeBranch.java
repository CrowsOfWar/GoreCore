package crowsofwar.gorecore.tree;

import java.util.Map;

import crowsofwar.gorecore.tree.TreeCommandException.Reason;

public class NodeBranch implements ICommandNode {
	
	private final ICommandNode[] nodes;
	private final IArgument<String> argName;
	private final IArgument<?>[] args;
	private final String name;
	
	public NodeBranch(String name, ICommandNode... nodes) {
		this.nodes = nodes;
		this.argName = new ArgumentDirect<String>("node-name", ITypeConverter.CONVERTER_STRING);
		this.args = new IArgument<?>[] { argName };
		this.name = name;
	}
	
	@Override
	public ICommandNode execute(CommandCall call) {
		ArgumentList args = call.popArguments(argName);
		String name = args.get(argName);
		for (int i = 0; i < nodes.length; i++) {
			System.out.println(nodes[i].getNodeName() + "/" + name);
			if (nodes[i].getNodeName().equals(name)) return nodes[i];
		}
		throw new TreeCommandException(Reason.NO_BRANCH_NODE, name, getHelp());
	}

	@Override
	public boolean needsOpPermission() {
		return false;
	}

	@Override
	public String getNodeName() {
		return name;
	}

	@Override
	public IArgument<?>[] getArgumentList() {
		return args;
	}

	@Override
	public String getHelp() {
		return getNodeName() + " " + argName.getHelpString();
	}
	
}
