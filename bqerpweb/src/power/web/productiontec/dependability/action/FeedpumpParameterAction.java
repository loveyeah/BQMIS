package power.web.productiontec.dependability.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.dependabilityAnalysis.business.PtJFeedpumpParameter;
import power.ejb.productiontec.dependabilityAnalysis.business.PtJFeedpumpParameterFacadeRemote;
import power.ejb.productiontec.dependabilityAnalysis.business.PtKkxAuxiliaryInfoFacadeRemote;
import power.ejb.productiontec.dependabilityAnalysis.business.form.FeedpumpParameterForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class FeedpumpParameterAction extends AbstractAction{

	private PtJFeedpumpParameterFacadeRemote remote;
	private PtKkxAuxiliaryInfoFacadeRemote aRemote;
	private PtJFeedpumpParameter feed;
	
	public FeedpumpParameterAction()
	{
		remote = (PtJFeedpumpParameterFacadeRemote)factory.getFacadeRemote("PtJFeedpumpParameterFacade");
		aRemote = (PtKkxAuxiliaryInfoFacadeRemote)factory.getFacadeRemote("PtKkxAuxiliaryInfoFacade");
	}
	
	public void saveFeedpumpParameter()
	{
		if(feed.getFeedpumpId() == null)
		{
			feed.setEnterpriseCode(employee.getEnterpriseCode());
			remote.save(feed);
		}
		else
		{
			feed.setIsUse("Y");
			feed.setEnterpriseCode(employee.getEnterpriseCode());
			remote.update(feed);
		}
		write("{success : true,msg : '数据保存成功！'}");
	}
	
	public void deleteFeedpumpParameter()
	{
		String ids=request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	public void findFeedpumpParameterList() throws JSONException
	{
		Long blockId = null;
		Long typeId = null;
		String blockIdStr = request.getParameter("blockId");
		String auxiliaryName = request.getParameter("auxiliaryName");
		
		if(blockIdStr != null && !blockIdStr.equals(""))
			blockId = Long.parseLong(blockIdStr);
		
		String typeIdStr = request.getParameter("typeId");
		if(typeIdStr != null)
			typeId = Long.parseLong(typeIdStr);
		
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = aRemote.getAllAuxiliaryRec(blockId, typeId, auxiliaryName, employee.getEnterpriseCode(), start,limit);
		
		} else {
			object = aRemote.getAllAuxiliaryRec(blockId, typeId, auxiliaryName, employee.getEnterpriseCode());
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}

	public void findParameterById() throws JSONException
	{
		String auxiliaryIdStr = request.getParameter("auxiliaryId");
		Long auxiliaryId = null;
		FeedpumpParameterForm form = new FeedpumpParameterForm();
		if(auxiliaryIdStr != null && !auxiliaryIdStr.equals(""))
			auxiliaryId = Long.parseLong(auxiliaryIdStr);
		List<PtJFeedpumpParameter> list = remote.findByProperty("auxiliaryId", auxiliaryId);
		if(list != null && list.size() > 0)
		{
			form.setData(list.get(0));
			write(JSONUtil.serialize(form));
		}
		else
			write(JSONUtil.serialize(form));
	}
	
	public PtJFeedpumpParameter getFeed() {
		return feed;
	}

	public void setFeed(PtJFeedpumpParameter feed) {
		this.feed = feed;
	}
}
