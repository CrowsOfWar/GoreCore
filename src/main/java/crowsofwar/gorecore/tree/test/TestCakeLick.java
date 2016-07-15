package crowsofwar.gorecore.tree.test;

import java.util.List;

import crowsofwar.gorecore.tree.ArgumentDirect;
import crowsofwar.gorecore.tree.CommandCall;
import crowsofwar.gorecore.tree.IArgument;
import crowsofwar.gorecore.tree.ICommandNode;
import crowsofwar.gorecore.tree.ITypeConverter;
import crowsofwar.gorecore.tree.NodeFunctional;
import net.minecraft.util.ChatComponentTranslation;

public class TestCakeLick extends NodeFunctional {

	private IArgument<Double> argGallons;
	
	public TestCakeLick() {
		super("lick", false);
		argGallons = new ArgumentDirect<Double>("gallons", ITypeConverter.CONVERTER_DOUBLE);
	}

	@Override
	protected ICommandNode doFunction(CommandCall call, List<String> options) {
		double gallons = call.popArguments(argGallons).get(argGallons);
		call.getFrom().addChatMessage(new ChatComponentTranslation("test.lickCake", gallons));
		return null;
	}

}
