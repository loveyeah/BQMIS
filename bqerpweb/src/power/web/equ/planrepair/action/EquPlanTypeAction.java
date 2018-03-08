package power.web.equ.planrepair.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.equ.planrepair.EquCPlanType;
import power.ejb.equ.planrepair.EquCPlanTypeFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class EquPlanTypeAction extends AbstractAction{

	private EquCPlanTypeFacadeRemote remote;
	private EquCPlanType type;
	
	public EquPlanTypeAction()
	{
		remote = (EquCPlanTypeFacadeRemote)factory.getFacadeRemote("EquCPlanTypeFacade");
	}
	
	/**
	 * 增加一条计划类型维护记录
	 */
	public void addEquPlanType() {
		type.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			remote.save(type);
			write("{success:true,msg:'增加成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	/**
	 * 修改一条计划类型维护记录
	 */
	public void updateEquPlanType()
	{
		EquCPlanType model = remote.findById(type.getPlanTypeId());
		model.setPlanTypeName(type.getPlanTypeName());
		model.setMemo(type.getMemo());
		try {
			remote.update(model);
			write("{success:true,msg:'修改成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	/**
	 * 删除一条或多条计划类型维护记录
	 */
	public void deleteEquPlanType()
	{
		String ids=request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 查找计划类型列表
	 * @throws JSONException
	 */
	public void findEquPlanTypeList() throws JSONException {
		String planTypeName = request.getParameter("planTypeName");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findEquPlanTypeList(employee.getEnterpriseCode(), planTypeName, start,limit);
		} else {
			object = remote.findEquPlanTypeList(employee.getEnterpriseCode(), planTypeName);
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
		
	}

	
	public EquCPlanType getType() {
		return type;
	}

	public void setType(EquCPlanType type) {
		this.type = type;
	}
}
