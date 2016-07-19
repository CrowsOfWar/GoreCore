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
		branchRoot = new NodeBranch(getCommandName(), addCommands());
		this.chatSender = new ChatSender();
		this.messages = new ChatMessages(chatSender);
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
			
			ICommandNode node = branchRoot;
			while (node != null) {
				
				if (node.needsOpPermission() && !call.isOpped()) throw new TreeCommandException(Reason.NO_PERMISSION);
				
				if (call.getArgumentsLeft() == 0 && node instanceof NodeBranch && options.contains("help")) {
					
					if (node == branchRoot) {
						sendCommandHelp(chatSender, sender);
					} else {
						sender.addChatMessage(new ChatComponentTranslation("gc.tree.help", node.getHelp(),
								getCommandName()));
					}
					
					node = null;
				} else {
					node = node.execute(call, options);
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
	
	private void sendCommandHelp(ChatSender chat, ICommandSender sender) {
		messages.commandHelp.top.send(sender, getCommandName());
		messages.commandHelp.commandOverview.send(sender);
		
		MultiMessage multi = messages.commandHelp.nodes.chain();
		
		ICommandNode[] allNodes = branchRoot.getSubNodes();
		for (int i = 0; i < allNodes.length; i++) {
			multi.add(messages.commandHelp.nodeItem, allNodes[i]);
			if (i < allNodes.length - 1) {
				ChatMessage separator = messages.commandHelp.separator;
				if (i == allNodes.length - 2) separator = messages.commandHelp.separatorLast;
				multi.add(separator);
			}
		}
		multi.send(sender);
		
	}
	
	/**
	 * Called to instantiate all subclass Command Nodes. Return
	 * the ones that should be added to the root branch.
	 */
	protected abstract ICommandNode[] addCommands();

	protected abstract void registerChatMessages(ChatSender sender);
	
	protected class ChatMessages {
		
		protected ChatMessagesCommandHelp commandHelp;
		
		private ChatMessages(ChatSender chat) {
			this.commandHelp = new ChatMessagesCommandHelp(chat);
		}
		
	}
	
	protected class ChatMessagesCommandHelp {
		
		protected ChatMessage top;
		protected ChatMessage nodes;
		protected ChatMessage nodeItem;
		protected ChatMessage separator;
		protected ChatMessage separatorLast;
		protected ChatMessage commandOverview;
		
		private ChatMessagesCommandHelp(ChatSender chat) {
			
			top = chat.newChatMessage("gc.tree.cmdHelp.top", "name");
			nodes = chat.newChatMessage("gc.tree.cmdhelp.nodes");
			nodeItem = chat.newChatMessage( "gc.tree.cmdhelp.nodes.item", "node");
			separator = chat.newChatMessage("gc.tree.cmdhelp.nodes.separator");
			separatorLast = chat.newChatMessage("gc.tree.cmdhelp.nodes.separatorLast");
			commandOverview = chat.newChatMessage("gc.tree.cmdhelp.showCmdInfo");
			
		}
		
	}
	
}