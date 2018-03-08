package power.web.run.runlog.action;


import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.run.runlog.shift.RunCShift;
import power.ejb.run.runlog.shift.RunCShiftInitial;
import power.ejb.run.runlog.shift.RunCShiftInitialFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftTime;
import power.ejb.run.runlog.shift.RunCShiftTimeFacadeRemote;
import power.web.comm.AbstractAction;

public class ShiftTimeMaintAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	RunCShiftTimeFacadeRemote remote;
	RunCShiftInitialFacadeRemote iRemote;
	private RunCShiftTime shiftTime;
	private String specialCode;
	private String id;
	private String method;
	private String onDutyTime;
	private String offDutyTime;
	
	
	/**
	 * 构造函数
	 */
	public ShiftTimeMaintAction(){
		remote =(RunCShiftTimeFacadeRemote) factory.getFacadeRemote("RunCShiftTimeFacade");
		iRemote=(RunCShiftInitialFacadeRemote) factory.getFacadeRemote("RunCShiftInitialFacade");
	}
	/*
	 * 查询班次设置列表
	 */
	public void findShiftTimeList(){
		List<Object[]> list=remote.findShfitTimeListBySpecial(specialCode, employee.getEnterpriseCode());
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(Object[] o : list)
		{
			sb.append("{");
			sb.append("\"shiftTimeId\":"+o[0]+",");
			sb.append("\"initialName\":\""+(o[2]==null?"":o[2])+"\",");
			sb.append("\"shiftTimeName\":\""+(o[3]==null?"":o[3])+"\",");
			sb.append("\"onDutyTime\":\""+(o[4]==null?"":o[4])+"\",");
			sb.append("\"offDutyTime\":\""+(o[5]==null?"":o[5])+"\",");
			sb.append("\"shiftSerial\":\""+(o[6]==null?"":o[6])+"\",");
			sb.append("\"shiftTimeDesc\":\""+(o[7]==null?"":o[7])+"\",");
			sb.append("\"isRest\":\""+(o[9]==null?"":o[9])+"\"");
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
	 * 获取班次
	 */
	public void findShiftTimeModel() throws JSONException{
		Object o=remote.findTimeModel(Long.parseLong(id));
		write(JSONUtil.serialize(o));
	}
	/*
	 * 班次维护
	 */
	public void shiftTimeMaint(){
		try
		{
			RunCShiftTime model=new RunCShiftTime();
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			ParsePosition pos = new ParsePosition(0);
			onDutyTime=request.getParameter("onDutyTime");
			offDutyTime=request.getParameter("offDutyTime");
			
			if(method.equals("add")){
			model.setInitialNo(shiftTime.getInitialNo());
			model.setShiftTimeName(shiftTime.getShiftTimeName());
			model.setShiftSerial(shiftTime.getShiftSerial());
			model.setOnDutyTime(formatter.parse(onDutyTime));
			model.setOffDutyTime(formatter.parse(offDutyTime));
			model.setShiftTimeDesc(shiftTime.getShiftTimeDesc());
			model.setIsRest(shiftTime.getIsRest());
			model.setIsUse("Y");
			model.setEnterpriseCode(employee.getEnterpriseCode());
			if(remote.isTimeNameExsit(model.getShiftTimeName(), model.getInitialNo(), model.getShiftTimeId(), model.getEnterpriseCode()))
			{
				write("{failure:true,data:'班次名称已存在，请重新填写！'}");
			}
			else
			{
				remote.save(model);
				RunCShiftInitial imodel=iRemote.findById(model.getInitialNo());
				imodel.setTimeAmount(remote.tiemCount(model.getInitialNo(), model.getEnterpriseCode()));
				iRemote.update(imodel);
				write("{success:true,data:'数据保存成功！'}");
			}
		}
		else{
			model=remote.findById(Long.parseLong(id));
			model.setInitialNo(shiftTime.getInitialNo());
			model.setShiftTimeName(shiftTime.getShiftTimeName());
			model.setShiftSerial(shiftTime.getShiftSerial());
			model.setOnDutyTime(formatter.parse(onDutyTime));
			model.setOffDutyTime(formatter.parse(offDutyTime));
			model.setShiftTimeDesc(shiftTime.getShiftTimeDesc());
			model.setIsRest(shiftTime.getIsRest());
			if(remote.isTimeNameExsit(model.getShiftTimeName(), model.getInitialNo(), model.getShiftTimeId(), model.getEnterpriseCode()))
			{
				write("{failure:true,data:'班次名称已存在，请重新填写！'}");
			}
			else
				{
					remote.update(model);
					RunCShiftInitial imodel=iRemote.findById(model.getInitialNo());
					imodel.setTimeAmount(remote.tiemCount(model.getInitialNo(), model.getEnterpriseCode()));
					iRemote.update(imodel);
					write("{success:true,data:'数据保存成功！'}");
				}
			}
		}
		catch(Exception exc)
		{
			write("{failure:true,data:'"+exc.getMessage()+"'}");
		}
	}
	/*
	 * 删除
	 */
	public void deleteShiftTime(){
	
		RunCShiftTime model=remote.findById(Long.parseLong(id));
		model.setIsUse("N");
		remote.update(model);
		RunCShiftInitial imodel=iRemote.findById(model.getInitialNo());
		imodel.setTimeAmount(remote.tiemCount(model.getInitialNo(), model.getEnterpriseCode()));
		iRemote.update(imodel);
	}
	public String getSpecialCode() {
		return specialCode;
	}
	public void setSpecialCode(String specialCode) {
		this.specialCode = specialCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public RunCShiftTime getShiftTime() {
		return shiftTime;
	}
	public void setShiftTime(RunCShiftTime shiftTime) {
		this.shiftTime = shiftTime;
	}
//	public String getOnDutyTime() {
//		return onDutyTime;
//	}
//	public void setOnDutyTime(String onDutyTime) {
//		this.onDutyTime = onDutyTime;
//	}
//	public String getOffDutyTime() {
//		return offDutyTime;
//	}
//	public void setOffDutyTime(String offDutyTime) {
//		this.offDutyTime = offDutyTime;
//	}
}
