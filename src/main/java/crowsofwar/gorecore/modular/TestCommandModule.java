package crowsofwar.gorecore.modular;

public class TestCommandModule implements ICommandModule {

	private IArgument<String> argA;
	private IArgument<Integer> argB;
	
	@Override
	public ICommandModule execute(CommandCall call) {
		
		ArgumentList args = call.loadArguments(argA, argB);
		String a = args.get(argA);
		Integer b = args.get(argB);
		System.out.println(b + " " + a + "s"); // e.g. '5 pineapples'
		
		return null;
	}

	@Override
	public boolean needsOpPermission() {
		return false;
	}

}
