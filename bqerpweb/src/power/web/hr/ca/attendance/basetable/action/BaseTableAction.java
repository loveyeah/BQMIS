/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.ca.attendance.basetable.action;

import java.sql.SQLException;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.ca.BaseTableInfo;
import power.ejb.hr.ca.BaseTableInfoFacadeRemote;
import power.ejb.hr.ca.HrCOvertime;
import power.ejb.hr.ca.HrCOvertimeFacadeRemote;
import power.ejb.hr.ca.HrCWorkshift;
import power.ejb.hr.ca.HrCWorkshiftFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 基础表维护action
 * 
 * @author zhengzhipeng
 * @version 1.0
 */
public class BaseTableAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    /** 查询结果为null,传到前台的string */
    private static String STR_JSON_NULL = "{\"list\":[],\"totalCount\":0}";
    /** 新增Flag */
    private static String STR_ADDFLAG_1 = "1";
    /** 修改Flag */
    private static String STR_ADDFLAG_0 = "0";
    /** 表名1："加班类别表" */
    private static String STR_TABLENAME_1 = "加班类别表";
    /** 表名2："运行班类别表" */
    private static String STR_TABLENAME_2 = "运行班类别表";
    /** 是否使用：使用：Y */
    private static String STR_ISUSE_Y = "Y";
    /** 基础表维护Remote */
    private BaseTableInfoFacadeRemote baseTableRemote;
    /** 加班类别维护Remote */
    private HrCOvertimeFacadeRemote overTimeRemote;
    /** 运行班类别维护Remote */
    private HrCWorkshiftFacadeRemote workShiftRemote;

    /** 查询参数: 开始行 */
    private int start;
    /** 查询参数: 开始行 */
    private int limit;
    /** 查询参数: 基础表名 */
    private String tableName;
    /** 基础表been */
    private BaseTableInfo baseTableBeen;

    /** 参数: 基础表ID */
    private String recordId;
    /** 参数: 加班类别 or 运行班类别 */
    private String recordTypeName;
    /** 参数: 是否发放费用 or 津贴标准 */
    private String ifFeeOrShiftFee;
    /** 参数: 考勤标志 */
    private String recordMark;

    /** 参数: 新增or修改 */
    private String addOrUpdate;

    /**
     * @return the baseTableBeen
     */
    public BaseTableInfo getBaseTableBeen() {
        return baseTableBeen;
    }

    /**
     * @param baseTableBeen
     *            the baseTableBeen to set
     */
    public void setBaseTableBeen(BaseTableInfo baseTableBeen) {
        this.baseTableBeen = baseTableBeen;
    }

    /**
     * 构造函数
     */
    public BaseTableAction() {
        baseTableRemote = (BaseTableInfoFacadeRemote) factory
                .getFacadeRemote("BaseTableInfoFacade");
        overTimeRemote = (HrCOvertimeFacadeRemote) factory
                .getFacadeRemote("HrCOvertimeFacade");
        workShiftRemote = (HrCWorkshiftFacadeRemote) factory
                .getFacadeRemote("HrCWorkshiftFacade");
    }

    /**
     * 基础表维护初始化,查询
     */
    public void getBaseTableRecordList() {
        try {
            LogUtil.log("Action:基础表维护(" + tableName + ")初始化开始", Level.INFO,
                    null);
            // 取得查询参数: 企业编码
            String enterpriseCode = employee.getEnterpriseCode();
            PageObject obj = baseTableRemote.getBaseTableRecordList(tableName,
                    enterpriseCode, start, limit);
            // 查询结果为null,设置页面显示
            String str = STR_JSON_NULL;
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
            LogUtil.log("Action:基础表维护(" + tableName + ")初始化结束", Level.INFO,
                    null);
        } catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:基础表维护(" + tableName + ")初始化失败", Level.SEVERE,
                    jsone);
        } catch (SQLException pe) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:基础表维护(" + tableName + ")初始化失败", Level.SEVERE,
                    pe);
        } catch (Exception e) {
            write(Constants.SQL_FAILURE);
            LogUtil
                    .log("Action:基础表维护(" + tableName + ")初始化失败", Level.SEVERE,
                            e);
        }
    }

    /**
     * 新增OR修改
     */
    public void saveOrUpdateBaseTable() {
        if (STR_ADDFLAG_1.equals(addOrUpdate)) {
            // 新增
            saveBaseTable();
        } else if (STR_ADDFLAG_0.equals(addOrUpdate)) {
            // 修改
            updateBaseTable();
        }
    }

    /**
     * 修改
     */
    private void updateBaseTable() {
        try {
            LogUtil
                    .log("Action:基础表维护(" + tableName + ")修改开始", Level.INFO,
                            null);
            if (STR_TABLENAME_1.equals(tableName)) {
                // 根据画面选择项目的基础表维护Id找到类型实体
                HrCOvertime overTimeBeen = overTimeRemote.findById(Long
                        .parseLong(recordId));
                // 设定加班类别
                overTimeBeen.setOvertimeType(recordTypeName);
                // 设定是否发放费用
                overTimeBeen.setIfOvertimeFee(ifFeeOrShiftFee);
                // 设定考勤标志
                overTimeBeen.setOvertimeMark(recordMark);
                // 设定修改者
                overTimeBeen.setLastModifiyBy(employee.getWorkerCode());
                // 设定企业编码
                overTimeBeen.setEnterpriseCode(employee.getEnterpriseCode());
                // 保存(含修改时间)
                overTimeRemote.update(overTimeBeen);

            } else if (STR_TABLENAME_2.equals(tableName)) {
                // 根据画面选择项目的基础表维护Id找到类型实体
                HrCWorkshift workShiftBeen = workShiftRemote.findById(Long
                        .parseLong(recordId));
                // 设定运行班类别
                workShiftBeen.setWorkShift(recordTypeName);
                // 设定津贴标准
                if (ifFeeOrShiftFee != null
                        && !Constants.BLANK_STRING.equals(ifFeeOrShiftFee)) {
                    workShiftBeen.setWorkShitFee(Double
                            .parseDouble(ifFeeOrShiftFee));
                } else {
                    workShiftBeen.setWorkShitFee(null);
                }
                // 设定考勤标志
                workShiftBeen.setWorkShiftMark(recordMark);
                // 设定修改者
                workShiftBeen.setLastModifiyBy(employee.getWorkerCode());
                // 设定企业编码
                workShiftBeen.setEnterpriseCode(employee.getEnterpriseCode());
                // 保存(含修改时间)
                workShiftRemote.update(workShiftBeen);
            }
            // 保存成功
            write(Constants.MODIFY_SUCCESS);
            LogUtil
                    .log("Action:基础表维护(" + tableName + ")修改结束", Level.INFO,
                            null);
        } catch (Exception e) {
            // 保存失败
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:基础表维护(" + tableName + ")修改失败", Level.SEVERE, e);
        }
    }

    /**
     * 新增
     */
    private void saveBaseTable() {
        try {
            LogUtil
                    .log("Action:基础表维护(" + tableName + ")新增开始", Level.INFO,
                            null);
            if (STR_TABLENAME_1.equals(tableName)) {
                // 根据画面选择项目的基础表维护Id找到类型实体
                HrCOvertime overTimeBeen = new HrCOvertime();
                // 设定加班类别
                overTimeBeen.setOvertimeType(recordTypeName);
                // 设定是否发放费用
                overTimeBeen.setIfOvertimeFee(ifFeeOrShiftFee);
                // 设定考勤标志
                overTimeBeen.setOvertimeMark(recordMark);
                // 设定修改者
                overTimeBeen.setLastModifiyBy(employee.getWorkerCode());
                // 设定企业编码
                overTimeBeen.setEnterpriseCode(employee.getEnterpriseCode());
                // 设定是否使用
                overTimeBeen.setIsUse(STR_ISUSE_Y);
                // 保存(含修改时间,流水号)
                overTimeRemote.save(overTimeBeen);

            } else if (STR_TABLENAME_2.equals(tableName)) {
                // 根据画面选择项目的基础表维护Id找到类型实体
                HrCWorkshift workShiftBeen = new HrCWorkshift();
                // 设定运行班类别
                workShiftBeen.setWorkShift(recordTypeName);
                // 设定津贴标准
                if (ifFeeOrShiftFee != null
                        && !Constants.BLANK_STRING.equals(ifFeeOrShiftFee)) {
                    workShiftBeen.setWorkShitFee(Double
                            .parseDouble(ifFeeOrShiftFee));
                } else {
                    workShiftBeen.setWorkShitFee(null);
                }
                // 设定考勤标志
                workShiftBeen.setWorkShiftMark(recordMark);
                // 设定修改者
                workShiftBeen.setLastModifiyBy(employee.getWorkerCode());
                // 设定企业编码
                workShiftBeen.setEnterpriseCode(employee.getEnterpriseCode());
                // 设定是否使用
                workShiftBeen.setIsUse(STR_ISUSE_Y);
                // 保存(含修改时间,流水号)
                workShiftRemote.save(workShiftBeen);
            }
            // 保存成功
            write(Constants.ADD_SUCCESS);
            LogUtil.log("Action基础表维护(" + tableName + ")新增结束", Level.INFO, null);
        } catch (Exception e) {
            // 保存失败
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action基础表维护(" + tableName + ")新增失败", Level.SEVERE, e);
        }
    }

    /**
     * 删除 基础表维护Id找到实体，然后进行逻辑删除
     */
    public void deleteBaseTableRecord() {
        try {
            LogUtil
                    .log("Action:基础表维护(" + tableName + ")删除开始", Level.INFO,
                            null);
            if (STR_TABLENAME_1.equals(tableName)) {
                // 根据画面选择项目的基础表维护Id找到类型实体
                HrCOvertime overTimeBeen = overTimeRemote.findById(Long
                        .parseLong(recordId));
                // 1.设定修改者
                overTimeBeen.setLastModifiyBy(employee.getWorkerCode());
                // 2.设定不可用
                overTimeBeen.setIsUse(Constants.IS_USE_N);
                // 逻辑删除(含修改时间)
                overTimeRemote.update(overTimeBeen);
            } else if (STR_TABLENAME_2.equals(tableName)) {
                // 根据画面选择项目的基础表维护Id找到类型实体
                HrCWorkshift workShiftBeen = workShiftRemote.findById(Long
                        .parseLong(recordId));
                // 1.设定修改者
                workShiftBeen.setLastModifiyBy(employee.getWorkerCode());
                // 2.设定不可用
                workShiftBeen.setIsUse(Constants.IS_USE_N);
                // 逻辑删除(含修改时间)
                workShiftRemote.update(workShiftBeen);
            }
            // 逻辑删除成功
            write(Constants.DELETE_SUCCESS);
            LogUtil
                    .log("Action:基础表维护(" + tableName + ")删除结束", Level.INFO,
                            null);
        } catch (Exception e) {
            // 逻辑删除失败
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:基础表维护(" + tableName + ")删除失败", Level.SEVERE, e);
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
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName
     *            the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the recordId
     */
    public String getRecordId() {
        return recordId;
    }

    /**
     * @param recordId
     *            the recordId to set
     */
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    /**
     * @return the recordMark
     */
    public String getRecordMark() {
        return recordMark;
    }

    /**
     * @param recordMark
     *            the recordMark to set
     */
    public void setRecordMark(String recordMark) {
        this.recordMark = recordMark;
    }

    /**
     * @return the addOrUpdate
     */
    public String getAddOrUpdate() {
        return addOrUpdate;
    }

    /**
     * @param addOrUpdate
     *            the addOrUpdate to set
     */
    public void setAddOrUpdate(String addOrUpdate) {
        this.addOrUpdate = addOrUpdate;
    }

    /**
     * @return the recordTypeName
     */
    public String getRecordTypeName() {
        return recordTypeName;
    }

    /**
     * @param recordTypeName
     *            the recordTypeName to set
     */
    public void setRecordTypeName(String recordTypeName) {
        this.recordTypeName = recordTypeName;
    }

    /**
     * @return the ifFeeOrShiftFee
     */
    public String getIfFeeOrShiftFee() {
        return ifFeeOrShiftFee;
    }

    /**
     * @param ifFeeOrShiftFee
     *            the ifFeeOrShiftFee to set
     */
    public void setIfFeeOrShiftFee(String ifFeeOrShiftFee) {
        this.ifFeeOrShiftFee = ifFeeOrShiftFee;
    }
}