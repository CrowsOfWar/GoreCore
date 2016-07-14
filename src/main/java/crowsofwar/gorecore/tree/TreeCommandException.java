package crowsofwar.gorecore.tree;

import net.minecraft.command.CommandException;

public class TreeCommandException extends RuntimeException {

	private final Reason reason;
	private final Object[] format;
	
	public TreeCommandException(Reason reason, Object... format) {
		super(reason.getMessage());
		this.reason = reason;
		this.format = format;
	}
	
	public String getMessage() {
		return reason.getMessage();
	}
	
	public Object[] getFormattingArgs() {
		return format;
	}
	
	public static enum Reason {
		ARGUMENT_MISSING("gc.tree.error.missingArgs"),
		NO_BRANCH_NODE("gc.tree.error.noBranchNode"),
		CANT_CONVERT("gc.tree.error.cantConvert");
		
		private final String message;
		
		private Reason(String message) {
			this.message = message;
		}
		
		public String getMessage() {
			return message;
		}
		
	}
	
}
