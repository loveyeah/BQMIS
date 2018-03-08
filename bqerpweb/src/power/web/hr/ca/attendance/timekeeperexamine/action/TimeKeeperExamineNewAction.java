/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.ca.attendance.timekeeperexamine.action;

import java.io.IOException;
import java.io.InputStream;
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
import java.util.zip.DataFormatException;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.LogUtil;
import power.ejb.hr.ca.MetaData;
import power.ejb.hr.ca.StoreObject;
import power.ejb.hr.ca.TimeKeeperExamineFacadeNewRemote;
import power.ejb.hr.ca.TimeKeeperExamineFacadeRemote;
import power.ejb.hr.ca.TimeKeeperExamineForm;
import power.ejb.hr.ca.TimeKeeperExamineStandardTime;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.ExportXsl;
import power.web.hr.ca.common.CaCommonBusinessAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

/**
 * 考勤员审核action
 * 
 * @author zhouxu
 * @version 1.0
 */
@SuppressWarnings("serial")
public class TimeKeeperExamineNewAction extends AbstractAction {
    /**
     * 查询条件类
     * 
     * @author huangweijie
     */
    class QueryObj {
        /** 查询条件bean 审核年月 */
        private String examineDate;
        /** 查询条件bean 审核部门 */
        private String examineDept;
        /** 查询条件bean 员工部门ID */
        private String empDeptId;
        /** 查询条件bean 员工企业编码 */
        private String enterpriseCode;

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
         * @return the empDeptId
         */
        public String getEmpDeptId() {
            return empDeptId;
        }

        /**
         * @param empDeptId
         *            the empDeptId to set
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
         * @param enterpriseCode
         *            the enterpriseCode to set
         */
        public void setEnterpriseCode(String enterpriseCode) {
            this.enterpriseCode = enterpriseCode;
        }
    }

    /** 审核年月 */
    private String examineDate;
    /** 审核部门 */
    private String examineDept;
    /** 树节点 */
    private String node;
    /** 定义ejb接口 */
    private TimeKeeperExamineFacadeNewRemote remote;
    /** 开始日期 */
    private String strStartDate;
    /** 结束日期 */
    private String strEndDate;
    /** 员工id */
    private String empId;
    /** 员工部门id */
    private String deptId;
    /** 画面flag */
    private String checkFlag;
    /** 工作休息flag */
    private String workOrRest;
    /** 员工个人考勤部门id */
    private String attendanceDeptId;
    /** 员工的记录考勤集 */
    private String records;
    /** 审核年份 */
    private String checkYear;
    /** 审核月份 */
    private String checkMonth;
    /** 上次修改时间 */
    private String lastModifiyDate;
    /** 所有部门 */
    private String deptIds;
    /** 要撤销所有部门的 */
    private String cancelDeptInfos;
    /** 合计项的关键字：C */
    private static final String SUM = "C";
    /** 0天 */
    private static final String DAY_ZERO = "0";
    /** 小数点 */
    private static final String POINT = ".";
    /** 导出excel文件名前缀 */
    private static final String STR_FILE_NAME = "职工考勤记录申报表";
    /** 配置文件名 */
    private static final String CONFIG_FILE_NAME = "colorAndsharpConfig.properties";
    /** 错误类型X */
    private static final String ERROE_X = "{success:true,msg:'X'}";
    /** 成功类型S */
    private static final String SUCC_S = "{success:true,msg:'S'}";
    /** 时间格式 */
    private static final String TIME_STR = "yyyyMMddHHmmss";
    /** 空字符串 */
    private static final String BLANK_STRING = "";
    /** property文件 */
    private static Properties p = null;

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
     * @param node
     *            the node to set
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
     * @param strStartDate
     *            the strStartDate to set
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
     * @param strEndDate
     *            the strEndDate to set
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
     * @param empId
     *            the empId to set
     */
    public void setEmpId(String empId) {
        this.empId = empId;
    }

    /**
     * @return the checkFlag
     */
    public String getCheckFlag() {
        return checkFlag;
    }

    /**
     * @param checkFlag
     *            the checkFlag to set
     */
    public void setCheckFlag(String checkFlag) {
        this.checkFlag = checkFlag;
    }

    /**
     * @return the workOrRest
     */
    public String getWorkOrRest() {
        return workOrRest;
    }

    /**
     * @param workOrRest
     *            the workOrRest to set
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
     * @param attendanceDeptId
     *            the attendanceDeptId to set
     */
    public void setAttendanceDeptId(String attendanceDeptId) {
        this.attendanceDeptId = attendanceDeptId;
    }

    /**
     * @return the records
     */
    public String getRecords() {
        return records;
    }

