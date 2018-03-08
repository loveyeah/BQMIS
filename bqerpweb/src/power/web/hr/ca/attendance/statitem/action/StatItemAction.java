/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.ca.attendance.statitem.action;

import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.ca.HrCStatitem;
import power.ejb.hr.ca.HrCStatitemFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 考勤合计项维护action
 * 
 * @author zhengzhipeng
 * @version 1.0
 */
public class StatItemAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    /** 新增Flag */
    private static String STR_ADDFLAG_1 = "1";
    /** 修改Flag */
    private static String STR_ADDFLAG_0 = "0";
    /** 空 */
    private static String STR_NULL = "";
    /** 查询结果为null,传到前台的string */
    private static String STR_JSON_NULL = "{\"list\":[],\"totalCount\":0}";
    /** 成功 */
    private static String STR_SUCCESS = "{success:true,flag:'1'}";
    /** 成功 */
    private static String STR_FAILURE = "{success:true,flag:'0'}";
    /** 考勤合计项ejb之remote */
    private HrCStatitemFacadeRemote statItemRemote;
    /** 查询参数: 开始行 */
    private int start;
    /** 查询参数: 开始行 */
    private int limit;

    /** 参数: 流水号 */
    private String statId;
    /** 参数: 合计项类型 */
    private String statItemType;
    /** 参数: 合计项名称id */
    private String statItemId;
    /** 参数: 合计项名称 */
    private String statItemName;
    /** 参数: 合计项别名 */
    private String statItemByname;
    /** 参数: 表示顺序 */
    private String orderBy;
    /** 参数: 是否使用 */
    private String useFlg;
    /** 参数: 新增标记 */
    private String addOrUpdate;

    /** 合计项类型code */
    private String typeCode;

    /**
     * 构造函数
     */
    public StatItemAction() {
        statItemRemote = (HrCStatitemFacadeRemote) factory
                .getFacadeRemote("HrCStatitemFacade");
    }

    /**
     * 页面加载
     * 
     * @throws JSONException
     */
    public void getStatItemList() {
        try {
            LogUtil.log("Action:考勤合计项维护初始化开始", Level.INFO, null);
            // 取得查询参数: 企业编码
            String enterpriseCode = employee.getEnterpriseCode();
            PageObject obj = statItemRemote.getStatItemList(enterpriseCode,
                    start, limit);
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
            LogUtil.log("Action:考勤合计项维护初始化结束", Level.INFO, null);
        } catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:考勤合计项维护初始化失败", Level.SEVERE, jsone);
        } catch (Exception e) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:考勤合计项维护初始化失败", Level.SEVERE, e);
        }
    }

    /**
     * 新增OR修改
     */
    public void saveOrUpdateStatItem() {
        if (STR_ADDFLAG_1.equals(addOrUpdate)) {
            // 新增
            saveStatItem();
        } else if (STR_ADDFLAG_0.equals(addOrUpdate)) {
            // 修改
            updateStatItem();
        }
    }

    /**
     * 修改
     */
    private void updateStatItem() {
        try {
            LogUtil.log("Action:考勤合计项维护修改开始", Level.INFO, null);
            // 取得画面流水号
            Long statid = Long.parseLong(statId);
            // 根据画面选择项目的工号Id找到类型实体
            HrCStatitem oldBeen = statItemRemote.findById(statid);
            // 设定画面的 流水号,ID,合计项名称,合计项别名,合计项类型,单位,表示顺序,是否使用
            oldBeen.setStatItemType(statItemType);
            oldBeen.setStatItemId(Long.parseLong(statItemId));
            oldBeen.setStatItemName(statItemName);
            oldBeen.setStatItemByname(statItemByname);
            if (!STR_NULL.equals(orderBy)) {
                oldBeen.setOrderBy(Long.parseLong(orderBy));
            } else {
                oldBeen.setOrderBy(null);
            }
            oldBeen.setUseFlg(useFlg);
            // 设定修改者
            oldBeen.setLastModifiyBy(employee.getWorkerCode());
            // 设定企业编码
            oldBeen.setEnterpriseCode(employee.getEnterpriseCode());
            // 保存(含流水号)
            statItemRemote.update(oldBeen);
            // 保存成功
            write(STR_SUCCESS);
            LogUtil.log("Action考勤合计项维护修改结束", Level.INFO, null);
        } catch (Exception e) {
            // 保存失败
            write(STR_FAILURE);
            LogUtil.log("Action考勤合计项维护修改失败", Level.SEVERE, e);
        }
    }

    /**
     * 新增
     */
    private void saveStatItem() {
        try {
            LogUtil.log("Action:考勤合计项维护新增开始", Level.INFO, null);
            // 考勤合计项 been
            HrCStatitem statItemBeen = new HrCStatitem();
            // 设定画面的 合计项类型,合计项名称ID,合计项名称,合计项别名,单位,表示顺序,是否使用
            statItemBeen.setStatItemType(statItemType);
            if (!STR_NULL.equals(statItemId)) {
                statItemBeen.setStatItemId(Long.parseLong(statItemId));
            } else {
                statItemBeen.setStatItemId(null);
            }
            statItemBeen.setStatItemName(statItemName);
            statItemBeen.setStatItemByname(statItemByname);
            if (!STR_NULL.equals(orderBy)) {
                statItemBeen.setOrderBy(Long.parseLong(orderBy));
            } else {
                statItemBeen.setOrderBy(null);
            }
            statItemBeen.setUseFlg(useFlg);
            // 设定修改者
            statItemBeen.setLastModifiyBy(employee.getWorkerCode());
            // 设定企业编码
            statItemBeen.setEnterpriseCode(employee.getEnterpriseCode());
            // 设定可用
            statItemBeen.setIsUse(Constants.IS_USE_Y);
            // 保存(含修改时间和流水号)
            statItemRemote.save(statItemBeen);
            // 保存成功
            write(STR_SUCCESS);
            LogUtil.log("Action考勤合计项维护新增结束", Level.INFO, null);
        } catch (Exception e) {
            // 保存失败
            write(STR_FAILURE);
            LogUtil.log("Action考勤合计项维护新增失败", Level.SEVERE, e);
        }
    }

    /**
     * 删除 考勤合计项维护Id找到实体，然后进行逻辑删除
     */
    public void deleteStatItem() {
        try {
            LogUtil.log("Action:考勤合计项维护删除开始", Level.INFO, null);
            // 根据画面选择项目的考勤合计项维护Id找到类型实体
            HrCStatitem oldBeen = statItemRemote.findById(Long
                    .parseLong(statId));
            // 1.设定修改者
            oldBeen.setLastModifiyBy(employee.getWorkerCode());
            // 2.设定不可用
            oldBeen.setIsUse(Constants.IS_USE_N);
            // 逻辑删除(含修改时间)
            statItemRemote.update(oldBeen);
            // 逻辑删除成功
            write(STR_SUCCESS);
            LogUtil.log("Action:考勤合计项维护删除结束", Level.INFO, null);
        } catch (Exception e) {
            // 逻辑删除失败
            write(STR_FAILURE);
            LogUtil.log("Action:考勤合计项维护删除失败", Level.SEVERE, e);
        }
    }

    /**
     * 通过合计项类型code获得合计项名称list
     * 
     * @throws JSONException
     */
    public void getStatNameListByTypeCode() throws JSONException {
        try {
            LogUtil.log("Action:合计项名称初始化开始", Level.INFO, null);
            // 根据企业编码，部门id取得所有物料类型
            PageObject obj = new PageObject();
            obj = statItemRemote.getStatNameList(employee.getEnterpriseCode(),
                    typeCode);
            String str = JSONUtil.serialize(obj);
            write(str);
            LogUtil.log("Action合计项名称初始化结束", Level.INFO, null);
        } catch (Exception e) {
            // 保存失败
            write(STR_FAILURE);
            LogUtil.log("Action合计项名称初始化失败", Level.SEVERE, e);
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
     * @return the statId
     */
    public String getStatId() {
        return statId;
    }

    /**
     * @param statId
     *            the statId to set
     */
    public void setStatId(String statId) {
        this.statId = statId;
    }

    /**
     * @return the statItemType
     */
    public String getStatItemType() {
        return statItemType;
    }

    /**
     * @param statItemType
     *            the statItemType to set
     */
    public void setStatItemType(String statItemType) {
        this.statItemType = statItemType;
    }

    /**
     * @return the statItemId
     */
    public String getStatItemId() {
        return statItemId;
    }

    /**
     * @param statItemId
     *            the statItemId to set
     */
    public void setStatItemId(String statItemId) {
        this.statItemId = statItemId;
    }

    /**
     * @return the statItemName
     */
    public String getStatItemName() {
        return statItemName;
    }

    /**
     * @param statItemName
     *            the statItemName to set
     */
    public void setStatItemName(String statItemName) {
        this.statItemName = statItemName;
    }

    /**
     * @return the statItemByname
     */
    public String getStatItemByname() {
        return statItemByname;
    }

    /**
     * @param statItemByname
     *            the statItemByname to set
     */
    public void setStatItemByname(String statItemByname) {
        this.statItemByname = statItemByname;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * @return the useFlg
     */
    public String getUseFlg() {
        return useFlg;
    }

    /**
     * @param useFlg
     *            the useFlg to set
     */
    public void setUseFlg(String useFlg) {
        this.useFlg = useFlg;
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
     * @return the typeCode
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * @param typeCode
     *            the typeCode to set
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}