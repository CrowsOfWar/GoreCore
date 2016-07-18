package crowsofwar.gorecore.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import crowsofwar.gorecore.tree.TreeCommandException.Reason;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;

public abstract class TreeCommand implements ICommand {
	
	private NodeBranch branchRoot;
	private final ChatSender chatSender;
	
	public TreeCommand() {
		branchRoot = new NodeBranch(getCommandName(), addCommands());
		this.chatSender = new ChatSender();
		registerChatMessages(chatSender);
		
		chatSender.registerChatMessage("commandHelp.top", "gc.tree.cmdhelp.top", "name");
		chatSender.registerChatMessage("commandHelp.nodes", "gc.tree.cmdhelp.nodes");
		chatSender.registerChatMessage("commandHelp.nodes.item", "gc.tree.cmdhelp.nodes.item", "node");
		chatSender.registerChatMessage("commandHelp.nodes.separator", "gc.tree.cmdhelp.nodes.separator");
		chatSender.registerChatMessage("commandHelp.nodes.separatorLast", "gc.tree.cmdhelp.nodes.separatorLast");
		
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
			
			CommandCall call = new CommandCall(sender, arguments, chatSender);
			
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
		chat.send(sender, "commandHelp.top", getCommandName());
		
		List<String> listKeys = new ArrayList<String>();
		List<Object[]> listArgs = new ArrayList<Object[]>();
		listKeys.add("commandHelp.nodes");
		listArgs.add(new Object[0]);
		
		ICommandNode[] allNodes = branchRoot.getSubNodes();
		for (int i = 0; i < allNodes.length; i++) {
			listKeys.add("commandHelp.nodes.item");
			listArgs.add(new Object[] { allNodes[i].getNodeName() });
			if (i < allNodes.length - 1) {
				listKeys.add("commandHelp.nodes.separator" + (i == allNodes.length - 2 ? "Last" : ""));
				listArgs.add(new Object[0]);
			}
		}
		chat.send(sender, listKeys, listArgs);
		
	}
	
	/**
	 * Called to instantiate all subclass Command Nodes. Return
	 * the ones that should be added to the root branch.
	 */
	protected abstract ICommandNode[] addCommands();

	protected abstract void registerChatMessages(ChatSender sender);
	
}