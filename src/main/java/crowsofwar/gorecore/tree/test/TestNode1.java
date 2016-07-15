package crowsofwar.gorecore.tree.test;

import java.util.List;

import crowsofwar.gorecore.tree.ArgumentDirect;
import crowsofwar.gorecore.tree.ArgumentList;
import crowsofwar.gorecore.tree.CommandCall;
import crowsofwar.gorecore.tree.IArgument;
import crowsofwar.gorecore.tree.ICommandNode;
import crowsofwar.gorecore.tree.ITypeConverter;
import crowsofwar.gorecore.tree.NodeFunctional;
import net.minecraft.util.ChatComponentText;

public class TestNode1 extends NodeFunctional {

	private final IArgument<String> argA;
	private final IArgument<Integer> argB;
	
	public TestNode1() {
		super("node1", false);
		argA = new ArgumentDirect<String>("item", ITypeConverter.CONVERTER_STRING);
		argB = new ArgumentDirect<Integer>("amount", ITypeConverter.CONVERTER_INTEGER);
		addArguments(argA, argB);
	}
	
	@Override
	protected ICommandNode doFunction(CommandCall call, List<String> options) {
		ArgumentList args = call.popArguments(argA, argB);
		String a = args.get(argA);
		Integer b = args.get(argB);
		call.getFrom().addChatMessage(new ChatComponentText(b + " " + a + "s"));
		
		return null;
	}

}
