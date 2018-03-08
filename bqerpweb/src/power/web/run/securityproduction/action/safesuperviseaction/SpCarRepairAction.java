package power.web.run.securityproduction.action.safesuperviseaction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.safesupervise.SpJCarRepair;
import power.ejb.run.securityproduction.safesupervise.SpJCarRepairFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SpCarRepairAction extends AbstractAction{

	protected SpJCarRepairFacadeRemote remote;
	private SpJCarRepair repair;
	
	public SpCarRepairAction()
	{
		remote = (SpJCarRepairFacadeRemote)factory.getFacadeRemote("SpJCarRepairFacade");
	}
	
	//增加一条车辆维修记录
	public void addCarRepair()
	{
		repair.setEnterpriseCode(employee.getEnterpriseCode());
		remote.save(repair);
		write("{success:true,msg:'增加成功！'}");
	}
	
	//修改车辆维修记录
	public void updateCarRepair()
	{
		SpJCarRepair model = remote.findById(repair.getRepairId());
		model.setCarId(repair.getCarId());
		model.setNowKmNum(repair.getNowKmNum());
		model.setRepairContend(repair.getRepairContend());
		model.setRepairDate(repair.getRepairDate());
		model.setSendPerson(employee.getWorkerCode());
		remote.update(model);
		write("{success:true,msg:'修改成功！'}");
	}
	
	//删除一条或多条车辆维修记录
	public void deleteCarRepair()
	{
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	//根据维修时间段，车牌号查询车辆维修记录列表
	public void findCarRepairList() throws JSONException
	{
		String sDate = request.getParameter("sDate");
		String eDate = request.getParameter("eDate");
		String carNo = request.getParameter("carNo");
		String flag = request.getParameter("flag");
		String workCode = "";
		if(flag!=null&&flag.equals("G"))
		{
			workCode = employee.getWorkerCode();
		}
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) 
		{
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findCarRepairList(employee.getEnterpriseCode(),workCode, sDate, eDate, carNo, start,limit);
		}
		else {
			obj = remote.findCarRepairList(employee.getEnterpriseCode(),workCode, sDate, eDate, carNo);
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}
	
	
	public SpJCarRepair getRepair() {
		return repair;
	}
	public void setRepair(SpJCarRepair repair) {
		this.repair = repair;
	}
	
}
