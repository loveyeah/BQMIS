/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.constant;

import java.util.ArrayList;
import java.util.List;

import power.ejb.hr.ContractQuery;
import power.ejb.hr.HrJTotalBean;

/**
 * 共通换行类
 * 
 * @author zhaozhijie
 * @version 1.0
 */
public class commUtils {

	/**
	 * 构造函数
	 */
	public commUtils() {

	}

	/**
	 * 重新换行
	 * 
	 * @param oldString
	 *            字符串
	 * @param lineCount
	 *            每行长度
	 * @return newLineString 换行后的数据
	 */
	public static String newLine(String oldString, int lineCount) {

		// 添加<BR>
		if (oldString != null) {
			return addBrByByteLength(oldString, lineCount * 2);
		}
		return "";
	}

	/**
	 * 搜索目标字符串中的给定字符串的个数
	 * 
	 * @param targeStr
	 *            目标字符串
	 * @param selectStr
	 *            给定字符串
	 * @return count 个数
	 */
	public static int countSubStringNumber(String targeStr, String selectStr) {
		
		int count = 0;
		// 目标字符串的非空判断追加
		if ((targeStr != null)&&(!"".equals(targeStr))) {
		int fromIndex = 0;
		while (targeStr.indexOf(selectStr, fromIndex) > 0) {
			count++;
			fromIndex = targeStr.indexOf(selectStr, fromIndex) + 1;
			}
		}
		return count;
	}

	/**
	 * 去掉目标字符串中结尾的<B R>换行符
	 * 
	 * @param oldString
	 *            目标字符串
	 * @return newString 结果字符串
	 */
	public static String removeLastBR(String oldString) {
		String newString = "";
		if (oldString != null && oldString.length() > 3) {
			if (oldString.endsWith(Constant.HTML_CHANGE_LINE)) {
				newString = oldString.substring(0, oldString.length() - 4);
			}else{
				return oldString;
			}
		}
		return newString;
	}

	/**
	 * 检查字符串是否为空
	 * 
	 * @param s
	 *            目标字符串
	 * @return s 结果字符串
	 */
	public static String checkString(String s) {
		if (s != null) {
			return s;
		}
		return "";
	}

	/**
	 * 截取字符串<BR/> 以字节数截取。
	 * 
	 * @param argStr
	 *            被截取文字对象
	 * @param argLength
	 *            指定的字节数
	 * @return 截取结果
	 */
	public static String getSubString(String argStr, int argStart, int argEnd) {

		StringBuffer sbf = new StringBuffer();
		int size = 0;
		int length = argStr.getBytes().length;
		if (argStart <= length) {
			if (argEnd >= length) {
				// 比指定的字符串长
				sbf.append(argStr);
			} else {
				// 比指定的字符串短
				for (int i = argStart; i < length; i++) {
					String str = String.valueOf(argStr.getBytes()[i]);
					byte[] bt = str.getBytes();
					size += bt.length;
					if (size <= argEnd - argStart) {
						sbf.append(argStr.charAt(i));
					} else {
						break;
					}
				}
			}
		}
		return sbf.toString();
	}

	/**
	 * 截取字符串<BR/> 起始位置不指定
	 * 
	 * @param argStr
	 *            被截取文字对象
	 * @param argEnd
	 *            指定的字节数
	 * @return 截取结果
	 */
	public static String getSubString(String argStr, int argEnd) {

		return getSubString(argStr, 0, argEnd);
	}

	/**
	 * 对指定字符串给定长度做添加分割处理
	 * 
	 * @param argStr
	 *            被处理的文字对象
	 * @param count
	 *            指定长度
	 * @param argDevide
	 *            分隔符
	 * @return 截取结果
	 */
	public static String addBrByByteLength(String argStr, int count,
			String argDevide) {

		StringBuffer sbf = new StringBuffer();
		int size = 0;
		int length = argStr.getBytes().length;
		// 比指定的字符串长
		if (count >= length) {
			sbf.append(argStr);
		} else {
			for (int i = 0; i < argStr.length(); i++) {
				String str = String.valueOf(argStr.charAt(i));
				byte[] bt = str.getBytes();
				size += bt.length;
				if (size <= count) {
					sbf.append(argStr.charAt(i));
				} else {
					// 添加分隔符
					sbf.append(argDevide);
					size = 0;
					i -= 1;
				}
			}
		}
		return sbf.toString();
	}

