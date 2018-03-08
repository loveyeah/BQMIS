/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.ca.leave.vacation.action;
    
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.WriteXls;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.LogUtil;
import power.ejb.hr.ca.CACodeConstants;
import power.ejb.hr.ca.HrJVacationByW;
import power.ejb.hr.ca.HrJVacationFacadeRemote;
/**
 * 请假登记查询 
 * @author wujiao
 * @version 1.0
 */
public class Vacation extends AbstractAction{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /** 查询，保存，修改数据远程对象 */
    private HrJVacationFacadeRemote remote;
    /** 分页信息 */
    private int start;
    private int limit;
    /** 请假开始日期from */
    private String startTime;
    /** 请假开始日期to */
    private String endTime;
    /** 机能id */
    public static String WORK_ID_VACATION = "QJ006";
    /** 机能名 */
    public static String WORK_NAME_VACATION = "请假登记查询";
    /** 签字状态 */
    public static String SIGN_UNREPORT = "未上报";
    public static String SIGN_REPORTED = "已上报";
    public static String SIGN_COMPLETE = "已终结";
    public static String SIGN_BACK = "已退回";
    /** 是否销假 */
    public static String IFCLEAR_YES = "是";
    public static String IFCLEAR_NO = "否";
    /** 空字符串 */
    public static String BLANK = "";
    /**
     * 构造函数
     */
    public Vacation() {
        remote = (HrJVacationFacadeRemote) factory.getFacadeRemote("HrJVacationFacade");
    }

    /**
     * 请假登记查询 查询所有信息
     * @param void
     * @return void
     * @throws SQLException,JSONException
     * 
     */
    public void searchVacationList() throws SQLException, JSONException {
        try {
            LogUtil.log("Action:请假登记查询开始。", Level.INFO, null);
            // 查询
            PageObject object = remote.getAllVacations(employee
                    .getEnterpriseCode(), startTime, endTime, start, limit);
            // 输出
            write(JSONUtil.serialize(object));
            LogUtil.log("Action:请假登记查询开始。", Level.INFO, null);
        } catch (JSONException jsone) {
            LogUtil.log("Action:请假登记查询失败。", Level.SEVERE, jsone);
            throw jsone;
        } catch (SQLException sqle) {
            LogUtil.log("Action:请假登记查询失败。", Level.SEVERE, sqle);
            throw sqle;
        }
    }
    /**
     * 导出查询结果
     * @param void
     * @return void
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public void exportVacationExcel() throws SQLException {
        LogUtil.log("Action:请假登记查询导出开始。", Level.INFO, null);
        // 查询结果
        PageObject object = new PageObject();
        // 操作excel
        WriteXls xls = new WriteXls(Constants.BLANK_STRING, new ArrayList());
        // 数字输出格式化
		String patternDays = "###,###,###,###,##0.00";
		String patternTime = "###,###,###,###,##0.0";
		// 天数保留2位小数
		DecimalFormat df2 = new DecimalFormat(patternDays);
		// 时长保留一位小数
		DecimalFormat df1 = new DecimalFormat(patternTime);
        try {
            object = remote.getAllVacations(employee.getEnterpriseCode(), startTime, endTime);
        }catch(SQLException e) {
            LogUtil.log("Action:请假登记查询导出失败。", Level.SEVERE, e);
            throw e;
        }
        List<HrJVacationByW> list = object.getList();
        List<HrJVacationByW> reportList = new ArrayList<HrJVacationByW>();
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                HrJVacationByW vacation = new HrJVacationByW();
                // 设置签字状态和是否销假的状态
                vacation = (HrJVacationByW)it.next();
                // 未上报
                if(vacation.getSignState().equals(CACodeConstants.SIGN_STATE_UNREPORT)) {
                    vacation.setSignState(SIGN_UNREPORT);
                // 已上报
                }else if(vacation.getSignState().equals(CACodeConstants.SIGN_STATE_REPORTED)) {
                    vacation.setSignState(SIGN_REPORTED);
                // 已终结
                }else if(vacation.getSignState().equals(CACodeConstants.SIGN_STATE_COMPLETE)){
                    vacation.setSignState(SIGN_COMPLETE);
                // 已退回
                }else {
                    vacation.setSignState(SIGN_BACK);
                }
                // 请假天数
				if (vacation.getVacationDays() != null
						&& !BLANK.equals(vacation.getVacationDays())) {
					vacation.setVacationDays(df2.format(Double.parseDouble(vacation.getVacationDays())));
				}
				// 请假时长
				if (vacation.getVacationTime() != null
						&& !BLANK.equals(vacation.getVacationTime())) {
					vacation.setVacationTime(df1.format(Double.parseDouble(vacation.getVacationTime())));
				}
				// 销假时间
				if (vacation.getClearDate() != null
						&& !BLANK.equals(vacation.getClearDate())) {

				}
                // 是否销假：是
                if(vacation.getIfClear().equals(CACodeConstants.IF_CLEAR_YES)) {
                    vacation.setIfClear(IFCLEAR_YES);
                // 是否销假：否
                } else {
                    vacation.setIfClear(IFCLEAR_NO);
                }
                reportList.add(vacation);
            }
        }
        // 生成
        xls = new WriteXls(WORK_ID_VACATION, reportList);
        // 设置机能名
        xls.setBusinessName(WORK_NAME_VACATION);
        try {
            // 导出
            xls.xlsExportFile(response);
            LogUtil.log("Action:请假登记查询导出结束。", Level.INFO, null);
        } catch (Exception e) {
            LogUtil.log("Action:请假登记查询导出失败。", Level.SEVERE, e);
            write(Constants.IO_FAILURE);
        }
    }
    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * @return the limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * @param limit the limit to set
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    
}
