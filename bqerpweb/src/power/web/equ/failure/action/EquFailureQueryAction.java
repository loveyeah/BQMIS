package power.web.equ.failure.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.equ.failure.BqEquJfailuresRemote;
import power.ejb.equ.failure.EquFailuresQueryForm;
import power.ejb.equ.failure.EquJFailuresFacadeRemote;
import power.ejb.equ.failure.EquJFailuresQueryRemote;
import power.ejb.item.ItemJDataFacadeRemote;
import power.web.comm.AbstractAction;

public class EquFailureQueryAction extends AbstractAction {
	private EquJFailuresQueryRemote queryremote;
	private EquJFailuresFacadeRemote fremote;
	private BqEquJfailuresRemote bqremote;
	private String type;
	private String startDate;
	private String endDate;
	private String workercode;
	private String dominationProfession;
	private String repairDep;
	private String awaitType;
	private String failureStatus;
	public EquFailureQueryAction(){
		queryremote = (EquJFailuresQueryRemote) factory
		.getFacadeRemote("EquJFailuresQuery");
		fremote = (EquJFailuresFacadeRemote) factory
		.getFacadeRemote("EquJFailuresFacade");
		bqremote = (BqEquJfailuresRemote) factory
		.getFacadeRemote("BqEquJfailures");

	}
	//缺陷综合查询
	public void failureIntegerQuery()
	{
		try{
			List<EquFailuresQueryForm> list=queryremote.failureIntegerQuery(startDate, endDate, type, employee.getEnterpriseCode());
			write(JSONUtil.serialize(list));
		}
		catch(Exception e)
		{
			write(e.getMessage());
		}
	}
	//缺陷综合查询饼图分析
	public void getIntegerPie(){
		List<EquFailuresQueryForm> list=queryremote.failureIntegerQuery(startDate, endDate, type, employee.getEnterpriseCode());
		String strPie = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
		"  <pie>\n";
		String title;
		String value;
		if(list != null && list.size() > 0)
		{
			for(int i=0;i < list.size();i ++)
			{
				EquFailuresQueryForm item = list.get(i);
				title = item.getQueryTypeName();
				value = item.getCount();
				strPie += "<slice title=\"";
				strPie += title;
				strPie += "\">" + value + "</slice>\n"; 
			}
		strPie += "</pie>";
		}
		super.writeXml(strPie);
	}
	public void getIntegerColumn(){
		List<EquFailuresQueryForm> list=queryremote.failureIntegerQuery(startDate, endDate, type, employee.getEnterpriseCode());
		String strCol = " <?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
				"<chart>\n" + 
				"<series>\n";
		String unit;
		String value;
		if(list.size() > 0)
		{
			for(int i = 0;i < list.size();i ++)
			{
				EquFailuresQueryForm item = list.get(i);
				unit = item.getQueryTypeName();
				strCol += "<value xid=\"" + Integer.toString(i) + "\">" + unit + "</value>\n";
			}
		}
		strCol += " </series>\n" + 
					"<graphs>\n" +
					"<graph gid=\"1\" >\n";
		if(list != null && list.size() > 0)
		{
			for(int i=0;i < list.size();i ++)
			{
				EquFailuresQueryForm item = list.get(i);
				value = item.getCount();
					strCol += "<value xid=\"" + Integer.toString(i) +"\">" + value + "</value>\n";
			}
		}
		strCol += "</graph>\n";
		strCol += "</graphs>\n";
		strCol += "</chart>";
		super.writeXml(strCol);
	}
	public void getIntegerLine(){
		String unit;
		List<EquFailuresQueryForm> list=queryremote.failureIntegerQuery(startDate, endDate, type, employee.getEnterpriseCode());
		String slice = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
						" <chart>\n" + 
						" <series>\n";
		if(list.size()>0)
		{
			for(int i=0;i<list.size();i++)
			{
				EquFailuresQueryForm item = list.get(i);
				unit = item.getQueryTypeName();
				slice+="  <value xid=\""+i+"\">"+unit+"</value>\n"; 
			}
		}
	slice+=	"  </series>\n" +
			"<graphs>\n";
		String value;
		String month;
		if(list != null && list.size()>0)
		{
			slice += " <graph gid=\"1\" title=\"统计\">\n";
			for(int i=0;i < list.size();i ++)
			{
				EquFailuresQueryForm item = list.get(i);
				value = item.getCount();
				unit = item.getQueryTypeName();
				slice += "<value xid=\""+i+"\" color=\"#6699FF\" description=\"条缺陷\">";
				slice += value;
				slice += "</value>";
			}
			slice += "</graph>\n";
		}
		slice += "  </graphs></chart>";
	   super.writeXml(slice);
	}
	//缺陷个人发现查询
	public void failureFindQuery() throws JSONException
	{
		int start = 0;
		int limit = 900000000;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject list=queryremote.failureFindQuery(startDate, endDate, employee.getEnterpriseCode(), workercode,start,limit);
		String str = "{total:" + list.getTotalCount() + ",list:"
		+ JSONUtil.serialize(list.getList()) + "}";
		write(str);
	}
	//缺陷个人消缺查询
	public void failureEliminateQuery() throws JSONException
	{
		int start = 0;
		int limit = 900000000;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject list=queryremote.failueEliminateQuery(startDate, endDate, employee.getEnterpriseCode(), workercode,start,limit);
		String str = "{total:" + list.getTotalCount() + ",list:"
		+ JSONUtil.serialize(list.getList()) + "}";
		write(str);
	}
	//无kks编码缺陷统计
	public void noKKSfailureQuery() throws JSONException
	{
		int start = 0;
		int limit = 900000000;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject list=queryremote.noKKSfailureQuery(employee.getEnterpriseCode(),start,limit);
		String str = "{total:" + list.getTotalCount() + ",list:"
		+ JSONUtil.serialize(list.getList()) + "}";
		write(str);
	}
	//查询员工登记无kks编码缺陷列表
	public void noKKSfailureQueryByWorker() throws JSONException
	{
		int start = 0;
		int limit = 900000000;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject list=queryremote.findNoKKSfailureByWorker(employee.getEnterpriseCode(),workercode,start,limit);
		String str = "{total:" + list.getTotalCount() + ",list:"
		+ JSONUtil.serialize(list.getList()) + "}";
		write(str);
	}
	//缺陷统计按（系统）
	public void failureQueryBySystem() throws JSONException
	{
		int start = 0;
		int limit = 900000000;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject list=queryremote.failureQueryBySystem(startDate, endDate,employee.getEnterpriseCode(),start,limit);
		String str = "{total:" + list.getTotalCount() + ",list:"
		+ JSONUtil.serialize(list.getList()) + "}";
		write(str);
	}
	//缺陷统计（按设备）
	public void failureQueryByEqu() throws JSONException
	{
		int start = 0;
		int limit = 900000000;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject list=queryremote.failureQueryByEqu(startDate, endDate,employee.getEnterpriseCode(),start,limit);
		String str = "{total:" + list.getTotalCount() + ",list:"
		+ JSONUtil.serialize(list.getList()) + "}";
		write(str);
		
	}
	public void failureAwaitQuery() throws JSONException
	{
		int start = 0;
		int limit = 900000000;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject list=queryremote.awaitFailureQuery(startDate, endDate, repairDep, dominationProfession, awaitType, failureStatus, employee.getEnterpriseCode(), start, limit);
		String str = "{total:" + list.getTotalCount() + ",list:"
		+ JSONUtil.serialize(list.getList()) + "}";
		write(str);
	}
	public void failureMonthReport() throws JSONException
	{
		List<EquFailuresQueryForm> list=queryremote.failureMonthReport(startDate, employee.getEnterpriseCode());
		String str=JSONUtil.serialize(list);
		write(str);
//		queryremote.yearReportQueryByDept("2009", employee.getEnterpriseCode());
	}
	
	
	/**
	 * 查询已消除缺陷数列、未消除缺陷数、退回缺陷数、超时缺陷数详细明细
	 * add by sychen 20100915
	 */
	public void getEquDetailList() throws JSONException {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String writeDate= request.getParameter("writeDate");
		String specialty= request.getParameter("specialty");
		String defectType= request.getParameter("defectType");
		PageObject pg = new PageObject();
		if (start != null && limit != null)
			pg = queryremote.getEquDetailList(writeDate,specialty, defectType,employee.getEnterpriseCode(),Integer.parseInt(start), Integer.parseInt(limit));
		else
			pg = queryremote.getEquDetailList(writeDate,specialty, defectType,employee.getEnterpriseCode());

		if (pg.getTotalCount() > 0) {
			write(JSONUtil.serialize(pg));
		} else {
			write("{totalCount : 0,list :[]}");
		}
	}
	
	
	public void findNotEliminateFailureList() throws JSONException
	{
		int start = 0;
		int limit = 100000;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject list = bqremote.findListByStatus("'1', '2', '5', '7', '8', '9', '11', '12', '13', '15', '16', '17','18','20','21'", employee
				.getEnterpriseCode(), startDate, endDate, start, limit, "",
				dominationProfession, "", "");
		String str = "{total:" + list.getTotalCount() + ",list:"
				+ JSONUtil.serialize(list.getList()) + "}";
		write(str);
	}
	public void findBackFailureList() throws JSONException
	{
		int start = 0;
		int limit = 100000;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject list = bqremote.findListByStatus("'10'", employee
				.getEnterpriseCode(), startDate, endDate, start, limit, "",
				dominationProfession, "", "");
		String str = "{total:" + list.getTotalCount() + ",list:"
				+ JSONUtil.serialize(list.getList()) + "}";
		write(str);
	}
	public void failureYearReport()
	{
		
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getWorkercode() {
		return workercode;
	}
	public void setWorkercode(String workercode) {
		this.workercode = workercode;
	}
	public String getDominationProfession() {
		return dominationProfession;
	}
	public void setDominationProfession(String dominationProfession) {
		this.dominationProfession = dominationProfession;
	}
	public String getRepairDep() {
		return repairDep;
	}
	public void setRepairDep(String repairDep) {
		this.repairDep = repairDep;
	}
	public String getAwaitType() {
		return awaitType;
	}
	public void setAwaitType(String awaitType) {
		this.awaitType = awaitType;
	}
	public String getFailureStatus() {
		return failureStatus;
	}
	public void setFailureStatus(String failureStatus) {
		this.failureStatus = failureStatus;
	}
}
