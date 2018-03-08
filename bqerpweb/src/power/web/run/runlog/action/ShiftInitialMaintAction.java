package power.web.run.runlog.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import power.ejb.run.runlog.shift.RunCShiftInitial;
import power.ejb.run.runlog.shift.RunCShiftInitialFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
 

public class ShiftInitialMaintAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	RunCShiftInitialFacadeRemote remote;
	private RunCShiftInitial shiftInitial;
	private String id;
	private String method;
	/**
	 * 构造函数
	 */
	public ShiftInitialMaintAction() {
		remote =(RunCShiftInitialFacadeRemote) factory.getFacadeRemote("RunCShiftInitialFacade");
	}
	/*
	 * 查询各专业值班初始化设置
	 */
	public void findInitialList() throws JSONException{
		List<Object[]> list=remote.findInitialList(employee.getEnterpriseCode());
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(Object[] o : list)
		{
			sb.append("{");
			sb.append("\"initialNo\":"+o[0]+",");
			sb.append("\"initialName\":\""+(o[1]==null?"":o[1])+"\",");
			sb.append("\"specialityCode\":\""+(o[2]==null?"":o[2])+"\",");
			sb.append("\"specialityName\":\""+(o[3]==null?"":o[3])+"\",");
			sb.append("\"shiftAmount\":"+(o[4]==null?"":o[4])+",");
			sb.append("\"timeAmount\":"+(o[5]==null?"":o[5])+",");
			sb.append("\"activeDate\":\""+(o[6]==null?"":o[6])+"\",");
			sb.append("\"disconnectDate\":\""+(o[7]==null?"":o[7])+"\"");
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
	 * 查询一条记录详细信息
	 */
	public void findInitialModel() throws JSONException{
		Object o=remote.findModel(Long.parseLong(id));
		write(JSONUtil.serialize(o));
	}
	/*
	 * 初始化维护
	 */
	public void initialMaint() throws JSONException{
		try
		{
		if(method.equals("add")){
			RunCShiftInitial addmodel=new RunCShiftInitial();
			addmodel.setInitialName(shiftInitial.getInitialName());
			addmodel.setSpecialityCode(shiftInitial.getSpecialityCode());
			addmodel.setShiftAmount(shiftInitial.getShiftAmount());
			addmodel.setTimeAmount(shiftInitial.getTimeAmount());
			addmodel.setActiveDate(shiftInitial.getActiveDate());
			addmodel.setDisconnectDate(shiftInitial.getDisconnectDate());
			addmodel.setIsUse("Y");
			addmodel.setEnterpriseCode(employee.getEnterpriseCode());
			if(remote.isInitialNameExsit(employee.getEnterpriseCode(), addmodel.getInitialName(),addmodel.getInitialNo()))
			{
				write("{failure:true,data:'设置名称已存在，请重新填写！'}");
			}
			else
			{
				Long addId=remote.save(addmodel);
				write("{success:true,data:'数据保存成功！'}");
			}
		}
		else{
			
			RunCShiftInitial model=remote.findById(Long.parseLong(id));
			model.setInitialName(shiftInitial.getInitialName());
			model.setSpecialityCode(shiftInitial.getSpecialityCode());
			model.setActiveDate(shiftInitial.getActiveDate());
			model.setDisconnectDate(shiftInitial.getDisconnectDate());
			if(remote.isInitialNameExsit(employee.getEnterpriseCode(), model.getInitialName(),model.getInitialNo()))
				{
					write("{failure:true,data:'设置名称已存在，请重新填写！'}");
				}
			else
				{
					remote.update(model);
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
	public void deleteInitial(){
		RunCShiftInitial model=remote.findById(Long.parseLong(id));
		model.setIsUse("N");
		remote.update(model);
	}
	/*
	 * 查询专业的所有初始化设置
	 */
	public void findInitialBySpecial() throws JSONException{
		String specialCode=request.getParameter("specialCode");
		List<RunCShiftInitial> list=remote.findInitialBySpecial(specialCode, employee.getEnterpriseCode());
		write (JSONUtil.serialize(list));
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
	public RunCShiftInitial getShiftInitial() {
		return shiftInitial;
	}
	public void setShiftInitial(RunCShiftInitial shiftInitial) {
		this.shiftInitial = shiftInitial;
	}
}
