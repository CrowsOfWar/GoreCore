package crowsofwar.gorecore.tree.test;

import java.util.List;

import crowsofwar.gorecore.tree.CommandCall;
import crowsofwar.gorecore.tree.ICommandNode;
import crowsofwar.gorecore.tree.NodeFunctional;
import net.minecraft.util.ChatComponentTranslation;

public class TestCakeFrost extends NodeFunctional {

	public TestCakeFrost() {
		super("frost", true);
	}

	@Override
	protected ICommandNode doFunction(CommandCall call, List<String> options) {
		String end = options.contains("fancy") ? ".fancy" : "";
		call.getFrom().addChatMessage(new ChatComponentTranslation("test.frostCake" + end));
		return null;
	}

}
