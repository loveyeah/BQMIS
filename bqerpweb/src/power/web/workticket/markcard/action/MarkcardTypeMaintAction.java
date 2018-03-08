/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.workticket.markcard.action;

import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCMarkcardType;
import power.ejb.workticket.RunCMarkcardTypeFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 标识牌处理Action
 * @author huyou
 *
 */
public class MarkcardTypeMaintAction extends AbstractAction{

    /** 标识牌处理远程对象 */
    private RunCMarkcardTypeFacadeRemote remote;
    /** 标识牌 */
    private RunCMarkcardType cardType;
    
    /**
     * 构造函数
     */
    public MarkcardTypeMaintAction () {
        // 取得标识牌处理远程对象
        remote = (RunCMarkcardTypeFacadeRemote) factory.getFacadeRemote("RunCMarkcardTypeFacade");
    }
    
    /**
     * 取得标识牌
     */
    public RunCMarkcardType getCardType() {
        return cardType;
    }
    
    /**
     * 设置标识牌
     * @param argCardType 标识牌
     */
    public void setCardType(RunCMarkcardType argCardType) {
        cardType = argCardType;
    }

    /**
     * 获得标识牌列表
     * @throws JSONException
     */
    public void getMarkcardTypeList() throws JSONException
    {
        String fuzzy="";
        // 取得查询参数
        Object myobj = request.getParameter("fuzzy");
        if(myobj != null)
        {
            fuzzy = myobj.toString();
        }
        String enterpriseCode = Constants.ENTERPRISE_CODE;
        // 查询标识牌信息列表
        PageObject obj = remote.findAll(enterpriseCode, fuzzy);
        
        // 序列化为JSON对象的字符串形式
        String str=JSONUtil.serialize(obj);
        // 以html方式输出字符串
        write(str);
    }

    /**
     * 增加标识牌
     * @throws Exception
     */
    public void addMarkcardType() throws Exception
    {
        cardType.setEnterpriseCode(Constants.ENTERPRISE_CODE);
        cardType.setModifyBy(employee.getWorkerCode());
        try {
            cardType.setMarkcardTypeId(null);
            // 增加一条标识牌记录
            remote.save(cardType);

            write(Constants.ADD_SUCCESS);
        } catch (Exception e) {
            write(Constants.MODIFY_FAILURE_CARD);
            throw e;
        } finally {
            cardType.setMarkcardTypeId(new Long(-1));
        }
    }
    
    /**
     * 修改标识牌
     */
    public void updateMarkcardType() throws Exception
    {
        // 查找这条标识牌记录
        RunCMarkcardType model = remote.findById(cardType.getMarkcardTypeId());
        // 标识牌类型名称
        model.setMarkcardTypeName(cardType.getMarkcardTypeName());
        // 填写人
        model.setModifyBy(employee.getWorkerCode());
        // 填写日期
        model.setModifyDate(cardType.getModifyDate());
        
        try {
            // 修改这条标识牌记录
            remote.update(model);

            write(Constants.MODIFY_SUCCESS);
        } catch (Exception e) {
            write(Constants.MODIFY_FAILURE);
            throw e;
        }
    }

    /**
     * 删除标识牌
     */
    public void deleteMarkcardType()
    {
        // 从请求中获得删除的ID
        String ids = request.getParameter("ids");

        if(ids != null && ids.trim().length() > 0) {
            // 删除标识牌记录
            remote.deleteMulti(ids);
        }
        
        write(Constants.DELETE_SUCCESS);
    }
    
}
