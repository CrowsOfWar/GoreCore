package crowsofwar.gorecore.tree;

import net.minecraft.command.CommandException;

public class TreeCommandException extends RuntimeException {

	private final String message;
	private final Object[] format;
	
	public TreeCommandException(String message, Object... format) {
		super(message);
		this.message = message;
		this.format = format;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Object[] getFormattingArgs() {
		return format;
	}
	
}
