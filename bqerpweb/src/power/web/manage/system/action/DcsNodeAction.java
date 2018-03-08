package power.web.manage.system.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.system.RtCDcsNode;
import power.ejb.manage.system.RtCDcsNodeFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class DcsNodeAction extends AbstractAction{

	private RtCDcsNode node;
	private RtCDcsNodeFacadeRemote remote;
	
	public DcsNodeAction()
	{
		remote = (RtCDcsNodeFacadeRemote)factory.getFacadeRemote("RtCDcsNodeFacade");
	}
	/**
	 * 增加
	 */
	public void addDcsNode()
	{
		node.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			remote.save(node);
			write("{success:true,msg:'增加成功！'}");
		} catch (CodeRepeatException e) {
			 write("{success:true,repeat:true,msg:'"+e.getMessage()+"'}"); 
		}
	}
	/**
	 * 修改
	 * @return
	 */
	public void updateDcsNode()
	{
		RtCDcsNode model = remote.findById(node.getNodeCode());
		model.setNodeName(node.getNodeName());
		model.setApartCode(node.getApartCode());
		model.setDescriptor(node.getDescriptor());
		model.setNodeType(node.getNodeType());
		model.setMinValue(node.getMinValue());
		model.setMaxValue(node.getMaxValue());
		model.setStandardValue(node.getStandardValue());
		model.setCollectNow(node.getCollectNow());
		model.setCollectHis(node.getCollectHis());
		model.setIfCompute(node.getIfCompute());
		try {
			remote.update(model);
			write("{success:true,msg:'修改成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'"+e.getMessage()+"'}"); 
		}
		
	}
	/**
	 * 删除
	 */
	public void deleteDcsNode()
	{
		String node= request.getParameter("ids");
		RtCDcsNode model = remote.findById(node);
		remote.delete(model);
		write("{success:true,msg:'删除成功！'}");
	}
	/**
	 * 列表查询
	 * @return
	 * @throws JSONException 
	 */
	public void findDcsNode() throws JSONException
	{
		PageObject obj=new  PageObject();
		
		String sys = request.getParameter("sys");
		String queryKey = request.getParameter("queryKey");
		
		Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.findDcsNodeList(employee.getEnterpriseCode(), sys,queryKey, start,limit);
	    }
	    else
	    {
	    	obj=remote.findDcsNodeList(employee.getEnterpriseCode(), sys,queryKey);
	    }
	    String str=JSONUtil.serialize(obj);
		write(str);
	}
	public RtCDcsNode getNode() {
		return node;
	}

	public void setNode(RtCDcsNode node) {
		this.node = node;
	}
}
