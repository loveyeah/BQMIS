/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.web.hr.employee.empmovequery.action;

import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJStationremoveFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.WriteXls;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
* 员工调动查询 Action
* 
* @author huangweijie
* @version 1.0
*/
public class EmpMoveQueryAction extends AbstractAction {

   private static final long serialVersionUID = 1L;
   
   // remote 
   /** 岗位调动单 remote */
   private HrJStationremoveFacadeRemote remote;
   

   /** 画面参数开始页 */
   public Long start;
   /** 画面参数页面最大值 */
   public Long limit;
   /** 开始日期 */
   public String startDate;
   /** 结束日期 */
   public String endDate;
   /** 所属部门名称或调动前部门code */
   public String deptBFCode;
   /** 借调部门名称或调动后部门code */
   public String deptAFCode;
   /** 岗位调动单单据状态 */
   public String dcmState;
   /** 员工借调登记是否已回 */
   public String ifBack;
   /** 员工借调登记单据状态 */
   public String dcmStatus;
   // 导出时要用的业务名
   /** 班组调动查询 */
   public static String WORK_NAME_BZMOVE = "班组调动查询";
   /** 员工调动查询 */
   public static String WORK_NAME_EMPMOVE = "员工调动查询";
   /** 员工借调查询 */
   public static String WORK_NAME_EMPBORROW = "员工借调查询";
   // 导出时的business id
   /** 班组调动查询 */
   public static String BUSINESS_BZMOVE = "PE007BZ";
   /** 员工调动查询 */
   public static String BUSINESS_EMPMOVE = "PE007RM";
   /** 员工借调查询 */
   public static String BUSINESS_EMPBORROW = "PE007BR";
   
   /**
    * 初始化函数，用于实例化remote
    */
   public EmpMoveQueryAction() {
       /** 岗位调动单 remote */
       remote = (HrJStationremoveFacadeRemote) 
       factory.getFacadeRemote("HrJStationremoveFacade");
   }
   
   /**
    * 取得班组调动信息list
    */
   public void getBanZuList() {
       try {
           LogUtil.log("Action:查询班组调动信息开始", Level.INFO, null);
           PageObject pobj = new PageObject();
           int intStart = Integer.parseInt(start.toString());
           int intLimit = Integer.parseInt(limit.toString());
           // 把查询条件记录下来，导出的时候用
	       	request.getSession().setAttribute("startDateBZ", startDate);
	       	request.getSession().setAttribute("endDateBZ", endDate);
	       	request.getSession().setAttribute("deptBFCodeBZ", deptBFCode);
	       	request.getSession().setAttribute("deptAFCodeBZ", deptAFCode);
           // 查询操作
           pobj = remote.getBanZuList(startDate, endDate, deptBFCode, 
                   deptAFCode, employee.getEnterpriseCode(), intStart, intLimit);
           // 解析字符串
           String str = null;
           if(null == pobj.getList() || 0 == pobj.getList().size()) {
               str = "{\"list\":[],\"totalCount\":0}";
           } else {
               str = JSONUtil.serialize(pobj);
           }
           LogUtil.log("Action:查询班组调动信息结束", Level.INFO, null);
           write(str);
       } catch (JSONException jsone) {
           LogUtil.log("Action:查询班组调动信息失败", Level.SEVERE, jsone);
           write(Constants.DATA_FAILURE);
       } catch (RuntimeException re) {
           LogUtil.log("Action:查询班组调动信息失败", Level.INFO, re);
           write(Constants.SQL_FAILURE);
       }
   }
   
   /**
    * 取得员工调动信息list
    */
   public void getEmpMoveList() {
       try {
           LogUtil.log("Action:查询员工调动信息开始", Level.INFO, null);
           PageObject pobj = new PageObject();
           int intStart = Integer.parseInt(start.toString());
           int intLimit = Integer.parseInt(limit.toString());
           // 把查询条件记录下来，导出的时候用
	       	request.getSession().setAttribute("startDateRM", startDate);
	       	request.getSession().setAttribute("dcmStateRM", dcmState);
	       	request.getSession().setAttribute("endDateRM", endDate);
	       	request.getSession().setAttribute("deptBFCodeRM", deptBFCode);
	       	request.getSession().setAttribute("deptAFCodeRM", deptAFCode);
           // 查询操作
           pobj = remote.getEmpMoveList(startDate, endDate, dcmState, 
                   deptBFCode, deptAFCode, employee.getEnterpriseCode(),
                   intStart, intLimit);
           // 解析字符串
           String str = null;
           if(null == pobj.getList() || 0 == pobj.getList().size()) {
               str = "{\"list\":[],\"totalCount\":0}";
           } else {
               str = JSONUtil.serialize(pobj);
           }
           LogUtil.log("Action:查询员工调动信息结束", Level.INFO, null);
           write(str);
       } catch (JSONException jsone) {
           LogUtil.log("Action:查询员工调动信息失败", Level.SEVERE, jsone);
           write(Constants.DATA_FAILURE);
       } catch (RuntimeException re) {
           LogUtil.log("Action:查询员工调动信息失败", Level.INFO, re);
           write(Constants.SQL_FAILURE);
       }
   }
   
