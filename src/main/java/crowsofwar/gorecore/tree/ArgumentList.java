package crowsofwar.gorecore.tree;

public class ArgumentList {
	
	private final Object[] argumentValues;
	
	public ArgumentList(String[] userInput, IArgument<?>[] arguments) {
		
		argumentValues = new Object[arguments.length];
		for (int i = 0; i < argumentValues.length; i++) {
			IArgument<?> argument = arguments[i];
			Object out = null;
			if (i < userInput.length) { // If possible, prefer user input over default
				out = argument.convert(userInput[i]);
			} else { // Try to use the default value if the argument is optional
				if (argument.isOptional()) { // Argument has a default value, which can be used
					out = argument.convert(userInput[i]);
				} else { // Argument isn't optional, but user input hasn't been specified. Throw an error.
					throw new TreeCommandException("Non optional argument was not entered", arguments[i].getArgumentName());
				}
			}
			argumentValues[i] = out;
		}
		
	}
	
	public <T> T get(IArgument<T> argument) {
		return (T) argumentValues;
	}
	
}
