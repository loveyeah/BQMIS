/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.employee.stationremove.action;

import java.util.Date;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJStationremove;
import power.ejb.hr.HrJStationremoveFacadeRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.StationRemoveInfo;
import power.ejb.hr.StationRemoveInfoFacadeRemote;
import power.web.comm.*;

/**
 * 班组人员调动Action
 * 
 * @author zhengzhipeng
 * @version 1.0
 */

public class StationRemoveAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    /** 班组人员调动信息been */
    private StationRemoveInfo stationRemoveInfoBeen;
    /** 岗位调动信息been */
    private HrJStationremove stationRemoveBeen;
    /** 岗位调动Remote */
    private HrJStationremoveFacadeRemote stationRemoveRemote;
    /** 班组人员调动远程接口 */
    private StationRemoveInfoFacadeRemote stationRemoveInfoRemote;
    /** 查询结果为null,传到前台的string */
    private static String STR_JSON_NULL = "{\"list\":[],\"totalCount\":0}";
    /** 操作后，传到前台的string：1：成功 0：失败 2：重复 */
    private static String STR_SUCCESS_0 = "{success:true,flag:'0'}";
    private static String STR_SUCCESS_1 = "{success:true,flag:'1'}";
    private static String STR_SUCCESS_2 = "{success:true,flag:'2'}";

    /** 查询参数：调动开始日期 */
    private String startDate;
    /** 查询参数：调动结束日期 */
    private String endDate;
    /** 查询参数：调动前部门ID */
    private String oldDeptId;
    /** 查询参数：调动后部门ID */
    private String newDeptId;
    /** 查询参数: 开始行 */
    private int start;
    /** 查询参数: 开始行 */
    private int limit;

    /** 排他参数：修改时间 */
    private String updateTime;

    /** 员工姓名加载参数 */
    private String deptId;

    /**
     * 构造函数
     */
    public StationRemoveAction() {
        // 远程处理对象的取得
        stationRemoveInfoRemote = (StationRemoveInfoFacadeRemote) factory
                .getFacadeRemote("StationRemoveInfoFacade");
        stationRemoveRemote = (HrJStationremoveFacadeRemote) factory
                .getFacadeRemote("HrJStationremoveFacade");
    }

    /**
     * 查询
     */
    public void getStationRemoveInfoList() {
        try {
            LogUtil.log("Action:班组人员调动查询开始", Level.INFO, null);
            // 取得查询参数: 企业编码
            String enterpriseCode = employee.getEnterpriseCode();
            // 根据查询条件，取得相应信息
            PageObject object = stationRemoveInfoRemote
                    .getStationRemoveInfoList(startDate, endDate, oldDeptId,
                            newDeptId, enterpriseCode, start, limit);
            // 查询结果为null,设置页面显示
            String str = STR_JSON_NULL;
            if (object == null) {
                write(str);
                // 不为null
            } else {
                if (object.getList() == null) {
                    write(str);
                } else {
                    str = JSONUtil.serialize(object);
                    write(str);
                }
            }
            LogUtil.log("Action:班组人员调动查询结束", Level.INFO, null);
        } catch (Exception e) {
            LogUtil.log("Action:班组人员调动失败", Level.SEVERE, e);
        }

    }

    /**
     * 通过部门id获取员工姓名列表
     */
    public void getEmpNameListByDepId() throws JSONException {
        // 根据企业编码，部门id取得所有物料类型
        PageObject obj = new PageObject();
        obj = stationRemoveInfoRemote.getEmpNameList(employee
                .getEnterpriseCode(), deptId);
        String str = JSONUtil.serialize(obj);
        write(str);
    }

    /**
     * 新增
     */
    public void addStationRemove() {
        try {
            LogUtil.log("Action:班组人员调动新增开始", Level.INFO, null);
            // 岗位调动类别ID
            stationRemoveBeen.setStationMoveTypeId(0L);
            // 设定企业编码
            stationRemoveBeen.setEnterpriseCode(employee.getEnterpriseCode());
            // 设定修改者
            stationRemoveBeen.setLastModifiedBy(employee.getWorkerCode());
            // 设定是否可用
            stationRemoveBeen.setIsUse(Constants.IS_USE_Y);
            // 记录人
            stationRemoveBeen.setInsertby(employee.getWorkerCode());
            // 记录日期
            stationRemoveBeen.setInsertdate(new Date());
            // 岗位调动单ID，修改时间在保存时设定
            stationRemoveInfoRemote.save(stationRemoveBeen);
            // 保存成功
            write(STR_SUCCESS_1);
            LogUtil.log("Action:班组人员调动新增结束", Level.INFO, null);
        } catch (Exception e) {
            // 保存失败
            write(STR_SUCCESS_0);
            LogUtil.log("Action:班组人员调动新增失败", Level.SEVERE, e);
        }
    }

    /**
     * 修改
     */
    public void updateStationRemove() {
        try {
            LogUtil.log("Action:班组人员调动修改开始", Level.INFO, null);
            // 获得岗位调动ID
            Long stationRemoveId = stationRemoveBeen.getStationremoveid();
            // 通过岗位调动ID获得实体信息
            HrJStationremove OldRemoveBeen = stationRemoveRemote
                    .findById(stationRemoveId);
            // 设置修改值：调动日期(画面)
            OldRemoveBeen.setRemoveDate(stationRemoveBeen.getRemoveDate());
            // 设置修改值：执行日期(画面)
            OldRemoveBeen.setDoDate(stationRemoveBeen.getDoDate());
            // 设置修改值：备注(画面)
            OldRemoveBeen.setMemo(stationRemoveBeen.getMemo());
            // 设置修改值：上次修改时间(画面) 用于排他
            OldRemoveBeen.setLastModifiedDate(stationRemoveBeen
                    .getLastModifiedDate());
            // 设置: 本次修改者
            OldRemoveBeen.setLastModifiedBy(employee.getWorkerCode());

            // 保存(含本次修改时间)排他
            stationRemoveInfoRemote.update(OldRemoveBeen);

            // 保存成功
            write(STR_SUCCESS_1);
            LogUtil.log("Action:班组人员调动修改结束", Level.INFO, null);
        } catch (DataChangeException e) {
            LogUtil.log("Action:退回处理失败。", Level.SEVERE, null);
            // 排他错误
            write(STR_SUCCESS_2);
        } catch (Exception e) {
            // 保存失败
            write(STR_SUCCESS_0);
            LogUtil.log("Action:班组人员调动修改失败", Level.SEVERE, e);
        }
    }

    /**
     * @return 修改时间
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * @param 修改时间
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the oldDeptId
     */
    public String getOldDeptId() {
        return oldDeptId;
    }

    /**
     * @param oldDeptId
     *            the oldDeptId to set
     */
    public void setOldDeptId(String oldDeptId) {
        this.oldDeptId = oldDeptId;
    }

    /**
     * @return the newDeptId
     */
    public String getNewDeptId() {
        return newDeptId;
    }

    /**
     * @param newDeptId
     *            the newDeptId to set
     */
    public void setNewDeptId(String newDeptId) {
        this.newDeptId = newDeptId;
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
     * @return the stationRemoveInfoBeen
     */
    public StationRemoveInfo getStationRemoveInfoBeen() {
        return stationRemoveInfoBeen;
    }

    /**
     * @param stationRemoveInfoBeen
     *            the stationRemoveInfoBeen to set
     */
    public void setStationRemoveInfoBeen(StationRemoveInfo stationRemoveInfoBeen) {
        this.stationRemoveInfoBeen = stationRemoveInfoBeen;
    }

    /**
     * @return the stationRemoveBeen
     */
    public HrJStationremove getStationRemoveBeen() {
        return stationRemoveBeen;
    }

    /**
     * @param stationRemoveBeen
     *            the stationRemoveBeen to set
     */
    public void setStationRemoveBeen(HrJStationremove stationRemoveBeen) {
        this.stationRemoveBeen = stationRemoveBeen;
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
}
