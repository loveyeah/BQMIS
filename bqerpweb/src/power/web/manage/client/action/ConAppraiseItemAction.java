package power.web.manage.client.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.client.ConCAppraiseItem;
import power.ejb.manage.client.ConCAppraiseItemFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class ConAppraiseItemAction extends AbstractAction{

	private ConCAppraiseItemFacadeRemote remote;
	private ConCAppraiseItem item;
	
	public ConAppraiseItemAction()
	{
		remote = (ConCAppraiseItemFacadeRemote)factory.getFacadeRemote("ConCAppraiseItemFacade");
	}
	
	/**
	 * 增加
	 * @return
	 */
	public void addConAppraiseItem() {
		item.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			item = remote.save(item);
			write("{success:true,id:'" + item.getEventId() + "',displayNo:'" + item.getDisplayNo() + "',msg:'增加成功！'}");
			
		} catch (CodeRepeatException e) {
			write("{success:true,id:'" + item.getEventId() + "',msg:'增加失败:评价项目名称、显示顺序不能重复！'}");
		}
	}
	
	/**
	 * 修改
	 * @return
	 */
	public void updateConAppraiseItem()
	{
		ConCAppraiseItem model = remote.findById(item.getEventId());
		model.setAppraiseItem(item.getAppraiseItem());
		model.setAppraiseMark(item.getAppraiseMark());
		model.setAppraiseCriterion(item.getAppraiseCriterion());
		model.setDisplayNo(item.getDisplayNo());
		try {
			remote.update(model);
			write("{success:true,id:'"+item.getEventId()+"',displayNo:'"+item.getDisplayNo()+"',msg:'修改成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'修改失败:评价项目名称、显示顺序不能重复！'}");
		}
	}
	
	/**
	 * 删除
	 * @return
	 */
	public void deleteConAppraiseItem()
	{
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 点“确定”按钮设置表中所有记录
	 */
	public void confirmConAppraiseItem()
	{
		remote.confirmMulti();
		write("{success:true,msg:'设置成功！'}");
	}
	
	/**
	 * 列表显示
	 * @return
	 * @throws JSONException 
	 */
	public void findConAppraiseItemList() throws JSONException
	{
		String fuzzy = request.getParameter("fuzzytext");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) 
		{
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findAll(fuzzy, employee.getEnterpriseCode(), start,limit);
		}
		else {
			obj = remote.findAll(fuzzy, employee.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}
	
	public ConCAppraiseItem getItem() {
		return item;
	}
	public void setItem(ConCAppraiseItem item) {
		this.item = item;
	}
	
}
