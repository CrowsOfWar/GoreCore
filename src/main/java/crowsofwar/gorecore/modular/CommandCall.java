package crowsofwar.gorecore.modular;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListOpsEntry;

public class CommandCall {
	
	private ICommandSender from;
	private boolean isOp;
	private String[] passedArgs;
	
	public CommandCall(ICommandSender from, String[] passedArgs) {
		this.from = from;
		this.passedArgs = passedArgs;
		
		if (from instanceof CommandBlockLogic) {
			isOp = true;
		} else if (from instanceof MinecraftServer) {
			isOp = true;
		} else if (from instanceof RConConsoleSource) {
			isOp = true;
		} else if (from instanceof EntityPlayer) {
			isOp = false;
			if (from instanceof EntityPlayerMP) {
				EntityPlayerMP player = (EntityPlayerMP) from; // TODO needs testing
				isOp = player.mcServer.getConfigurationManager().func_152596_g(player.getGameProfile());
			}
		}
		
		
		
	}
	
	public ArgumentList loadArguments(IArgument<?>... arguments) {
		return null;
	}
	
	public boolean isOpped() {
		return isOp;
	}
	
}