	/**
	 * 对指定字符串给定长度做添加<BR>
	 * 处理
	 * 
	 * @param argStr
	 *            被处理的文字对象
	 * @param count
	 *            指定长度
	 * @return 截取结果
	 */
	public static String addBrByByteLength(String argStr, int count) {
		String strTemp = "";
		String[] arrTemp = argStr.split(Constant.HTML_CHANGE_LINE);
		for (int i = 0; i < arrTemp.length; i++) {

			strTemp += addBrByByteLength(arrTemp[i], count,
					Constant.HTML_CHANGE_LINE);
			if (i != arrTemp.length - 1) {
				strTemp += Constant.HTML_CHANGE_LINE;
			}
		}
		// 添加<BR>
		return strTemp;
	}
	
		/**
	 * 对指定字符串给定长度做添加\r\n
	 * 处理
	 * 
	 * @param argStr
	 *            被处理的文字对象
	 * @param count
	 *            指定长度
	 * @return 截取结果
	 */
	public static String addDBByteLength(String argStr, int count) {
		String strTemp = "";
		if ((argStr != null)&&(!"".equals(argStr))) {
		String[] arrTemp = argStr.split(Constant.DB_CHANGE_LINE);
		for (int i = 0; i < arrTemp.length; i++) {

			strTemp += addBrByByteLength(arrTemp[i], count,
					Constant.DB_CHANGE_LINE);
			if (i != arrTemp.length - 1) {
				strTemp += Constant.DB_CHANGE_LINE;
				}
			}
		}
		// 添加\r\n
		return strTemp;
	}

	/**
	 * 对指定字符串用<BR>
	 * 替换\r\n
	 * 
	 * @param argStr
	 *            被处理的文字对象
	 * @return 替换结果
	 */
	public static String replaceWithBR(String argStr) {
		String strTemp = "";
		String strResult = "";
		if (argStr != null) {
//			// 替换\r\n
//			strTemp = argStr.replace(Constant.DB_CHANGE_LINE,
//					Constant.HTML_CHANGE_LINE);
			// 替换\n
			strResult = argStr.replace(Constant.DB_CHANGE_LINE2,
					Constant.HTML_CHANGE_LINE);
		}
		return strResult;
	}

	/**
	 * 时间格式转换
	 * 
	 * @param argStr
	 *            被处理的时间字符串对象
	 * @return 转化结果
	 */
	public static String formatTime(String argStr) {
		String strResult = argStr;
//		if (argStr != null && argStr.length() >= 4) {
//			// 年
//			strResult = argStr.substring(0, 4) + Constant.YEAR;
//			// 月
//			if (argStr.length() >= 7)
//				strResult += argStr.substring(5, 7) + Constant.MONTH;
//			// 日
//			if (argStr.length() >= 10)
//				strResult += argStr.substring(8, 10) + Constant.DAY;
//			// 时
//			if (argStr.length() >= 13)
//				strResult += argStr.substring(11, 13) + Constant.HOUR;
//			// 分
//			if (argStr.length() >= 16)
//				strResult += argStr.substring(14, 16) + Constant.MINUTE;
//
//		}
		return strResult;
	}

	/**
	 * 对指定字符串集获取其中包含给定字符串的最大个数
	 * 
	 * @param lst
	 *            被处理的字符串集
	 * @param taxStr
	 *            指定的字符串
	 * @return 最大个数
	 */
	public static int countMaxContain(List<String> lst, String taxStr) {
		int maxcount = 0;
		List<Integer> intList = new ArrayList<Integer>();
		if (lst != null && lst.size() > 0) {
			for (int i = 0; i < lst.size(); i++) {
				intList.add(countSubStringNumber(lst.get(i), taxStr));
			}
		}
		if (intList != null && intList.size() > 0) {
			for (int i = 0; i < intList.size(); i++) {
				if (intList.get(i) > maxcount) {
					maxcount = intList.get(i);
				}
			}
		}
		return maxcount;
	}

