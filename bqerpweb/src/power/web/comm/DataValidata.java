package power.web.comm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidata {
	// check 类型 begin
	public static String TYPE_CHECK = "type";

	public static String LENGTH_CHECK = "length";

	public static String EMPTY_CHECK = "empty";

	public static String KEY_CHECK = "key";

	public static String MAX_CHECK = "maxValue";

	public static String MIN_CHECK = "minValue";

	public static String DB_CHECK = "dbCheck";

	public static String CODE_CHECK = "codeCheck";
	// check 类型 End

	// 类型 begin
	public static String INTEGER_TYPE = "int";

	public static String STRING_TYPE = "string";

	public static String DOUBLE_TYPE = "double";

	// 类型 end

	// 常量YES
	public static String YES_CONSTANT = "yes";

	// 常量 NO
	public static String NO_CONSTANT = "no";

	// 常量method
	public static String METHOD_CONSTANT = "method";

	// 常量 class
	public static String CLASS_CONSTANT = "class";

	// 类型check
	public static boolean checkType(String type, String value) {
		if (value == null) {
			return true;
		}
		try {
			switch (type.charAt(0)) {
			case 'i':
				Integer.parseInt(value);
				return true;
			case 's':
				return true;
			case 'd':
				Double.parseDouble(value);
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	// 长度check
	public static boolean checkLength(String length, String value) {
		if (value == null) {
			return true;
		}
		try {
			int intLength = Integer.parseInt(length);
			if (value.length() > intLength)
				return false;
		} catch (Exception e) {
			System.out.println("config.xml 有错误");
		}
		return true;
	}

	// 非空check
	public static boolean checkEmpty(String empty, String value) {
		if (empty == null || "".equals(empty)) {
			return true;
		}

		// 不能为空的时候
		if (empty.equalsIgnoreCase(YES_CONSTANT)) {
			if (value == null || "".equals(value)) {
				return false;
			}
		}
		return true;
	}

	// max值check
	public static boolean checkMax(String maxValue, String value) {
		try {
			double tempMaxValue = Double.parseDouble(maxValue);
			double tempValue = Double.parseDouble(value);
			if (tempMaxValue >= tempValue) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	// min值check
	public static boolean checkMin(String minValue, String value) {
		try {
			double tempMinValue = Double.parseDouble(minValue);
			double tempValue = Double.parseDouble(value);
			if (tempMinValue <= tempValue) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	// 半角英数字的check
	public static boolean checkHalf(String half, String value) {
		Pattern pattern = Pattern.compile("^[a-zA-Z_0-9]*$");
		Matcher matcher = pattern.matcher(value);
		boolean matResult = matcher.matches();
		if(value == null){
			return true;
		}
		
		if (half == NO_CONSTANT || half == null) {
			return true;
		} else {
			if (!matResult)
				return false;
		}
		return true;
	}
}
