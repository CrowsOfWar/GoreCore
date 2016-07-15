package crowsofwar.gorecore.tree.test;

import java.util.List;

import crowsofwar.gorecore.tree.CommandCall;
import crowsofwar.gorecore.tree.ICommandNode;
import crowsofwar.gorecore.tree.NodeFunctional;
import net.minecraft.util.ChatComponentTranslation;

public class TestCakeLick extends NodeFunctional {

	public TestCakeLick() {
		super("lick", false);
	}

	@Override
	protected ICommandNode doFunction(CommandCall call, List<String> options) {
		call.getFrom().addChatMessage(new ChatComponentTranslation("test.lickCake"));
		return null;
	}

}
