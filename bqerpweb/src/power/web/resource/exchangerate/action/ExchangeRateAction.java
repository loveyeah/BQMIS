/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.exchangerate.action;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.resource.SysCExchangeRate;
import power.ejb.resource.SysCExchangeRateFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 汇率维护action
 * @author zhengzhipeng
 * @version 1.0
 */
public class ExchangeRateAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    //ejb之been
    private SysCExchangeRate rateBeen;
    //ejb之remote
    private SysCExchangeRateFacadeRemote rateRemote;
    
    /**
     * 构造函数
     */
    public ExchangeRateAction() {
    	rateRemote = (SysCExchangeRateFacadeRemote) factory.getFacadeRemote("SysCExchangeRateFacade");
    }

    /**
     * 页面加载   获得 基准币别,兑换币别,汇率,有效开始日期,有效截止日期,流水号
     * @throws JSONException
     */
    public void getExchangeRateList() throws JSONException {    	
		// 取得查询参数: 开始行
		int intStart = Integer.parseInt(request.getParameter("start"));
		// 取得查询参数: 结束行
		int intLimit = Integer.parseInt(request.getParameter("limit"));
    	// 获取企业编码
		String enterpriseCode =  employee.getEnterpriseCode();		
    	PageObject obj = rateRemote.getExchangeRateList(enterpriseCode, intStart,intLimit);
    	// 查询结果为null,设置页面显示
		if(obj == null) {
		String str = "{\"list\":[],\"totalCount\":0}";
			write(str);
		 // 不为null
		} else {
			if(obj.getList() == null) {
				String str = "{\"list\":[],\"totalCount\":0}";
				write(str);
			}
			String str = JSONUtil.serialize(obj);
			write(str);			
		}
    }

    /**
     * 增加汇率信息
     * @throws CodeRepeatException
     */

    public void addExchangeRate() throws CodeRepeatException {
        try {
	    	// 设定企业编码
    		rateBeen.setEnterpriseCode(employee.getEnterpriseCode());
	    	// 设定修改者
    		rateBeen.setLastModifiedBy(employee.getWorkerCode());
         	// 是否有效
    		rateBeen.setIsUse(Constants.IS_USE_Y);
    		PageObject obj = rateRemote.isDateExist(rateBeen);
    		if(obj.getTotalCount() > 0){
    			// 相同的基准货币和兑换货币在相同时段内不能有不同汇率。
    			write("{success:true,flag:'1'}");
    			return;
    		}
        	rateRemote.save(rateBeen);
        	// 保存成功
        	write("{success:true,flag:'0'}");
        } catch (Exception e) {
        	write("{success:true,flag:'2'}");
        }
    }

    /**
     * 更新汇率信息
     * @throws CodeRepeatException
     */
    public void updateExchangeRate() throws CodeRepeatException {
    	try {
	    	// 设定企业编码
    		rateBeen.setEnterpriseCode(employee.getEnterpriseCode());
	    	// 设定修改者
    		rateBeen.setLastModifiedBy(employee.getWorkerCode());
    		// 是否有效
    		rateBeen.setIsUse(Constants.IS_USE_Y); 
    		PageObject obj = rateRemote.isDateExist(rateBeen);
    		if(obj.getTotalCount() > 0){
    			// 相同的基准货币和兑换货币在相同时段内不能有不同汇率。
    			write("{success:true,flag:'1'}");
    			return;
    		}
    		rateRemote.update(rateBeen);
        	// 保存成功
        	write("{success:true,flag:'0'}");
	    } catch (Exception e) {
	    	write("{success:true,flag:'2'}");
	    }
    }

    /**
     * 删除汇率信息    
     */
    public void deleteExchangeRate() {
    	try {
	    	// 设定企业编码
    		rateBeen.setEnterpriseCode(employee.getEnterpriseCode());
	    	// 设定修改者
    		rateBeen.setLastModifiedBy(employee.getWorkerCode());
    		rateRemote.delete(rateBeen);
    		write(Constants.DELETE_SUCCESS);
	    } catch (Exception e) {
	    	write(Constants.DELETE_FAILURE);
	    }
    }
    /**
     *  获得货币名称列表
     * @throws JSONException 
     */
    public void getCurrencyNameList() throws  JSONException {
    	PageObject obj = rateRemote.getCurrencyNameList();
		String str = JSONUtil.serialize(obj);
		write(str);
    }

	public SysCExchangeRate getRateBeen() {
		return rateBeen;
	}

	public void setRateBeen(SysCExchangeRate rateBeen) {
		this.rateBeen = rateBeen;
	}
}
