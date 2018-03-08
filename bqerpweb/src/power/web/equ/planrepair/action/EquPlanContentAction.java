package power.web.equ.planrepair.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.equ.planrepair.EquCPlanContent;
import power.ejb.equ.planrepair.EquCPlanContentFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class EquPlanContentAction extends AbstractAction{

	private EquCPlanContentFacadeRemote remote;
	private EquCPlanContent content;

	
	public EquPlanContentAction()
	{
		remote = (EquCPlanContentFacadeRemote)factory.getFacadeRemote("EquCPlanContentFacade");
	}
	
	/**
	 * 增加一条检修项目内容记录
	 */
	public void addPlanContent()
	{
		content.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			remote.save(content);
			write("{success:true,msg:'增加成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	/**
	 * 修改一条检修项目内容记录
	 */
	public void updatePlanContent()
	{
		EquCPlanContent model = remote.findById(content.getContentId());
		model.setContentName(content.getContentName());
		model.setMemo(content.getMemo());
		try {
			remote.update(model);
			write("{success:true,msg:'修改成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	/**
	 * 删除一条或多条检修项目内容记录
	 */
	public void deletePlanContent()
	{
		String ids=request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 查询所有检修项目内容记录列表
	 * @throws JSONException
	 */
	public void findPlanContentList() throws JSONException
	{
		String contentName = request.getParameter("contentName");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findEquPlanContentList(employee.getEnterpriseCode(), contentName, start,limit);
		} else {
			object = remote.findEquPlanContentList(employee.getEnterpriseCode(), contentName);
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}

	public EquCPlanContent getContent() {
		return content;
	}

	public void setContent(EquCPlanContent content) {
		this.content = content;
	}
}
