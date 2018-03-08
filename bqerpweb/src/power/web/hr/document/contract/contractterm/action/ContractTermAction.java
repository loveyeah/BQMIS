/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.document.contract.contractterm.action;

import java.util.Date;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrCContractterm;
import power.ejb.hr.HrCContracttermFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 合同有效期维护Action
 * 
 * @author zhouxu
 * 
 */
public class ContractTermAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    /** 定义接口 */
    protected HrCContracttermFacadeRemote remote;
    /** 定义bean */
    private HrCContractterm bean;
    /** 定义空store常量 */
    private String NULL_STORE = "{\"list\":[],\"totalCount\":null}";
    /** 操作成功定义 */
    private String DO_SUCCESS = "{success:true,msg:'S'}";

    /** 构造函数 */
    public ContractTermAction() {
        remote = (HrCContracttermFacadeRemote) factory.getFacadeRemote("HrCContracttermFacade");
    }

    /**
     * 获取合同有效期列表
     */
    public void getContractTerm() {
        try {
            LogUtil.log("Action:合同有效期查询开始。", Level.INFO, null);
            // 定义最后write的字符串变量
            String str = "";
            // 开始查询数
            int strStart = Integer.parseInt(request.getParameter("start"));
            // 最大查询数
            int strLimit = Integer.parseInt(request.getParameter("limit"));
            // 查询并返回pageobject型
            PageObject obj = remote.findAll(employee.getEnterpriseCode(), strStart, strLimit);
            // 如果查询结果不为空
            if (obj.getTotalCount() > 0) {
                str = JSONUtil.serialize(obj);
            } else {
                // 如果查询结果为空
                str = NULL_STORE;
            }
            // 输出结果
            write(str);
            LogUtil.log("Action:合同有效期查询结束。", Level.INFO, null);
        } catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
            LogUtil.log("Action:合同有效期查询失败。", Level.SEVERE, jsone);
        } catch (RuntimeException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:合同有效期查询失败。", Level.SEVERE, sqle);
        }
    }

    /**
     * 增加合同有效期
     */
    public void addContractTerm() {
        try {
            LogUtil.log("Action:合同有效期增加开始。", Level.INFO, null);
            HrCContractterm beanOld = new HrCContractterm();
            // 设置合同有效期
            beanOld.setContractTerm(bean.getContractTerm());
            // 设置显示顺序
            beanOld.setContractDisplayNo(bean.getContractDisplayNo());
            // 设置企业编码
            beanOld.setEnterpriseCode(employee.getEnterpriseCode());
            // 设置是否使用
            beanOld.setIsUse(Constants.IS_USE_Y);
            // 设置最后修改人
            beanOld.setLastModifiedBy(employee.getWorkerCode());
            // 设置插入人
            beanOld.setInsertby(employee.getWorkerCode());
            // 设置设置插入时间
            beanOld.setInsertdate(new Date());
            // 保存进db
            remote.save(beanOld);
            write(DO_SUCCESS);
            LogUtil.log("Action:合同有效期增加结束。", Level.INFO, null);
        } catch (RuntimeException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:合同有效期增加失败。", Level.SEVERE, sqle);
        }
    }

    /**
     * 修改合同有效期
     */
    public void updateContractTerm() {
        try {
            LogUtil.log("Action:合同有效期修改开始。", Level.INFO, null);
            // 获取需要修改的id
            HrCContractterm beanOld = remote.findById(bean.getContractTermId());
            // 设置新的合同有效期
            beanOld.setContractTerm(bean.getContractTerm());
            // 设置显示顺序
            beanOld.setContractDisplayNo(bean.getContractDisplayNo());
            // 设置最后修改人
            beanOld.setLastModifiedBy(employee.getWorkerCode());
            // 更新db
            remote.update(beanOld);
            write(DO_SUCCESS);
            LogUtil.log("Action:合同有效期修改结束。", Level.INFO, null);
        } catch (RuntimeException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:合同有效期修改失败。", Level.SEVERE, sqle);
        }
    }

    /**
     * 删除合同有效期
     */
    public void deleteContractTerm() {
        try {
            LogUtil.log("Action:合同有效期删除开始。", Level.INFO, null);
            // 设置需要删除的id
            HrCContractterm beanOld = remote.findById(bean.getContractTermId());
            // 设置IS_USE字段为N
            beanOld.setIsUse(Constants.IS_USE_N);
            // 设置最后修改人
            beanOld.setLastModifiedBy(employee.getWorkerCode());
            // 更新进db
            remote.update(beanOld);
            write(DO_SUCCESS);
            LogUtil.log("Action:合同有效期删除结束。", Level.INFO, null);
        } catch (RuntimeException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:合同有效期删除失败。", Level.SEVERE, sqle);
        }
    }

    /**
     * @return the bean
     */
    public HrCContractterm getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(HrCContractterm bean) {
        this.bean = bean;
    }

}
