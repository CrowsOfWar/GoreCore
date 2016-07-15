package crowsofwar.gorecore.tree.test;

import java.util.List;

import crowsofwar.gorecore.tree.ArgumentDirect;
import crowsofwar.gorecore.tree.ArgumentList;
import crowsofwar.gorecore.tree.CommandCall;
import crowsofwar.gorecore.tree.IArgument;
import crowsofwar.gorecore.tree.ICommandNode;
import crowsofwar.gorecore.tree.ITypeConverter;
import crowsofwar.gorecore.tree.NodeFunctional;
import net.minecraft.util.ChatComponentTranslation;

public class TestBuyVideogames extends NodeFunctional {

	private final IArgument<Integer> argAmount;
	
	public TestBuyVideogames() {
		super("buy", true);
		addArguments(argAmount = new ArgumentDirect<Integer>("amount", ITypeConverter.CONVERTER_INTEGER, 1));
	}
	
	@Override
	protected ICommandNode doFunction(CommandCall call, List<String> options) {
		ArgumentList args = call.popArguments(argAmount);
		int amount = args.get(argAmount);
		call.getFrom().addChatMessage(new ChatComponentTranslation("test.buyVideogames", amount));
		return null;
	}
	
}
