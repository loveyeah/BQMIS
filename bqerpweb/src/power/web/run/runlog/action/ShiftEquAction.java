package power.web.run.runlog.action;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.runlog.RunCEquRunstatusFacadeRemote;
import power.ejb.run.runlog.RunCRunWay;
import power.ejb.run.runlog.RunCShiftEquFacadeRemote;
import power.ejb.run.runlog.shift.RunJEquRunStatusHis;
import power.ejb.run.runlog.shift.RunJEquRunStatusHisFacadeRemote;
import power.ejb.run.runlog.shift.RunJShiftEqustatus;
import power.ejb.run.runlog.shift.RunJShiftEqustatusFacadeRemote;
import power.web.comm.AbstractAction;

public class ShiftEquAction extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RunCShiftEquFacadeRemote shiftRemote;
	private RunJShiftEqustatusFacadeRemote statusRemote;
	private RunCEquRunstatusFacadeRemote runStatusRemote;
	private RunJEquRunStatusHisFacadeRemote hisRemote;
	
	private RunCRunWay runWay;
	private RunJEquRunStatusHis his;
	private RunJShiftEqustatus equStatus;
	private RunJEquRunStatusHis hisStatus;
	//private RunJEquRealtimeStatus realStatus;
	
	private Long runlogId;
	private Long runKeyId;
	private String shiftEqustatusId;
	private String memo;
	private Long runEquId;
	private Long statusId;
	private String equCode;
	private String specialcode;
	private String operateTime;
	
	public ShiftEquAction()
	{
		shiftRemote = (RunCShiftEquFacadeRemote)factory.getFacadeRemote("RunCShiftEquFacade");
		statusRemote = (RunJShiftEqustatusFacadeRemote)factory.getFacadeRemote("RunJShiftEqustatusFacade");
		runStatusRemote = (RunCEquRunstatusFacadeRemote)factory.getFacadeRemote("RunCEquRunstatusFacade");
		hisRemote = (RunJEquRunStatusHisFacadeRemote)factory.getFacadeRemote("RunJEquRunStatusHisFacade");
		
	}
	
	public void addAndUpdateShiftEqu() throws ParseException
	{  
		//runlogid = Long.parseLong(request.getParameter("runlogid"));
		Long newsid=Long.parseLong(request.getParameter("newstatusId"));
		String newsname=request.getParameter("newstatusName");
		memo = request.getParameter("operateMemo");
		operateTime = request.getParameter("operateTime");
		RunJShiftEqustatus model = new RunJShiftEqustatus();
		model = statusRemote.findById(Long.parseLong(shiftEqustatusId));		
		
		//  增加设备运行状态变更历史记录
		RunJEquRunStatusHis hmodel = new RunJEquRunStatusHis();
		
		hmodel.setRunLogid(model.getRunLogid());
		hmodel.setRunLogno(model.getRunLogno());
		hmodel.setAttributeCode(model.getAttributeCode());
		hmodel.setEquName(model.getEquName());
		hmodel.setFromStatusId(model.getEquStatusId());
		hmodel.setFromStatusName(model.getEquStatusName());
		hmodel.setToStatusId(newsid);
		hmodel.setToStatusName(newsname);
		hmodel.setOperaterBy(employee.getWorkerCode());
		hmodel.setOperateTime(new Date());
		hmodel.setOperateMemo(memo);
		hmodel.setIsUse("Y");
		hmodel.setEnterpriseCode(employee.getEnterpriseCode());
		
		//hmodel.setStandingTime(1.0);
		List<Object[]> list = hisRemote.getEquStatusHisList(model.getAttributeCode(), employee.getEnterpriseCode());
		if(list.size() > 0)
		{
			//Date prevdate =list.get(0).getOperateTime();//最近一次更改设备运行状态操作时间
			Object[] o=list.get(0);
			RunJEquRunStatusHis xmodel=hisRemote.findById(Long.parseLong(o[0].toString()));
			Date prevdate=xmodel.getOperateTime();
			Date nowtime=new Date();
			long millisecond=nowtime.getTime()-prevdate.getTime(); 
			double yy=(double)millisecond/(1000 * 60 * 60);
			double timespan=Math.floor(yy *100+.5)/100;
			hmodel.setStandingTime(timespan);
		}
		
		//	修改交接班设备状态表 
		model.setEquStatusId(newsid);
		model.setEquStatusName(newsname);
		
		hisRemote.save(hmodel);
		statusRemote.update(model);
		write("{success:true,msg:'修改成功！'}");
	}
	
	//运行方式列表
	public void findRunWayByProfession() throws JSONException
	{
		List<Object[]> list = shiftRemote.getRunWayByProfession(specialcode, employee.getEnterpriseCode());
		String sb = "";
		for (Object[] o : list) {
			sb += "{";
			sb +=  "\"runKeyId\":" + o[0] + ",";
			sb +=  "'runWayName':'" + (o[1] == null ? "" : o[1]) + "'";
			sb +=  "},";
		}
		if (sb.length() > 1) {
			sb = sb.substring(0, sb.length() - 1);
		} 
		sb = "{data:[" + sb + "]}";
		write(sb.toString());
	}
	
	//根据专业查询某种运行方式下的设备运行状态
	public void findShiftEquStatus()
	{
		String fuzzy = "";
		Object myobj = request.getParameter("fuzzy");
		if (myobj != null) {
			fuzzy = myobj.toString();
		}
		String enterpriseCode = employee.getEnterpriseCode();
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");

		if (runKeyId == null) {
			return;
		}
		if (objstart != null && objlimit != null) {
			PageObject obj = new PageObject();
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));

			obj = statusRemote.getShiftEquStatusList(fuzzy, specialcode,runlogId, runKeyId, enterpriseCode, start, limit);

			String sb = "";
			List<Object[]> list = obj.getList();
			for (Object[] o : list) {
				sb += "{";
				sb += "\"shiftEqustatusId\":" + o[0] + ",";
				sb += "\"runLogid\":\"" + (o[1] == null ? "" : o[1]) + "\",";
				sb += "\"runLogno\":\"" + (o[2] == null ? "" : o[2]) + "\",";
				sb += "\"specialityCode\":\"" + (o[3] == null ? "" : o[3])
						+ "\",";
				sb += "\"attributeCode\":\"" + (o[4] == null ? "" : o[4])
						+ "\",";
				sb += "\"equName\":\"" + (o[5] == null ? "" : o[5]) + "\",";
				sb += "\"equStatusId\":\"" + (o[6] == null ? "" : o[6]) + "\",";
				sb += "\"equStatusName\":\"" + (o[7] == null ? "" : o[7])
						+ "\",";
				sb += "'colorValue':'" + (o[10] == null ? "" : o[10]) + "'";
				sb += "},";
			}
			if (sb.length() > 1) {
				sb = sb.substring(0, sb.length() - 1);
			}
			sb = "{root:[" + sb + "]}";
			write(sb.toString());
		}
	}

	//查找设备运行状态变更记录
	
	public void findEquStatusHis() throws JSONException
	{
	    //List<RunJEquRunStatusHis> list = hisRemote.getEquStatusHisList(equCode, employee.getEnterpriseCode());
		List<Object[]> list =hisRemote.getEquStatusHisList(equCode, employee.getEnterpriseCode());
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (Object[] o : list) {
			sb.append("{");
			sb.append("\"runStatusHisId\":"+o[0]+",");
			sb.append("\"runLogid\":\""+(o[1]==null?"":o[1])+"\",");
			sb.append("\"runLogno\":\""+(o[2]==null?"":o[2])+"\",");
			sb.append("\"attributeCode\":\""+(o[3]==null?"":o[3])+"\",");
			sb.append("\"equName\":\""+(o[4]==null?"":o[4])+"\",");
			sb.append("\"fromStatusId\":\""+(o[5]==null?"":o[5])+"\",");
			sb.append("\"fromStatusName\":\""+(o[6]==null?"":o[6])+"\",");
			sb.append("\"toStatusId\":\""+(o[7]==null?"":o[7])+"\",");
			sb.append("\"toStatusName\":\""+(o[8]==null?"":o[8])+"\",");
			sb.append("\"standingTime\":\""+(o[9]==null?"":o[9])+"\",");
			sb.append("\"operaterBy\":\""+(o[10]==null?"":o[10])+"\",");
			sb.append("\"operateTime\":\""+(o[12]==null?"":o[12])+"\",");
			sb.append("\"operateMemo\":\""+(o[13]==null?"":o[13])+"\",");
			sb.append("\"workerName\":\""+(o[11]==null?"":o[11])+"\"");
			sb.append("},");
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.lastIndexOf(","));
		} 
		sb.append("]");
		write(sb.toString());
		//write(JSONUtil.serialize(list));
	}
	
	//取设备运行状态
	public void findListExcept() throws Exception
	{	 	
		RunJShiftEqustatus shiftModel = new RunJShiftEqustatus();
		shiftModel = statusRemote.findById(Long.parseLong(shiftEqustatusId));
		Long statusid=shiftModel.getEquStatusId();
		if(shiftModel.getEquStatusId() == null)
		{
			statusid=0L;
		}
		if(specialcode!=null && runKeyId!=null)
		{
			Long runequid=shiftRemote.GetRunWayIdModel(specialcode, runKeyId,shiftModel.getAttributeCode(), employee.getEnterpriseCode()).getRunEquId();
			GetDataForCombox(runStatusRemote.GetListExcept(runequid, statusid, employee.getEnterpriseCode()));
		}   
		else{
			write("[]");
		}
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
		//System.out.println(str);
		write(str);
	}

	public RunCRunWay getRunWay() {
		return runWay;
	}

	public void setRunWay(RunCRunWay runWay) {
		this.runWay = runWay;
	}

	public Long getRunKeyId() {
		return runKeyId;
	}

	public void setRunKeyId(Long runKeyId) {
		this.runKeyId = runKeyId;
	}

	public RunJShiftEqustatus getEquStatus() {
		return equStatus;
	}

	public void setEquStatus(RunJShiftEqustatus equStatus) {
		this.equStatus = equStatus;
	}

	public Long getRunEquId() {
		return runEquId;
	}

	public void setRunEquId(Long runEquId) {
		this.runEquId = runEquId;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public String getShiftEqustatusId() {
		return shiftEqustatusId;
	}

	public void setShiftEqustatusId(String shiftEqustatusId) {
		this.shiftEqustatusId = shiftEqustatusId;
	}

	public RunJEquRunStatusHis getHis() {
		return his;
	}

	public void setHis(RunJEquRunStatusHis his) {
		this.his = his;
	}

	public RunJEquRunStatusHis getHisStatus() {
		return hisStatus;
	}

	public void setHisStatus(RunJEquRunStatusHis hisStatus) {
		this.hisStatus = hisStatus;
	}

	public String getSpecialcode() {
		return specialcode;
	}

	public void setSpecialcode(String specialcode) {
		this.specialcode = specialcode;
	}

	public String getEquCode() {
		return equCode;
	}

	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

	public Long getRunlogId() {
		return runlogId;
	}

	public void setRunlogId(Long runlogId) {
		this.runlogId = runlogId;
	}



}
