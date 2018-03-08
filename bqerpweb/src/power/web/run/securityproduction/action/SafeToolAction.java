package power.web.run.securityproduction.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.SpJSafetools;
import power.ejb.run.securityproduction.SpJSafetoolsFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * 安全工器具管理
 * @author jiaa
 *
 */
public class SafeToolAction extends AbstractAction{

	private SpJSafetoolsFacadeRemote remote;
	private SpJSafetools tool;
	
	public SafeToolAction()
	{
		remote=(SpJSafetoolsFacadeRemote)factory.getFacadeRemote("SpJSafetoolsFacade");
	}
	
	/**
	 * 增加一条安全工器具信息
	 */
	public void addSafeToolInfo()
	{
		tool.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			remote.save(tool);
			write("{success:true,msg:'增加成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
		
	}
	
	/**
	 * 修改一条安全工器具信息
	 */
	public void updateSafeToolInfo()
	{
		SpJSafetools model=remote.findById(tool.getToolsId());
		model.setChargeMan(tool.getChargeMan());
		model.setCheckDate(tool.getCheckDate());
		model.setFactory(tool.getFactory());
		model.setManuDate(tool.getManuDate());
		model.setMemo(tool.getMemo());
		model.setState(tool.getState());
		model.setToolsNames(tool.getToolsNames());
		model.setToolsCode(tool.getToolsCode());
		model.setToolsSizes(tool.getToolsSizes());
		try {
			remote.update(model);
			write("{success:true,msg:'修改成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
		
		
	}
	
	/**
	 * 删除一条或多条安全工器具信息
	 */
	public void deleteSafeToolInfo()
	{
		String ids=request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 根据工具名称或责任人查找安全工器具信息列表
	 * @throws JSONException 
	 */
	public void findSafeToolList() throws JSONException
	{
		Object objToolsNameOrChargeMan=request.getParameter("toolOrMan");
		String toolsNameOrChargeMan="";
		if(objToolsNameOrChargeMan!=null)
		{
			toolsNameOrChargeMan=objToolsNameOrChargeMan.toString();
		}
		 Object objStart = request.getParameter("start");
	        /** 查询行数 */
		 Object objLimit = request.getParameter("limit");
		PageObject obj=new PageObject();
		if(objStart!=null&&objLimit!=null)
		{
		obj=remote.findAll(toolsNameOrChargeMan, employee.getEnterpriseCode(),Integer.parseInt(objStart.toString()),Integer.parseInt(objLimit.toString()));
		}
		else
		{
			obj=remote.findAll(toolsNameOrChargeMan, employee.getEnterpriseCode());
		}
		
		String  str = "{\"list\":[],\"totalCount\":null}";
		if(obj!=null)
		{
			str=JSONUtil.serialize(obj);
		}
		write(str);
	}

	public SpJSafetools getTool() {
		return tool;
	}

	public void setTool(SpJSafetools tool) {
		this.tool = tool;
	}
}
