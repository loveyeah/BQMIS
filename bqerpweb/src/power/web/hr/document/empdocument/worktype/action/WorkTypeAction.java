/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.document.empdocument.worktype.action;

import java.util.logging.Level;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrCTypeOfWork;
import power.ejb.hr.HrCTypeOfWorkFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 工种维护action
 * 
 * @author zhengzhipeng
 * @version 1.0
 */
public class WorkTypeAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    /** 查询结果为null,传到前台的string */
    private static String STR_JSON_NULL = "{\"list\":[],\"totalCount\":0}";
    /** 操作后，传到前台的string：1：成功 0：失败 2：重复  */
    private static String STR_SUCCESS_0 = "{success:true,flag:'0'}";
    private static String STR_SUCCESS_1 = "{success:true,flag:'1'}";
    private static String STR_SUCCESS_2 = "{success:true,flag:'2'}";
    /** 工种ejb之been */
    private HrCTypeOfWork workTypeBeen;
    /** 工种ejb之remote */
    private HrCTypeOfWorkFacadeRemote workTypeRemote;
    /** 查询参数: 开始行 */
    private int start;
    /** 查询参数: 开始行 */
    private int limit;

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
     * 构造函数
     */
    public WorkTypeAction() {
        workTypeRemote = (HrCTypeOfWorkFacadeRemote) factory
                .getFacadeRemote("HrCTypeOfWorkFacade");
    }

    /**
     * 页面加载
     * 
     * @throws JSONException
     */
    public void getWortTypeList() throws JSONException {
        try {
            LogUtil.log("Action:工种维护初始化开始", Level.INFO, null);
            // 取得查询参数: 企业编码
            String enterpriseCode = employee.getEnterpriseCode();

            PageObject obj = workTypeRemote.getWorkTypeList(enterpriseCode,
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
            LogUtil.log("Action:工种维护初始化结束", Level.INFO, null);
        } catch (Exception e) {
            LogUtil.log("Action:工种维护初始化失败", Level.SEVERE, e);
        }
    }

    /**
     * 新增
     */
    public void addWorkType() {
        try {
            LogUtil.log("Action:工种维护新增开始", Level.INFO, null);
            // 设定企业编码
            // modified by liuyi 091123 表中无该属性
//            workTypeBeen.setEnterpriseCode(employee.getEnterpriseCode());
            // 设定修改者
            // modified by liuyi 091123 表中无该属性
//            workTypeBeen.setLastModifiedBy(employee.getWorkerCode());
            // 设定是否可用
//            workTypeBeen.setIsUse(Constants.IS_USE_Y);
            // 工号ID在保存时设定
            workTypeRemote.save(workTypeBeen);
            // 保存成功
            write(STR_SUCCESS_1);
            LogUtil.log("Action:工种维护新增结束", Level.INFO, null);
        } catch (CodeRepeatException ce) {
            write(STR_SUCCESS_2);
        } catch (Exception e) {
            // 保存失败
            write(STR_SUCCESS_0);
            LogUtil.log("Action:工种维护新增失败", Level.SEVERE, e);
        }
    }

    /**
     * 修改
     */
    public void updateWorkType() {
        try {
            LogUtil.log("Action:工种维护修改开始", Level.INFO, null);
            // 取得画面工种Id
            Long typeOfWorkId = workTypeBeen.getTypeOfWorkId();
            // 根据画面选择项目的工号Id找到类型实体
            HrCTypeOfWork oldBeen = workTypeRemote.findById(typeOfWorkId);
            // 设定画面修改值
            oldBeen.setTypeOfWorkName(workTypeBeen.getTypeOfWorkName());
            oldBeen.setTypeOfWorkType(workTypeBeen.getTypeOfWorkType());
            oldBeen.setRetrieveCode(workTypeBeen.getRetrieveCode());
            oldBeen.setIsUse(workTypeBeen.getIsUse());
            workTypeRemote.update(oldBeen);
            // 保存成功
            write(STR_SUCCESS_1);
            LogUtil.log("Action:工种维护修改结束", Level.INFO, null);
        } catch (Exception e) {
            // 保存失败
            write(STR_SUCCESS_0);
            LogUtil.log("Action:工种维护修改失败", Level.SEVERE, e);
        }
    }

    /**
     * 删除 通过工号Id找到实体，然后进行逻辑删除
     */
    public void deleteWorkType() {
        try {
            LogUtil.log("Action:工种维护删除开始", Level.INFO, null);
            String ids = request.getParameter("ids");
            // 根据画面选择项目的工号Id找到类型实体
            //HrCTypeOfWork oldBeen = workTypeRemote.findById(workTypeBeen
                    //.getTypeOfWorkId());
            //oldBeen.setIsUse(Constants.IS_USE_N);
            // 逻辑删除
            workTypeRemote.delete(ids);
            // 逻辑删除成功
            write(Constants.DELETE_SUCCESS);
            LogUtil.log("Action:工种维护删除结束", Level.INFO, null);
        } catch (Exception e) {
            // 逻辑删除失败
            write(Constants.DELETE_FAILURE);
            LogUtil.log("Action:工种维护删除失败", Level.SEVERE, e);
        }
    }

    /**
     * @return the workTypeBeen
     */
    public HrCTypeOfWork getWorkTypeBeen() {
        return workTypeBeen;
    }

    /**
     * @param workTypeBeen
     *            the workTypeBeen to set
     */
    public void setWorkTypeBeen(HrCTypeOfWork workTypeBeen) {
        this.workTypeBeen = workTypeBeen;
    }
}