package crowsofwar.gorecore.tree;

public interface ITypeConverter<T> {
	
	public static final ITypeConverter<Integer> CONVERTER_INTEGER = new ITypeConverter<Integer>() {
		@Override
		public Integer convert(String str) {
			try {
				return Integer.parseInt(str);
			} catch (Exception e) {
				throw new TreeCommandException("Could not convert argument to integer", str);
			}
		}
	};
	
	public static final ITypeConverter<Float> CONVERTER_FLOAT = new ITypeConverter<Float>() {
		@Override
		public Float convert(String str) {
			try {
				return Float.parseFloat(str);
			} catch (Exception e) {
				throw new TreeCommandException("Could not convert argument to float", str);
			}
		}
	};
	
	public static final ITypeConverter<Double> CONVERTER_DOUBLE = new ITypeConverter<Double>() {
		@Override
		public Double convert(String str) {
			try {
				return Double.parseDouble(str);
			} catch (Exception e) {
				throw new TreeCommandException("Could not convert argument to double", str);
			}
		}
	};
	
	public static final ITypeConverter<Long> CONVERTER_LONG = new ITypeConverter<Long>() {
		@Override
		public Long convert(String str) {
			try {
				return Long.parseLong(str);
			} catch (Exception e) {
				throw new TreeCommandException("Could not convert argument to long", str);
			}
		}
	};
	
	public static final ITypeConverter<Short> CONVERTER_SHORT = new ITypeConverter<Short>() {
		@Override
		public Short convert(String str) {
			try {
				return Short.parseShort(str);
			} catch (Exception e) {
				throw new TreeCommandException("Could not convert argument to short", str);
			}
		}
	};
	
	public static final ITypeConverter<Boolean> CONVERTER_BOOLEAN = new ITypeConverter<Boolean>() {
		@Override
		public Boolean convert(String str) {
			return Boolean.parseBoolean(str);
		}
	};
	
	public static final ITypeConverter<Byte> CONVERTER_BYTE = new ITypeConverter<Byte>() {
		@Override
		public Byte convert(String str) {
			try {
				return Byte.parseByte(str);
			} catch (Exception e) {
				throw new TreeCommandException("Could not convert argument to byte", str);
			}
		}
	};
	
	public static final ITypeConverter<Character> CONVERTER_CHAR = new ITypeConverter<Character>() {
		@Override
		public Character convert(String str) {
			return str.charAt(0);
		}
	};
	
	public static final ITypeConverter<String> CONVERTER_STRING = new ITypeConverter<String>() {
		@Override
		public String convert(String str) {
			return str;
		}
	};
	
	T convert(String str);
	
}
