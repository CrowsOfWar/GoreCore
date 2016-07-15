package crowsofwar.gorecore.tree.test;

import java.util.List;

import crowsofwar.gorecore.tree.CommandCall;
import crowsofwar.gorecore.tree.ICommandNode;
import crowsofwar.gorecore.tree.NodeFunctional;
import net.minecraft.util.ChatComponentTranslation;

public class TestPlayVideogames extends NodeFunctional {

	public TestPlayVideogames() {
		super("play", false);
	}
	
	@Override
	protected ICommandNode doFunction(CommandCall call, List<String> options) {
		String videogame = options.isEmpty() ? "" : options.get(0);
		String send = options.isEmpty() ? "test.videogames.none" : "test.videogames";
		call.getFrom().addChatMessage(new ChatComponentTranslation(send, videogame));
		return null;
	}
	
}
