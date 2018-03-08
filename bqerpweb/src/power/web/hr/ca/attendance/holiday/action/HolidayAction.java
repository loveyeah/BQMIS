/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.ca.attendance.holiday.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.ca.HrCHoliday;
import power.ejb.hr.ca.HrCHolidayFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 节假日维护action
 * 
 * @author zhengzhipeng
 * @version 1.0
 */
public class HolidayAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    /** 新增Flag */
    private static String STR_ADDFLAG_1 = "1";
    /** 修改Flag */
    private static String STR_ADDFLAG_0 = "0";
    /** 节假日类别:1 非周末休息日期 */
    private static String STR_DATE_TYPE_1 = "1";
    /** 节假日类别:2 周末上班日期 */
    private static String STR_DATE_TYPE_2 = "2";
    /** 查询结果为null,传到前台的string */
    private static String STR_JSON_NULL = "{\"list\":[],\"totalCount\":0}";
    /** 时间重复 */
    private static String STR_SUCCESS_REPEAT = "{success:true,msg:'DATE_REPEAT'}";
    /** 节假日ejb之remote */
    private HrCHolidayFacadeRemote holidayRemote;
    /** 查询参数: 开始行 */
    private int start;
    /** 查询参数: 开始行 */
    private int limit;
    /** 查询参数: 年份 */
    private String year;

    /** 参数: 节假日ID */
    private String holidayId;
    /** 参数: 节假日日期 */
    private Date holidayDate;
    /** 参数: 节假日类别 */
    private String holidayType;
    /** 参数: 新增or修改 */
    private String addOrUpdate;

    /**
     * 构造函数
     */
    public HolidayAction() {
        holidayRemote = (HrCHolidayFacadeRemote) factory
                .getFacadeRemote("HrCHolidayFacade");
    }

    /**
     * 页面加载(非周末休息日期)
     */
    public void getRestDateList() {
        getDateList(STR_DATE_TYPE_1);
    }

    /**
     * 页面加载(周末上班日期)
     */
    public void getWorkDateList() {
        getDateList(STR_DATE_TYPE_2);
    }

    private void getDateList(String dateType) {
        try {
            LogUtil.log("Action:节假日维护初始化开始", Level.INFO, null);
            // 取得查询参数: 企业编码
            String enterpriseCode = employee.getEnterpriseCode();
            PageObject obj = holidayRemote.getHolidayDateList(enterpriseCode,
                    dateType, year, start, limit);
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
            LogUtil.log("Action:节假日维护初始化结束", Level.INFO, null);
        } catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:节假日维护初始化失败", Level.SEVERE, jsone);
        } catch (ParseException pe) {
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:节假日维护初始化失败", Level.SEVERE, pe);
        } catch (SQLException pe) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:节假日维护初始化失败", Level.SEVERE, pe);
        } catch (Exception e) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:节假日维护初始化失败", Level.SEVERE, e);
        }
    }

    /**
     * 新增OR修改
     */
    public void saveOrUpdateHoliday() {
        if (STR_ADDFLAG_1.equals(addOrUpdate)) {
            // 新增
            saveHoliday();
        } else if (STR_ADDFLAG_0.equals(addOrUpdate)) {
            // 修改
            updateHoliday();
        }
    }

    /**
     * 修改
     */
    private void updateHoliday() {
        try {
            LogUtil.log("Action:节假日维护修改开始", Level.INFO, null);
            // 根据画面选择项目的节假日Id找到类型实体
            HrCHoliday oldBeen = holidayRemote.findById(Long
                    .parseLong(holidayId));
            // 设定画面的 节假日日期,节假日类型
            oldBeen.setHolidayDate(holidayDate);
            oldBeen.setHolidayType(holidayType);
            // 设定修改者
            oldBeen.setLastModifiyBy(employee.getWorkerCode());
            // 设定企业编码
            oldBeen.setEnterpriseCode(employee.getEnterpriseCode());

            // 时间重复性check
            PageObject obj = holidayRemote.isDateExist(oldBeen);
            if (obj.getTotalCount() > 0) {
                // 时间已存在
                write(STR_SUCCESS_REPEAT);
                LogUtil.log("Action节假日维护修改失败", Level.INFO, null);
                return;
            }

            // 保存(含修改时间)
            holidayRemote.update(oldBeen);
            // 保存成功
            write(Constants.MODIFY_SUCCESS);
            LogUtil.log("Action节假日维护修改结束", Level.INFO, null);
        } catch (Exception e) {
            // 保存失败
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action节假日维护修改失败", Level.SEVERE, e);
        }
    }

    /**
     * 新增
     */
    private void saveHoliday() {
        try {
            LogUtil.log("Action:节假日维护新增开始", Level.INFO, null);
            // 节假日 been
            HrCHoliday holidayBeen = new HrCHoliday();
            // 设定画面的 节假日日期,节假日类型
            holidayBeen.setHolidayDate(holidayDate);
            holidayBeen.setHolidayType(holidayType);
            // 设定修改者
            holidayBeen.setLastModifiyBy(employee.getWorkerCode());
            // 设定企业编码
            holidayBeen.setEnterpriseCode(employee.getEnterpriseCode());
            // 设定可用
            holidayBeen.setIsUse(Constants.IS_USE_Y);

            // 时间重复性check
            PageObject obj = holidayRemote.isDateExist(holidayBeen);
            if (obj.getTotalCount() > 0) {
                // 时间已存在
                write(STR_SUCCESS_REPEAT);
                LogUtil.log("Action节假日维护新增失败", Level.INFO, null);
                return;
            }

            // 保存(含修改时间和流水号)
            holidayRemote.save(holidayBeen);
            // 保存成功
            write(Constants.ADD_SUCCESS);
            LogUtil.log("Action节假日维护新增结束", Level.INFO, null);
        } catch (Exception e) {
            // 保存失败
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action节假日维护新增失败", Level.SEVERE, e);
        }
    }

    /**
     * 删除 节假日维护Id找到实体，然后进行逻辑删除
     */
    public void deleteHoliday() {
        try {
            LogUtil.log("Action:节假日维护删除开始", Level.INFO, null);
            // 根据画面选择项目的节假日维护Id找到类型实体
            HrCHoliday oldBeen = holidayRemote.findById(Long
                    .parseLong(holidayId));
            // 1.设定修改者
            oldBeen.setLastModifiyBy(employee.getWorkerCode());
            // 2.设定不可用
            oldBeen.setIsUse(Constants.IS_USE_N);
            // 逻辑删除(含修改时间)
            holidayRemote.update(oldBeen);
            // 逻辑删除成功
            write(Constants.DELETE_SUCCESS);
            LogUtil.log("Action:节假日维护删除结束", Level.INFO, null);
        } catch (Exception e) {
            // 逻辑删除失败
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:节假日维护删除失败", Level.SEVERE, e);
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
     * @return the holidayId
     */
    public String getHolidayId() {
        return holidayId;
    }

    /**
     * @param holidayId
     *            the holidayId to set
     */
    public void setHolidayId(String holidayId) {
        this.holidayId = holidayId;
    }

    /**
     * @return the holidayDate
     */
    public Date getHolidayDate() {
        return holidayDate;
    }

    /**
     * @param holidayDate
     *            the holidayDate to set
     */
    public void setHolidayDate(Date holidayDate) {
        this.holidayDate = holidayDate;
    }

    /**
     * @return the holidayType
     */
    public String getHolidayType() {
        return holidayType;
    }

    /**
     * @param holidayType
     *            the holidayType to set
     */
    public void setHolidayType(String holidayType) {
        this.holidayType = holidayType;
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
     * @return the year
     */
    public String getYear() {
        return year;
    }

    /**
     * @param year
     *            the year to set
     */
    public void setYear(String year) {
        this.year = year;
    }
}