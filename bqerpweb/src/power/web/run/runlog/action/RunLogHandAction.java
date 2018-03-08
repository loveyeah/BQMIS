package power.web.run.runlog.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.runlog.RunCLogWeather;
import power.ejb.run.runlog.RunCLogWeatherFacadeRemote;
import power.ejb.run.runlog.RunJRunLogMainModel;
import power.ejb.run.runlog.RunJRunlogMain;
import power.ejb.run.runlog.RunJRunlogMainFacadeRemote;
import power.ejb.run.runlog.RunJRunlogWorker;
import power.ejb.run.runlog.RunJRunlogWorkerFacadeRemote;
import power.ejb.run.runlog.RunJShiftParm;
import power.ejb.run.runlog.RunJShiftParmFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftTimeFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftWorker;
import power.ejb.run.runlog.shift.RunCShiftWorkerFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftWorkerStation;
import power.ejb.run.runlog.shift.RunCShiftWorkerStationFacadeRemote;
import power.ejb.run.runlog.shift.RunJShiftEqustatus;
import power.ejb.run.runlog.shift.RunJShiftEqustatusFacadeRemote;
import power.ejb.run.runlog.shift.RunJShiftRecord;
import power.ejb.run.runlog.shift.RunJShiftRecordFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.run.timework.action.TimeworkGenerateAction;


