package power.web.run.runlog.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.run.runlog.RunCLogWeatherFacadeRemote;
import power.ejb.run.runlog.RunJRunlogMain;
import power.ejb.run.runlog.RunJRunlogMainFacadeRemote;
import power.ejb.run.runlog.RunJRunlogWorkerFacadeRemote;
import power.ejb.run.runlog.RunJShiftParm;
import power.ejb.run.runlog.RunJShiftParmFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftTime;
import power.ejb.run.runlog.shift.RunCShiftTimeFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftWorkerFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftWorkerStationFacadeRemote;
import power.ejb.run.runlog.shift.RunJShiftEqustatusFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.run.timework.action.TimeworkGenerateAction;

public class RunLogQueryAction extends AbstractAction {
	private RunCLogWeatherFacadeRemote weatherremote;
	private RunJRunlogMainFacadeRemote mainremote;
	private RunCShiftTimeFacadeRemote timeremote;
	private RunCShiftFacadeRemote shiftremote;
	private RunJRunlogWorkerFacadeRemote workerremote;
	private RunJShiftEqustatusFacadeRemote equremote;
	private RunJShiftParmFacadeRemote queryparmremote;
	private BpCMeasureUnitFacadeRemote unitremote;
	private String specialcode;
	private String formdate;
	private String todate;
	private String shiftid;
	private String method;
	private Long runkeyid;
	public RunLogQueryAction(){
		mainremote =(RunJRunlogMainFacadeRemote) factory.getFacadeRemote("RunJRunlogMainFacade");
		timeremote =(RunCShiftTimeFacadeRemote) factory.getFacadeRemote("RunCShiftTimeFacade");
		weatherremote=(RunCLogWeatherFacadeRemote) factory.getFacadeRemote("RunCLogWeatherFacade");
		shiftremote=(RunCShiftFacadeRemote) factory.getFacadeRemote("RunCShiftFacade");
		workerremote=(RunJRunlogWorkerFacadeRemote) factory.getFacadeRemote("RunJRunlogWorkerFacade");
		equremote=(RunJShiftEqustatusFacadeRemote) factory.getFacadeRemote("RunJShiftEqustatusFacade");
		queryparmremote=(RunJShiftParmFacadeRemote) factory.getFacadeRemote("RunJShiftParmFacade");
		unitremote=(BpCMeasureUnitFacadeRemote) factory.getFacadeRemote("BpCMeasureUnitFacade");
	}
	/**
	 * 运行日志基本信息查询
	 * @throws JSONException 
	 */
	public void RunLogMainQuery() throws JSONException{
		int start=0;
		int limit=99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			 start = Integer.parseInt(request.getParameter("start"));
			 limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject obj=mainremote.getRunLogList(specialcode, employee.getEnterpriseCode(), formdate, todate,start,limit);
		String str = "{total:" + obj.getTotalCount() + ",root:"+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
	}
	public void queryWorkerList() throws JSONException{
		
		if(method.equals("all"))
		{
			
			int start=0;
			int limit=99999999;
			Object objstart = request.getParameter("start");
			Object objlimit = request.getParameter("limit");
			if (objstart != null && objlimit != null) {
				 start = Integer.parseInt(request.getParameter("start"));
				 limit = Integer.parseInt(request.getParameter("limit"));
			}
			PageObject obj=workerremote.queryWorkerList(specialcode, employee.getEnterpriseCode(), formdate, todate,start,limit);
			String str = "{total:" + obj.getTotalCount() + ",root:"+ JSONUtil.serialize(obj.getList()) + "}";
			write(str);
		}
		else
		{
			int start=0;
			int limit=99999999;
			Object objstart = request.getParameter("start");
			Object objlimit = request.getParameter("limit");
			if (objstart != null && objlimit != null) {
				start = Integer.parseInt(request.getParameter("start"));
				limit = Integer.parseInt(request.getParameter("limit"));
			}
			PageObject obj=workerremote.queryAbsentWorkerList(specialcode, employee.getEnterpriseCode(), formdate, todate,start,limit);
			String str = "{total:" + obj.getTotalCount() + ",root:"+ JSONUtil.serialize(obj.getList()) + "}";
			write(str);
		}
	}
	public void queryEquStatusList() throws JSONException{
		int start=0;
		int limit=99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject obj=equremote.queryEquStatusList(specialcode, runkeyid, employee.getEnterpriseCode(), formdate, todate,start,limit);
		String str = "{total:" + obj.getTotalCount() + ",root:"+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
	}
	public String findLogTimeName(String logno)
	{
		String str="";
		String date=logno.substring(0, 4)+"年"+logno.substring(4,6)+"月"+logno.substring(6,8)+"日";
		String timeseq=logno.substring(8,9);
		str=str+date;
		List<RunCShiftTime> list=timeremote.findIsShfitTimeListBySpecial(employee.getEnterpriseCode(), specialcode);
		if(list.size() >= Integer.parseInt(timeseq))
		{
			str=str+list.get(Integer.parseInt(timeseq)-1).getShiftTimeName();
		}
		else
		{
			str=str+timeseq;
		}
		return str;
	}
	public void queryRunlogParm(){
		String str="{'columModle':[";
		str=str+"{'header':'专业', 'dataIndex' : 'special','align':'center'},{'header':'参数名称', 'dataIndex' : 'parmname','align':'center'},{'header':'计量单位', 'dataIndex' : 'unitname', 'align':'center'} ";
		List<String> loglist=queryparmremote.getDistinctRunlog(specialcode, employee.getEnterpriseCode(), formdate, todate);
		if(loglist.size() >0)
		{
			for(int i=0;i<loglist.size();i++)
			{	
				String o=loglist.get(i);
				String name=this.findLogTimeName(o);
				str=str+",{'header':'"+(name==null?"":name)+"', 'dataIndex' : 'runlogno"+i+"','align':'center','width':130,'renderer': changeColor}";
				
			}
			str=str+"],";
			str=str+"'data' :[";
			List<Object[]> parmlist=queryparmremote.getDistinctParm(specialcode, employee.getEnterpriseCode(), formdate, todate);
			for(int j=0;j<parmlist.size();j++)
			{
				Object [] o=parmlist.get(j);
				str=str+"{";
				str=str+"'special':'"+(o[2]==null?"":o[2])+"' ,'parmname':'"+(o[1]==null?"":o[1])+"' ,'unitname':'"+(o[3]==null?"":o[3])+"' ";
				for(int m=0;m<loglist.size();m++)
				{
					String logo=loglist.get(m);
					if(o[0] !=null && logo != null)
					{
						RunJShiftParm model=queryparmremote.queryRunlogParm(employee.getEnterpriseCode(), Long.parseLong(o[0].toString()), logo.toString());
						if(model != null)
						{
							str=str+",'runlogno"+m+"':"+(model.getItemNumberValue()==null?"''":model.getItemNumberValue())+"";
						}
					}
				}
				str=str+"},";
			}
			str = str.substring(0, str.length() - 1);
			str=str+"]";
			str=str+",'fieldsNames':[{name:'special'},{name:'parmname'},{name:'unitname'}";
			for(int x=0;x<loglist.size();x++){
				str=str+",{name:'runlogno"+x+"'}";
			}
			str=str+"]}";
			write("{success:true,json:"+str+"}");
		}
	}
	
	public String getSpecialcode() {
		return specialcode;
	}
	public void setSpecialcode(String specialcode) {
		this.specialcode = specialcode;
	}
	public String getFormdate() {
		return formdate;
	}
	public void setFormdate(String formdate) {
		this.formdate = formdate;
	}
	public String getTodate() {
		return todate;
	}
	public void setTodate(String todate) {
		this.todate = todate;
	}
	public String getShiftid() {
		return shiftid;
	}
	public void setShiftid(String shiftid) {
		this.shiftid = shiftid;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Long getRunkeyid() {
		return runkeyid;
	}
	public void setRunkeyid(Long runkeyid) {
		this.runkeyid = runkeyid;
	}
}
