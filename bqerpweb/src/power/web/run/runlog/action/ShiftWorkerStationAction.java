package power.web.run.runlog.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.hr.HrCStation;
import power.ejb.hr.HrCStationFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftWorker;
import power.ejb.run.runlog.shift.RunCShiftWorkerStation;
import power.ejb.run.runlog.shift.RunCShiftWorkerStationFacadeRemote;
import power.web.comm.AbstractAction;

public class ShiftWorkerStationAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	RunCShiftWorkerStationFacadeRemote remote;
	HrCStationFacadeRemote stationremote;
	private String shiftWorkerId;
	private String shiftWorkerStationId;
	/**
	 * 构造函数
	 */
	public ShiftWorkerStationAction(){
		remote =(RunCShiftWorkerStationFacadeRemote) factory.getFacadeRemote("RunCShiftWorkerStationFacade");
		stationremote =(HrCStationFacadeRemote) factory.getFacadeRemote("HrCStationFacade");
	}
	/*
	 * 获取值班员工岗位信息
	 */
	public void findShiftWorkerStationList() throws JSONException
	{
		if(shiftWorkerId != null)
		{
			List<RunCShiftWorkerStation> list=remote.findWStationByShiftWorker(Long.parseLong(shiftWorkerId), employee.getEnterpriseCode());
			write(JSONUtil.serialize(list));
		}
		else
		{
			List<RunCShiftWorkerStation> list=remote.findWStationByShiftWorker(null, employee.getEnterpriseCode());
			write(JSONUtil.serialize(list));
		}
	}
	/*
	 * 删除值班员工岗位
	 */
	public void deleteShiftWorkerStation(){
		RunCShiftWorkerStation model=remote.findById(Long.parseLong(shiftWorkerStationId));
		model.setIsUse("N");
		remote.update(model);
	}
	/*
	 * 查询所有岗位列表
	 */
	public void findAllStationList() throws JSONException{
		String fuzzy=request.getParameter("fuzzy");
		if(fuzzy == null)
		{
			fuzzy="";
		}
		List<HrCStation> list=stationremote.findStationListByFuzzy(fuzzy);
		write(JSONUtil.serialize(list));
	}
	/*
	 * 增加岗位
	 */
	public void addShiftWorkerStation(){
		RunCShiftWorkerStation model=new RunCShiftWorkerStation();
		String ids= request.getParameter("ids");
		String names= request.getParameter("names");
	    String [] stationId= ids.split(",");
	    String [] stationName=names.split(",");
			for(int i=0;i<stationId.length;i++)
			{
				if(!remote.isExsit(Long.parseLong(shiftWorkerId), Long.parseLong(stationId[i]), employee.getEnterpriseCode())){
					model.setShiftWorkerId(Long.parseLong(shiftWorkerId));
					model.setStationId(Long.parseLong(stationId[i]));
					model.setStationName(stationName[i]);
					model.setEnterpriseCode(employee.getEnterpriseCode());
					model.setIsUse("Y");
					remote.save(model);
				}
				else
				{
					
				}
			}
	}
	public String getShiftWorkerId() {
		return shiftWorkerId;
	}
	public void setShiftWorkerId(String shiftWorkerId) {
		this.shiftWorkerId = shiftWorkerId;
	}
	public String getShiftWorkerStationId() {
		return shiftWorkerStationId;
	}
	public void setShiftWorkerStationId(String shiftWorkerStationId) {
		this.shiftWorkerStationId = shiftWorkerStationId;
	}
}
