package power.web.run.runlog.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.run.runlog.RunCRunlogParm;
import power.ejb.run.runlog.RunCRunlogParmFacadeRemote;
import power.ejb.run.runlog.RunCShiftEqu;
import power.ejb.run.runlog.RunCShiftEquFacadeRemote;
import power.ejb.run.runlog.RunJRunlogMain;
import power.ejb.run.runlog.RunJRunlogMainFacadeRemote;
import power.ejb.run.runlog.RunJRunlogWorker;
import power.ejb.run.runlog.RunJRunlogWorkerFacadeRemote;
import power.ejb.run.runlog.RunJShiftParm;
import power.ejb.run.runlog.RunJShiftParmFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftInitialFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftTime;
import power.ejb.run.runlog.shift.RunCShiftTimeFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftWorker;
import power.ejb.run.runlog.shift.RunCShiftWorkerFacadeRemote;
import power.ejb.run.runlog.shift.RunJShiftEqustatus;
import power.ejb.run.runlog.shift.RunJShiftEqustatusFacadeRemote;
import power.web.comm.AbstractAction;

public class RunLogInitialAction extends AbstractAction {
	private RunCShiftInitialFacadeRemote initialRemote;
	private RunCShiftFacadeRemote shiftRemote;
	private RunCShiftTimeFacadeRemote timeRemote;
	private RunCShiftWorkerFacadeRemote workerRemote;
	private RunJRunlogMainFacadeRemote mainremote;
	private RunCShiftEquFacadeRemote equremote;
	private RunJShiftEqustatusFacadeRemote shiftequremote;
	private RunCRunlogParmFacadeRemote parmremote;
	private RunJShiftParmFacadeRemote shiftparmremote;
	private RunCShiftWorkerFacadeRemote wremote;
	private RunJRunlogWorkerFacadeRemote workremote;
	RunLogDutyQueryAction dutybll;
	private String nowdate;
	private String specialcode;
	private String shiftId;
	private String shifttimeId;
	private String weatherid;
	private String workercode;
	