public class RunLogHandAction extends AbstractAction {
	RunJRunlogMainFacadeRemote remote;
	private String runlogid;
	private String handWorker;
	private String changeWorker;
	private Long weatherKeyId;
	private String method;
	private RunCShiftTimeFacadeRemote tremote;
	private RunCShiftWorkerFacadeRemote wremote;
	private RunJRunlogWorkerFacadeRemote workremote;
	private RunJShiftEqustatusFacadeRemote equremote;
	private RunJShiftParmFacadeRemote parmremote;
	private RunCShiftWorkerStationFacadeRemote stationremote;
	private RunJShiftRecordFacadeRemote  recordremote;
	RunLogDutyQueryAction dutybll;
	TimeworkGenerateAction timebll;
	private RunCLogWeatherFacadeRemote weatherremote;
	public RunLogHandAction(){
		remote =(RunJRunlogMainFacadeRemote) factory.getFacadeRemote("RunJRunlogMainFacade");
		tremote =(RunCShiftTimeFacadeRemote) factory.getFacadeRemote("RunCShiftTimeFacade");
		wremote =(RunCShiftWorkerFacadeRemote) factory.getFacadeRemote("RunCShiftWorkerFacade");
		workremote=(RunJRunlogWorkerFacadeRemote) factory.getFacadeRemote("RunJRunlogWorkerFacade");
		equremote=(RunJShiftEqustatusFacadeRemote) factory.getFacadeRemote("RunJShiftEqustatusFacade");
		parmremote=(RunJShiftParmFacadeRemote) factory.getFacadeRemote("RunJShiftParmFacade");
		stationremote=(RunCShiftWorkerStationFacadeRemote) factory.getFacadeRemote("RunCShiftWorkerStationFacade");
		weatherremote=(RunCLogWeatherFacadeRemote) factory.getFacadeRemote("RunCLogWeatherFacade");
		recordremote=(RunJShiftRecordFacadeRemote) factory.getFacadeRemote("RunJShiftRecordFacade");
		dutybll=new RunLogDutyQueryAction();
		timebll=new TimeworkGenerateAction();
	}
	/*
	 * 查询最近的运行日志列表
	 */
	public void findLatestRunLogList() throws JSONException{
		List<RunJRunLogMainModel> list=remote.findLatesRunLogList(employee.getEnterpriseCode());
		String str=JSONUtil.serialize(list);
		write(str);
	}
	/*
	 * 判断员工是否是当前运行日志使用者
	 */
	public void findRunLogByWorker() throws JSONException{
		if(method.equals("ZZ")){
			if(!stationremote.isStaionZZ( employee.getWorkerCode(),employee.getEnterpriseCode()))
			{
				write("{failure:true,errorMsg:'您不是值长，不能使用值长日志登记！'}");
			}
			else
			{
				List<Object[]> list=remote.findRunLogByWorker(employee.getEnterpriseCode(), employee.getWorkerCode());
				if(list.size() == 0){
					write("{failure:true,errorMsg:'您不是当值人员，不能使用日志！'}");
				}
	//			else if(list.size() > 1){
	//				write("{failure:true,errorMsg:'您同时在多个专业值班！'}");
	//			}
				else
				{
					Object[] o=list.get(0);
					String str=JSONUtil.serialize(o);
					write("{success:true,data:"+str+"}");
				}
			}
		}
		else
		{
			if(stationremote.isStaionZZ(employee.getWorkerCode(),employee.getEnterpriseCode()))
			{
				write("{failure:true,errorMsg:'您是值长，请使用值长日志登记！'}");
			}
			else
			{
				List<Object[]> list=remote.findRunLogByWorker(employee.getEnterpriseCode(), employee.getWorkerCode());
				if(list.size() == 0){
					write("{failure:true,errorMsg:'您不是当值人员，不能使用日志！'}");
				}
	//			else if(list.size() > 1){
	//				write("{failure:true,errorMsg:'您同时在多个专业值班！'}");
	//			}
				else
				{
					Object[] o=list.get(0);
					String str=JSONUtil.serialize(o);
					write("{success:true,data:"+str+"}");
				}
			}
		}
		
	}
	/*
	 *生成新的运行日志号
	 */
	public String getNewRunLogNo(String oldNo,int shiftTimeAmount){
		String runlogno = "";
        String date = "";
        int shiftTimeNo = 0;
        date = oldNo.substring(0, 8);
        shiftTimeNo = Integer.parseInt(oldNo.substring(8, 9));

        if (shiftTimeNo < shiftTimeAmount)
        {
            shiftTimeNo = shiftTimeNo + 1;
            
        }
        else
        {
            shiftTimeNo = 1;
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(4, 6));
            int day = Integer.parseInt(date.substring(6, 8));
            Calendar displayDate=Calendar.getInstance();  
            SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
            GregorianCalendar  runlogdate = new GregorianCalendar(year, month-1, day); 
            Calendar calendar = Calendar.getInstance();   
            calendar.setTime(runlogdate.getTime());   
            calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+1);
            date=sf.format(calendar.getTime());
            String newyear=date.substring(0,4);
            String newmonth=date.substring(5,7);
            String newday=date.substring(8,10);
            date=newyear+newmonth+newday;
        }
        
        runlogno = date + String.valueOf(shiftTimeNo);
        return runlogno;
	}
	/*
	 * 保存天气信息
	 */
	public void saveWeather(){
		RunJRunlogMain model=remote.findById(Long.parseLong(runlogid));
		model.setWeatherKeyId(weatherKeyId);
		remote.update(model);
		write("{success:true,data:'保存成功！'}");
	}
	/*
	 * 换班
	 */
	public void changeShiftCharger()
	{
		String reason=request.getParameter("reason");
		reason="紧急换班，原因："+reason;
		RunJRunlogMain log=remote.findById(Long.parseLong(runlogid));
		
		boolean pw=wremote.isPrShiftWorker(log.getShiftId(), log.getEnterpriseCode(), changeWorker);
		if(changeWorker.equals(log.getAwayClassLeader())){
			write("{failure:true,errorMsg:'您是当前日志使用者，不能换班！'}");
		}
		else if(!pw){
			write("{failure:true,errorMsg:'您不是该专业的值班人员，不能代班！'}");
		}
		else
		{
			
			RunJRunlogWorker logworkermodel=workremote.findModelByemp(log.getRunLogid(), log.getAwayClassLeader(), log.getEnterpriseCode());
			if(logworkermodel != null)
			{
				logworkermodel.setWoWorktype("LOGABS");
				logworkermodel.setOperateMemo(reason);
				workremote.update(logworkermodel);
			}
			RunJRunlogWorker workermodel=workremote.findModelByemp(log.getRunLogid(), changeWorker, log.getEnterpriseCode());
			if(workermodel == null)
			{
				workermodel=new RunJRunlogWorker();
				workermodel.setBookedEmployee(changeWorker);
				workermodel.setEnterpriseCode(log.getEnterpriseCode());
				workermodel.setIsUse("Y");
				workermodel.setOperateBy(log.getAwayClassLeader());
				workermodel.setOperateTime(new Date());
				workermodel.setRunLogid(log.getRunLogid());
				workermodel.setWoWorktype("LOGSUC");
				workremote.save(workermodel);
			}
			else
			{
				workermodel.setWoWorktype("LOGSUC");
				workremote.update(workermodel);
			}
			log.setAwayClassLeader(changeWorker);
			remote.update(log);
			write("{success:true,data:'操作成功！请重新登陆系统！'}");
	        session.removeAttribute("employee");
		}
	}

	/*
	 * 交接班
	 */
	public void shiftHandOver() throws Exception{
		try
		{
		RunJRunlogMain oldLog=remote.findById(Long.parseLong(runlogid));
		List<RunJRunlogMain> lowList=remote.findLowSpecial(oldLog.getEnterpriseCode(), oldLog.getSpecialityCode(), oldLog.getRunLogno());
		if(lowList.size() != 0)
		{
			write("{failure:true,errorMsg:'下级专业没有交班，您不能交班！'}");
			
		}
		else if(handWorker.equals(oldLog.getAwayClassLeader())){
			write("{failure:true,errorMsg:'不能连班！'}");
		}
		
		else 
		{
			int shiftTimeAmount=tremote.getShiftTimeAmount(oldLog.getShiftTimeId());
			String newLogno=this.getNewRunLogNo(oldLog.getRunLogno(), shiftTimeAmount);
			String nexttime = newLogno.substring(0, 4) + "-" + newLogno.substring(4, 6) + "-" + newLogno.substring(6, 8);
            int shifttimeseq = Integer.parseInt(newLogno.substring(8, 9));
            String ss=oldLog.getSpecialityCode();
            Object[] o=dutybll.getNextShift(nexttime, shifttimeseq, oldLog.getSpecialityCode(),oldLog.getEnterpriseCode());
            String shifttimeid =o[0].toString();
            String shiftid=o[1].toString();
            boolean pw=wremote.isPrShiftWorker(Long.parseLong(shiftid), oldLog.getEnterpriseCode(), handWorker);
            boolean ishand=wremote.isHand(handWorker, oldLog.getEnterpriseCode(), Long.parseLong(shiftid));
            // RunCShiftWorker swmodel=wremote.getShiftWorker(oldLog.getEnterpriseCode(), handWorker, Long.parseLong(shiftid));
//            if(swmodel == null){
//            	write("{failure:true,errorMsg:'您不是下一值班班组的值班人员，不能接班！'}");
//            }
            if(!pw)
            {
            	write("{failure:true,errorMsg:'您不是该专业值班人员，不能接班！'}");
            }
            else if(!ishand)
            {
            	write("{failure:true,errorMsg:'您不具备日志使用权限，不能接班！'}");
            }
//            else if("N".equals(swmodel.getIsHand()))
//            {
//            	write("{failure:true,errorMsg:'您不具备日志使用权限，不能接班！'}");
//            }
            else
            {
            	RunJRunlogMain newLog=remote.shiftHandOperation(Long.parseLong(runlogid), newLogno, Long.parseLong(shiftid), Long.parseLong(shifttimeid), handWorker);
            	//触发定期工作
	            timebll.generateTimework(newLog.getSpecialityCode(),newLog.getShiftTimeId(),newLog.getShiftId(),newLog.getEnterpriseCode(),newLog.getRunLogno());
	            write("{success:true,data:'交接班成功！请重新登陆系统！'}");
	            session.removeAttribute("employee");
            }
            
		}
		}
		catch(Exception e)
		{
			write("{failure:true,errorMsg:'出现错误，请联系管理员！'}");
		}
	}
	public void RunLogMainQuery(){
		
	}
	public String getRunlogid() {
		return runlogid;
	}

	public void setRunlogid(String runlogid) {
		this.runlogid = runlogid;
	}

	public String getHandWorker() {
		return handWorker;
	}

	public void setHandWorker(String handWorker) {
		this.handWorker = handWorker;
	}

	public String getChangeWorker() {
		return changeWorker;
	}

	public void setChangeWorker(String changeWorker) {
		this.changeWorker = changeWorker;
	}

	public Long getWeatherKeyId() {
		return weatherKeyId;
	}

	public void setWeatherKeyId(Long weatherKeyId) {
		this.weatherKeyId = weatherKeyId;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}
