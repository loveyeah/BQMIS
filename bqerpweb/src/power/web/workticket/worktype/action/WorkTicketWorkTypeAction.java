/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.workticket.worktype.action;

import java.util.ArrayList;
import java.util.List;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketType;
import power.ejb.workticket.RunCWorkticketTypeFacadeRemote;
import power.ejb.workticket.RunCWorkticketWorktype;
import power.ejb.workticket.RunCWorkticketWorktypeFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 工作类型维护页面action
 * 
 * @author 周旭
 * @version 1.0
 */

public class WorkTicketWorkTypeAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    /** 工作类型编码 */
    private String workypeID;
    /** RUN_C_WORKTICKET_WORKTYPE */
    private RunCWorkticketWorktype bean;

    protected RunCWorkticketWorktypeFacadeRemote remote;
    private RunCWorkticketTypeFacadeRemote remote1;

    public String getWorkypeID() {
        return workypeID;
    }

    public void setWorkypeID(String workypeID) {
        this.workypeID = workypeID;
    }

    public RunCWorkticketWorktype getBean() {
        return bean;
    }

    public void setBean(RunCWorkticketWorktype bean) {
        this.bean = bean;
    }

    /**
     * 构造函数
     */
    public WorkTicketWorkTypeAction() {
        remote = (RunCWorkticketWorktypeFacadeRemote) factory.getFacadeRemote("RunCWorkticketWorktypeFacade");
        // 工作票类型远程处理对象
        remote1 = (RunCWorkticketTypeFacadeRemote) factory
            .getFacadeRemote("RunCWorkticketTypeFacade");
    }

    /**
     * 页面加载
     * 
     * @throws JSONException
     */
    public void getWorkTicketWorkType() throws JSONException {
        /** 获取企业编码 */
        String enterpriseCode = Constants.ENTERPRISE_CODE;
        String fuzzy = "%";
        String workticketTypeCode="";
        /** 获取动态参数 */
        Object myobj = request.getParameter("fuzzy");
        Object myobjType = request.getParameter("workticketTypeCode");
        if (myobj != null) {
            if(!"".equals(myobj.toString())){
                fuzzy=myobj.toString();
            }
        }
        if (myobjType != null) {
            workticketTypeCode = request.getParameter("workticketTypeCode");
        }
        int start = Integer.parseInt(request.getParameter("start"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        PageObject obj = new PageObject();
        List<RunCWorkticketWorktype> list =new ArrayList<RunCWorkticketWorktype>();
        if ("".equals(workticketTypeCode)) {
            obj = remote.findAll(enterpriseCode, fuzzy, start, limit);
        } else if((!"".equals(workticketTypeCode))&&"%".equals(fuzzy)){
            list=remote.findByWorkticketTypeCode(enterpriseCode, workticketTypeCode);
            Long totalCount=(long)list.size();
            int topCount=list.size();
            List<RunCWorkticketWorktype> needList=list.subList(start+0, (start+18)>totalCount?topCount:(start+18));
            obj.setList(needList);
            obj.setTotalCount(totalCount);
        }else if ((!"".equals(workticketTypeCode))&&(!"%".equals(fuzzy))) {
            list=remote.findByWorkticketTypeCodeAndName(enterpriseCode, workticketTypeCode, fuzzy);
            Long totalCount=(long)list.size();
            int topCount=list.size();
            List<RunCWorkticketWorktype> needList=list.subList(start+0, (start+18)>totalCount?topCount:(start+18));
            obj.setList(needList);
            obj.setTotalCount(totalCount);
        }
        
        String str = JSONUtil.serialize(obj);
        write(str);
    }

    /**
     * 增加
     * 
     * @throws CodeRepeatException
     */
    public void addWorkTicketWorkType() throws CodeRepeatException {
        try {
            // 设定企业编码
            bean.setEnterpriseCode(Constants.ENTERPRISE_CODE);
            // 设定修改者
            bean.setModifyBy(employee.getWorkerCode());
            remote.save(bean);
            write(Constants.ADD_SUCCESS);
        } catch (Exception e) {
            write(Constants.ADD_FAILURE);
        }
    }

    /**
     * 更新
     * 
     * @throws CodeRepeatException
     */
    public void updateWorkTicketWorkType() throws CodeRepeatException {
        try {
            // 设定企业编码 
            bean.setEnterpriseCode(Constants.ENTERPRISE_CODE);
            // 设定修改者 
            bean.setModifyBy(employee.getWorkerCode());
            bean.setIsUse(Constants.IS_USE_Y);
            remote.update(bean);
            write(Constants.MODIFY_SUCCESS);
        } catch (Exception e) {
            write(Constants.MODIFY_FAILURE);
        }

    }

    /**
     * 批量删除
     * 
     * 
     */
    public void delsWorkTicketWorkType() {
        /** 设定要批量删除的id */
        String delIDs = request.getParameter("bean.workTypeIds");
        remote.deleteMulti(delIDs);
        write(Constants.DELETE_SUCCESS);
    }
    
    @SuppressWarnings("unchecked")
    public void getTicketKeywordList() throws JSONException {

        PageObject obj = new PageObject();
        obj = remote1.findAll(Constants.ENTERPRISE_CODE);
        List<RunCWorkticketType> list=obj.getList();
        RunCWorkticketType opt = new RunCWorkticketType();
        // 显示为“所有”
        opt.setWorkticketTypeName(Constants.ALL_SELECT);
        // value为空值
        opt.setWorkticketTypeCode(Constants.BLANK_STRING);
        // 添加到list
        list.add(0, opt);
        // 添加到obj
        obj.setList(list);
        // 记录
        String str=JSONUtil.serialize(obj);
        write(str);
    }
}
