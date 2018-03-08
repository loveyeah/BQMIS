package power.web.run.runlog.action;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.run.runlog.RunCEquRunstatus;
import power.ejb.run.runlog.RunCEquRunstatusFacadeRemote;
import power.ejb.run.runlog.RunCEqustatus;
import power.ejb.run.runlog.RunCShiftEqu;
import power.ejb.run.runlog.RunCShiftEquFacadeRemote;
import power.ejb.run.runlog.RunJRunlogMain;
import power.ejb.run.runlog.RunJRunlogMainFacadeRemote;
import power.ejb.run.runlog.RunJRunlogWorker;
import power.ejb.run.runlog.shift.RunJShiftEqustatus;
import power.ejb.run.runlog.shift.RunJShiftEqustatusFacadeRemote;
import power.web.comm.AbstractAction;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class RunShiftEquAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RunCShiftEquFacadeRemote remote;
	private RunCEquRunstatusFacadeRemote statusRemote;
	private RunJShiftEqustatusFacadeRemote shiftRemote;
	private RunJRunlogMainFacadeRemote mainremote;
	private RunCShiftEqu equ; // 班组所关心设备运行方式数据
	private RunCEqustatus equstatus; // 设备运行状态维护
	private RunCEquRunstatus status; // 设备在某种运行方式下的状态维护
	private RunJShiftEqustatus shiftequStatus;
	private String specialcode;
	private String runEquId;
	
	private Long runwayId;

	public RunShiftEquAction() {
		remote = (RunCShiftEquFacadeRemote) factory.getFacadeRemote("RunCShiftEquFacade");
		statusRemote = (RunCEquRunstatusFacadeRemote) factory.getFacadeRemote("RunCEquRunstatusFacade");
		shiftRemote = (RunJShiftEqustatusFacadeRemote)factory.getFacadeRemote("RunJShiftEqustatusFacade");
		mainremote=(RunJRunlogMainFacadeRemote)factory.getFacadeRemote("RunJRunlogMainFacade");
	}

	//修改交接班设备
	public void updateRunShiftEqu() throws JSONException, CodeRepeatException
	{
		String str=request.getParameter("data");
		   Object obj = JSONUtil.deserialize(str);
		   List<Map> list = (List<Map>)obj;
		   for(Map data : list)
		   {
		       String id = ((Map) ((Map) data)).get("runEquId").toString();
		       String value = ((Map) ((Map)data)).get("equName").toString();
		       RunCShiftEqu model = remote.findById(Long.parseLong(id));
		       model.setEquName(value);
		       remote.update(model);
		       RunJShiftEqustatus xmodel=shiftRemote.findModle(model.getSpecialityCode(), model.getEnterpriseCode(),model.getAttributeCode());
				if(xmodel != null)
				{
					xmodel.setEquName(model.getEquName());
					shiftRemote.update(xmodel);
				}
		   } 
		  write("{success:true,id:'-1',msg:'保存成功！'}");
	}
	// 增加交接班设备
	public void addShiftEqu() 
	{
		    
			RunCShiftEqu model = new RunCShiftEqu();
			model.setSpecialityCode(equ.getSpecialityCode());
			model.setRunKeyId(equ.getRunKeyId());
			model.setEquName(equ.getEquName());
			model.setEnterpriseCode(employee.getEnterpriseCode());
			model.setIsUse("Y");			
			int equId = remote.save(model);		
			if(equId != -1)
			{
				RunCShiftEqu newmodel=remote.findById(Long.parseLong(String.valueOf(equId)));
				RunJShiftEqustatus nsmodel=new RunJShiftEqustatus();
				if(mainremote.findLatestModelBySpecial(newmodel.getSpecialityCode(), newmodel.getEnterpriseCode()) != null){
					RunJRunlogMain mainmodel=mainremote.findLatestModelBySpecial(newmodel.getSpecialityCode(), newmodel.getEnterpriseCode());
					nsmodel.setRunLogid(mainmodel.getRunLogid());
					nsmodel.setRunLogno(mainmodel.getRunLogno());
					nsmodel.setSpecialityCode(mainmodel.getSpecialityCode());
					nsmodel.setAttributeCode(newmodel.getAttributeCode());
					nsmodel.setEquName(newmodel.getEquName());
					nsmodel.setEnterpriseCode(newmodel.getEnterpriseCode());
					nsmodel.setEquStatusId(null);
					nsmodel.setEquStatusName(null);
					nsmodel.setIsUse("Y");
					shiftRemote.save(nsmodel);
				}
				write("{success:true,equId:'"+equId+"',msg:'增加成功！'}");
			}
			else
			{
				write("{success:true,equId:'-1',msg:'设备名称重复！'}");
			}
	}

	// 增加运行方式下的设备状态
	public void addRunStatus() {
		String ids = request.getParameter("ids");
		String code = request.getParameter("runEquId");
		status = new RunCEquRunstatus();
		status.setRunEquId(Long.parseLong(code));
		status.setEqustatusId(Long.parseLong(ids));
		status.setIsUse("Y");
		status.setEnterpriseCode(employee.getEnterpriseCode());

		int equstatusid = statusRemote.save(status);
	
		if(equstatusid != -1 )
		{
			write("{success:true,equId:'"+equstatusid+"',msg:'增加成功！'}");
		}
		else
		{
			write("{success:true,equId:'"+equstatusid+"',msg:'增加失败:状态已存在！'}");
		}
	}

	// 专业所关心的运行方式下的设备状态
	public void findRunStatus() throws JSONException 
	{
		String runEquId = request.getParameter("runEquId");
		 
		List<Object[]> list = statusRemote.findRunStatusList(runEquId, employee.getEnterpriseCode());
		StringBuffer sb = new StringBuffer();
		sb.append("{data:[");

		for (Object[] o : list) {
			sb.append("{");
			sb.append("\"runstatusId\":" + o[0] + ",");
			sb.append("\"runEquId\":\"" + (o[1] == null ? "" : o[1]) + "\",");
			sb.append("\"equstatusId\":\"" + (o[2] == null ? "" : o[2])+ "\",");
			sb.append("\"statusName\":\"" + (o[3] == null ? "" : o[3]) + "\",");
			sb.append("\"colorValue\":\"" + (o[4] == null ? "" : o[4]) + "\",");
			sb.append("'statusDesc':'" + (o[5] == null ? "" : o[5]) + "'");
			sb.append("},");
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("]}");
		write(sb.toString());
	}

	
	// 专业所关心的运行方式设备
	public void findShiftEqu() throws JSONException
	{
		int start = 0;
		int limit = 10000;
		if (runwayId == null) {
			return;
		}
		String enterpriseCode = employee.getEnterpriseCode();
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			 start = Integer.parseInt(request.getParameter("start"));
			 limit = Integer.parseInt(request.getParameter("limit"));
		}
			obj = remote.getShiftEquList(specialcode, runwayId, enterpriseCode,
					start, limit);
		
		String str = "{total:" + obj.getTotalCount() + ",root:"+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);

	}

	// 删除运行方式下的设备状态
	public void deleteRunStatus() {
		RunCEquRunstatus model = statusRemote.findById(status.getRunstatusId());
		model.setIsUse("N");
		statusRemote.update(model);
		write("{success:true}");
	}

	// 删除运行方式设备及其状态记录
	public void deleteShiftEqu() throws CodeRepeatException {
		RunCShiftEqu model = remote.findById(equ.getRunEquId());
		model.setIsUse("N");
		remote.update(model);
		RunJShiftEqustatus xmodel=shiftRemote.findModle(model.getSpecialityCode(), model.getEnterpriseCode(),model.getAttributeCode());
		if(xmodel != null)
		{
			xmodel.setIsUse("N");
			shiftRemote.update(xmodel);
		}
		write("{success:true}");
	}

	
	// 运行方式下拉列表数据源
	public void getRunWayAllList() throws Exception {
		GetDataForCombox(remote.getRunWayList());
	}

	// 获得某个combox的数据源
	public void GetDataForCombox(List list) throws Exception {
		Iterator it = list.iterator();
		String str = "[";
		String id = "";
		String name = "";
		while (it.hasNext()) {
			id = "";
			name = "";
			Object[] data = (Object[]) it.next();
			if (data[0] != null) {
				id = data[0].toString();
			}
			if (data[1] != null) {
				name = data[1].toString();
			}
			str = str + "{'id':" + id + ",'name':'" + name + "'},";
		}
		if (!str.equals("[")) {
			str = str.substring(0, str.length() - 1);
		}
		str = str + "]";
		write(str);
	}

	public RunCShiftEqu getEqu() {
		return equ;
	}

	public void setEqu(RunCShiftEqu equ) {
		this.equ = equ;
	}

	public RunCEquRunstatus getStatus() {
		return status;
	}

	public void setStatus(RunCEquRunstatus status) {
		this.status = status;
	}

	public RunCEqustatus getEqustatus() {
		return equstatus;
	}

	public void setEqustatus(RunCEqustatus equstatus) {
		this.equstatus = equstatus;
	}

	public Long getRunwayId() {
		return runwayId;
	}

	public void setRunwayId(Long runwayId) {
		this.runwayId = runwayId;
	}

	public String getRunEquId() {
		return runEquId;
	}

	public void setRunEquId(String runEquId) {
		this.runEquId = runEquId;
	}

	public String getSpecialcode() {
		return specialcode;
	}

	public void setSpecialcode(String specialcode) {
		this.specialcode = specialcode;
	}

	public RunJShiftEqustatus getShiftequStatus() {
		return shiftequStatus;
	}

	public void setShiftequStatus(RunJShiftEqustatus shiftequStatus) {
		this.shiftequStatus = shiftequStatus;
	}

}