	/**
	 * 计算字符串占用行数
	 * 
	 * @param argStr
	 *            被处理的文字对象
	 * @param count
	 *            指定一行最大长度
	 * @return 截取结果
	 */
	public static int countLineTotal(String argStr, int count) {
		int intReturn = 0;
		if (argStr != null) {
			intReturn = addBrByByteLength(argStr, count*2).split(
					Constant.HTML_CHANGE_LINE).length;
		}
		return intReturn;
	}
	
	/**
	 * 对指定字符串给定长度做添加<BR>
	 * 处理
	 * 
	 * @param argStr
	 *            被处理的文字对象
	 * @param count
	 *            指定长度
	 * @return 截取结果
	 */
	public static String addBrByByteLengthForMaterial(String argStr, int count) {
		if (argStr != null) {
			return addBrByByteLength(argStr, count, Constant.HTML_CHANGE_LINE);
		}
		else return "";
	}
	
	/**
	 * 时间格式转换
	 * 
	 * @param argStr
	 *            被处理的时间字符串对象
	 * @return 转化结果
	 */
	public static String formatTimeYMD(String argStr) {
		String strResult = "";
		if (argStr != null && argStr.length() >= 4) {
			// 年
			strResult = argStr.substring(0, 4) + Constant.YEAR;
			// 月
			if (argStr.length() >= 7)
				strResult += argStr.substring(5, 7) + Constant.MONTH;
			// 日
			if (argStr.length() >= 10)
				strResult += argStr.substring(8, 10) + Constant.DAY;
		}
		return strResult;
	}
	
	/**
	 * add by liuyi 090923 15:26 
     * 根据字节数截断
     * 
     * @param oldStr
     *            需要截断的字符串
     * @param count 
     *               需要截断的个数          
     * @return 转化结果
     */
    public static String cutByByte(String oldStr,int count){
        int size = 0;
        StringBuffer sbf = new StringBuffer();
        if(oldStr!=null){
            if(oldStr.getBytes().length>count){
                for (int i = 0; i < oldStr.length(); i++){
                    String str = String.valueOf(oldStr.charAt(i));
                    byte[] bt = str.getBytes();
                    size += bt.length;
                    if (size <= count){
                        sbf.append(oldStr.charAt(i));
                    }else{
                        return sbf.toString();
                    }
                }
            }            
        }
        return oldStr;
    }
    
    /**
	  * 将相同员工ID的员工姓名去掉
	  * @param List<ContractQuery> oldList 未处理List
	  * @return List<ContractQuery> newList 处理后List
	  */
	public static List<ContractQuery> delectName(List<ContractQuery> oldList,double pg){
		List<ContractQuery> newList = new ArrayList<ContractQuery>();
		if(oldList!=null&&oldList.size()>0){
			double sumRow = (double)oldList.get(0).getCntRow() + 0.3;
			double countRow = 0;
			double beforeSet = 1;
			double nowSet = 1;
			double pageBreak = pg;
			boolean flag = true;
			String tempId = oldList.get(0).getEmpId();
			for(int i=1;i<oldList.size();i++){
				countRow = oldList.get(i).getCntRow();
				// 前一行所属集合数
				beforeSet = Math.floor(sumRow/pageBreak) + 1;
				// 到此行为止（包括此行）的总行数
				sumRow = sumRow + countRow+0.3;
				// 当前行所属集合数
				nowSet = Math.floor(sumRow/pageBreak) + 1;
				if(nowSet!=beforeSet){
					flag=true;
					sumRow = countRow;
					beforeSet = 1;
					nowSet = 1;
				}else{
					flag=false;
				}
				if(tempId!=null&&tempId.equals(oldList.get(i).getEmpId())&&flag==false){
					ContractQuery lstBean = oldList.get(i);
					lstBean.setChsName("");
				}else{
					tempId = oldList.get(i).getEmpId();
				}
			}
			return oldList;
		}else{
			return newList;
		}
	}
    
