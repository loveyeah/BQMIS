/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.comm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.googlecode.jsonplugin.JSONException;

/**
 * excel文件读取
 * 
 * @author qhhu
 * 
 */
public class ReadXls {
	/** 文件名 */
	private String strFileName = "";
	/** xls文件的数据 */
	private List<HashMap<String, String>> xlsData = null;
	/** xls文件的sheet位置 */
	private int sheetNo = 0;
	/** 数据对应业务ID */
	private String businessID = null;
	/** property文件 */
	public static Properties p = null;
	public String keyOfAll = null;
	private InputStream inp;
	/** hidden常量 */
	private static final String HIDDEN_MARK = "hidden";
	/** colomn常量 */
	private static final String COLOMN = "colomn";
	/** 字符连接常量 */
	private static final String LINE_DEVIDE = "_";
	/** 隐藏列的默认值常量 */
	private String defaultVlaue = "";
	/** 字符分割常量 */
	private static final String STRING_DEVIDE = ",";
	/** 读文件开始行（默认第二行） */
	private static final int READ_START = 1;
	// 数字类型的cell
	private static final int CELL_TYPE_NUMERIC = 0;

	// 字符类型的cell
	private static final int CELL_TYPE_STRING = 1;

	// 空类型的cell
	private static final int CELL_TYPE_BLANCK = 2;

	// 布尔类型的cell
	private static final int CELL_TYPE_BOOLEAN = 4;

	// 数字类型 Begin
	private final static String INT_TYPE = "int";
	private final static String LONG_TYPE = "long";
	private final static String DOUBLE_TYPE = "double";
	private static final int New = 0;

	// 数字类型 End

	public ReadXls() {

	}

	/**
	 * 构造函数
	 * 
	 * @param strFileName
	 *            取入的文件名
	 * @param colomnNo
	 *            文件的列数
	 */
	public ReadXls(String strFileName, String businessID) {
		this.strFileName = strFileName;
		this.businessID = businessID;
	}

	public int getSheetNo() {
		return sheetNo;
	}

	public void setSheetNo(int sheetNo) {
		this.sheetNo = sheetNo;
	}

	public String getStrFileName() {
		return strFileName;
	}

	public void setStrFileName(String strFileName) {
		this.strFileName = strFileName;
	}

	public List<HashMap<String, String>> getXlsData() {
		return xlsData;
	}

	public void setXlsData(List<HashMap<String, String>> xlsData) {
		this.xlsData = xlsData;
	}

	public String getBusinessID() {
		return businessID;
	}

	public void setBusinessID(String businessID) {
		this.businessID = businessID;
	}

	public String getDefaultVlaue() {
		return defaultVlaue;
	}

	public void setDefaultVlaue(String defaultVlaue) {
		this.defaultVlaue = defaultVlaue;
	}

