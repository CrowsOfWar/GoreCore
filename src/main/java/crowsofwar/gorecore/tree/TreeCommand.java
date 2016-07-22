package crowsofwar.gorecore.tree;

import java.util.Arrays;
import java.util.List;

import crowsofwar.gorecore.chat.ChatMessage;
import crowsofwar.gorecore.chat.ChatSender;
import crowsofwar.gorecore.chat.MultiMessage;
import crowsofwar.gorecore.tree.TreeCommandException.Reason;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;

public abstract class TreeCommand implements ICommand {
	
	private NodeBranch branchRoot;
	private final ChatSender chatSender;
	
	public TreeCommand() {
		this.chatSender = new ChatSender();
		initChatMessages(chatSender);
		branchRoot = new NodeBranch(chatSender.newChatMessage("gc.tree.branchHelp.root", "command"), getCommandName(),
				addCommands());
		registerChatMessages(chatSender);
		
	}
	
	@Override
	public int compareTo(Object o) {
		return 0;
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return branchRoot.getHelp();
	}
	
	@Override
	public List getCommandAliases() {
		return null;
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] arguments) {
		
		try {
			
			String allOptions = arguments.length > 0 ? arguments[arguments.length - 1] : "";
			boolean hasOptions = allOptions.startsWith("--");
			List<String> options = Arrays.asList(new String[0]);
			if (hasOptions) {
				options = Arrays.asList(allOptions.substring(2).split(","));
				arguments = Arrays.copyOfRange(arguments, 0, arguments.length - 1);
			}
			
			CommandCall call = new CommandCall(sender, arguments);
			
			String path = "/" + getCommandName();
			
			ICommandNode node = branchRoot;
			while (node != null) {
				
				if (node.needsOpPermission() && !call.isOpped()) throw new TreeCommandException(Reason.NO_PERMISSION);
				
				if (call.getArgumentsLeft() == 0 && options.contains("help")) {
					
					if (node instanceof NodeBranch) {
						if (node == branchRoot) {
							sendCommandHelp(sender);
						} else {
							sendBranchHelp(sender, (NodeBranch) node, path);
						}
					} else {
						sendNodeHelp(sender, node);
					}
					
					node = null;
				} else {
					node = node.execute(call, options);
				}
				
				if (node != null)
					path += " " + node.getNodeName();
				
			}
			
		} catch (TreeCommandException e) {
			
			sender.addChatMessage(new ChatComponentTranslation(e.getMessage(), e.getFormattingArgs()));
		}
		
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}
	
	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return null;
	}
	
	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}
	
	private void sendCommandHelp(ICommandSender sender) {
		cmdHelpTop.send(sender, getCommandName());
		cmdHelpCommandOverview.send(sender);
		
		MultiMessage multi = cmdHelpNodes.chain();
		
		ICommandNode[] allNodes = branchRoot.getSubNodes();
		for (int i = 0; i < allNodes.length; i++) {
			multi.add(cmdHelpNodeItem, allNodes[i].getNodeName());
			if (i < allNodes.length - 1) {
				ChatMessage separator = cmdHelpSeparator;
				if (i == allNodes.length - 2) separator = cmdHelpSeparatorLast;
				multi.add(separator);
			}
		}
		multi.send(sender);
		
	}
	
	private void sendBranchHelp(ICommandSender sender, NodeBranch branch, String path) {
		
		branchHelpTop.send(sender, branch.getNodeName());
		branchHelpNotice.send(sender);
		branchHelpInfo.chain().add(branch.getInfoMessage()).send(sender);
		
		MultiMessage chain = branchHelpOptions.chain();
		ICommandNode[] subNodes = branch.getSubNodes();
		for (int i = 0; i < subNodes.length; i++) {
			chain.add(branchHelpOptionsItem, subNodes[i].getNodeName());
			if (i < subNodes.length - 1)
				chain.add(i == subNodes.length - 2 ? branchHelpOptionsSeparatorLast :
					branchHelpOptionsSeparator);
			
		}
		chain.send(sender);
		
		branchHelpExample.send(sender, path, subNodes[0].getNodeName());
		
	}
	
	private void sendNodeHelp(ICommandSender sender, ICommandNode node) {
		
		nodeHelpTop.send(sender, node.getNodeName());
		nodeHelpDesc.chain().add(node.getInfoMessage()).send(sender);
		
		MultiMessage msgArguments = nodeHelpArgs.chain();
		for (IArgument<?> arg : node.getArgumentList()) msgArguments.add(nodeHelpArgsItem, arg.getArgumentName());
		msgArguments.send(sender);
		
		MultiMessage msgAccepted = nodeHelpAccepted.chain();
		for (IArgument<?> arg : node.getArgumentList()) msgAccepted.add(nodeHelpAcceptedItem, arg.getHelpString());
		msgAccepted.send(sender);
		
	}
	
	/**
	 * Called to instantiate all subclass Command Nodes. Return
	 * the ones that should be added to the root branch.
	 */
	protected abstract ICommandNode[] addCommands();

	protected abstract void registerChatMessages(ChatSender sender);
	
	protected ChatMessage cmdHelpTop;
	protected ChatMessage cmdHelpNodes;
	protected ChatMessage cmdHelpNodeItem;
	protected ChatMessage cmdHelpSeparator;
	protected ChatMessage cmdHelpSeparatorLast;
	protected ChatMessage cmdHelpCommandOverview;
	
	protected ChatMessage branchHelpTop;
	protected ChatMessage branchHelpNotice;
	protected ChatMessage branchHelpInfo;
	protected ChatMessage branchHelpOptions;
	protected ChatMessage branchHelpOptionsItem;
	protected ChatMessage branchHelpOptionsSeparator;
	protected ChatMessage branchHelpOptionsSeparatorLast;
	protected ChatMessage branchHelpExample;
	protected ChatMessage branchHelpDefault;
	
	protected ChatMessage nodeHelpTop;
	protected ChatMessage nodeHelpDesc;
	protected ChatMessage nodeHelpArgs;
	protected ChatMessage nodeHelpArgsItem;
	protected ChatMessage nodeHelpAccepted;
	protected ChatMessage nodeHelpAcceptedItem;
	
	private void initChatMessages(ChatSender chat) {
		
		cmdHelpTop = chat.newChatMessage("gc.tree.cmdhelp.top", "name");
		cmdHelpNodes = chat.newChatMessage("gc.tree.cmdhelp.nodes");
		cmdHelpNodeItem = chat.newChatMessage( "gc.tree.cmdhelp.nodes.item", "node");
		cmdHelpSeparator = chat.newChatMessage("gc.tree.cmdhelp.nodes.separator");
		cmdHelpSeparatorLast = chat.newChatMessage("gc.tree.cmdhelp.nodes.separatorLast");
		cmdHelpCommandOverview = chat.newChatMessage("gc.tree.cmdhelp.showCmdInfo");
		
		branchHelpTop = chat.newChatMessage("gc.tree.branchHelp.top", "name");
		branchHelpNotice = chat.newChatMessage("gc.tree.branchHelp.notice");
		branchHelpInfo = chat.newChatMessage("gc.tree.branchHelp.info");
		branchHelpOptions = chat.newChatMessage("gc.tree.branchHelp.options");
		branchHelpOptionsItem = chat.newChatMessage("gc.tree.branchHelp.options.item", "node");
		branchHelpOptionsSeparator = chat.newChatMessage("gc.tree.branchHelp.options.separator");
		branchHelpOptionsSeparatorLast = chat.newChatMessage("gc.tree.branchHelp.options.separatorLast");
		branchHelpExample = chat.newChatMessage("gc.tree.branchHelp.example", "path", "node-name");
		branchHelpDefault = chat.newChatMessage("gc.tree.branch.defaultInfo");
		
		nodeHelpTop = chat.newChatMessage("gc.tree.nodeHelp.top", "name");
		System.out.println(nodeHelpTop);
		nodeHelpDesc = chat.newChatMessage("gc.tree.nodeHelp.desc");
		nodeHelpArgs = chat.newChatMessage("gc.tree.nodeHelp.args");
		nodeHelpArgsItem = chat.newChatMessage("gc.tree.nodeHelp.args.item", "argument");
		nodeHelpAccepted = chat.newChatMessage("gc.tree.nodeHelp.accepted");
		nodeHelpAcceptedItem = chat.newChatMessage("gc.tree.nodeHelp.accepted.item", "input");
		
	}
	
}