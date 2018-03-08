package power.web.equ.standardpackage.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.standardpackage.WoJWorkorder;
import power.ejb.equ.standardpackage.WoJWorkorderFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class EquMaintenanceAction extends AbstractAction{
	private WoJWorkorderFacadeRemote remote;
	private String equCode;
	private WoJWorkorder order;
	public EquMaintenanceAction(){
		remote = (WoJWorkorderFacadeRemote) factory
		.getFacadeRemote("WoJWorkorderFacade");
	}
	
	
	public void findListByEquCode() throws JSONException
	{
		Object start=request.getParameter("start");
		Object limit=request.getParameter("limit");
		PageObject obj=remote.findListByCode(equCode, employee.getEnterpriseCode(), Integer.parseInt(start.toString()), Integer.parseInt(limit.toString()));		
		write(JSONUtil.serialize(obj));
	}
	public void addMaintenance()
	{
		order.setEnterprisecode(employee.getEnterpriseCode());
		order.setIsUse("Y");
		remote.save(order);
		write("{success:true,Msg :'操作成功！'}");
	}
	public void updateMaintenance()
	{
		WoJWorkorder model=remote.findById(order.getId());
		order.setIsUse(model.getIsUse());
		order.setEnterprisecode(model.getEnterprisecode());
		order.setAnnex(model.getAnnex());
		remote.update(order);
		write("{success:true,Msg :'操作成功！'}");
	}
	public void deleteMaintenance()
	{
		WoJWorkorder model=remote.findById(order.getId());
		model.setIsUse("N");
		remote.update(model);
		write("{success:true,Msg :'操作成功！'}");
	}
	public String getEquCode() {
		return equCode;
	}

	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}


	public WoJWorkorder getOrder() {
		return order;
	}


	public void setOrder(WoJWorkorder order) {
		this.order = order;
	}

}
