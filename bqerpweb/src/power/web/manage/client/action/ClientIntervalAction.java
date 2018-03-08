package power.web.manage.client.action;

import java.text.ParseException;
import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.client.ConCInterval;
import power.ejb.manage.client.ConCIntervalFacadeRemote;
import power.web.comm.AbstractAction;

public class ClientIntervalAction extends AbstractAction{

	private ConCIntervalFacadeRemote intervalRemote;
	private ConCInterval interval;
	
	public ClientIntervalAction()
	{
		intervalRemote=(ConCIntervalFacadeRemote) factory.getFacadeRemote("ConCIntervalFacade");
	}
	
	public void findClientIntervalList() throws JSONException
	{
		
		Object objstart = request.getParameter("start");
		
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			
			object = intervalRemote.findAll(employee.getEnterpriseCode(), start,limit);
		} else {
			
			object = intervalRemote.findAll(employee.getEnterpriseCode());
		}
		// 输出结果
		String strOutput = "";
		// 查询结果为null
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
			// 不为null
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	public void addClientIntervalInfo()
	{
		interval.setEnterpriseCode(employee.getEnterpriseCode());
		interval.setRecordBy(employee.getWorkerCode());
		interval.setRecordDate(new Date());
		try {
			intervalRemote.save(interval);
			write("{success:true,msg:'增加成功！'}");
		} catch (ParseException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	public void updateClientIntervalInfo()
	{
		ConCInterval model=intervalRemote.findById(interval.getIntervalId());
		model.setBeginDate(interval.getBeginDate());
		model.setEndDate(interval.getEndDate());
		model.setEvaluationDays(interval.getEvaluationDays());
		model.setMemo(interval.getMemo());
		model.setRecordBy(employee.getWorkerCode());
		model.setRecordDate(new Date());
		try {
			intervalRemote.update(model);
			write("{success:true,msg:'修改成功！'}");
		} catch (ParseException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	public void deleteClientIntervalInfo()
	{
		String ids=request.getParameter("ids");
		intervalRemote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}

	public ConCInterval getInterval() {
		return interval;
	}

	public void setInterval(ConCInterval interval) {
		this.interval = interval;
	}
	
}
