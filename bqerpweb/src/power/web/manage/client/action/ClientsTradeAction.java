package power.web.manage.client.action;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.client.ConCClientsTrade;
import power.ejb.manage.client.ConCClientsTradeFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 合作伙伴行业字典信息
 * @author fyyang 090615
 *
 */
public class ClientsTradeAction extends AbstractAction {
	
	private ConCClientsTradeFacadeRemote tradeRemote;
    private ConCClientsTrade trade;
	
	public ClientsTradeAction() {
		tradeRemote = (ConCClientsTradeFacadeRemote)factory.getFacadeRemote("ConCClientsTradeFacade");
	}
	
	/**
	 * 根据输入条件查询合作伙伴行业信息
	 */
	public void getClientsTradeList() throws JSONException {
		//取得模糊查询条件
		String tradeName = request.getParameter("fuzzytext");
		// 取得查询条件: 开始行
		Object objstart = request.getParameter("start");
		// 取得查询条件：结束行
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		// 判断是否为null
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			// 查询
			object = tradeRemote.findAll(tradeName,employee.getEnterpriseCode(), start, limit);
		} else {
			// 查询
			object = tradeRemote.findAll(tradeName,employee.getEnterpriseCode());
		}
		// 输出结果
		String strOutput = "";
		// 查询结果为null
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
			// 不为null
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	/**
	 * 增加一条合作伙伴行业信息
	 */
	public void addClientsTradeInfo()
	{
		trade.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			tradeRemote.save(trade);
			write("{success:true,msg:'增加成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	/**
	 * 修改一条合作伙伴行业信息
	 */
	public void updateClientsTrade()
	{
		ConCClientsTrade model=tradeRemote.findById(trade.getTradeId());
		model.setMemo(trade.getMemo());
		model.setTradeName(trade.getTradeName());
		try {
			tradeRemote.update(model);
			write("{success:true,msg:'修改成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	/**
	 * 删除一条或多条一合作伙伴行业信息
	 */
	public void deleteClientsTrade()
	{
		String ids=request.getParameter("ids");
		tradeRemote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}

	public ConCClientsTrade getTrade() {
		return trade;
	}

	public void setTrade(ConCClientsTrade trade) {
		this.trade = trade;
	}

}
