package crowsofwar.gorecore.tree.test;

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
		ICommandNode node1 = new TestNode1();
		ICommandNode node2 = new TestNode2();
		return new ICommandNode[] { node1, node2 };
	}
	
}
