package power.web.run.securityproduction.action.safesuperviseaction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.safesupervise.SpJBeltRepair;
import power.ejb.run.securityproduction.safesupervise.SpJBeltRepairFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SpJBeltRepairAction extends  AbstractAction 
{
	protected  SpJBeltRepairFacadeRemote  remote;
	private  SpJBeltRepair  beltRepair;
	public SpJBeltRepairAction()
	{
		remote = (SpJBeltRepairFacadeRemote)factory.getFacadeRemote("SpJBeltRepairFacade");
	}
	//增加安全清册记录
	public void addBeltRepair() throws ParseException
	{
		
		String nextTime = request.getParameter("nextTime");
		String useTime = request.getParameter("useTime");
		String repairBegin = request.getParameter("repairBegin");
		String repairEnd = request.getParameter("repairEnd");
		java.text.SimpleDateFormat sf= new java.text.SimpleDateFormat("yyyy-MM-dd");
		if(nextTime!=null&&!nextTime.equals(""))
		{
			beltRepair.setNextTime(sf.parse(nextTime));
		}
		beltRepair.setUseTime(useTime);
		if(repairBegin!=null&&!repairBegin.equals(""))
		{
			beltRepair.setRepairBegin(sf.parse(repairBegin));
		}
		if(repairEnd!=null&&!repairEnd.equals(""))
		{
			beltRepair.setRepairEnd(sf.parse(repairEnd));
		}
		beltRepair.setFillBy(employee.getWorkerCode());
		beltRepair.setFillTime(new Date());
		beltRepair.setEnterpriseCode(employee.getEnterpriseCode());
		remote.addBeltRepair(beltRepair);
		write("{success:true,msg:'增加成功！'}");
	}
	public void updateBeltRepair() throws ParseException
	{
		SpJBeltRepair model = remote.findById(beltRepair.getRepairId());
		String nextTime = request.getParameter("nextTime");
		String useTime = request.getParameter("useTime");
		String repairBegin = request.getParameter("repairBegin");
		String repairEnd = request.getParameter("repairEnd");
		java.text.SimpleDateFormat sf= new java.text.SimpleDateFormat("yyyy-MM-dd");
		if(nextTime!=null&&!nextTime.equals(""))
		{
			model.setNextTime(sf.parse(nextTime));
		}
		model.setUseTime(useTime);
		if(repairBegin!=null&&!repairBegin.equals(""))
		{
		model.setRepairBegin(sf.parse(repairBegin));
		}
		if(repairEnd!=null&&!repairEnd.equals(""))
		{
			model.setRepairEnd(sf.parse(repairEnd));
		}
		model.setToolId(beltRepair.getToolId());
		model.setBelongDep(beltRepair.getBelongDep());
		model.setBeltNumber(beltRepair.getBeltNumber());
		model.setEnterpriseCode(employee.getEnterpriseCode());
		model.setIsUse("Y");
		model.setMemo(beltRepair.getMemo());
		model.setRepairBy(beltRepair.getRepairBy());
		model.setRepairDep(beltRepair.getRepairDep());
		model.setRepairResult(beltRepair.getRepairResult());
		remote.update(model);
		write("{success:true,msg:'修改成功！'}");
	}
	public void deleteBeltRepair()
	{
		String ids = request.getParameter("ids");
		remote.deleteBeltRepair(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	public void findBeltRepairList() throws JSONException
	{
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.getBeltRepair(employee.getWorkerCode(), beginTime,
					endTime, employee.getEnterpriseCode(), start, limit);
		} else {
			// modified by liuyi 20100426 
//			remote.getBeltRepair(employee.getWorkerCode(), beginTime, endTime,
//					employee.getEnterpriseCode());
			obj = remote.getBeltRepair(employee.getWorkerCode(), beginTime, endTime,
					employee.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}
	public SpJBeltRepair getBeltRepair() {
		return beltRepair;
	}
	public void setBeltRepair(SpJBeltRepair beltRepair) {
		this.beltRepair = beltRepair;
	}
	
	
	
	}