	private Map<String, String> readFile() {
		Map<String, String> error = new HashMap<String, String>();
		// 文件名为空
		if (strFileName == null || strFileName == "") {
			error.put("errorType", Constants.FILE_NOT_FOUND);
			return error;
		}
		// 文件后缀检查
		String[] names = strFileName.split(".");
		if (names.length > 1) {
			// 不是excel文件
			if (!"xls".equalsIgnoreCase(names[names.length - 1])) {
				error.put("errorType", Constants.FILE_NOT_RIGHT);
				return error;
			}
		}

		// 读取文件
		try {
			inp = new FileInputStream(strFileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			error.put("errorType", Constants.FILE_NOT_FOUND);
			return error;
		}
		return null;
	}

	public Map<String, String> ValidateXlsData() {
		Map<String, String> validateResult = null;
		validateResult = readFile();
		if (validateResult != null && validateResult.size() > 0) {
			return validateResult;
		}

		try {
			return validateXlsData(inp);
		} catch (Exception e) {
			validateResult = new HashMap<String, String>();
			validateResult.put("errorType", Constants.FILE_CONTENT_NOT_RIGHT);
			return validateResult;
		}
	}

	/**
	 * 
	 * @param i
	 *            文件流
	 * @return Map<String,String> 错误信息
	 * @throws IOException
	 *             流异常
	 * @throws ClassNotFoundException
	 *             类未找到异常
	 * @throws NoSuchMethodException
	 *             方法未找到异常
	 * @throws SecurityException
	 *             安全异常
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private Map<String, String> validateXlsData(InputStream is)
			throws IOException, ClassNotFoundException, SecurityException,
			NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		// 数据存在flg
		boolean dataIsExist = false;

		// 配置的xml文件bean
		XlsConfigBean config = XlsConfigParser.getXlsConfigBean();

		// 对应的业务的config内容bean
		XlsConfigBean.Excel excel = config.getExcel(businessID);

		// excel的book对象
		HSSFWorkbook workbook = new HSSFWorkbook(is);

		// book对象的sheet对象
		HSSFSheet sheet = workbook.getSheetAt(sheetNo);

		// 获取最后列的号码
		int lastRow = sheet.getLastRowNum();

		if (lastRow < 1) {
			return getErrorMessage("0", "1", Constants.FILE_NO_DATA);
		}
		// 文件总行数
		int rowCount = sheet.getLastRowNum() + 1;

		// 数据容器
		List<HashMap<String, String>> arrData = new ArrayList<HashMap<String, String>>(
				rowCount);

		// 对应的业务的属性列
		List<XlsConfigBean.Excel.Column> cols = excel.getColumns();
		int colNum = cols.size();

		// 对应的存在性check的对象
		List<XlsConfigBean.Excel.Check> chks = excel.getChecks();

		// 获取数据并且进行check
		for (int r = 1; r <= lastRow; r++) {

			Map<String, String> rowData = new HashMap<String, String>();
			// 对应行的全部数据
			HSSFRow row = sheet.getRow(r);

			for (short c = 0; c < colNum; c++) {

				// 获取对应的列的配置信息
				XlsConfigBean.Excel.Column column = (XlsConfigBean.Excel.Column) cols
						.get(c);
				// 获取对应列的内容
				HSSFCell cell = row.getCell(c);

				if (cell == null || cell.getCellType() == CELL_TYPE_BLANCK) {
					// 非空的check为false
					if (!DataValidata.checkEmpty(column.getEmpty(), null)) {
						return getErrorMessage(Integer.toString(r), column
								.getName(), Constants.IS_EMPTY);
					} else {
						// 追加数据
						rowData.put(column.getId(), null);
					}

				} else {
					switch (cell.getCellType()) {
					// 数字
					case CELL_TYPE_NUMERIC:
						// cell的内容
						String temp = new DecimalFormat("#.#####").format(
								cell.getNumericCellValue()).toString();
						// 验证结果
						String resultNum = NumeriCellCheck(column, temp);
						// 验证失败的时候
						if (resultNum != null) {
							return getErrorMessage(Integer.toString(r), column
									.getName(), resultNum);
						}
						// 验证成功的时候，数据处理
						setNumField(rowData, column, temp);
						break;

					// 字符
					case CELL_TYPE_STRING:
						// cell的内容
						String value = cell.getStringCellValue().trim();
						// 验证结果
						String resultString = StringCellCheck(column, value);
						if (resultString != null) {
							return getErrorMessage(Integer.toString(r), column
									.getName(), resultString);
						}
						// 验证成功的时候，数据设置
						rowData.put(column.getId(), value);
						break;
					// boolean
					case CELL_TYPE_BOOLEAN:
						rowData.put(column.getId(), new Boolean(cell
								.getBooleanCellValue()).toString());
						break;
					// 其他的设置为null(公式之类的异常数据)
					default:
						if (!DataValidata.checkEmpty(column.getEmpty(), null)) {
							return getErrorMessage(Integer.toString(r), column
									.getName(), Constants.IS_EMPTY);
						} else {
							// 追加数据
							rowData.put(column.getId(), null);
						}
					}
				}

			}

			// 追加数据
			arrData.add((HashMap<String, String>) rowData);
			dataIsExist = true;
		}

		// 数据本身的key的重复性check
		List<String> keys = getKeys(cols);
		keyOfAll = getKeyString(cols);

		Map<String, String> repeatCheckResult = keyRepeatCheck(arrData, keys);
		if (repeatCheckResult != null) {
			return repeatCheckResult;
		}

		// ID和name的hashMap(性质非法check消息用)
		HashMap<String, String> keyValue = getKeyValue(cols);

		// DB中的存在性check和code定义书中的存在性check
		if (chks != null && dataIsExist) {
			for (int i = 0; i < chks.size(); i++) {

				// 获取当前的check
				XlsConfigBean.Excel.Check chk = (XlsConfigBean.Excel.Check) chks
						.get(i);
				// 获取当前check的方法
				String method = chk.getMethod();
				// 获取当前check的参数
				String args = chk.getArgs();
				String[] aa = null;
				if (args != null) {
					aa = args.split(",");
				}

				// 获取当前类的定义
				Class<?> cls = Class.forName(chk.getClazz());
				// 获取当前方法
				Method mtd = cls.getMethod(method, String[].class);
				// 当前class的实例
				Object instance = cls.newInstance();

				for (int j = 0; j < arrData.size(); j++) {
					// 参数数组
					String[] params = null;
					String namesInfo = null;
					// 参数获取
					if (aa != null) {
						params = new String[aa.length];
						for (int w = 0; w < aa.length; w++) {
							params[w] = arrData.get(j).get(aa[w]);
							if (namesInfo == null) {
								namesInfo = keyValue.get(aa[w]);
							} else {
								namesInfo += "," + keyValue.get(aa[w]);
							}
						}
					}

					// 调用方法(返回错误类型)
					Object erroType = mtd.invoke(instance,
							new Object[] { params });

					// 当有错误的时候
					if (erroType != null) {
						return getErrorMessage(Integer.toString(j + 1),
								namesInfo, erroType.toString());
					}

				}

			}

		}

		// 隐藏列的默认值的设定
		XlsConfigBean.Excel.HiddenColumn hidColumn = excel.getHiddenColumn();
		if (hidColumn != null) {
			String hiddenColumn = excel.getHiddenColumn().getId();
			// 有隐藏列的时候
			if (hiddenColumn != null && !"".equals(hiddenColumn)) {
				// 有数据的时候
				if (arrData.size() > 0) {
					setHiddenValue(arrData, hiddenColumn);
				}
			}
		}

		// 设置xlsData内容
		setXlsData(arrData);
		return null;
	}

	/**
	 * 获取check中Name
	 * 
	 * @param keyValue
	 *            所有的列的Id和Name
	 * @return Name的字符串
	 */
	private String getNameString(HashMap<String, String> keyValue) {
		if (keyValue == null) {
			return null;
		}

		Set<String> nameSet = keyValue.keySet();
		Iterator<String> nameIte = nameSet.iterator();
		String names = null;
		while (nameIte.hasNext()) {
			if (names == null) {
				names = keyValue.get(nameIte.next());
			} else {
				names += "," + keyValue.get(nameIte.next());
			}
		}
		return names;
	}

	/**
	 * 获取ID和Name的hashMap
	 * 
	 * @param cols
	 *            所有的列
	 * @return ID和Name的hashMap
	 */
	private HashMap<String, String> getKeyValue(
			List<XlsConfigBean.Excel.Column> cols) {
		// 当没有列的时候，返回null
		if (cols == null) {
			return null;
		}

		HashMap<String, String> keyValue = new HashMap<String, String>();
		for (int i = 0; i < cols.size(); i++) {
			keyValue.put(cols.get(i).getId(), cols.get(i).getName());
		}

		return keyValue;
	}

	/**
	 * 把所有的key用逗号连接起来
	 * 
	 * @param keys
	 *            所有的key
	 * @return String key的连接结果
	 */
	private String getKeyString(List<XlsConfigBean.Excel.Column> cols) {
		// 空值的时候
		if (cols == null) {
			return null;
		}

		List<String> keys = new ArrayList<String>();

		for (int i = 0; i < cols.size(); i++) {
			XlsConfigBean.Excel.Column col = cols.get(i);
			// 对应的column为key时
			if (col.getKey() != null
					&& DataValidata.YES_CONSTANT.equalsIgnoreCase(col.getKey())) {
				keys.add(col.getName());
			}
		}
		// key的遍历器
		Iterator<String> keyIter = keys.iterator();
		StringBuffer sbKey = new StringBuffer();
		while (keyIter.hasNext()) {
			// 当前key
			String tempKey = (String) keyIter.next();
			// 第一个key的时候
			if (sbKey.length() == 0) {
				sbKey.append(tempKey);
			}
			// 第一个key之后
			else {
				sbKey.append("," + tempKey);
			}
		}
		return sbKey.toString();
	}

	/**
	 * excel文件中的key重复check
	 * 
	 * @param arrData
	 *            excel文件内容
	 * @param cols
	 *            列的配置信息
	 * @return String 错误类型
	 */
	private Map<String, String> keyRepeatCheck(
			List<HashMap<String, String>> arrData, List<String> keys) {
		for (int i = 0; i < arrData.size(); i++) {
			for (int j = i + 1; j < arrData.size(); j++) {
				// key的遍历器
				Iterator<String> keyIter = keys.iterator();
				boolean isSame = true;
				if (keys.size() == 0) {
					return null;
				}
				while (keyIter.hasNext()) {
					// 当前key
					String tempKey = (String) keyIter.next();

					if (!arrData.get(i).get(tempKey).equals(
							arrData.get(j).get(tempKey))) {
						isSame = false;
						break;
					}
				}

				// 两条记录key重复
				if (isSame) {
					Map<String, String> error = new HashMap<String, String>();
					error.put("row1No", Integer.toString(i + 2));
					error.put("rwo2No", Integer.toString(j + 2));
					error.put("errorType", Constants.SELF_REPEAT);
					error.put("keys", keyOfAll);
					return error;
				}

			}
		}
		return null;
	}

	/**
	 * 获取配置文件中的key列
	 * 
	 * @param cols
	 *            列的配置信息
	 * @return String[] 所有的key的属性名
	 */
	private List<String> getKeys(List<XlsConfigBean.Excel.Column> cols) {
		// 空值的时候
		if (cols == null) {
			return null;
		}

		List<String> keys = new ArrayList<String>();

		for (int i = 0; i < cols.size(); i++) {
			XlsConfigBean.Excel.Column col = cols.get(i);
			// 对应的column为key时
			if (col.getKey() != null
					&& DataValidata.YES_CONSTANT.equalsIgnoreCase(col.getKey())) {
				keys.add(col.getId());
			}
		}

		return keys;
	}

	/**
	 * stringcell中的内容check
	 * 
	 * @param column
	 *            验证的column信息
	 * @param value
	 *            cell的内容
	 * @return String 错误类型
	 */
	private String StringCellCheck(XlsConfigBean.Excel.Column column,
			String value) {
		// 必须输入check
		if (column.getEmpty() != null) {
			if (column.getEmpty().equalsIgnoreCase(DataValidata.YES_CONSTANT)) {
				if (value == null || "".equals(value)) {
					return Constants.IS_EMPTY;
				}
			}
		}

		// 数字check
		if (INT_TYPE.equalsIgnoreCase(column.getType())
				|| LONG_TYPE.equalsIgnoreCase(column.getType())
				|| DOUBLE_TYPE.equalsIgnoreCase(column.getType())) {
			try {
				Double.parseDouble(value);
			} catch (Exception e) {
				return Constants.IS_NUMBER;
			}
			return Constants.IS_NUMBER;
		}

		// int和long型的check
		Pattern pattern = Pattern.compile("^\\d+\\.0$");
		Matcher matcher = pattern.matcher(value);
		boolean matResult = matcher.matches();
		if (INT_TYPE.equalsIgnoreCase(column.getType())
				|| LONG_TYPE.equalsIgnoreCase(column.getType())) {
			// 不是以.0结尾
			if (!matResult) {
				return Constants.FLOART_TYPE;
			}
		}

		// 长度check
		if (column.getLength() != null && !"".equals(column.getLength())) {
			if (!DataValidata.checkLength(column.getLength(), value))
				return Constants.IS_LENGTH;
		}

		// 最大值的check
		if (column.getMaxValue() != null && !"".equals(column.getMaxValue())) {
			if (!DataValidata.checkMax(column.getMaxValue(), value))
				return Constants.MAX_TYPE;
		}

		// 最小值的check
		if (column.getMinValue() != null && !"".equals(column.getMinValue())) {
			if (!DataValidata.checkMin(column.getMinValue(), value))
				return Constants.MIN_TYPE;
		}

		// 半角英数字check
		if (column.getHalf() != null && !"".equals(column.getHalf())) {
			if (!DataValidata.checkHalf(column.getHalf(), value))
				return Constants.IS_HALF;
		}

		return null;
	}

	/**
	 * 设置numcell的内容
	 * 
	 * @param rowdata
	 *            操作的行对象
	 * @param column
	 *            列配置信息
	 * @param value
	 *            值
	 */
	private void setNumField(Map<String, String> rowdata,
			XlsConfigBean.Excel.Column column, String value) {
		Pattern pattern = Pattern.compile("^\\d+\\.0$");
		Matcher matcher = pattern.matcher(value);
		boolean matResult = matcher.matches();
		// 除去结尾.0
		if (matResult) {
			rowdata.put(column.getId(), value.substring(0, value.length() - 2));
		} else {
			rowdata.put(column.getId(), value);
		}

	}

	/**
	 * 去除.0的数字
	 * 
	 * @param value
	 *            值
	 */
	private String operateNumFieldValue(String value) {
		Pattern pattern = Pattern.compile("^\\d+\\.0$");
		Matcher matcher = pattern.matcher(value);
		boolean matResult = matcher.matches();
		// 除去结尾.0
		if (matResult) {
			return value.substring(0, value.length() - 2);
		} else {
			return value;
		}

	}

	/**
	 * 组装错误message
	 * 
	 * @param rowNo
	 *            出错的行数
	 * @param colomnName
	 *            出错的列名
	 * @param errorType
	 *            错误类型
	 * @return String[] 错误信息
	 */
	private Map<String, String> getErrorMessage(String rowNo,
			String colomnName, String errorType) {

		int rowNO = new Integer(rowNo) + 1;
		Map<String, String> error = new HashMap<String, String>();
		error.put("rowNo", Integer.toString(rowNO));
		error.put("colomnName", colomnName);
		error.put("errorType", errorType);
		return error;
	}

	/**
	 * Numericell中的内容check
	 * 
	 * @param column
	 *            验证的column信息
	 * @param value
	 *            cell的内容
	 * @return String 错误类型
	 */
	private String NumeriCellCheck(XlsConfigBean.Excel.Column column,
			String value) {

		// 类型的check(不是数字类型（int,long,double）)
		if (!INT_TYPE.equalsIgnoreCase(column.getType())
				&& !LONG_TYPE.equalsIgnoreCase(column.getType())
				&& !DOUBLE_TYPE.equalsIgnoreCase(column.getType())) {
			return Constants.IS_STRING;
		}

		// 最大值的check
		if (column.getMaxValue() != null && !"".equals(column.getMaxValue())) {
			if (!DataValidata.checkMax(column.getMaxValue(), value))
				return Constants.MAX_TYPE;
		}

		// 最小值的check
		if (column.getMinValue() != null && !"".equals(column.getMinValue())) {
			if (!DataValidata.checkMin(column.getMinValue(), value))
				return Constants.MIN_TYPE;
		}

		Pattern pattern = Pattern.compile("^\\d+(\\.0)?$");
		Matcher matcher = pattern.matcher(value);
		boolean matResult = matcher.matches();
		
		// 有效的整数的check
		if (INT_TYPE.equalsIgnoreCase(column.getType())
				|| LONG_TYPE.equalsIgnoreCase(column.getType())){
			if(!matResult){
				return Constants.IS_NOT_INTEGER;
			}
		}
				
		// 长度check
		if (INT_TYPE.equalsIgnoreCase(column.getType())
				|| LONG_TYPE.equalsIgnoreCase(column.getType())) {
			if (column.getLength() != null && !"".equals(column.getLength())) {
				if (!DataValidata.checkLength(column.getLength(),
						operateNumFieldValue(value)))
					return Constants.IS_LENGTH;
			}
		} else {
			if (column.getLength() != null && !"".equals(column.getLength())) {
				if (!DataValidata.checkLength(column.getLength(), value))
					return Constants.IS_LENGTH;
			}
		}

		return null;
	}

	/**
	 * 隐藏列的默认值null的设置
	 * 
	 * @param arrData
	 *            修改前的list
	 * @param hiddenContext
	 *            隐藏列内容
	 */
	private void setHiddenValue(List<HashMap<String, String>> arrData,
			String hiddenContext) {
		String[] hiddenNames = getAllPoperties(hiddenContext);
		for (int i = 0; i < arrData.size(); i++) {
			for (int j = 0; j < hiddenNames.length; j++) {
				arrData.get(i).put(hiddenNames[j], defaultVlaue);
			}
		}
	}

	/**
	 * 隐藏列的拆分
	 * 
	 * @return String[] 拆分结果
	 */
	private String[] getAllPoperties(String names) {
		// 不存在时返回null
		if (names == null) {
			return null;
		}
		return names.split(STRING_DEVIDE);
	}
}
