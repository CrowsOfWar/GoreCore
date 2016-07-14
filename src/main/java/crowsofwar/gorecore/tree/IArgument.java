package crowsofwar.gorecore.tree;

public interface IArgument<T> {
	
	T getFrom(ArgumentList list);
	
	boolean isOptional();
	
	T getDefaultValue();
	
	T convert(String input);
	
	String getArgumentName();
	
}
