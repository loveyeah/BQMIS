package power.web.run.securityproduction.action.safesuperviseaction;

import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.safesupervise.SpJPressureRepair;
import power.ejb.run.securityproduction.safesupervise.SpJPressureRepairFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SpJPressureRepairAction extends AbstractAction {
	private SpJPressureRepairFacadeRemote maintRemote;
	private SpJPressureRepair repair;
	private int start;
	private int limit;

	public SpJPressureRepairAction() {
		maintRemote = (SpJPressureRepairFacadeRemote) factory
				.getFacadeRemote("SpJPressureRepairFacade");
	}

	public void saveSpJPressureRepair() throws Exception {
		String type = request.getParameter("type");
		repair.setType(type);
		repair.setEnterpriseCode(employee.getEnterpriseCode());
		repair.setFillBy(employee.getWorkerCode());// add by ltong
		repair.setFillTime(new Date());// add by ltong
		maintRemote.saveSpJPressureRepair(repair);
		write("{success:true,msg:'增加成功！'}");
	}

	public void updateSpJPressureRepair() throws Exception {
		SpJPressureRepair entity = maintRemote.findById(repair
				.getBoilerRepairId());
		entity.setBoilerId(repair.getBoilerId());
		entity.setRepairBegin(repair.getRepairBegin());
		entity.setRepairEnd(repair.getRepairEnd());
		entity.setReportNo(repair.getReportNo());
		entity.setRepairUnit(repair.getRepairUnit());
		entity.setRepairResult(repair.getRepairResult());
		entity.setNextTime(repair.getNextTime());
		entity.setIsUse("Y");
		entity.setFillBy(employee.getWorkerCode());// add by ltong
		entity.setFillTime(new Date());// add by ltong
		repair.setEnterpriseCode(employee.getEnterpriseCode());
		maintRemote.updateSpJPressureRepair(entity);
		write("{success:true,msg:'修改成功！'}");
	}

	public void deleteSpJPressureRepair() {
		String ids = request.getParameter("boilerRepairId");
		maintRemote.deleteSpJPressureRepair(ids);
		write("{success:true,msg:'删除成功！'}");
	}

	public void getSpJPressureRepairList() throws JSONException {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String nextTime = request.getParameter("sNextTime");
		String type = request.getParameter("type");
		String isMaint = request.getParameter("isMaint");// add by ltong
		String queryName = request.getParameter("queryName");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		PageObject pg = null;
		if (start != null && limit != null && !start.equals(""))
			pg = maintRemote.findSpJPressureRepairList(nextTime, type, employee
					.getEnterpriseCode(), queryName, startTime, endTime,
					employee.getWorkerCode(), isMaint, Integer.parseInt(start),
					Integer.parseInt(limit));// update by ltong
		else
			pg = maintRemote.findSpJPressureRepairList(nextTime, type, employee
					.getEnterpriseCode(), queryName, startTime, endTime,
					employee.getWorkerCode(), isMaint);// update by ltong
		write(JSONUtil.serialize(pg));// update by ltong

	}

	// add by ltong 20100517
	public void findptYlrqJDjList() throws JSONException {
		PageObject pg = maintRemote.findptYlrqJDjList();
		write(JSONUtil.serialize(pg));
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public SpJPressureRepair getRepair() {
		return repair;
	}

	public void setRepair(SpJPressureRepair repair) {
		this.repair = repair;
	}

}
