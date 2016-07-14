package crowsofwar.gorecore.tree;

public interface IArgument<T> {
	
	boolean isOptional();
	
	T getDefaultValue();
	
	T convert(String input);
	
	String getArgumentName();
	
}