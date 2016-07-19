package crowsofwar.gorecore.tree.test;

import crowsofwar.gorecore.chat.ChatSender;
import crowsofwar.gorecore.tree.ICommandNode;
import crowsofwar.gorecore.tree.NodeBranch;
import crowsofwar.gorecore.tree.TreeCommand;

public class TreeTest extends TreeCommand {
	
	@Override
	public String getCommandName() {
		return "test";
	}
	
	@Override
	protected ICommandNode[] addCommands() {
		ICommandNode cakeFrost = new TestCakeFrost();
		ICommandNode cakeLick = new TestCakeLick();
		ICommandNode branchCake = new NodeBranch("cake", cakeFrost, cakeLick);
		
		ICommandNode videogamesPlay = new TestPlayVideogames();
		ICommandNode videogamesBuy = new TestBuyVideogames();
		ICommandNode branchVideogames = new NodeBranch("videogames", videogamesPlay, videogamesBuy);
		
		return new ICommandNode[] { branchCake, branchVideogames, new TestUseChatSender() };
		
	}

	@Override
	protected void registerChatMessages(ChatSender sender) {
		
	}
	
}
