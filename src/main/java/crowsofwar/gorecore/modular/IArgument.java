package crowsofwar.gorecore.modular;

public interface IArgument<T> {
	
	T getFrom(ArgumentList list);
	
	boolean isOptional();
	
}