   /**
    * 取得员工借调信息list
    */
   public void getEmpBorrowList() {
       try {
           LogUtil.log("Action:查询员工借调信息开始", Level.INFO, null);
           PageObject pobj = new PageObject();
           int intStart = Integer.parseInt(start.toString());
           int intLimit = Integer.parseInt(limit.toString());
           // 把查询条件记录下来，导出的时候用
	       	request.getSession().setAttribute("startDateRB", startDate);
	       	request.getSession().setAttribute("endDateRB", endDate);
	       	request.getSession().setAttribute("deptBFCodeRB", deptBFCode);
	       	request.getSession().setAttribute("deptAFCodeRB", deptAFCode);
	       	request.getSession().setAttribute("ifBackRB", ifBack);
	       	request.getSession().setAttribute("dcmStatusRB", dcmStatus);
           // 查询操作
           pobj = remote.getEmpBorrowList(startDate, endDate, deptBFCode, 
                   deptAFCode, ifBack, dcmStatus, employee.getEnterpriseCode(),
                   intStart, intLimit);
           // 解析字符串
           String str = null;
           if(null == pobj.getList() || 0 == pobj.getList().size()) {
               str = "{\"list\":[],\"totalCount\":0}";
           } else {
               str = JSONUtil.serialize(pobj);
           }
           LogUtil.log("Action:查询员工借调信息结束", Level.INFO, null);
           write(str);
       } catch (JSONException jsone) {
           LogUtil.log("Action:查询员工借调信息失败", Level.SEVERE, jsone);
           write(Constants.DATA_FAILURE);
       } catch (RuntimeException re) {
           LogUtil.log("Action:查询员工借调信息失败", Level.INFO, re);
           write(Constants.SQL_FAILURE);
       }
   }
   
   /**
    * 将班组调动信息导出到excel
    * @throws Exception
    */
   @SuppressWarnings("unchecked")
    public void banzuExportFile() throws Exception {
        LogUtil.log("Action:班组调动信息导出Excel文件开始。", Level.INFO, null);
        try {
            // 获得需要导出的excel文件内容
        	// 得到查询时选择的参数
        	startDate = (String)request.getSession().getAttribute("startDateBZ");
        	endDate = (String)request.getSession().getAttribute("endDateBZ");
        	deptBFCode = (String)request.getSession().getAttribute("deptBFCodeBZ");
        	deptAFCode = (String)request.getSession().getAttribute("deptAFCodeBZ");
        	// 查询操作
        	PageObject pobj = remote.getBanZuList(startDate, endDate, deptBFCode, 
                    deptAFCode, employee.getEnterpriseCode());

            WriteXls testWrite = new WriteXls(BUSINESS_BZMOVE, pobj.getList());
    		// 设置机能名
            testWrite.setBusinessName(WORK_NAME_BZMOVE);
            testWrite.xlsExportFile(response);
            LogUtil.log("Action:班组调动信息导出Excel文件结束。", Level.INFO, null);
        }
        catch (RuntimeException e) {
            LogUtil.log("Action:班组调动信息导出Excel文件失败", Level.INFO, e);
            write(Constants.IO_FAILURE);
        }
    }
   
   /**
    * 将人员调动信息导出到excel
    * @throws Exception
    */
   @SuppressWarnings("unchecked")
    public void removeExportFile() throws Exception {
        LogUtil.log("Action:人员调动信息导出Excel文件开始。", Level.INFO, null);

        try {
            // 获得需要导出的excel文件内容
        	// 得到查询时选择的参数
        	startDate = (String)request.getSession().getAttribute("startDateRM");
        	endDate = (String)request.getSession().getAttribute("endDateRM");
        	dcmState = (String)request.getSession().getAttribute("dcmStateRM");
        	deptBFCode = (String)request.getSession().getAttribute("deptBFCodeRM");
        	deptAFCode = (String)request.getSession().getAttribute("deptAFCodeRM");
            // 查询操作
        	PageObject pobj = remote.getEmpMoveList(startDate, endDate, dcmState, 
                    deptBFCode, deptAFCode, employee.getEnterpriseCode());

            WriteXls testWrite = new WriteXls(BUSINESS_EMPMOVE, pobj.getList());
    		// 设置机能名
            testWrite.setBusinessName(WORK_NAME_EMPMOVE);
            testWrite.xlsExportFile(response);
            LogUtil.log("Action:人员调动信息导出Excel文件结束。", Level.INFO, null);
        }
        catch (RuntimeException e) {
            LogUtil.log("Action:人员调动信息导出Excel文件失败", Level.INFO, e);
            write(Constants.IO_FAILURE);
        }
    }
   
   /**
    * 将人员借调信息导出到excel
    * @throws Exception
    */
   @SuppressWarnings("unchecked")
    public void borrowExportFile() throws Exception {
        LogUtil.log("Action:人员借调信息导出Excel文件开始。", Level.INFO, null);
        try {
            // 获得需要导出的excel文件内容
        	// 得到查询时选择的参数
        	startDate = (String)request.getSession().getAttribute("startDateRB");
        	endDate = (String)request.getSession().getAttribute("endDateRB");
        	deptBFCode = (String)request.getSession().getAttribute("deptBFCodeRB");
        	deptAFCode = (String)request.getSession().getAttribute("deptAFCodeRB");
        	ifBack = (String)request.getSession().getAttribute("ifBackRB");
        	dcmStatus = (String)request.getSession().getAttribute("dcmStatusRB");

            PageObject pobj = remote.getEmpBorrowList(startDate, endDate, deptBFCode, 
                    deptAFCode, ifBack, dcmStatus, employee.getEnterpriseCode());

            WriteXls testWrite = new WriteXls(BUSINESS_EMPBORROW, pobj.getList());
    		// 设置机能名
            testWrite.setBusinessName(WORK_NAME_EMPBORROW);
            testWrite.xlsExportFile(response);
            LogUtil.log("Action:人员借调信息导出Excel文件结束。", Level.INFO, null);
        }
        catch (RuntimeException e) {
            LogUtil.log("Action:人员借调信息导出Excel文件失败", Level.INFO, e);
            write(Constants.IO_FAILURE);
        }
    }
}

