package power.web.run.runlog.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.hr.HrCDept;
import power.ejb.hr.HrCDeptFacadeRemote;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.run.runlog.RunJRunlogMainFacadeRemote;
import power.ejb.run.runlog.RunJRunlogWorker;
import power.ejb.run.runlog.RunJRunlogWorkerFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftWorker;
import power.ejb.run.runlog.shift.RunCShiftWorkerFacadeRemote;
import power.web.comm.AbstractAction;

public class ShiftWorkerAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	RunCShiftWorkerFacadeRemote remote;
	HrCDeptFacadeRemote  hremote;
	HrJEmpInfoFacadeRemote eremote;
	RunJRunlogMainFacadeRemote mremote;
	RunJRunlogWorkerFacadeRemote logworkerremote;
	private String shiftId;
	private String shiftworkerid;
	private String pid;
	private String deptId;
	
	/**
	 * 构造函数
	 */
	public ShiftWorkerAction(){
		remote =(RunCShiftWorkerFacadeRemote) factory.getFacadeRemote("RunCShiftWorkerFacade");
		hremote =(HrCDeptFacadeRemote) factory.getFacadeRemote("HrCDeptFacade");
		eremote =(HrJEmpInfoFacadeRemote) factory.getFacadeRemote("HrJEmpInfoFacade");
		mremote =(RunJRunlogMainFacadeRemote) factory.getFacadeRemote("RunJRunlogMainFacade");
		logworkerremote =(RunJRunlogWorkerFacadeRemote) factory.getFacadeRemote("RunJRunlogWorkerFacade");
	}
	/*
	 * 查询班组值班人员设置列表
	 */
	public void findShiftWorkerList() throws JSONException
	{
		List<Object[]> list=remote.findWorkerList(Long.parseLong(shiftId), employee.getEnterpriseCode());
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(Object[] o : list)
		{
			sb.append("{");
			sb.append("\"emp_code\":\""+(o[0]==null?"":o[0])+"\",");
			sb.append("\"emp_name\":\""+(o[1]==null?"":o[1])+"\",");
			sb.append("\"shift_worker_id\":"+(o[2]==null?"":o[2])+",");
			sb.append("\"isHand\":\""+(o[3]==null?"":o[3])+"\"");
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
	 * 修改交接班权限
	 */
	public void updateShiftWorker() throws JSONException{
		String str=request.getParameter("data");
		   Object obj = JSONUtil.deserialize(str);
		   List<Map> list = (List<Map>)obj;
		   for(Map data : list)
		   {
		       String id = ((Map) ((Map) data)).get("shiftworkerid").toString();
		       String value = ((Map) ((Map)data)).get("ishand").toString(); 
		       RunCShiftWorker model=remote.findById(Long.parseLong(id));
		       model.setIsHand(value);
		       remote.update(model);
		   } 
		  write("{success:true,id:'-1',msg:'保存成功！'}");
	}
	/*
	 * 删除值班人员
	 */
	public void deleteShiftWorker(){
		RunCShiftWorker model=remote.findById(Long.parseLong(shiftworkerid));
//		model.setIsUse("N");
//		remote.update(model);
		remote.delete(model);
		if(mremote.findLogidByShift(employee.getEnterpriseCode(), model.getShiftId()) != null){
			Long runlogid=mremote.findLogidByShift(employee.getEnterpriseCode(), model.getShiftId());
			RunJRunlogWorker lmodel=logworkerremote.findModelByemp(runlogid, model.getEmpCode(), model.getEnterpriseCode());
			if(lmodel != null)
			{
				lmodel.setIsUse("N");
				logworkerremote.update(lmodel);
			}
		}
	}
	private boolean isLeafdept(Long pid) throws NamingException {
		List<HrCDept> ld = hremote.findByPdeptId(pid);
		if (ld != null && ld.size() > 0)
		{
		 return false;
		}
		else
		{
		return true;
		}
	}
	/*
	 * 部门树
	 */
	
	private String toDeptTreeJsonStr(List<HrCDept> list) throws Exception{
		StringBuffer JSONStr = new StringBuffer(); 
		JSONStr.append("[");
		String icon="";
		for(int i=0;i<list.size();i++){
			HrCDept dept=list.get(i);
			if(isLeafdept(dept.getDeptId())){
				icon="file";
			}else{
				icon="folder";
			}
			JSONStr.append("{\"text\":\"" + dept.getDeptName()+ 
					"\",\"id\":\"" + dept.getDeptId() + 
					"\",\"leaf\":" + isLeafdept(dept.getDeptId())+
					",\"cls\":\"" + icon+ "\"},");	
		}
		if (JSONStr.length() > 1) {
			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
		}
		JSONStr.append("]"); 
		return JSONStr.toString();
	} 
	/*
	 * 查询所有部门
	 */
	public void getDeptTree() throws Exception{
		List<HrCDept> list=hremote.findByPdeptId(Long.parseLong(pid));
		String str=toDeptTreeJsonStr(list);
		write(str);
	}
	/*
	 * 查询部门下的员工列表
	 */
	public void getEmpbyList() throws JSONException{
		String fuzzy=request.getParameter("fuzzy");
		String method=request.getParameter("method");
		List<HrJEmpInfo> list;
		if(method.equals("dept"))
		{
			 list=eremote.findByDeptId(Long.parseLong(deptId));
		}
		else
		{
			list=eremote.findEmpListByDept(fuzzy);
		}
		
		write(JSONUtil.serialize(list));
	}
	/*
	 * 增加班组值班人员
	 */
	public void addShiftWorker(){
		RunCShiftWorker model=new RunCShiftWorker();
		String ids= request.getParameter("ids");
		String deps=request.getParameter("deps");
	    String [] empcodes= ids.split(",");
	    String [] deptids=deps.split(",");
		for(int i=0;i<empcodes.length;i++)
		{
			if(!remote.isWorkerExsit(Long.parseLong(shiftId), empcodes[i], employee.getEnterpriseCode())){
				model.setEmpCode(empcodes[i]);
				model.setShiftId(Long.parseLong(shiftId));
				model.setEnterpriseCode(employee.getEnterpriseCode());
				model.setIsUse("Y");
				if(!deptids[i].equals(""))
				{
					model.setDeptId(Long.parseLong(deptids[i]));
				}
				model.setIsHand("N");
				remote.save(model);
				if(mremote.findLogidByShift(employee.getEnterpriseCode(), Long.parseLong(shiftId)) != null){
					Long runlogid=mremote.findLogidByShift(employee.getEnterpriseCode(), Long.parseLong(shiftId));
					RunJRunlogWorker lmodel=new RunJRunlogWorker();
					lmodel.setBookedEmployee(model.getEmpCode());
					lmodel.setEnterpriseCode(employee.getEnterpriseCode());
					lmodel.setIsUse("Y");
					lmodel.setOperateTime(new Date());
					lmodel.setRunLogid(runlogid);
					lmodel.setWoWorktype("LOGONS");
					lmodel.setOperateBy(mremote.findById(runlogid).getAwayClassLeader());
					logworkerremote.save(lmodel);
				}
			}
			else
			{
				
			}
		}
	}
	public String getShiftId() {
		return shiftId;
	}
	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}
	public String getShiftworkerid() {
		return shiftworkerid;
	}
	public void setShiftworkerid(String shiftworkerid) {
		this.shiftworkerid = shiftworkerid;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
}
