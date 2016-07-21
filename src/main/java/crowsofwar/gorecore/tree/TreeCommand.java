package crowsofwar.gorecore.tree;

import java.util.ArrayList;
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
	private final ChatMessages messages;
	
	public TreeCommand() {
		this.chatSender = new ChatSender();
		this.messages = new ChatMessages(chatSender);
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
				
				if (call.getArgumentsLeft() == 0 && node instanceof NodeBranch && options.contains("help")) {
					
					if (node == branchRoot) {
						sendCommandHelp(sender);
					} else {
						sendBranchHelp(sender, (NodeBranch) node, path);
					}
					
					node = null;
				} else {
					node = node.execute(call, options);
					path += " " + node.getNodeName();
				}
				
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
		messages.cmdHelpTop.send(sender, getCommandName());
		messages.cmdHelpCommandOverview.send(sender);
		
		MultiMessage multi = messages.cmdHelpNodes.chain();
		
		ICommandNode[] allNodes = branchRoot.getSubNodes();
		for (int i = 0; i < allNodes.length; i++) {
			multi.add(messages.cmdHelpNodeItem, allNodes[i].getNodeName());
			if (i < allNodes.length - 1) {
				ChatMessage separator = messages.cmdHelpSeparator;
				if (i == allNodes.length - 2) separator = messages.cmdHelpSeparatorLast;
				multi.add(separator);
			}
		}
		multi.send(sender);
		
	}
	
	private void sendBranchHelp(ICommandSender sender, NodeBranch branch, String path) {
		
		messages.branchHelpTop.send(sender, branch.getNodeName());
		messages.branchHelpNotice.send(sender);
		messages.branchHelpInfo.chain().add(branch.getInfoMessage()).send(sender);
		
		MultiMessage chain = messages.branchHelpOptions.chain();
		ICommandNode[] subNodes = branch.getSubNodes();
		for (int i = 0; i < subNodes.length; i++) {
			chain.add(messages.branchHelpOptionsItem, subNodes[i].getNodeName());
			chain.add(i == subNodes.length - 1 ? messages.branchHelpOptionsSeparatorLast :
				messages.branchHelpOptionsSeparator);
			
		}
		chain.send(sender);
		
		messages.branchHelpExample.send(sender, path, subNodes[0].getNodeName());
		
	}
	
	/**
	 * Called to instantiate all subclass Command Nodes. Return
	 * the ones that should be added to the root branch.
	 */
	protected abstract ICommandNode[] addCommands();

	protected abstract void registerChatMessages(ChatSender sender);
	
	protected class ChatMessages {
		
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
		
		private ChatMessages(ChatSender chat) {
			
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
			
		}
		
	}
	
	
}