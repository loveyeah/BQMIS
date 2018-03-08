package power.web.run.securityproduction.action.safesuperviseaction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.safesupervise.SpCFireControl;
import power.ejb.run.securityproduction.safesupervise.SpCFireControlFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SpFireControlAction extends AbstractAction{

	protected SpCFireControlFacadeRemote remote;
	private SpCFireControl control;
	
	public SpFireControlAction()
	{
		remote = (SpCFireControlFacadeRemote)factory.getFacadeRemote("SpCFireControlFacade");
	}
	
	/**
	 * 增加一条消防器材配置表记录
	 */
	public void saveFireControlRecord()
	{
		control.setEnterpriseCode(employee.getEnterpriseCode());
		remote.save(control);
		write("{success:true,msg:'增加成功！'}");
	}
	
	/**
	 * 修改一条消防器材配置表记录
	 */
	public void updateFireControlRecord()
	{
		SpCFireControl model = remote.findById(control.getId());
		model.setChangeDate(control.getChangeDate());
		model.setCheckBy(control.getCheckBy());
		model.setCheckDate(control.getCheckDate());
		model.setControlNumber(control.getControlNumber());
		model.setDeployPart(control.getDeployPart());
		model.setMemo(control.getMemo());
		model.setParam(control.getParam());
		model.setSerialCode(control.getSerialCode());
		model.setType(control.getType());
		model.setValidDate(control.getValidDate());
		remote.update(model);
		
		write("{success:true,msg:'修改成功！'}");
	}
	
	/**
	 * 删除一条或多条消防器材配置表记录
	 */
	public void deleteFireControlRecord()
	{
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 查找符合条件的消防器材配置表记录
	 * @throws JSONException 
	 */
	public void findFireControlList() throws JSONException
	{
		String deployPart = request.getParameter("deployPart");
		
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) 
		{
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findFireControlList(employee.getEnterpriseCode(), deployPart, start,limit);
		}
		else {
			obj = remote.findFireControlList(employee.getEnterpriseCode(), deployPart);
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}
	
	
	public SpCFireControl getControl() {
		return control;
	}
	public void setControl(SpCFireControl control) {
		this.control = control;
	}
	
}
