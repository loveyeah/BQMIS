package power.web.manage.stat.action;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.BpCStatItemRealtime;
import power.ejb.manage.stat.BpCStatItemRealtimeFacadeRemote;
import power.ejb.manage.stat.form.StatItemFrom;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class StatItemRealtimeAction extends AbstractAction { 
	private BpCStatItemRealtimeFacadeRemote realtimeRemote; 
	public StatItemRealtimeAction() {
		realtimeRemote = (BpCStatItemRealtimeFacadeRemote) factory.getFacadeRemote("BpCStatItemRealtimeFacade");
	}
	
	/**
	 * 查询指标对应数据
	 * 
	 */
	public void getStatItemList() throws JSONException {
		
		String argFuzzy = request.getParameter("argFuzzy");
		
		PageObject obj = new PageObject();
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		obj = realtimeRemote.findStatItemForCorrespond(argFuzzy,start,limit);
		String str = JSONUtil.serialize(obj);
		if ("".equals(str)) {
			str = "{\"list\":[],\"totalCount\":null}";
		}
		write(str);
	}
	
//	/**
//	 * 查询节点对应数据
//	 * 
//	 */
//	public void getDcsNodeList() throws JSONException {
//		PageObject obj = new PageObject();
//		int start = Integer.parseInt(request.getParameter("start"));
//		int limit = Integer.parseInt(request.getParameter("limit"));
//		String queryKey = request.getParameter("queryKey");
//		obj = realtimeRemote.findDcsNodeForCorrespond(queryKey,start,limit);
//		String str = JSONUtil.serialize(obj);
//		if ("".equals(str)) {
//			str = "{\"list\":[],\"totalCount\":null}";
//		}
//		write(str);
//	}
	/**
	 * 查询指标对应的采集点信息
	 * @throws JSONException
	 */
	public void findCorrespondByItem() throws JSONException{
		String itemCode = request.getParameter("itemCode");
		StatItemFrom model=realtimeRemote.findItemCorrespondInfo(itemCode);
		write(JSONUtil.serialize(model));
	}
	/**
	 * 生成指标编码与节点的对应信息
	 */
	public void generateCorrespond() {
		String itemCode = request.getParameter("itemCode");
		String nodeCode = request.getParameter("nodeCode");
		String apartCode = request.getParameter("apartCode");
		BpCStatItemRealtime model = realtimeRemote.findById(itemCode);
		if(model == null)
		{
			BpCStatItemRealtime entity=new BpCStatItemRealtime();
			entity.setItemCode(itemCode);
			entity.setNodeCode(nodeCode);
			entity.setApartCode(apartCode); 
			realtimeRemote.save(entity);
		}
		else
		{
			model.setNodeCode(nodeCode);
			model.setApartCode(apartCode);
			realtimeRemote.update(model);
		}
		write(Constants.ADD_SUCCESS);
	}
	
	/**
	 * 删除指标编码与节点的对应信息
	 */
	public void cancelCorrespond() {
		String itemCode = request.getParameter("itemCode");
		BpCStatItemRealtime entity = new BpCStatItemRealtime();
		entity.setItemCode(itemCode);
		BpCStatItemRealtime returnEntity = realtimeRemote.delete(entity);
		if(returnEntity != null ) {
			write(Constants.DELETE_SUCCESS);
		} else {
			write(Constants.DELETE_FAILURE);
		}
	}

}
