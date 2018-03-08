/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.currency.action;

import power.ear.comm.ejb.PageObject;
import power.ejb.resource.SysCCurrency;
import power.ejb.resource.SysCCurrencyFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
/**
 * 币种维护办理
 * 
 * @author wujiao
 * @version 1.0
 */
public class CurrencyTypeAction extends AbstractAction{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/** 查询，保存，修改数据远程对象 */
	private SysCCurrencyFacadeRemote moneyRemote;
	/**
	 * 构造函数
	 */
	public CurrencyTypeAction() {
		moneyRemote = (SysCCurrencyFacadeRemote) factory
				.getFacadeRemote("SysCCurrencyFacade");
	}
	/**
	 *  币种维护办理 查询所有币种信息
	 * 
	 * @throws JSONException
	 * 
	 */
	public void searchMsg() throws JSONException, Exception {
		PageObject obj = new PageObject();
		// 分页信息
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		// 分页信息不为空时执行
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = moneyRemote.findAllAdd( employee.getEnterpriseCode(),start,limit);
		} else {
			// 无分页信息时执行
			obj = moneyRemote.findAllAdd(employee.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}
	
	/**
	 *  币种维护办理 删除币种信息
	 * 
	 * @throws JSONException
	 * 
	 */
	public void deleteMsg() throws JSONException, Exception {
		// 取得币种流水号
		String currencyId = request.getParameter("currencyIdValue");
		if(!Constants.BLANK_STRING.equals(currencyId)) {
			moneyRemote.deleteAdd(Long.parseLong(currencyId),employee.getWorkerCode());
		}
		write(Constants.DELETE_SUCCESS);
	}
	/**
	 *  币种维护办理 修改币种信息
	 * @param entity 
	 * 
	 * @throws JSONException
	 * 
	 */
	public void updateMsg() throws JSONException, Exception {
		// 取得币种流水号
		String currencyId = request.getParameter("currencyIdValue");
		// 取得编码
		String currencyNo = request.getParameter("currencyNoValue");
		// 取得名称
		String currencyName = request.getParameter("currencyNameValue");
		// 取得描述
		String currencyDesc = request.getParameter("currencyDescValue");
		// 检查数据长度
		// 根据流水号寻找这条币种信息
		SysCCurrency entity = moneyRemote.findById(Long.parseLong(currencyId
				.toString()));
		// 设置新编码
		entity.setCurrencyNo(currencyNo);
		// 设置新名称
		entity.setCurrencyName(currencyName);
		// 设置新描述
		entity.setCurrencyDesc(currencyDesc);
		// 设置更新时间
		entity.setLastModifiedDate(new java.util.Date());
		// 设置更新者
		entity.setLastModifiedBy(employee.getWorkerCode());
		// 设置
		if (!Constants.BLANK_STRING.equals(currencyId)) {
			int flag = moneyRemote.updateAdd(entity);
			if (flag == 0) {
				// 编码已经存在
				write("0");
			} else if (flag == 1) {
				// 名称已经存在
				write("1");
			} else {
				// 更新成功
				write("2");
			}
		}
	}
	/**
	 * 币种维护办理 增加币种信息
	 * 
	 * @param entity
	 * 
	 * @throws JSONException
	 * 
	 */
	public void addMsg() throws JSONException, Exception {
		// 取得币种流水号
		// String currencyId = request.getParameter("currencyIdValue");
		// 取得编码
		String currencyNo = request.getParameter("currencyNoValue");
		// 取得名称
		String currencyName = request.getParameter("currencyNameValue");
		// 取得描述
		String currencyDesc = request.getParameter("currencyDescValue");
		// 检查数据长度
		// 根据流水号寻找这条币种信息
		SysCCurrency entity = new SysCCurrency();
		// 设置新编码
		entity.setCurrencyNo(currencyNo);
		// 企业代码
		entity.setEnterpriseCode(employee.getEnterpriseCode());
		// 设置新名称
		entity.setCurrencyName(currencyName);
		// 设置新描述
		entity.setCurrencyDesc(currencyDesc);
		// 设置创建时间
		entity.setLastModifiedDate(new java.util.Date());
		// 设置创建者
		entity.setLastModifiedBy(employee.getWorkerCode());
		// 设置
		int flag = moneyRemote.insert(entity);
		if (flag == 0) {
			// 编码已经存在
			write("0");
		} else if (flag == 1) {
			// 名称已经存在
			write("1");
		} else {
			write("2");
		}
	}
	
	/**
	 * 币种维护办理 检查数据长度
	 * 
	 * @param 编码，名称，描述
	 * 
	 * @return boolean
	 * 
	 */
//	public Boolean checkMsg( String currencyNo, String currencyName,String currencyDesc ) {
//		if(currencyNo.getBytes().length > 10 ||
//			currencyName.getBytes().length >100 ||
//            currencyDesc.getBytes().length >200 ) {
//			return false;
//		}
//		return true;
//	}
}
