package crowsofwar.gorecore.modular;

public interface ICommandModule {
	
	ICommandModule execute(CommandCall call);
	
	boolean needsOpPermission();
	
}
