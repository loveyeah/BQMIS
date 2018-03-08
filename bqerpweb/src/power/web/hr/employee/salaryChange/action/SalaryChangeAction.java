/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.employee.salaryChange.action;

import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJSalayradjust;
import power.ejb.hr.HrJSalayradjustFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.HRCodeConstants;

/**
 * 薪酬变动单申报action
 * 
 * @author wujiao
 * @version 1.0
 */
public class SalaryChangeAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    /** 工种ejb之been */
    private HrJSalayradjust salaryBean;
    /** 工种ejb之remote */
    private HrJSalayradjustFacadeRemote salaryRemote;
    /** 查询参数: 开始行 */
    private int start;
    /** 查询参数: 结束行 */
    private int limit;
    /** 查询参数: 起薪开始日期 */
    private String strStartDate;
    /** 查询参数: 起薪结束日期 */
    private String strEndDate;
    /** 查询参数: 部门code */
    private String strDeptCode;
    /** 查询参数: 单据状态 */
    private String strDcmStatus;
    /** 查询参数: 标志位 */
    private int strflag = 1;
    /** 查询参数: 员工编码 */
    private String strWorkerCode;
    /** 查询参数: 员工id */
    private Long empId;
    /** 变动类别 */
    private String adjustType;
    /** 岗薪变化类别 */
    private String stationChangeType; 
    /** 设置岗位调动单ID */
    private Long stationremoveid;
    /** 变更前执行岗级 */
    private Long oldCheckStationGrade;
    /** 修改时间,排它用 */
    private Date updateTime;
    /** 变更前标准岗级 */
    private Long oldStationGrade;
    /** 变更前薪级 */
    private Long oldSalaryGrade;
    /** 薪酬申报单id*/
    private Long salaryId;
    /** 常量定义 已上报 */
    public static final String ALREADY_REPORT = "{success:true,msg:'report'}";
    


    /**
     * @return the salaryId
     */
    public Long getSalaryId() {
        return salaryId;
    }


    /**
     * @param salaryId the salaryId to set
     */
    public void setSalaryId(Long salaryId) {
        this.salaryId = salaryId;
    }


    /**
     * @return the adjustType
     */
    public String getAdjustType() {
        return adjustType;
    }


    /**
     * @param adjustType the adjustType to set
     */
    public void setAdjustType(String adjustType) {
        this.adjustType = adjustType;
    }


    /**
     * @return the stationChangeType
     */
    public String getStationChangeType() {
        return stationChangeType;
    }


    /**
     * @param stationChangeType the stationChangeType to set
     */
    public void setStationChangeType(String stationChangeType) {
        this.stationChangeType = stationChangeType;
    }


    /**
     * 构造函数
     */
    public SalaryChangeAction() {
        salaryRemote = (HrJSalayradjustFacadeRemote) factory
                .getFacadeRemote("HrJSalayradjustFacade");
    }

    
    /**
     * 页面加载，初始化时 / 点击查询按钮
     * 
     * @throws JSONException
     */
    public void getSalaryChangeList() {
        try {
             PageObject obj = new PageObject();    
            LogUtil.log("Action:薪酬变动单申报查询开始。", Level.INFO, null);
            // 取得查询参数: 企业编码
            String enterpriseCode = employee.getEnterpriseCode();
            
			// 条件查询
			obj = salaryRemote.getSalaryChangeList(strStartDate, strEndDate,
					strDeptCode, strDcmStatus, enterpriseCode, start, limit);
			
            // 查询结果为null,设置页面显示
            String str = "{\"list\":[],\"totalCount\":0}";
            
            if (obj == null) {
                write(str);
                // 不为null
            } else {
                if (obj.getList() == null) {
                    write(str);
                } else {
                    str = JSONUtil.serialize(obj);
                    write(str);
                }
            }
        } catch (SQLException sqle) {
            LogUtil.log("Action:薪酬变动单申报查询失败。", Level.SEVERE, sqle);
        } catch (JSONException jsone) {
            LogUtil.log("Action:薪酬变动单申报查询失败。", Level.SEVERE, jsone);
        }
    }
    
    /**
     * 根据人员id取得执行岗级，标准岗级，薪级
     * @param empId
     */
    @SuppressWarnings("unchecked")
    public void searchSalaryByEmployeeQuery() {
        LogUtil.log("Action:执行岗级，标准岗级，薪级查询开始", Level.INFO, null);
        try {
            // 按人员编码查找人员基本信息
            PageObject pob = salaryRemote.getEmpInfoMsg(empId.toString());
            if (pob.getList() != null && pob.getList().size() > 0) {
                write("{success:true,checkStationGrade:'" + pob.getList().get(0)
                        + "',stationGrade:'" + pob.getList().get(1)
                        + "',salaryGrade:'" + pob.getList().get(2)
                        +"'}");
            } else {
                write("{success:false}");
            }
            LogUtil.log("Action:执行岗级，标准岗级，薪级查询结束", Level.INFO, null);
        } catch (SQLException e) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:数据库操作失败", Level.SEVERE, e);
        }
    }
    /**
     * 删除数据
     * @param empCode
     * @throws DataChangeException 
     */
    @SuppressWarnings("unchecked")
    public void deleteSalaryRecord() throws DataChangeException {
        LogUtil.log("Action:删除薪酬变动申报单数据开始", Level.INFO, null);
        try {
            HrJSalayradjust salary = new HrJSalayradjust();
            // 根据id查找出此条数据
            salary = salaryRemote.findById(salaryId);
            // 逻辑删除
            salary.setIsUse(Constants.IS_USE_N);
            // 设置修改人
            salary.setLastModifiedBy(employee.getWorkerCode());
            // 排他: 更新时间
            salary.setLastModifiedDate(updateTime);
            
            salaryRemote.update(salary);
            LogUtil.log("Action:删除薪酬变动申报单数据结束", Level.INFO, null);
            write("{success:true,msg:'删除成功'}");
        } catch (SQLException e) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:删除薪酬变动申报单数据失败", Level.SEVERE, e);
        } catch(DataChangeException de) {
            LogUtil.log("Action:删除薪酬变动申报单数据失败(数据已修改)", Level.INFO, de);
            write(Constants.DATA_USING);
        } 
    }
    /**
     * 新增数据
     * @param empCode
     */
    @SuppressWarnings("unchecked")
    public void addSalaryRecord() {
        LogUtil.log("Action:新增薪酬变动申报单数据开始", Level.INFO, null);
        try {
            HrJSalayradjust salary = new HrJSalayradjust();
            
            boolean result = salaryRemote.empInfoIsReapeat(empId,employee.getEnterpriseCode());
            if(result) {
                write("{success:true,msg:'empRepeat'}");
                return;
            }
            // 员工id
            salary.setEmpId(empId);
            // 起薪日期
            salary.setDoDate(salaryBean.getDoDate());
            // 变更前执行岗级
            salary.setOldCheckStationGrade(oldCheckStationGrade);
            // 变更后执行岗级
            salary.setNewCheckStationGrade(salaryBean.getNewCheckStationGrade());
            // 变更前标准岗级
            salary.setOldStationGrade(oldStationGrade);
            // 变更后标准岗级
            salary.setNewStationGrade(salaryBean.getNewStationGrade());
            // 变更前薪级
            salary.setOldSalaryGrade(oldSalaryGrade);
            // 变更后薪级
            salary.setNewSalaryGrade(salaryBean.getNewSalaryGrade());
            // 变动类别
            salary.setAdjustType(adjustType);
            // 岗薪变化类别
            salary.setStationChangeType(stationChangeType);
            // 原因
            salary.setReason(salaryBean.getReason());
            // 设置单据状态为0（未上报）
            salary.setDcmState(HRCodeConstants.DCM_STATUS_UNREPORT);
            // 备注
            salary.setMemo(salaryBean.getMemo());
            // 设置是否使用为Y
            salary.setIsUse(Constants.IS_USE_Y);
           // 设置更新时间
            salary.setLastModifiedDate(new Date());
            // 设置更新人
            salary.setLastModifiedBy(employee.getWorkerCode());
            // 设置插入人
            salary.setInsertby(employee.getWorkerCode());
            // 设置插入时间
            salary.setInsertdate(new Date());
            // 设置企业编码
            salary.setEnterpriseCode(employee.getEnterpriseCode());  
            // 设置岗位调动单ID
            salary.setStationremoveid(stationremoveid);
            // 增加信息
            salaryRemote.save(salary);
            LogUtil.log("Action:新增薪酬变动申报单数据结束", Level.INFO, null);
            write("{success:true,msg:'addSuccessful'}");
            
        } catch (SQLException e) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:新增薪酬变动申报单数据失败", Level.SEVERE, e);
        }
    }
    /**
     * 修改数据
     * @param empCode
     * @throws DataChangeException 
     */
    @SuppressWarnings("unchecked")
    public void updateSalaryRecord() throws DataChangeException {
        LogUtil.log("Action:修改薪酬变动申报单数据开始", Level.INFO, null);
        try {
            HrJSalayradjust salary = new HrJSalayradjust();
            // 把画面上的实体的值复制过来
            salary = salaryRemote.findById(salaryBean.getSalayradjustid());
            // 起薪日期
            salary.setDoDate(salaryBean.getDoDate());
            // 变更后执行岗级
            salary.setNewCheckStationGrade(salaryBean.getNewCheckStationGrade());
            // 变更后标准岗级
            salary.setNewStationGrade(salaryBean.getNewStationGrade());
            // 变更后薪级
            salary.setNewSalaryGrade(salaryBean.getNewSalaryGrade());
            // 变动类别
            salary.setAdjustType(adjustType);
            // 岗薪变化类别
            salary.setStationChangeType(stationChangeType);
            // 原因
            salary.setReason(salaryBean.getReason());
            // // 设置单据状态为0（未上报）
//            salary.setDcmState(HRCodeConstants.DCM_STATUS_UNREPORT);
            // 备注
            salary.setMemo(salaryBean.getMemo());
           // 设置更新时间
            salary.setLastModifiedDate(updateTime);
            // 设置更新人
            salary.setLastModifiedBy(employee.getWorkerCode());
            salaryRemote.update(salary);
            LogUtil.log("Action:修改薪酬变动申报单数据结束", Level.INFO, null);
            write("{success:true,msg:'changeSuccessful'}");
        } catch(DataChangeException de) {
            LogUtil.log("Action:修改薪酬变动申报单数据失败(数据已修改)", Level.INFO, de);
            write(Constants.DATA_USING);
        } catch (SQLException e) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:修改薪酬变动申报单数据失败", Level.SEVERE, e);
        }
    }
    /**上报数据
     * @param empCode
     * @throws DataChangeException 
     */
    @SuppressWarnings("unchecked")
    public void reportSalaryRecord() {
        LogUtil.log("Action:上报薪酬变动申报单数据开始", Level.INFO, null);
        try {
            HrJSalayradjust salary = new HrJSalayradjust();
            // 根据id查找出此条数据
            salary = salaryRemote.findById(salaryId);
            // 设置修改人
            salary.setLastModifiedBy(employee.getWorkerCode());
            // 设置单据状态为已上报
            salary.setDcmState(HRCodeConstants.DCM_STATUS_REPORTED);
            // 排他: 更新时间
            salary.setLastModifiedDate(updateTime);
            
            salaryRemote.update(salary);
            LogUtil.log("Action:上报薪酬变动申报单数据结束", Level.INFO, null);
            write("{success:true,msg:'reportSuccess'}");
        } catch (SQLException e) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:上报薪酬变动申报单数据失败", Level.SEVERE, e);
            write(Constants.SQL_FAILURE);
        }catch(DataChangeException de) {
            LogUtil.log("Action:修改薪酬变动申报单数据失败(数据已修改)", Level.INFO, de);
            write(Constants.DATA_USING);
        } 
    }
    /**
     * @return the salaryBean
     */
    public HrJSalayradjust getSalaryBean() {
        return salaryBean;
    }

    /**
     * @param salaryBean the salaryBean to set
     */
    public void setSalaryBean(HrJSalayradjust salaryBean) {
        this.salaryBean = salaryBean;
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
     * @return the strDeptCode
     */
    public String getStrDeptCode() {
        return strDeptCode;
    }

    /**
     * @param strDeptCode the strDeptCode to set
     */
    public void setStrDeptCode(String strDeptCode) {
        this.strDeptCode = strDeptCode;
    }

    /**
     * @return the strDcmStatus
     */
    public String getStrDcmStatus() {
        return strDcmStatus;
    }

    /**
     * @param strDcmStatus the strDcmStatus to set
     */
    public void setStrDcmStatus(String strDcmStatus) {
        this.strDcmStatus = strDcmStatus;
    }


    /**
     * @return the strflag
     */
    public int getStrflag() {
        return strflag;
    }


    /**
     * @param strflag the strflag to set
     */
    public void setStrflag(int strflag) {
        this.strflag = strflag;
    }


    /**
     * @return the strWorkerCode
     */
    public String getStrWorkerCode() {
        return strWorkerCode;
    }


    /**
     * @param strWorkerCode the strWorkerCode to set
     */
    public void setStrWorkerCode(String strWorkerCode) {
        this.strWorkerCode = strWorkerCode;
    }


    /**
     * @return the empId
     */
    public Long getEmpId() {
        return empId;
    }


    /**
     * @param empId the empId to set
     */
    public void setEmpId(Long empId) {
        this.empId = empId;
    }


    /**
     * @return the stationremoveid
     */
    public Long getStationremoveid() {
        return stationremoveid;
    }


    /**
     * @param stationremoveid the stationremoveid to set
     */
    public void setStationremoveid(Long stationremoveid) {
        this.stationremoveid = stationremoveid;
    }


    /**
     * @return the oldCheckStationGrade
     */
    public Long getOldCheckStationGrade() {
        return oldCheckStationGrade;
    }


    /**
     * @param oldCheckStationGrade the oldCheckStationGrade to set
     */
    public void setOldCheckStationGrade(Long oldCheckStationGrade) {
        this.oldCheckStationGrade = oldCheckStationGrade;
    }


    /**
     * @return the oldStationGrade
     */
    public Long getOldStationGrade() {
        return oldStationGrade;
    }


    /**
     * @param oldStationGrade the oldStationGrade to set
     */
    public void setOldStationGrade(Long oldStationGrade) {
        this.oldStationGrade = oldStationGrade;
    }


    /**
     * @return the oldSalaryGrade
     */
    public Long getOldSalaryGrade() {
        return oldSalaryGrade;
    }


    /**
     * @param oldSalaryGrade the oldSalaryGrade to set
     */
    public void setOldSalaryGrade(Long oldSalaryGrade) {
        this.oldSalaryGrade = oldSalaryGrade;
    }


    /**
     * @return the updateTime
     */
    public Date getUpdateTime() {
        return updateTime;
    }


    /**
     * @param updateTime the updateTime to set
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    

    
}
