/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.ejb.administration.comm;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 编码生成处理
 * 
 * @author 谭晓苏
 */
@Stateless
public class CodeCommonFacade implements CodeCommonFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/** 日期格式 */
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	/** 填充字符串 */
	private static final String PAD_CHAR_0 = "0";
	/** 填充字符串 */
	private static final String CHAR_SUB = "-";
	/** 工作项目编码长度5位 */
	private static final int WORKITEMCODE_LENGTH = 5;
	/** 工作项目编码 */
	private static final String WORKITEMCODE_PRE = "W";
	/** 菜谱编码长度5位 */
	private static final int MENUCODE_LENGTH = 5;
	/** 菜谱编码 */
	private static final String MENUCODE_PRE = "D";
	/** 子工作类别编码长度2位 */
	private static final int SUBWORKTYPECODE_LENGTH = 2;
	/** 值别编码长度2位 */
	private static final int DUTYCODE_LENGTH = 2;
	/** 车辆维修单位编码长度4位 */
	private static final int CARWHCODE_LENGTH = 4;
	/** 车辆维修单位编码 */
	private static final String CARWHCODE_PRE = "RP";
	/** 会议申请单号编码长度5位 */
	private static final int MEETAPLNOCODE_LENGTH = 5;
	/** 会议申请单号编码 */
	private static final String MEETAPLNOCODE_PRE = "M";
	/** 接待申请单号编码长度5位 */
	private static final int RECIVEAPLNOCODE_LENGTH = 5;
	/** 接待申请单号编 */
	private static final String RECIVEAPLNOCODE_PRE = "R";
	/** 申请人ID编码长度6位 */
	private static final int APPLYMANID_LENGTH = 6;
	/** 签报编号编码长度5位 */
	private static final int REPORTNUMBERCODE_LENGTH = 5;
	/** 签报申请单号编码长度5位 */
	private static final int REPORTAPPNOCODE_LENGTH = 5;
	/** 车辆维修申请单号 */
	private static final String CARMAINTENANCEAPPNO_PRE = "F";
	/** 车辆维修申请单号长度5位 */
	private static final int CARMAINTENANCEAPPNO_LENGTH = 5;
	/** 车辆申请单号 */
	private static final String CARAPPNO_PRE = "A";
	/** 车辆申请单号长度5位 */
	private static final int CARAPPNO_LENGTH = 5;
	/** 维修项目编码长度2位 */
	private static final int MAINTENANCEITEMCODE_LENGTH = 2;
	
	/**
	 * 生成SQL文
	 * @param tableName
	 *        查询表名
	 * @param colName
	 *        查询列名
	 * @param start
	 *        编码开始位置
	 * @param length
	 *        编码长度
	 * @return
	 */
	private String getSql(String tableName, String colName, int start, int length){
		String strSql = "";
		strSql = "SELECT CASE WHEN MAX(";
		if (start != 0) {
			strSql = strSql
			       + "SUBSTR("
			       + colName
			       + "," + String.valueOf(start)
			       + "," + String.valueOf(length)
			       + ")";
		} else {
			strSql = strSql 
			       + colName;
		}
		strSql = strSql + ") IS NULL THEN TO_CHAR(1) ELSE TO_CHAR(MAX(";
		if (start != 0) {
			strSql = strSql
			       + "SUBSTR("
			       + colName
			       + "," + String.valueOf(start)
			       + "," + String.valueOf(length)
			       + ") ";
		} else {
			strSql = strSql 
			       + colName;
		}
		strSql = strSql
	             + ") + 1) END FROM "
	             + tableName;
		return strSql;
	}
	
	/**
	 * 生成SQL文
	 * @param tableName
	 *        查询表名
	 * @param colName
	 *        查询列名
	 * @return
	 */
	private String getSql(String tableName, String colName){
		String strSql = this.getSql(tableName, colName, 0, 0);
		return strSql;
	}

	/**
	 * 格式化由DB生成的编码
	 * 
	 * @param strMax 从DB生成的编码
	 * @return strRes 格式化后的编码
	 */
	private String padCode(String strMax, int length) {
		String strRes = "";
		for (int i = length; i > 0; i--) {
			if (i > strMax.length()) {
				strRes += PAD_CHAR_0;
			} else {
				strRes += strMax;
				break;
			}
		}
		return strRes;
	}

	/**
	 * 工作项目编码生成 定期工作画面向DB保存时，工作项目编码生成
	 * 
	 * @return String 生成的工作项目编码
	 * @throws SQLException  
	 */
	public String getWorkItemCode() throws SQLException {
		try {
			String strSql = this.getSql("AD_C_TIMEWORK", "WORKITEM_CODE",
					WORKITEMCODE_PRE.length() + 1,
					WORKITEMCODE_LENGTH);
	
	        LogUtil.log("EJB:生成工作项目编码开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			String strMax = bll.getSingal(strSql).toString();
			String strRes = padCode(strMax, WORKITEMCODE_LENGTH);
			strRes = WORKITEMCODE_PRE + strRes;
	        LogUtil.log("EJB:生成工作项目编码结束。", Level.INFO, null);
			return strRes;
        } catch (Exception e) {
            LogUtil.log("EJB:生成工作项目编码失败。", Level.SEVERE, e);
        	throw new SQLException();
        }
	}

	/**
	 * 菜谱编码生成 菜谱维护画面向DB保存时，菜谱编码生成
	 * 
	 * @return String 生成的菜谱编码
	 * @throws SQLException  
	 */
	public String getCMenuCode() throws SQLException {
		try {
			String strSql = this.getSql("AD_C_MENU_WH", "MENU_CODE",
					MENUCODE_PRE.length() + 1,
					MENUCODE_LENGTH);
	
	        LogUtil.log("EJB:生成菜谱编码开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			String strMax = bll.getSingal(strSql).toString();
			String strRes = padCode(strMax, MENUCODE_LENGTH);
			strRes = MENUCODE_PRE + strRes;
	        LogUtil.log("EJB:生成菜谱编码结束。", Level.INFO, null);
			return strRes;
        } catch (Exception e) {
            LogUtil.log("EJB:生成菜谱编码失败。", Level.SEVERE, e);
        	throw new SQLException();
        }
	}

	/**
	 * 子工作类别编码生成 定期工作类别维护画面面向DB保存时，子工作类别编码生成
	 * 
	 * @param String
	 *            workTypeCode 工作类别编码
	 * @return String 生成的子工作类别编码
	 * @throws SQLException  
	 */
	public String getSubWorkTypeCode(String workTypeCode) throws SQLException {
        try {
			String strSql = this.getSql("AD_C_WORKTYPE", "SUB_WORKTYPE_CODE");
			strSql = strSql
			       + " WHERE WORKTYPE_CODE = ? ";
			// 查询参数数组
			Object[] params = new Object[1];
			params[0] = workTypeCode;
			
	        LogUtil.log("EJB:生成子工作类别编码开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
        	String strMax = bll.getSingal(strSql,params).toString();
    		String strRes = "";
    		if (strMax.length() > SUBWORKTYPECODE_LENGTH) {
    			strRes = strMax.substring(strMax.length() - SUBWORKTYPECODE_LENGTH, strMax
    					.length());
    		} else {
    			strRes = padCode(strMax, SUBWORKTYPECODE_LENGTH);
    		}
    		strRes = workTypeCode + strRes;
            LogUtil.log("EJB:生成子工作类别编码结束。", Level.INFO, null);
    		return strRes;
        } catch (Exception e) {
            LogUtil.log("EJB:生成子工作类别编码失败。", Level.SEVERE, e);
        	throw new SQLException();
        }
	}

	/**
	 * 值别编码生成 值别维护画面向DB保存时，值别编码生成。
	 * 
	 * @return String 生成的值别编码
	 * @throws SQLException  
	 */
	public String getDutyCode() throws SQLException {
		try {
			String strSql = this.getSql("AD_C_DUTYTYPE", "DUTY_TYPE");
	
	        LogUtil.log("EJB:生成值别编码开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			String strMax = bll.getSingal(strSql).toString();
			String strRes = padCode(strMax, DUTYCODE_LENGTH);
	        LogUtil.log("EJB:生成值别编码结束。", Level.INFO, null);
			return strRes;
        } catch (Exception e) {
            LogUtil.log("EJB:生成值别编码失败。", Level.SEVERE, e);
        	throw new SQLException();
        }
	}

	/**
	 * 车辆维修单位编码生成 车辆维修单位维护画面向DB保存时，单位编码生成
	 * 
	 * @return String 生成的车辆维修单位编码
	 * @throws SQLException  
	 */
	public String getCarWHCode() throws SQLException {
		try {
			String strSql = this.getSql("AD_C_CARMEND_WH", "CP_CODE",
					CARWHCODE_PRE.length() + 1,
					CARWHCODE_LENGTH);
	
	        LogUtil.log("EJB:生成车辆维修单位编码开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			String strMax = bll.getSingal(strSql).toString();
			String strRes = padCode(strMax, CARWHCODE_LENGTH);
			strRes = CARWHCODE_PRE + strRes;
	        LogUtil.log("EJB:生成车辆维修单位编码结束。", Level.INFO, null);
	        return strRes;
	    } catch (Exception e) {
	        LogUtil.log("EJB:生成车辆维修单位编码失败。", Level.SEVERE, e);
	    	throw new SQLException();
	    }
	}

	/**
	 * 会议申请单号编码生成 会议申请上报画面向DB保存时，会议申请单号编码生成
	 * 
	 * @param String
	 *            applyUserID 申请人ID
	 * 
	 * @return String 生成的会议申请单号编码
	 * @throws SQLException 
	 */
	public String getMeetAplNoCode(String applyUserID) throws SQLException {
		try {
			String strSql = this.getSql("AD_J_MEET", "MEET_ID",
					applyUserID.length() + MEETAPLNOCODE_PRE.length() + 1,
					MEETAPLNOCODE_LENGTH + APPLYMANID_LENGTH);
			strSql = strSql
		       + " WHERE APPLY_MAN = ? ";
			// 查询参数数组
			Object[] params = new Object[1];
			params[0] = applyUserID;
	
	        LogUtil.log("EJB:生成会议申请单号编码开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			String strMax = bll.getSingal(strSql, params).toString();
			String strRes = padCode(strMax, MEETAPLNOCODE_LENGTH);
			strRes = MEETAPLNOCODE_PRE + applyUserID + strRes;
	        LogUtil.log("EJB:生成会议申请单号编码结束。", Level.INFO, null);
	        return strRes;
        } catch (Exception e) {
            LogUtil.log("EJB:生成会议申请单号编码失败。", Level.SEVERE, e);
        	throw new SQLException();
        }
	}

	/**
	 * 接待申请单号编码生成 接待申请上报画面向DB保存时，接待申请单号编码生成
	 * 
	 * @param String
	 *            applyUserID 申请人ID
	 * 
	 * @return String 生成的接待申请单号编码
	 * @throws SQLException 
	 */
	public String getReciveAplNoCode(String applyUserID) throws SQLException {
	String strSql = this.getSql("AD_J_RECEPTION", "APPLY_ID",
			applyUserID.length() + RECIVEAPLNOCODE_PRE.length() + 1,
			RECIVEAPLNOCODE_LENGTH + APPLYMANID_LENGTH);
	strSql = strSql
	       + " WHERE APPLY_MAN = ? ";
	// 查询参数数组
	Object[] params = new Object[1];
	params[0] = applyUserID;

    LogUtil.log("EJB:生成接待申请单号编码开始。", Level.INFO, null);
    LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
	String strMax = bll.getSingal(strSql, params).toString();
	String strRes =  padCode(strMax, RECIVEAPLNOCODE_LENGTH);
	strRes =  RECIVEAPLNOCODE_PRE + applyUserID + strRes;
    LogUtil.log("EJB:生成接待申请单号编码结束。", Level.INFO, null);
	return strRes;
	}

	/**
	 * 签报申请单号生成
	 * 
	 * @param String appType 申请类别
	 *                内部申请 I , 董事会申请D
	 * @param String userID 登录用户ID
	 * 
	 * @return String 生成的签报申请单号
	 * @throws SQLException 
	 */
	public String getReportAppNoCode(String appType, String userID) throws SQLException {
		try {
			String strSql = this.getSql("AD_J_OUT_QUEST", "APPLY_ID",
					appType.length() + userID.length() + 1,
					REPORTAPPNOCODE_LENGTH);
			strSql = strSql
			       + " WHERE APPLY_ID LIKE ? ";
			// 查询参数数组
			Object[] params = new Object[1];
			params[0] = appType + userID + "%";
	
		    LogUtil.log("EJB:生成签报申请单号编码开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			String strMax = bll.getSingal(strSql, params).toString();
			String strRes = padCode(strMax, REPORTAPPNOCODE_LENGTH);
			strRes =  appType + userID + strRes;
		    LogUtil.log("EJB:生成签报申请单号编码结束。", Level.INFO, null);
			return strRes;
        } catch (Exception e) {
            LogUtil.log("EJB:生成签报申请单号编码失败。", Level.SEVERE, e);
        	throw new SQLException();
        }
	}

	/**
	 * 签报编号编码生成
	 * 
	 * @param String
	 *            appType 申请类别
	 *                内部申请 I , 董事会申请D
	 * @param String
	 *            appUserDeptCode 申请人部门编码
	 * @return String 生成的签报编号编码
	 * @throws SQLException 
	 */
	public String getReportNoCode(String appType, String appUserDeptCode) throws SQLException {
		try {
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			String strSysDate = df.format(new Date());
			// 生成当前年份后2位
			String strYear = strSysDate.substring(2,4);
			
			String strSql = this.getSql("AD_J_OUT_QUEST", "REPORT_ID",
					appType.length() + appUserDeptCode.length() + strYear.length()
					+ CHAR_SUB.length() + CHAR_SUB.length() + 1,
					REPORTNUMBERCODE_LENGTH);
	
			strSql = strSql
			       + " WHERE REPORT_ID LIKE ? ";
			// 查询参数数组
			Object[] params = new Object[1];
			params[0] = appType + appUserDeptCode + CHAR_SUB + strYear + CHAR_SUB + "%";
	
		    LogUtil.log("EJB:生成签报编号编码开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			String strMax = bll.getSingal(strSql, params).toString();
			String strRes = padCode(strMax, REPORTNUMBERCODE_LENGTH);
			strRes =  appType + appUserDeptCode + CHAR_SUB + strYear + CHAR_SUB + strRes;
		    LogUtil.log("EJB:生成签报编号编码结束。", Level.INFO, null);
			return strRes;
        } catch (Exception e) {
            LogUtil.log("EJB:生成签报编号编码失败。", Level.SEVERE, e);
        	throw new SQLException();
        }
	}

	/**
	 * 车辆维修申请单号生成
	 * 
	 * @param String userID 登录用户ID
	 * 
	 * @return String 生成的车辆维修申请单号
	 * @throws SQLException 
	 */
	public String getCarMaintenanceAppNo(String userID) throws SQLException {
		try {
			String strSql = this.getSql("AD_J_CARWH", "WH_ID",
					userID.length() + CARMAINTENANCEAPPNO_PRE.length() + 1,
					CARMAINTENANCEAPPNO_LENGTH + userID.length());
			strSql = strSql
		       + " WHERE WH_ID LIKE ? ";
			// 查询参数数组
			Object[] params = new Object[1];
			params[0] = CARMAINTENANCEAPPNO_PRE + userID + "%";
	
	        LogUtil.log("EJB:生成车辆维修申请单号开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			String strMax = bll.getSingal(strSql, params).toString();
			String strRes = padCode(strMax, CARMAINTENANCEAPPNO_LENGTH);
			strRes = CARMAINTENANCEAPPNO_PRE + userID + strRes;
	        LogUtil.log("EJB:生成车辆维修申请单号结束。", Level.INFO, null);
			return strRes;
        } catch (Exception e) {
            LogUtil.log("EJB:生成车辆维修申请单号失败。", Level.SEVERE, e);
        	throw new SQLException();
        }
	}

	/**
	 * 车辆申请单号生成
	 * 
	 * @param String userID 登录用户ID
	 * 
	 * @return String 生成的车辆申请单号
	 * @throws SQLException 
	 */
	public String getCarAppNo(String userID) throws SQLException{
		try {
			String strSql = this.getSql("AD_J_APPLYCAR", "CAR_APPLY_ID",
					userID.length() + CARAPPNO_PRE.length() + 1,
					CARAPPNO_LENGTH + userID.length());
			strSql = strSql
		       + " WHERE CAR_APPLY_ID LIKE ? ";
			// 查询参数数组
			Object[] params = new Object[1];
			params[0] = CARAPPNO_PRE + userID + "%";
	
	        LogUtil.log("EJB:生成车辆申请单号开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			String strMax = bll.getSingal(strSql, params).toString();
			String strRes = padCode(strMax, CARAPPNO_LENGTH);
			strRes = CARAPPNO_PRE + userID + strRes;
	        LogUtil.log("EJB:生成车辆申请单号结束。", Level.INFO, null);
			return strRes;
        } catch (Exception e) {
            LogUtil.log("EJB:生成车辆申请单号失败。", Level.SEVERE, e);
        	throw new SQLException();
        }
	}
	
	/**
	 * 维修项目编码生成
	 *  
	 * @param String strFeeType 费用类别编码
	 * 
	 * @return String 生成的维修项目编码
	 * @throws SQLException
	 */
	public String getMaintenanceItemCode(String strFeeType) throws SQLException {
        try {
			String strSql = this.getSql("AD_C_CARWH_PRO", "PRO_CODE");
			strSql = strSql
			       + " WHERE PRO_CODE LIKE ? ";
			// 查询参数数组
			Object[] params = new Object[1];
			params[0] = strFeeType + "%";
			
	        LogUtil.log("EJB:生成维修项目编码开始。", Level.INFO, null);
	        LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
        	String strMax = bll.getSingal(strSql,params).toString();
    		String strRes = "";
    		if (strMax.length() > MAINTENANCEITEMCODE_LENGTH) {
    			strRes = strMax.substring(strMax.length() - MAINTENANCEITEMCODE_LENGTH, strMax
    					.length());
    		} else {
    			strRes = padCode(strMax, MAINTENANCEITEMCODE_LENGTH);
    		}
    		strRes = strFeeType + strRes;
            LogUtil.log("EJB:生成维修项目编码结束。", Level.INFO, null);
    		return strRes;
        } catch (Exception e) {
            LogUtil.log("EJB:生成维修项目编码失败。", Level.SEVERE, e);
        	throw new SQLException();
        }
	}
}
