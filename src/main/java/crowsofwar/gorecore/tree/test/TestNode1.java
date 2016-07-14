package crowsofwar.gorecore.tree.test;

import crowsofwar.gorecore.tree.ArgumentDirect;
import crowsofwar.gorecore.tree.ArgumentList;
import crowsofwar.gorecore.tree.CommandCall;
import crowsofwar.gorecore.tree.IArgument;
import crowsofwar.gorecore.tree.ICommandNode;
import crowsofwar.gorecore.tree.ITypeConverter;
import net.minecraft.util.ChatComponentText;

public class TestNode1 implements ICommandNode {

	private final IArgument<?>[] args;
	private final IArgument<String> argA;
	private final IArgument<Integer> argB;
	
	public TestNode1() {
		argA = new ArgumentDirect<String>("item", ITypeConverter.CONVERTER_STRING);
		argB = new ArgumentDirect<Integer>("amount", ITypeConverter.CONVERTER_INTEGER);
		args = new IArgument<?>[] { argA, argB };
	}
	
	@Override
	public ICommandNode execute(CommandCall call) {
		
		ArgumentList args = call.popArguments(argA, argB);
		String a = args.get(argA);
		Integer b = args.get(argB);
		call.getFrom().addChatMessage(new ChatComponentText(b + " " + a + "s"));
		
		return null;
	}

	@Override
	public boolean needsOpPermission() {
		return false;
	}

	@Override
	public String getNodeName() {
		return "node1";
	}

	@Override
	public IArgument<?>[] getArgumentList() {
		return args;
	}

}
