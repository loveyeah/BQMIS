/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.employee.separatmaint;

import java.util.logging.Level;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrCOuttype;
import power.ejb.hr.HrCOuttypeFacadeRemote;
import power.ejb.hr.LogUtil;
import java.util.ArrayList;
import java.util.List;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 离职类别维护Action
 * 
 * @author liuxin
 * 
 */
public class SeparatMaint extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private HrCOuttypeFacadeRemote remote;
    private HrCOuttype entity;
    // 是否使用
    private static final String YES = "Y";
    private static final String NO = "N";
    /** 离职类别 */
    private String type;
    /** 显示顺序 */
    private String order;
    /** 调查类别ID */
    private String outId;
    /** 分页信息 */
    public int start;
    public int limit;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    // 构造函数
    public SeparatMaint() {
        remote = (HrCOuttypeFacadeRemote) factory
                .getFacadeRemote("HrCOuttypeFacade");
    }

    /**
     * 查询调出类别维护
     * 
     * @throws JSONException
     */
    public void getOutType() {
        LogUtil.log("Action:离职类别维护查询开始。", Level.INFO, null);
        PageObject obj = new PageObject();
        List<HrCOuttype> list = new ArrayList<HrCOuttype>();
        list = remote.getOutTypeList(YES, employee.getEnterpriseCode(), start,
                limit);
        obj.setList(list);
        list = remote.getOutTypeList(YES, employee.getEnterpriseCode());
        obj.setTotalCount(new Long(list.size()));
        // 解析字符串
        String str = null;
        try {
            str = JSONUtil.serialize(obj);
            write(str);
            LogUtil.log("Action:离职类别维护查询成功。", Level.INFO, null);
        } catch (JSONException e) {
            LogUtil.log("Action:离职类别维护查询失败。", Level.INFO, null);
            write("failture");
        }
    }

    /**
     * 增加离职类别维护
     */
    public void addOutType() {
        LogUtil.log("Action:离职类别维护增加开始。", Level.INFO, null);
        entity = new HrCOuttype();
        entity.setEnterpriseCode(Constants.ENTERPRISE_CODE);
        entity.setInsertby(employee.getWorkerCode());
        entity.setInsertdate(new java.util.Date());
        entity.setIsUse(YES);
        if (!"".equals(order)) {
            entity.setOrderBy(new Long(order));
        }
        entity.setLastModifiedBy(employee.getWorkerCode());
        entity.setLastModifiedDate(new java.util.Date());
        entity.setOutTypeType(type);
        remote.save(entity);
        LogUtil.log("Action:离职类别维护增加成功。", Level.INFO, null);
    }

    /**
     * 修改离职类别维护
     */
    public void updateOutType() {
        LogUtil.log("Action:离职类别维护修改开始。", Level.INFO, null);
        if (!remote.findByIdIsuseCode(new Long(outId), YES, employee
                .getEnterpriseCode())) {
            // 没有找到这条记录
            write("notfind");
        } else {
            HrCOuttype entity = remote.findById(new Long(outId));
            entity.setOutTypeType(type);
            entity.setLastModifiedBy(employee.getWorkerCode());
            entity.setLastModifiedDate(new java.util.Date());
            if (!"".equals(order)) {
                entity.setOrderBy(new Long(order));
            }else {
            	entity.setOrderBy(null);// modify by ywliu 2009/09/07   
            }
            remote.update(entity);
            LogUtil.log("Action:离职类别维护修改成功。", Level.INFO, null);
            write("success");
        }
    }

    /**
     * 删除离职类别维护
     */
    public void deleteOutType() {
        LogUtil.log("Action:离职类别维护删除开始。", Level.INFO, null);
        if (!remote.findByIdIsuseCode(new Long(outId), YES, employee
                .getEnterpriseCode())) {
            // 没有找到这条记录
            write("notfind");
        } else {
            entity = new HrCOuttype();
            entity = remote.findById(new Long(outId));
            entity.setIsUse(NO);
            entity.setLastModifiedBy(employee.getWorkerCode());
            entity.setLastModifiedDate(new java.util.Date());
            remote.update(entity);
            LogUtil.log("Action:离职类别维护删除成功。", Level.INFO, null);
            write("success");
        }
    }
}
