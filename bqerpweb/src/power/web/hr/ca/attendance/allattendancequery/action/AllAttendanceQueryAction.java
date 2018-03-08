/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.web.hr.ca.attendance.allattendancequery.action;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.LogUtil;
import power.ejb.hr.ca.AllAttendanceQueryFacadeRemote;
import power.ejb.hr.ca.HrJAttendancecheckFacadeRemote;
import power.ejb.hr.ca.MetaData;
import power.ejb.hr.ca.StoreObject;
import power.ejb.hr.ca.TimeKeeperExamineForm;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.ExportXsl;
import power.web.hr.ca.common.CaCommonBusinessAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 全公司考勤查询 Action
 * 
 * @author huangweijie
 * @version 1.0
 */
@SuppressWarnings("serial")
public class AllAttendanceQueryAction extends AbstractAction {

	/**
	 * 查询条件类
	 * @author huangweijie
	 */
	class QueryObj{
		/** 查询条件bean 审核年月 */
	    public String examineDate;
	    /** 查询条件bean 审核部门 */
	    public String examineDept;
	    /** 查询条件bean 员工部门ID */
	    public String empDeptId;
	    /** 查询条件bean 员工企业编码 */
	    public String enterpriseCode;
		/**
		 * @return the examineDate
		 */
		public String getExamineDate() {
			return examineDate;
		}
		/**
		 * @param examineDate the examineDate to set
		 */
		public void setExamineDate(String examineDate) {
			this.examineDate = examineDate;
		}
		/**
		 * @return the examineDept
		 */
		public String getExamineDept() {
			return examineDept;
		}
		/**
		 * @param examineDept the examineDept to set
		 */
		public void setExamineDept(String examineDept) {
			this.examineDept = examineDept;
		}
		/**
		 * @return the empDeptId
		 */
		public String getEmpDeptId() {
			return empDeptId;
		}
		/**
		 * @param empDeptId the empDeptId to set
		 */
		public void setEmpDeptId(String empDeptId) {
			this.empDeptId = empDeptId;
		}
		/**
		 * @return the enterpriseCode
		 */
		public String getEnterpriseCode() {
			return enterpriseCode;
		}
		/**
		 * @param enterpriseCode the enterpriseCode to set
		 */
		public void setEnterpriseCode(String enterpriseCode) {
			this.enterpriseCode = enterpriseCode;
		}
	}
	
    /** 导出excel文件名前缀 */
    private static final String STR_FILE_NAME = "职工考勤记录表";
    /** 配置文件名 */
    private static final String CONFIG_FILE_NAME = "colorAndsharpConfig.properties";
    /** property文件 */
    public static Properties p = null;
    /** 审核年月 */
    public String examineDate;
    /** 审核部门 */
    public String examineDept;
    /** 树节点*/
    public String node;
    /** 定义ejb接口 */
    public AllAttendanceQueryFacadeRemote remote;
    /** 考勤审核记录接口 */
    public HrJAttendancecheckFacadeRemote checkRemote;
    /** 开始日期 */
    public String strStartDate;
    /** 结束日期 */
    public String strEndDate;
    /** 员工id */
    public String empId;
    /** 考勤年月 */
    public String strYearMonth;
    /** 工作休息flag */
    public String workOrRest;
    /** 员工个人考勤部门id */
    public String attendanceDeptId;
    /** 考勤部门ID */
    public String strExamineDeptId;
    /** 合计项的关键字：C */
    private static final String SUM = "C";
    /** 0天 */
    private static final String DAY_ZERO = "0";
    /** 画面日期（中文） */
    private static final String FLAG_WEB = "WEB";
    /** 已审核标志 */
    public String checkFlag;
    /** 小数点 */
    private static final String POINT = ".";
    /** 空字符串 */
    private static final String BLANK_STRING = "";

    /**
     * 构造函数
     */
    public AllAttendanceQueryAction() throws IOException {
        remote = (AllAttendanceQueryFacadeRemote) factory
                .getFacadeRemote("AllAttendanceQueryFacade");
        checkRemote = (HrJAttendancecheckFacadeRemote) factory
                .getFacadeRemote("HrJAttendancecheckFacade");
        // 加载配置文件
        InputStream fileStream = CaCommonBusinessAction.class
                .getResourceAsStream(CONFIG_FILE_NAME);
        p = new Properties();
        p.load(fileStream);
    }

