package crowsofwar.gorecore.tree;

import java.util.List;

import net.minecraft.util.ChatComponentTranslation;

public abstract class NodeFunctional implements ICommandNode {
	
	private final String name;
	private final boolean op;
	private IArgument<?>[] args;
	
	public NodeFunctional(String name, boolean op) {
		this.name = name;
		this.op = op;
	}
	
	protected void addArguments(IArgument<?>... args) {
		this.args = args;
	}
	
	@Override
	public final boolean needsOpPermission() {
		return op;
	}

	@Override
	public final String getNodeName() {
		return name;
	}

	@Override
	public final IArgument<?>[] getArgumentList() {
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

	@Override
	public final ICommandNode execute(CommandCall call, List<String> options) {
		if (options.contains("help")) {
			call.getFrom().addChatMessage(new ChatComponentTranslation("gc.tree.nodeHelp", getHelp()));
			return null;
		} else {
			return doFunction(call, options);
		}
	}
	
	protected abstract ICommandNode doFunction(CommandCall call, List<String> options);
	
}
