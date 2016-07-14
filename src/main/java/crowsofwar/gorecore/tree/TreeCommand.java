package crowsofwar.gorecore.tree;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;

public abstract class TreeCommand implements ICommand {
	
	protected ICommandNode branchRoot;
	
	public TreeCommand() {
		addCommands();
	}
	
	@Override
	public int compareTo(Object o) {
		return 0;
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "No can do"; // TODO Support help with tree commands
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
			String[] options = {};
			if (hasOptions) {
				options = allOptions.substring(2).split(",");
				arguments = Arrays.copyOfRange(arguments, 0, arguments.length - 1);
			}
			
			CommandCall call = new CommandCall(sender, arguments);
			
			ICommandNode node = branchRoot;
			while (node != null) {
				
				if (hasOptions && options[0].equals("help")) {
					if (node instanceof NodeBranch) {
						if (node == branchRoot) {
							sender.addChatMessage(new ChatComponentTranslation("gc.tree.help", getCommandUsage(sender)));
							node = null;
						} else {
							node = node.execute(call);
						}
					} else {
						sender.addChatMessage(new ChatComponentTranslation("gc.tree.help", node.getHelp()));
						node = null;
					}
				} else {
					node = node.execute(call);
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
	
	/**
	 * Called to initialize the root branch
	 */
	protected abstract void addCommands();

}