	/**
	 * 构造函数
	 */
	public RunLogInitialAction()
	{
		initialRemote=(RunCShiftInitialFacadeRemote) factory.getFacadeRemote("RunCShiftInitialFacade");
		shiftRemote=(RunCShiftFacadeRemote) factory.getFacadeRemote("RunCShiftFacade");
		timeRemote=(RunCShiftTimeFacadeRemote) factory.getFacadeRemote("RunCShiftTimeFacade");
		workerRemote=(RunCShiftWorkerFacadeRemote) factory.getFacadeRemote("RunCShiftWorkerFacade");
		dutybll=new RunLogDutyQueryAction();
		mainremote =(RunJRunlogMainFacadeRemote) factory.getFacadeRemote("RunJRunlogMainFacade");
		equremote =(RunCShiftEquFacadeRemote) factory.getFacadeRemote("RunCShiftEquFacade");
		shiftequremote =(RunJShiftEqustatusFacadeRemote) factory.getFacadeRemote("RunJShiftEqustatusFacade");
		parmremote=(RunCRunlogParmFacadeRemote) factory.getFacadeRemote("RunCRunlogParmFacade");
		shiftparmremote=(RunJShiftParmFacadeRemote) factory.getFacadeRemote("RunJShiftParmFacade");
		wremote =(RunCShiftWorkerFacadeRemote) factory.getFacadeRemote("RunCShiftWorkerFacade");
		workremote=(RunJRunlogWorkerFacadeRemote) factory.getFacadeRemote("RunJRunlogWorkerFacade");
	} 
	/*
	 * 查询初始化专业列表
	 */
	public void getInitialSpecial(){
		Date date=new Date();
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		String cudate=sf.format(date);
		List<Object[]> list=initialRemote.findActiveSpecialList(employee.getEnterpriseCode(),cudate);
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(Object[] o : list)
		{
			sb.append("{");
			sb.append("\"speciality_code\":\""+(o[0]==null?"":o[0])+"\",");
			sb.append("\"speciality_name\":\""+(o[1]==null?"":o[1])+"\"");
			sb.append("},");
		}
		if(sb.length()>1)
		{
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		sb.append("]");
		write(sb.toString());
	}
	/*
	 * 查询初始化班组列表
	 */
	public void getInitialShift() throws Exception{
		
		int shifttimeseq=0;
		//Long timeid=Long.parseLong(shifttimeId);
		List<RunCShiftTime> list=timeRemote.findIsShfitTimeListBySpecial(employee.getEnterpriseCode(), specialcode);
		for(int i=0;i<list.size();i++)
		{
			if(shifttimeId.equals(list.get(i).getShiftTimeId().toString()))
			{
				shifttimeseq=i+1;
			}
		}
		Object[] o=dutybll.getNextShift(nowdate, shifttimeseq, specialcode,employee.getEnterpriseCode());
		String shifttimeid =o[0].toString();
        String shiftid=o[1].toString();
        String shiftname=shiftRemote.findById(Long.parseLong(shiftid)).getShiftName();
        String [] x=new String[2];
        x[0]=shiftid;
        x[1]=shiftname;
        write(JSONUtil.serialize(x));
	}
	
	/*
	 * 查询初始化人员列表
	 */
	public void getInitialWorker() throws JSONException{
		List<Object[]> list=workerRemote.findWorkerList(Long.parseLong(shiftId), employee.getEnterpriseCode());
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(Object[] o : list)
		{
			sb.append("{");
			sb.append("\"emp_code\":\""+(o[0]==null?"":o[0])+"\",");
			sb.append("\"emp_name\":\""+(o[1]==null?"":o[1])+"\"");
			sb.append("},");
		}
		if(sb.length()>1)
		{
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		sb.append("]");
		write(sb.toString());
	}
	/**
	 * 运行日志初始化
	 */
	public void runlogInitial() throws Exception{
		int shifttimeseq=0;
		List<RunCShiftTime> list=timeRemote.findIsShfitTimeListBySpecial(employee.getEnterpriseCode(), specialcode);
		for(int i=0;i<list.size();i++)
		{
			if(shifttimeId.equals(list.get(i).getShiftTimeId().toString()))
			{
				shifttimeseq=i+1;
			}
		}
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		String runlogno=nowdate.substring(0, 4)+nowdate.substring(5, 7)+nowdate.substring(8,10)+ String.valueOf(shifttimeseq);
		if(mainremote.isRunlogExsit(runlogno, specialcode, employee.getEnterpriseCode()))
		{
			write("{failure:true,data:'运行日志已存在，不可进行初始化！'}");
		}
		else
		{
			RunJRunlogMain mainmodel=new RunJRunlogMain();
			mainmodel.setRunLogno(runlogno);
			mainmodel.setShiftId(Long.parseLong(shiftId));
			mainmodel.setAwayClassLeader(workercode);
			mainmodel.setEnterpriseCode(employee.getEnterpriseCode());
			mainmodel.setIsUse("Y");
			mainmodel.setShiftTimeId(Long.parseLong(shifttimeId));
			mainmodel.setSpecialityCode(specialcode);
			mainmodel.setTakeClassTime(new Date());
			mainmodel.setWeatherKeyId(Long.parseLong(weatherid));
			//增加新的运行日志记录
			Long runlogid=mainremote.save(mainmodel);
			//增加运行方式
			List<RunCShiftEqu> equlist=equremote.getListBySpecial(specialcode, employee.getEnterpriseCode());
			RunJShiftEqustatus shiftequmodel=new RunJShiftEqustatus();
			for(int i=0;i<equlist.size();i++)
			{
				RunCShiftEqu model=equlist.get(i);
				shiftequmodel.setAttributeCode(model.getAttributeCode());
				shiftequmodel.setEnterpriseCode(employee.getEnterpriseCode());
				shiftequmodel.setEquName(model.getEquName());
				shiftequmodel.setEquStatusId(null);
				shiftequmodel.setEquStatusName(null);
				shiftequmodel.setIsUse("Y");
				shiftequmodel.setRunLogid(runlogid);
				shiftequmodel.setRunLogno(mainmodel.getRunLogno());
				shiftequmodel.setSpecialityCode(specialcode);
				shiftequremote.save(shiftequmodel);
			}
			//增加运行参数
			List<RunCRunlogParm> parmlist=parmremote.findListBySpecial(specialcode, employee.getEnterpriseCode());
			RunJShiftParm newparmmodel=new RunJShiftParm();
			for(int j=0;j<parmlist.size();j++)
			{
				RunCRunlogParm model=parmlist.get(j);
				newparmmodel.setEnterpriseCode(employee.getEnterpriseCode());
				newparmmodel.setIsUse("Y");
				newparmmodel.setRunLogid(runlogid);
				newparmmodel.setRunlogParmId(model.getRunlogParmId());
				shiftparmremote.save(newparmmodel);
			}
			//增加值班人员
			List<RunCShiftWorker> wlist=wremote.findListByShift(mainmodel.getShiftId(), employee.getEnterpriseCode());
	        RunJRunlogWorker workermodel=new RunJRunlogWorker();
	        for(int i=0;i<wlist.size();i++)
	        {
	        	workermodel.setRunLogid(runlogid);
	        	workermodel.setBookedEmployee(wlist.get(i).getEmpCode());
	        	workermodel.setEnterpriseCode(employee.getEnterpriseCode());
	        	workermodel.setIsUse("Y");
	        	workermodel.setOperateBy(employee.getWorkerCode());
	        	workermodel.setOperateTime(new Date());
	        	if(wlist.get(i).getEmpCode().equals(workercode))
	        	{
	        		workermodel.setWoWorktype("LOGSUC");
	        	}
	        	else
	        	{
	        		workermodel.setWoWorktype("LOGONS");
	        	}
	        	workremote.save(workermodel);
	        }
	        write("{success:true,data:'初始化成功！'}");
		}
	}
	public String getNowdate() {
		return nowdate;
	}
	public void setNowdate(String nowdate) {
		this.nowdate = nowdate;
	}
	public String getSpecialcode() {
		return specialcode;
	}
	public void setSpecialcode(String specialcode) {
		this.specialcode = specialcode;
	}
	public String getShiftId() {
		return shiftId;
	}
	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}
	public String getShifttimeId() {
		return shifttimeId;
	}
	public void setShifttimeId(String shifttimeId) {
		this.shifttimeId = shifttimeId;
	}
	public String getWeatherid() {
		return weatherid;
	}
	public void setWeatherid(String weatherid) {
		this.weatherid = weatherid;
	}
	public String getWorkercode() {
		return workercode;
	}
	public void setWorkercode(String workercode) {
		this.workercode = workercode;
	}
	
}
