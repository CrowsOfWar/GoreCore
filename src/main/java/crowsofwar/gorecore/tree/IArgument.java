package crowsofwar.gorecore.tree;

public interface IArgument<T> {
	
	T getFrom(ArgumentList list);
	
	boolean isOptional();
	
}
