package crowsofwar.gorecore.tree.test;

import crowsofwar.gorecore.tree.ArgumentDirect;
import crowsofwar.gorecore.tree.ArgumentList;
import crowsofwar.gorecore.tree.CommandCall;
import crowsofwar.gorecore.tree.IArgument;
import crowsofwar.gorecore.tree.ICommandNode;
import crowsofwar.gorecore.tree.ITypeConverter;
import net.minecraft.util.ChatComponentText;

public class TestNode2 implements ICommandNode {
	
	private final IArgument<Double> argTemp;
	
	public TestNode2() {
		argTemp = new ArgumentDirect("temperature", ITypeConverter.CONVERTER_DOUBLE, 3);
	}
	
	@Override
	public ICommandNode execute(CommandCall call) {
		ArgumentList args = call.popArguments(argTemp);
		call.getFrom().addChatMessage(new ChatComponentText("The temperature is " + args.get(argTemp)));
		return null;
	}

	@Override
	public boolean needsOpPermission() {
		return false;
	}

	@Override
	public String getNodeName() {
		return "node2";
	}

}
