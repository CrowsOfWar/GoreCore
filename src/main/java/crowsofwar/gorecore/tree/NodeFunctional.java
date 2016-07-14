package crowsofwar.gorecore.tree;

public abstract class NodeFunctional implements ICommandNode {
	
	private final String name;
	private final boolean op;
	private IArgument<?>[] args;
	
	public NodeFunctional(String name, boolean op) {
		this.name = name;
		this.op = op;
	}
	
	private void addArguments(IArgument<?>... args) {
		this.args = args;
	}
	
	@Override
	public boolean needsOpPermission() {
		return op;
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
		String out = getNodeName();
		IArgument<?>[] args = getArgumentList();
		for (int i = 0; i < args.length; i++) {
			out += " " + args[i].getSpecificationString();
		}
		return out;
	}
	
}