    /**
     * @param records
     *            the records to set
     */
    public void setRecords(String records) {
        this.records = records;
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
     * @return the checkYear
     */
    public String getCheckYear() {
        return checkYear;
    }

    /**
     * @param checkYear
     *            the checkYear to set
     */
    public void setCheckYear(String checkYear) {
        this.checkYear = checkYear;
    }

    /**
     * @return the checkMonth
     */
    public String getCheckMonth() {
        return checkMonth;
    }

    /**
     * @param checkMonth
     *            the checkMonth to set
     */
    public void setCheckMonth(String checkMonth) {
        this.checkMonth = checkMonth;
    }

    /**
     * @return the lastModifiyDate
     */
    public String getLastModifiyDate() {
        return lastModifiyDate;
    }

    /**
     * @param lastModifiyDate
     *            the lastModifiyDate to set
     */
    public void setLastModifiyDate(String lastModifiyDate) {
        this.lastModifiyDate = lastModifiyDate;
    }

    /**
     * @return the deptIds
     */
    public String getDeptIds() {
        return deptIds;
    }

    /**
     * @param deptIds
     *            the deptIds to set
     */
    public void setDeptIds(String deptIds) {
        this.deptIds = deptIds;
    }

    /**
     * @return the cancelDeptInfos
     */
    public String getCancelDeptInfos() {
        return cancelDeptInfos;
    }

    /**
     * @param cancelDeptInfos
     *            the cancelDeptInfos to set
     */
    public void setCancelDeptInfos(String cancelDeptInfos) {
        this.cancelDeptInfos = cancelDeptInfos;
    }

    /**
     * 构造函数
     * 
     * @throws IOException
     */
    public TimeKeeperExamineNewAction() throws IOException {
        remote = (TimeKeeperExamineFacadeNewRemote) factory.getFacadeRemote("TimeKeeperExamineFacadeNew");
        // 加载配置文件
        InputStream fileSteam = CaCommonBusinessAction.class.getResourceAsStream(CONFIG_FILE_NAME);
        p = new Properties();
        p.load(fileSteam);
    }

    /**
     * 获取考勤记录列表
     */
    public void getNewAttendanceList() {
        try {
            LogUtil.log("Action:考勤记录查询开始", Level.INFO, null);
            TimeKeeperExamineForm obj = new TimeKeeperExamineForm();
            // 查询考勤记录列表
            
            obj = remote.getExamineInfo(null,examineDate, examineDept, employee.getEnterpriseCode(), p,"1");
            // 设置查询条件
//            QueryObj qObj = new QueryObj();
//            qObj.setExamineDate(examineDate);
//            qObj.setExamineDept(examineDept);
//            qObj.setEnterpriseCode(employee.getEnterpriseCode());
//            qObj.setEmpDeptId(String.valueOf(employee.getDeptId()));

            // 设置需要导出的查询条件
//            request.getSession().setAttribute("timeKeeper6585243", qObj);
            String str = JSONUtil.serialize(obj);
//            System.out.println(str);
            write(str);
        } catch (JSONException jsone) {
            // 格式化出错
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:考勤记录查询失败", Level.SEVERE, null);
        } catch (DataChangeException e) {
            // 获取审核人为空的部门，并返回前台
            write(Constants.DATA_USING);
            LogUtil.log("Action:考勤记录查询失败", Level.SEVERE, null);
        } catch (ParseException e) {
            // 日期格式化出错
            LogUtil.log("Action:考勤记录查询失败", Level.SEVERE, null);
            write(Constants.DATA_FAILURE);
        } catch (RuntimeException e) {
            // 没有对应考勤日期
            LogUtil.log("EJB:没有对应的考勤日期。", Level.SEVERE, null);
            write(ERROE_X);
        } catch (Exception e) {
            // 操作失败
            LogUtil.log("Action:考勤记录查询失败", Level.SEVERE, e);
            e.printStackTrace();
        }
    }

    /**
     * 查询考勤部门
     */
    public void getAttendanceDeptType2Info() {
        LogUtil.log("Action:查询考勤部门开始。", Level.INFO, null);
        try {
            PageObject obj = remote.getDeptsByTopDeptid(employee.getEnterpriseCode(), employee.getWorkerId());
            // 输出
            write(JSONUtil.serialize(obj));
            LogUtil.log("Action:查询考勤部门结束。", Level.INFO, null);
        } catch (JSONException e) {
            LogUtil.log("Action:查询考勤部门失败。", Level.SEVERE, e);
        }
    }

    /**
     * 查询员工开始日期结束日期的考勤信息
     */
    @SuppressWarnings("unchecked")
    public void getEmpAttendanceInfo() {
        try {
            LogUtil.log("Action:查询员工考勤信息开始", Level.INFO, null);
            TimeKeeperExamineStandardTime obj = new TimeKeeperExamineStandardTime();
            // 获取上班休息flag标记
            List<Map<String, Object>> workOrRestList = (List<Map<String, Object>>) JSONUtil.deserialize(workOrRest);
            // 查询员工的考勤记录
            obj = remote.getEmpAttendance(empId, employee.getEnterpriseCode(), strStartDate, strEndDate, checkFlag,
                    workOrRestList, attendanceDeptId, deptId);
            String str = JSONUtil.serialize(obj);
            write(str);
            LogUtil.log("Action:查询员工考勤信息结束", Level.INFO, null);
        } catch (Exception e) {
            // db操作失败
            LogUtil.log("Action:查询员工考勤信息失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
        }
    }

    /**
     * 保存员工考勤记录
     */
    @SuppressWarnings("unchecked")
    public void saveEmpAttendanceInfo() {
//        try {
//            LogUtil.log("Action:保存员工考勤信息开始", Level.INFO, null);
//            // 获取前台发送的数据集
//            ArrayList<Map<String, Object>> empAttendanceList = (ArrayList<Map<String, Object>>) JSONUtil
//                    .deserialize(records);
//            // 保存员工考勤信息
//            remote.saveEmpAttendance(empAttendanceList, employee.getEnterpriseCode(), employee.getWorkerCode(), empId);
//            write(SUCC_S);
//            LogUtil.log("Action:保存员工考勤信息结束", Level.INFO, null);
//        } catch (DataChangeException e) {
//            // 排他错误
//            LogUtil.log("Action:保存员工考勤信息失败", Level.SEVERE, null);
//            write(Constants.DATA_USING);
//        } catch (DataFormatException e) {
//            // 数据格式化错误
//            LogUtil.log("Action:保存员工考勤信息失败", Level.SEVERE, null);
//            write(Constants.DATA_FAILURE);
//        } catch (JSONException e) {
//            // 数据格式化错误
//            LogUtil.log("Action:保存员工考勤信息失败", Level.SEVERE, null);
//            write(Constants.DATA_FAILURE);
//        } catch (RuntimeException e) {
//            // db操作失败
//            LogUtil.log("Action:保存员工考勤信息失败", Level.SEVERE, null);
//            write(Constants.SQL_FAILURE);
//        }
    }

    /**
     * 考勤审核审核
     */
    @SuppressWarnings("unchecked")
    public void setAttendanceCheck() {
//        try {
//            LogUtil.log("Action:考勤审核开始", Level.INFO, null);
//            List<String> deptAllIds = (ArrayList<String>) JSONUtil.deserialize(deptIds);
//            // 考勤审核
//            remote.saveTotalCountByEmp(deptAllIds, strStartDate, strEndDate, employee.getEnterpriseCode(), p, employee
//                    .getWorkerCode(), attendanceDeptId, employee.getWorkerId());
//            // 成功返回
//            write(SUCC_S);
//            LogUtil.log("Action:考勤审核结束", Level.INFO, null);
//        } catch (DataChangeException e) {
//            // 排他失败
//            LogUtil.log("Action:考勤审核失败", Level.SEVERE, null);
//            write(Constants.DATA_USING);
//        } catch (DataFormatException e) {
//            // 格式化错误
//            LogUtil.log("Action:考勤审核失败", Level.SEVERE, null);
//            write(Constants.DATA_FAILURE);
//        } catch (RuntimeException e) {
//            // db操作失败
//            LogUtil.log("Action:考勤审核失败", Level.SEVERE, null);
//            write(Constants.SQL_FAILURE);
//        } catch (JSONException e) {
//            // 格式化错误
//            LogUtil.log("Action:考勤审核失败", Level.SEVERE, null);
//            write(Constants.DATA_FAILURE);
//        } catch (Exception e) {
//            // db操作失败
//            LogUtil.log("Action:考勤审核失败", Level.SEVERE, null);
//            write(Constants.SQL_FAILURE);
//        }
    }

    /**
     * 撤销前回审核
     */
    @SuppressWarnings("unchecked")
    public void cancelLastCheck() {
        try {
            LogUtil.log("Action:撤销前回审核开始", Level.INFO, null);
            ArrayList<Map<String, Object>> arrlist = (ArrayList<Map<String, Object>>) JSONUtil.deserialize(cancelDeptInfos);
            // 撤销审核
            remote.cancelLastCheck(arrlist, employee.getEnterpriseCode(), employee.getWorkerCode());
            // 成功返回
            write(SUCC_S);
            LogUtil.log("Action:撤销前回审核结束", Level.INFO, null);
        } catch (DataChangeException e) {
            // 排他失败
            LogUtil.log("Action:撤销前回审核失败", Level.SEVERE, null);
            write(Constants.DATA_USING);
        } catch (DataFormatException e) {
            // 格式化错误
            LogUtil.log("Action:撤销前回审核失败", Level.SEVERE, null);
            write(Constants.DATA_FAILURE);
        } catch (RuntimeException e) {
            // db操作失败
            LogUtil.log("Action:撤销前回审核失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
        } catch (JSONException e) {
            // 格式化错误
            LogUtil.log("Action:撤销前回审核失败", Level.SEVERE, null);
            write(Constants.DATA_FAILURE);
        }
    }

    /**
     * 导出文件
     * 
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    public void exportAttendanceQueryFile() throws Exception {
        try {
            LogUtil.log("Action:考勤查询导出文件开始", Level.INFO, null);
            // 获得需要导出的查询条件
            QueryObj qObj = (QueryObj) request.getSession().getAttribute("timeKeeper6585243");

            // 到数据库中查询
            TimeKeeperExamineForm bossForm = new TimeKeeperExamineForm();
            String flag=request.getParameter("flag");
            bossForm = remote.getExamineInfo(null,qObj.getExamineDate(), qObj.getExamineDept(), qObj.getEnterpriseCode(), p,"1");

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
                Map<String, Object> mapHeader = (HashMap<String, Object>) itFields.next();
                String header = (String) mapHeader.get("header");
                String names = (String) mapHeader.get("name");
                nameList.add(names);
                String color = "<";
                String cName = BLANK_STRING;
                // 如果包括有把字体颜色设为红色的html（休息时），则去掉
                if (null != header) {
                    if (header.contains(color)) {
                        cName = header.replaceAll("<.*>(.*)<br />(.*)<.*>", "$1($2)");
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
            SimpleDateFormat sdf = new SimpleDateFormat(TIME_STR);
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
                        if (String.class == cell.get(nameList.get(colNo)).getClass()) {
                            String s = (String) cell.get(nameList.get(colNo));
                            // 如果有小数点
                            if (s.contains(POINT)) {
                                s = s.substring(0, s.indexOf(POINT));

                            }
                            lstRow.add(s);
                        } else {
                            String s = String.valueOf(cell.get(nameList.get(colNo)));
                            // 如果有小数点
                            if (s.contains(POINT)) {
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
            LogUtil.log("Action:考勤查询导出文件结束", Level.INFO, null);
        } catch (IOException e) {
            write(Constants.IO_FAILURE);
            LogUtil.log("Action:考勤查询导出文件IO异常", Level.SEVERE, null);
        } catch (Exception e) {
            write(Constants.IO_FAILURE);
            LogUtil.log("Action:考勤查询导出文件异常", Level.SEVERE, null);
        }
    }
    
    
    /**
     * 获取考勤审批列表
     * add by fyyang 20100709
     */
    public void findNewAttendApproveList() {
        try {
            LogUtil.log("Action:考勤记录查询开始", Level.INFO, null);
            TimeKeeperExamineForm obj = new TimeKeeperExamineForm();
            // 查询考勤记录列表
            
            WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
    		String entryIds = workflowService.getAvailableWorkflow(new String[] {
    				"bqWorkAttendance"}, employee.getWorkerCode());
            obj = remote.getExamineInfo(employee.getWorkerId(),examineDate, examineDept, employee.getEnterpriseCode(), p,"approve",entryIds);
          
            String str = JSONUtil.serialize(obj);
            write(str);
        } catch (JSONException jsone) {
            // 格式化出错
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:考勤记录查询失败", Level.SEVERE, null);
        } catch (DataChangeException e) {
            // 获取审核人为空的部门，并返回前台
            write(Constants.DATA_USING);
            LogUtil.log("Action:考勤记录查询失败", Level.SEVERE, null);
        } catch (ParseException e) {
            // 日期格式化出错
            LogUtil.log("Action:考勤记录查询失败", Level.SEVERE, null);
            write(Constants.DATA_FAILURE);
        } catch (RuntimeException e) {
            // 没有对应考勤日期
            LogUtil.log("EJB:没有对应的考勤日期。", Level.SEVERE, null);
            write(ERROE_X);
        } catch (Exception e) {
            // 操作失败
            LogUtil.log("Action:考勤记录查询失败", Level.SEVERE, e);
            e.printStackTrace();
        }
    }

}