    /**
     * 获取考勤记录里列表
     */
    public void getAllAttendanceList() {
        try {
            LogUtil.log("Action:全公司考勤查询开始", Level.INFO, null);
            TimeKeeperExamineForm obj = new TimeKeeperExamineForm();
            obj = remote.getExamineInfo(examineDate, examineDept, employee
                    .getEnterpriseCode(), p, String.valueOf(employee.getDeptId()), FLAG_WEB);
            String str = JSONUtil.serialize(obj);
            // 设置查询条件
            QueryObj qObj = new QueryObj();
            qObj.setExamineDate(examineDate);
            qObj.setExamineDept(examineDept);
            qObj.setEnterpriseCode(employee.getEnterpriseCode());
            qObj.setEmpDeptId(String.valueOf(employee.getDeptId()));
            
            // 设置需要导出的查询条件
            request.getSession().setAttribute("allAttQueryObj", qObj);
            write(str);
            LogUtil.log("Action:全公司考勤查询结束", Level.INFO, null);
        } catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:全公司考勤查询失败", Level.SEVERE, null);
        } catch (DataChangeException e) {
            // 获取审核人为空的部门，并返回前台
            write("{success:true,msg:'" + e.getMessage() + "'}");
            LogUtil.log("Action:全公司考勤查询失败", Level.SEVERE, null);
        } catch (ParseException e) {
            LogUtil.log("Action:全公司考勤查询失败", Level.SEVERE, null);
            write(Constants.DATA_FAILURE);
        } catch (Exception e) {
            LogUtil.log("Action:全公司考勤查询失败", Level.SEVERE, null);
            write("{success:true,msg:'" + e.getMessage() + "'}");
        }
    }

    /**
     * 查询考勤部门维护信息
     * @throws JSONException
     * @throws Exception
     */
    public void getAttendanceDeptInfo() throws JSONException, Exception {
        LogUtil.log("Action:查询考勤部门开始。", Level.INFO, null);
        try {
            PageObject obj = remote.getDeptsByTopDeptid(employee.getEnterpriseCode());
            // 输出
            write(JSONUtil.serialize(obj));
            LogUtil.log("Action:查询考勤部门结束。", Level.INFO, null);
        } catch (JSONException e) {
            LogUtil.log("Action:查询考勤部门失败。", Level.SEVERE, e);
        }
    }

    /**
     * 查询员工考勤信息
     */
    @SuppressWarnings("unchecked")
    public void getEmpAttendance() {
        try {
            LogUtil.log("Action:查询员工考勤信息开始", Level.INFO, null);
            PageObject obj = new PageObject();
            List<Map<String, Object>> workOrRestList = (List<Map<String, Object>>) JSONUtil
                    .deserialize(workOrRest);
            obj = remote.getEmpAttendance(empId, employee.getEnterpriseCode(),
                    strStartDate, strEndDate, checkFlag, workOrRestList, strExamineDeptId, String.valueOf(employee.getDeptId()));
            String str = JSONUtil.serialize(obj);
            write(str);
            LogUtil.log("Action:查询员工考勤信息结束", Level.INFO, null);
        } catch (Exception e) {
            LogUtil.log("Action:查询员工考勤信息失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
        }
    }

    /**
     * 查询审核记录信息
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public void getAuditeDetail() throws SQLException {
        try {
            LogUtil.log("Action:查询审核记录信息开始", Level.INFO, null);
            PageObject obj = new PageObject();
            obj = remote.getAuditeList(examineDate, examineDept, employee
                    .getEnterpriseCode());
            String str = JSONUtil.serialize(obj);
            write(str);
            LogUtil.log("Action:查询审核记录信息结束", Level.INFO, null);
        } catch (Exception e) {
            LogUtil.log("Action:查询审核记录信息失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
        }
    }

    /**
     * 导出文件
     * 
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    public void exportFile() throws Exception {
        try {
            LogUtil.log("Action:全公司考勤查询导出文件开始", Level.INFO, null);
            // 获得需要导出的查询条件
            QueryObj qObj = (QueryObj) request
                    .getSession().getAttribute("allAttQueryObj");
            
            // 到数据库中查询
            TimeKeeperExamineForm bossForm = new TimeKeeperExamineForm();
            bossForm = remote.getExamineInfo(qObj.getExamineDate(), qObj.getExamineDept(), 
            		qObj.getEnterpriseCode(), p, qObj.getEmpDeptId(), FLAG_WEB);

            // 从formBean中取得列头信息
            StoreObject sObj = bossForm.getStore();
            MetaData metaData = sObj.getMetaData();
            // 设置列头
            // excel文件每列标题头
            List<String> lstHeader = new ArrayList<String>();
            List<Map<String, Object>> fields = metaData.getFields();
            List<String> nameList = new ArrayList();
            Iterator itFields = fields.iterator();
            // 添加第一列：行号
            lstHeader.add("行号");
            while (itFields.hasNext()) {
                Map<String, Object> mapHeader = (HashMap<String, Object>) itFields
                        .next();
                String header = (String) mapHeader.get("header");
                String names = (String) mapHeader.get("name");
                nameList.add(names);
                String color = "<";
                String cName = BLANK_STRING;
                // 如果包括有把字体颜色设为红色的html（休息时），则去掉
                if (null != header) {
                    if (header.contains(color)) {
                        cName = header.
                                replaceAll("<.*>(.*)<br />(.*)<.*>", "$1($2)");
                        cName = cName.replaceAll("<.*>(.*)<.*>", "$1");
                        cName = cName.replaceAll("(.*)<br />(.*)", "$1($2)");
                        // 如果包含全角括号（合计项单位）,则不用加半角括号
                        if (cName.contains("（")) {
                        	cName = cName.replace("(", BLANK_STRING);
                        	cName = cName.replace(")", BLANK_STRING);
                        }
                    } else {
                        // 如果不包括就把<br />去掉
                        cName = header.replaceAll("(.*)<br />(.*)", "$1($2)");
                        cName = cName.replaceAll("<.*>(.*)<.*>", "$1");
                        cName = cName.replaceAll("(.*)<br />(.*)", "$1($2)");
                        // 如果包含全角括号（合计项单位）,则不用加半角括号
                        if (cName.contains("（")) {
                        	cName = cName.replace("(", BLANK_STRING);
                        	cName = cName.replace(")", BLANK_STRING);
                        }
                    }
                    // 把名字放进列头
                    lstHeader.add(cName);
                }
            }
            // 操作excel文件对象
            ExportXsl exsl = new ExportXsl();
            // 设置response
            exsl.setResponse(response);
            // 当前时间作为文件名以部分
            Date dte = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String strDate = sdf.format(dte);
            // 设定文件名
            exsl.setStrFileName(STR_FILE_NAME + strDate);
            exsl.setLstTitle(lstHeader);

            // excel文件中的一行
            List<String> lstRow = new ArrayList();
            // excel文件中的所有行集合
            List lstRowSet = new ArrayList();
            lstRowSet = sObj.getList();
            List realLstRowSet = new ArrayList();
            // excel文件单行实体
            Iterator itRow = lstRowSet.iterator();
            int rowNo = 0;
            int colNo = 0;
            int nameNo = 1;
            while (itRow.hasNext()) {
                colNo = 0;
                nameNo = 1;
                lstRow = new ArrayList();
                Map<Object, Object> cell = new HashMap<Object, Object>();
                cell = (Map<Object, Object>) itRow.next();
                // 设置行号
                lstRow.add(rowNo + 1 + BLANK_STRING);
                for (colNo = 0, nameNo = 1; colNo < nameList.size(); colNo++) {
                    if (0 == colNo || 2 == colNo || 4 == colNo || 5 == colNo)
                        continue;
                    if (null == cell.get(nameList.get(colNo))) {
                    	// 如果是合计项列
                    	if (nameList.get(colNo).contains(SUM)) {
                    		lstRow.add(DAY_ZERO);
                    	}
                    	// 日期列
                    	else {
                    		lstRow.add(BLANK_STRING);
                    	}
                    } else {
                        if (String.class == cell.get(nameList.get(colNo))
                                .getClass()) {
                        	String s = (String) cell.get(nameList.get(colNo));
                        	// 如果有小数点
                        	if (s.contains(POINT)) {
                        		// 去掉小数部分
                        		s = s.substring(0, s.indexOf(POINT));
                        	}
                            lstRow.add(s);
                        } else {
                        	String s = String.valueOf(cell.get(nameList
                                    .get(colNo)));
                        	// 如果有小数点
                        	if (s.contains(POINT)) {
                        		// 去掉小数部分
                        		s = s.substring(0, s.indexOf(POINT));
                        	}
                            lstRow.add(s);
                        }
                    }
                    nameNo++;
                }
                realLstRowSet.add(lstRow);
                rowNo++;
            }
            // 设置所有行内容
            exsl.setLstRow(realLstRowSet);
            // 创建导出excel文件
            exsl.createXsl();
            LogUtil.log("Action:全公司考勤查询导出文件结束", Level.INFO, null);
        } catch (IOException e) {
            write(Constants.IO_FAILURE);
            LogUtil.log("Action:全公司考勤查询导出文件IO异常", Level.SEVERE, null);
        } catch (Exception e) {
            write(Constants.IO_FAILURE);
            LogUtil.log("Action:全公司考勤查询导出文件异常", Level.SEVERE, e);
        }
    }

    /**
     * @return the examineDate
     */
    public String getExamineDate() {
        return examineDate;
    }

    /**
     * @param examineDate
     *            the examineDate to set
     */
    public void setExamineDate(String examineDate) {
        this.examineDate = examineDate;
    }

    /**
     * @return the examineDept
     */
    public String getExamineDept() {
        return examineDept;
    }

    /**
     * @param examineDept
     *            the examineDept to set
     */
    public void setExamineDept(String examineDept) {
        this.examineDept = examineDept;
    }

    /**
     * @return the node
     */
    public String getNode() {
        return node;
    }

    /**
     * @param node the node to set
     */
    public void setNode(String node) {
        this.node = node;
    }

    /**
     * @return the strStartDate
     */
    public String getStrStartDate() {
        return strStartDate;
    }

    /**
     * @param strStartDate the strStartDate to set
     */
    public void setStrStartDate(String strStartDate) {
        this.strStartDate = strStartDate;
    }

    /**
     * @return the strEndDate
     */
    public String getStrEndDate() {
        return strEndDate;
    }

    /**
     * @param strEndDate the strEndDate to set
     */
    public void setStrEndDate(String strEndDate) {
        this.strEndDate = strEndDate;
    }

    /**
     * @return the empId
     */
    public String getEmpId() {
        return empId;
    }

    /**
     * @param empId the empId to set
     */
    public void setEmpId(String empId) {
        this.empId = empId;
    }

    /**
     * @return the strYearMonth
     */
    public String getStrYearMonth() {
        return strYearMonth;
    }

    /**
     * @param strYearMonth the strYearMonth to set
     */
    public void setStrYearMonth(String strYearMonth) {
        this.strYearMonth = strYearMonth;
    }

    /**
     * @return the workOrRest
     */
    public String getWorkOrRest() {
        return workOrRest;
    }

    /**
     * @param workOrRest the workOrRest to set
     */
    public void setWorkOrRest(String workOrRest) {
        this.workOrRest = workOrRest;
    }

    /**
     * @return the attendanceDeptId
     */
    public String getAttendanceDeptId() {
        return attendanceDeptId;
    }

    /**
     * @param attendanceDeptId the attendanceDeptId to set
     */
    public void setAttendanceDeptId(String attendanceDeptId) {
        this.attendanceDeptId = attendanceDeptId;
    }

    /**
     * @return the strExamineDeptId
     */
    public String getStrExamineDeptId() {
        return strExamineDeptId;
    }

    /**
     * @param strExamineDeptId the strExamineDeptId to set
     */
    public void setStrExamineDeptId(String strExamineDeptId) {
        this.strExamineDeptId = strExamineDeptId;
    }

	/**
	 * @return the checkFlag
	 */
	public String getCheckFlag() {
		return checkFlag;
	}

	/**
	 * @param checkFlag the checkFlag to set
	 */
	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

}
