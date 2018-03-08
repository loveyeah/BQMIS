/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.document.empdocument.empstationmaintain.action;

import java.sql.SQLException;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJDepstationcorrespondFacadeRemote;
import power.ejb.hr.HrJEmpStationFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 员工岗位维护
 * 
 * @author zhaozhijie
 * @version 1.0
 */
public class EmpStationMaintainAction extends AbstractAction {

    /** serial id */
    private static final long serialVersionUID = 1L;
    /** 职工岗位信息登记ejb远程维护对象 */
    protected HrJEmpStationFacadeRemote remote;
    /** 部门岗位对应表ejb远程维护对象 */
    protected HrJDepstationcorrespondFacadeRemote deptRemote;
    /** 常量pageObject为空 */
    public static final String PAGEOBJECT_NULL = "{\"list\":[],\"totalCount\":0}";
    /** 部门id */
    private String deptId;
    /** 人员id */
    private String empId;

    /**
     * 构造函数
     */
    public EmpStationMaintainAction() {
        // 职工岗位信息登记ejb远程维护对象
        remote = (HrJEmpStationFacadeRemote) factory
        .getFacadeRemote("HrJEmpStationFacade");
        // 部门岗位对应表ejb远程维护对象
        deptRemote = (HrJDepstationcorrespondFacadeRemote) factory
        .getFacadeRemote("HrJDepstationcorrespondFacade");
    }

    /**
     * 查询部门岗位信息
     */
    public void getStationMaintain() {
        LogUtil.log("Action:部门岗位信息查询开始", Level.INFO, null);
        try {
            PageObject obj = deptRemote.findStationMaintain(deptId, empId, employee.getEnterpriseCode());
            if(obj.getTotalCount() == null) {
                write(PAGEOBJECT_NULL);
            } else {
                write(JSONUtil.serialize(obj));
            }
            LogUtil.log("Action:部门岗位信息查询结束", Level.INFO, null);
        } catch(SQLException sqle) {
            LogUtil.log("Action:部门岗位信息查询失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
        }catch (JSONException jsone) {
            LogUtil.log("Action:部门岗位信息查询失败", Level.SEVERE, jsone);
            write(Constants.DATA_FAILURE);
        }catch(Exception e) {
            LogUtil.log("Action:职工岗位信息查询失败", Level.SEVERE, e);
            write(Constants.SQL_FAILURE);
        }
    }

    /**
     * 查询职工岗位信息
     */
    public void getEmpStationInfo() {
        LogUtil.log("Action:职工岗位信息查询开始", Level.INFO, null);
        try {
            PageObject obj = remote.findEmpStationInfo(empId, employee.getEnterpriseCode());
            if(obj.getTotalCount() == null) {
                write(PAGEOBJECT_NULL);
            } else {
                write(JSONUtil.serialize(obj));
            }
            LogUtil.log("Action:职工岗位信息查询结束", Level.INFO, null);
        } catch(SQLException sqle) {
            LogUtil.log("Action:职工岗位信息查询失败", Level.SEVERE, sqle);
            write(Constants.SQL_FAILURE);
        }catch (JSONException jsone) {
            LogUtil.log("Action:职工岗位信息查询失败", Level.SEVERE, jsone);
            write(Constants.DATA_FAILURE);
        } catch(Exception e) {
            LogUtil.log("Action:职工岗位信息查询失败", Level.SEVERE, e);
            write(Constants.SQL_FAILURE);
        }
    }

    /**
     * @return 部门id 
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * @param 部门id 
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**
     * @return 人员id
     */
    public String getEmpId() {
        return empId;
    }

    /**
     * @param 人员id
     */
    public void setEmpId(String empId) {
        this.empId = empId;
    }

}
