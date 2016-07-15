package crowsofwar.gorecore.tree.test;

import java.util.List;

import crowsofwar.gorecore.tree.CommandCall;
import crowsofwar.gorecore.tree.ICommandNode;
import crowsofwar.gorecore.tree.NodeFunctional;
import net.minecraft.util.ChatComponentTranslation;

public class TestBuyVideogames extends NodeFunctional {

	public TestBuyVideogames() {
		super("buy", true);
	}
	
	@Override
	protected ICommandNode doFunction(CommandCall call, List<String> options) {
		call.getFrom().addChatMessage(new ChatComponentTranslation("test.buyVideogames"));
		return null;
	}
	
}
