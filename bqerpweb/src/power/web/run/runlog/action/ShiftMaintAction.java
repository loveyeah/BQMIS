package power.web.run.runlog.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.run.runlog.shift.RunCShift;
import power.ejb.run.runlog.shift.RunCShiftFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftInitial;
import power.ejb.run.runlog.shift.RunCShiftInitialFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftTimeFacadeRemote;
import power.web.comm.AbstractAction;

public class ShiftMaintAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	RunCShiftFacadeRemote remote;
	RunCShiftInitialFacadeRemote iRemote;
	private RunCShift shift;
	private String specialCode;
	private String method;
	private String id;
	/**
	 * 构造函数
	 */
	public ShiftMaintAction(){
		remote =(RunCShiftFacadeRemote) factory.getFacadeRemote("RunCShiftFacade");
		iRemote=(RunCShiftInitialFacadeRemote) factory.getFacadeRemote("RunCShiftInitialFacade");
	}
	/*
	 * 查询班组列表
	 */
	public void findShiftList(){
		//String specialCode=request.getParameter("specialCode");
		List<Object[]> list=remote.findShiftList(employee.getEnterpriseCode(),specialCode);
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(Object[] o : list)
		{
			sb.append("{");
			sb.append("\"shiftId\":"+o[0]+",");
			sb.append("\"initialName\":\""+(o[2]==null?"":o[2])+"\",");
			sb.append("\"shiftName\":\""+(o[3]==null?"":o[3])+"\",");
			sb.append("\"shiftSequence\":"+(o[4]==null?"":o[4])+",");
			sb.append("\"isShift\":\""+(o[5]==null?"":o[5])+"\"");
			sb.append("},");
		}
		if(sb.length()>1)
		{
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		sb.append("]");
		write(sb.toString());
	}
	public void findShiftModel() throws JSONException{
		RunCShift o=remote.findById(Long.parseLong(id));
		write(JSONUtil.serialize(o));
	}
	/*
	 * 班组维护
	 */
	public void shiftMaint(){
		try
		{
			RunCShift model=new RunCShift();
			if(method.equals("add")){
			model.setInitialNo(shift.getInitialNo());
			model.setShiftName(shift.getShiftName());
			model.setIsShift(shift.getIsShift());
			model.setShiftSequence(shift.getShiftSequence());
			model.setIsUse("Y");
			model.setEnterpriseCode(employee.getEnterpriseCode());
			if(remote.isShiftNameExsit(model.getInitialNo(), model.getShiftName(), employee.getEnterpriseCode(),model.getShiftId()))
			{
				write("{failure:true,data:'班组名称已存在，请重新填写！'}");
			}
			else
			{
				remote.save(model);
				RunCShiftInitial imodel=iRemote.findById(model.getInitialNo());
				imodel.setShiftAmount(remote.shiftCount(model.getInitialNo(), model.getEnterpriseCode()));
				iRemote.update(imodel);
				write("{success:true,data:'数据保存成功！'}");
			}
		}
		else{
			model=remote.findById(Long.parseLong(id));
			model.setInitialNo(shift.getInitialNo());
			model.setIsShift(shift.getIsShift());
			model.setShiftName(shift.getShiftName());
			model.setShiftSequence(shift.getShiftSequence());
			if(remote.isShiftNameExsit(model.getInitialNo(), model.getShiftName(), employee.getEnterpriseCode(),model.getShiftId()))
				{
					write("{failure:true,data:'班组名称已存在，请重新填写！'}");
				}
			else
				{
					remote.update(model);
					RunCShiftInitial imodel=iRemote.findById(model.getInitialNo());
					imodel.setShiftAmount(remote.shiftCount(model.getInitialNo(), model.getEnterpriseCode()));
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
	 * 删除班组
	 */
	public void deleteShift(){
		RunCShift model=remote.findById(Long.parseLong(id));
		model.setIsUse("N");
		remote.update(model);
		RunCShiftInitial imodel=iRemote.findById(model.getInitialNo());
		imodel.setShiftAmount(remote.shiftCount(model.getInitialNo(), model.getEnterpriseCode()));
		iRemote.update(imodel);
	}
	public String getSpecialCode() {
		return specialCode;
	}
	public void setSpecialCode(String specialCode) {
		this.specialCode = specialCode;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public RunCShift getShift() {
		return shift;
	}
	public void setShift(RunCShift shift) {
		this.shift = shift;
	}
}