    /**
     * add by liuyi 090923 15:26
     * 对指定字符串给定长度做添加"\r\n"处理   考勤添加
     * 
     * @param argStr
     *            被处理的文字对象
     * @param count
     *            指定长度
     * @return 截取结果
     */
    public static String addChangeLineByByteLengthForCA(String argStr, int count) {
        String strTemp = "";
        if (argStr != null) {        	
            String[] arrTemp = argStr.split(Constant.DB_CHANGE_LINE);
            for (int i = 0; i < arrTemp.length; i++) {
                strTemp += addBrByByteLength(arrTemp[i], count,
                        Constant.DB_CHANGE_LINE);
                if (i != arrTemp.length - 1) {
                    strTemp += Constant.DB_CHANGE_LINE;
                }
            }
        }
        return strTemp;
    }
    
    /**
     * add by liuyi 090924 19:30
     * 对指定字符串给定长度做添加<BR>人力资源添加
     * 处理
     * 
     * @param argStr
     *            被处理的文字对象
     * @param count
     *            指定长度
     * @return 截取结果
     */
    public static String addBrByByteLengthForHR(String argStr, int count) {
        String strTemp = "";
        if (argStr != null) {        	
            String[] arrTemp = argStr.split(Constant.DB_CHANGE_LINE);
            for (int i = 0; i < arrTemp.length; i++) {
                strTemp += addBrByByteLength(arrTemp[i], count,
                        Constant.DB_CHANGE_LINE);
                if (i != arrTemp.length - 1) {
                    strTemp += Constant.DB_CHANGE_LINE;
                }
            }
            strTemp=strTemp.replace(Constant.MARK_AND, Constant.MARK_AND_HTML);
            strTemp=strTemp.replace(Constant.MARK_LT, Constant.MARK_LT_HTML);
            strTemp=strTemp.replace(Constant.MARK_GT, Constant.MARK_GT_HTML);
            strTemp=strTemp.replace(Constant.DB_CHANGE_LINE, Constant.HTML_CHANGE_LINE);
            
        }
        // 添加<BR>
        return strTemp;
    }
    
    
    /**
     * add by liuyi 091201
	  * 将相同所属部门去掉
	  * @param List<HrJTotalBean> oldList 未处理List
	  * @return List<HrJTotalBean> newList 处理后List
	  */
	public static List<HrJTotalBean> newDeptName(List<HrJTotalBean> oldList, 
			double pg, List<HrJTotalBean> lstTempList){
		List<HrJTotalBean> newList = new ArrayList<HrJTotalBean>();
		if(oldList!=null&&oldList.size()>0){
			double sumRow = (double)oldList.get(0).getCntRow() + 0.3;
			double countRow = 0;
			double beforeSet = 1;
			double nowSet = 1;
			double pageBreak = pg;
			boolean flag = true;
			for(int i=1;i<oldList.size();i++){
				countRow = oldList.get(i).getCntRow();
				// 前一行所属集合数
				beforeSet = Math.floor(sumRow/pageBreak) + 1;
				// 到此行为止（包括此行）的总行数
				sumRow = sumRow + countRow+0.3;
				// 当前行所属集合数
				nowSet = Math.floor(sumRow/pageBreak) + 1;
				if(nowSet!=beforeSet){
					flag=true;
					sumRow = countRow;
					beforeSet = 1;
					nowSet = 1;
				}else{
					flag=false;
				}
				if(flag){
					HrJTotalBean lstBean = oldList.get(i);
	                // 对所属部门进行换行处理
	                String strDept = addBrByByteLengthForHR(lstTempList.get(i).getDeptName(),
	                		Constant.EIGHTPOINTPERSETY24);
					lstBean.setDeptName(strDept.replaceAll(Constant.BLANK, "&nbsp;"));
				}
			}
			return oldList;
		}else{
			return newList;
		}
	}
}
