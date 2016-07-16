package crowsofwar.gorecore.tree;

public class ArgumentOptions<T> implements IArgument<T> {
	
	private final T[] options;
	private T defaultValue;
	private final ITypeConverter<T> convert;
	private final String name;
	
	public ArgumentOptions(ITypeConverter<T> convert, String name, T... options) {
		this.options = options;
		this.defaultValue = null;
		this.convert = convert;
		this.name = name;
	}
	
	public ArgumentOptions setOptional(T defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}
	
	@Override
	public boolean isOptional() {
		return defaultValue != null;
	}
	
	@Override
	public T getDefaultValue() {
		return defaultValue;
	}
	
	@Override
	public T convert(String input) {
		return convert.convert(input);
	}
	
	@Override
	public String getArgumentName() {
		return name;
	}
	
	@Override
	public String getHelpString() {
		String help = isOptional() ? "[" : "<";
		for (int i = 0; i < options.length; i++) {
			help += (i == 0 ? "" : "|") + options[i].toString();
		}
		help += isOptional() ? "]" : ">";
		return help;
	}
	
	@Override
	public String getSpecificationString() {
		String start = isOptional() ? "[" : "<";
		String end = isOptional() ? "]" : ">";
		return start + getArgumentName() + end;
	}
	
}
