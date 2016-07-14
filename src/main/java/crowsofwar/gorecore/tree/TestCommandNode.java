package crowsofwar.gorecore.tree;

public class TestCommandNode implements ICommandNode {

	private IArgument<String> argA;
	private IArgument<Integer> argB;
	
	@Override
	public ICommandNode execute(CommandCall call) {
		
		ArgumentList args = call.popArguments(argA, argB);
		String a = args.get(argA);
		Integer b = args.get(argB);
		System.out.println(b + " " + a + "s"); // e.g. '5 pineapples'
		
		return null;
	}

	@Override
	public boolean needsOpPermission() {
		return false;
	}

	@Override
	public String getNodeName() {
		return "test";
	}

}
