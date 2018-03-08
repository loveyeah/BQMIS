/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.document.jobmanagement.newanddimissionquery.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJNewAndDimissionFacadeRemote;
import power.ejb.hr.HrJTotalBean;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.WriteXls;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 新进/离职员工查询Action
 * 
 * @author 周旭
 * @version 1.0
 */
public class NewAndDimissionQueryAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    /** 开始行 */
    private int start;
    /** 查询行 */
    private int limit;
    /** 年度 */
    private String startDate;
    /** 部门id */
    private String deptId;
    /** 统计bean */
    private HrJTotalBean totalBean;
    /** 新进/离职员工统计 */
    private String STR_FILE_NAME_TOTAL = "新进/离职员工统计";
    private String STR_FILE_FLAG_TOTAL = "PD007TOTAL";
    /** 新进员工花名册 */
    private String STR_FILE_NAME_NEW = "新进员工花名册";
    private String STR_FILE_FLAG_NEW = "PD007NEW";
    /** 离职员工花名册 */
    private String STR_FILE_NAME_DIMISSION = "离职员工花名册";
    private String STR_FILE_FLAG_DIMISSION = "PD007DIMISSION";
    /** 接口 */
    private HrJNewAndDimissionFacadeRemote queryRemote;

    /**
     * 构造函数
     */
    public NewAndDimissionQueryAction() {
        // 接口取得
        queryRemote = (HrJNewAndDimissionFacadeRemote) factory.getFacadeRemote("HrJNewAndDimissionFacade");
    }

    /**
     * 新进/离职统计查询
     */
    public void getTotalInfo() {
        try {
            LogUtil.log("Action:新进/离职统计查询开始。", Level.INFO, null);
            // 把查询条件记录下来，导出的时候用
	       	request.getSession().setAttribute("startDateTT", startDate);
	       	request.getSession().setAttribute("deptIdTT", deptId);
            // 查询
            PageObject object = queryRemote.findTotal(startDate, deptId, employee.getEnterpriseCode(), start, limit);
            // 输出
            write(JSONUtil.serialize(object));
            LogUtil.log("Action:新进/离职统计查询结束。", Level.INFO, null);
        } catch (JSONException e) {
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:新进/离职统计查询失败。", Level.SEVERE, e);
        } catch (RuntimeException e) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:新进/离职统计查询失败。", Level.SEVERE, e);
        }
    }

    /**
     * 新进员工统计查询
     */
    public void getNewEmployInfo() {
        try {
            LogUtil.log("Action:新进员工统计查询开始。", Level.INFO, null);
            // 把查询条件记录下来，导出的时候用
	       	request.getSession().setAttribute("startDateNW", startDate);
	       	request.getSession().setAttribute("deptIdNW", deptId);
            // 查询
            PageObject object = queryRemote.findNew(startDate, deptId, employee.getEnterpriseCode(), start, limit);
            // 输出
            write(JSONUtil.serialize(object));
            LogUtil.log("Action:新进员工统计查询结束。", Level.INFO, null);
        } catch (JSONException e) {
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:新进员工统计查询失败。", Level.SEVERE, e);
        } catch (RuntimeException e) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:新进员工统计查询失败。", Level.SEVERE, e);
        }
    }

    /**
     * 离职员工统计查询
     */
    public void getDimissionEmployInfo() {
        try {
            LogUtil.log("Action:离职员工统计查询开始。", Level.INFO, null);
            // 把查询条件记录下来，导出的时候用
	       	request.getSession().setAttribute("startDateDM", startDate);
	       	request.getSession().setAttribute("deptIdDM", deptId);
	    	
	       	//add by fyyang 20100623
	       	String typeId=request.getParameter("typeId");
	       	String advicenoteNo=request.getParameter("advicenoteNo");
	       	request.getSession().setAttribute("typeIdDM", typeId);
	    	request.getSession().setAttribute("advicenoteNoDM", advicenoteNo);
            // 查询
            PageObject object = queryRemote
                    .findDimission(startDate, deptId, employee.getEnterpriseCode(),typeId,advicenoteNo, start, limit);
            // 输出
            write(JSONUtil.serialize(object));
            LogUtil.log("Action:离职员工统计查询结束。", Level.INFO, null);
        } catch (JSONException e) {
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:离职员工统计查询失败。", Level.SEVERE, e);
        } catch (RuntimeException e) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:离职员工统计查询失败。", Level.SEVERE, e);
        }
    }

    /**
     * 统计导出文件
     * 
     */
    @SuppressWarnings("unchecked")
    public void exportTotalEmployFile() {
        try {
            LogUtil.log("Action:新进/离职统计导出文件开始", Level.INFO, null);
            // 把查询时保存的信息取出，查询数据
        	startDate = (String)request.getSession().getAttribute("startDateTT");
        	deptId = (String)request.getSession().getAttribute("deptIdTT");
            // 操作excel
            WriteXls xls = new WriteXls(Constants.BLANK_STRING, new ArrayList());
            // 查询
            PageObject object = queryRemote.findTotal(startDate, deptId, employee.getEnterpriseCode());
            // 生成
            xls = new WriteXls(STR_FILE_FLAG_TOTAL, object.getList());
            xls.setBusinessName(STR_FILE_NAME_TOTAL);
            // 导出
            xls.xlsExportFile(response);
            LogUtil.log("Action:新进/离职统计导出文件结束", Level.INFO, null);
        } catch (IOException e) {
            write(Constants.IO_FAILURE);
            LogUtil.log("Action:新进/离职统计导出文件失败。", Level.SEVERE, e);
        } catch (Exception e) {
            write(Constants.IO_FAILURE);
            LogUtil.log("Action:新进/离职统计导出文件失败。", Level.SEVERE, e);
        }
    }

    /**
     * 新进员工导出文件
     * 
     */
    @SuppressWarnings("unchecked")
    public void exportNewEmployFile() {
        try {
            LogUtil.log("Action:新进员工统计导出文件开始", Level.INFO, null);
            // 把查询时保存的信息取出，查询数据
        	startDate = (String)request.getSession().getAttribute("startDateNW");
        	deptId = (String)request.getSession().getAttribute("deptIdNW");
            // 操作excel
            WriteXls xls = new WriteXls(Constants.BLANK_STRING, new ArrayList());
            // 查询
            PageObject object = queryRemote.findNew(startDate, deptId, employee.getEnterpriseCode());
            // 生成
            xls = new WriteXls(STR_FILE_FLAG_NEW, object.getList());
            xls.setBusinessName(STR_FILE_NAME_NEW);
            // 导出
            xls.xlsExportFile(response);
            LogUtil.log("Action:新进员工统计导出文件结束。", Level.INFO, null);
        } catch (IOException e) {
            write(Constants.IO_FAILURE);
            LogUtil.log("Action:新进员工统计导出文件失败。", Level.SEVERE, e);
        } catch (Exception e) {
            write(Constants.IO_FAILURE);
            LogUtil.log("Action:新进员工统计导出文件失败。", Level.SEVERE, e);
        }
    }

    /**
     * 离职员工导出文件
     * 
     */
    @SuppressWarnings("unchecked")
    public void exportDimisssionEmployFile() {
        try {
            LogUtil.log("Action:离职员工统计导出文件开始", Level.INFO, null);
            // 把查询时保存的信息取出，查询数据
        	startDate = (String)request.getSession().getAttribute("startDateDM");
        	deptId = (String)request.getSession().getAttribute("deptIdDM");
        	String typeId=(String)request.getSession().getAttribute("typeIdDM");
	       	String advicenoteNo=(String)request.getSession().getAttribute("advicenoteNoDM");
        	
            // 操作excel
            WriteXls xls = new WriteXls(Constants.BLANK_STRING, new ArrayList());
         // 查询
            PageObject object = queryRemote
                    .findDimission(startDate, deptId, employee.getEnterpriseCode(),typeId,advicenoteNo);
            // 生成
            xls = new WriteXls(STR_FILE_FLAG_DIMISSION, object.getList());
            xls.setBusinessName(STR_FILE_NAME_DIMISSION);
            // 导出
            xls.xlsExportFile(response);
            LogUtil.log("Action:离职员工统计导出文件结束。", Level.INFO, null);
        } catch (IOException e) {
            write(Constants.IO_FAILURE);
            LogUtil.log("Action:离职员工统计导出文件失败。", Level.SEVERE, e);
        } catch (Exception e) {
            write(Constants.IO_FAILURE);
            LogUtil.log("Action:离职员工统计导出文件失败。", Level.SEVERE, e);
        }
    }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start
     *            the start to set
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
     * @param limit
     *            the limit to set
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate
     *            the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the deptId
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * @param deptId
     *            the deptId to set
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**
     * @return the totalBean
     */
    public HrJTotalBean getTotalBean() {
        return totalBean;
    }

    /**
     * @param totalBean
     *            the totalBean to set
     */
    public void setTotalBean(HrJTotalBean totalBean) {
        this.totalBean = totalBean;
    }

}
