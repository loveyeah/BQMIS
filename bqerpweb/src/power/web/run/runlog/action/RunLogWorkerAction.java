package power.web.run.runlog.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.run.runlog.RunCLogWeatherFacadeRemote;
import power.ejb.run.runlog.RunJRunlogMain;
import power.ejb.run.runlog.RunJRunlogMainFacadeRemote;
import power.ejb.run.runlog.RunJRunlogWorker;
import power.ejb.run.runlog.RunJRunlogWorkerFacadeRemote;
import power.ejb.run.runlog.RunJRunlogWorkerModel;
import power.ejb.run.runlog.RunJShiftParm;
import power.web.comm.AbstractAction;

public class RunLogWorkerAction extends AbstractAction{
	RunJRunlogWorkerFacadeRemote remote;
	RunJRunlogMainFacadeRemote mremote;
	private String method;
	private Long runlogId;
	private Long runlogWorkerId;
	private String agentWorker;
	private String memo;
	/*
	 * 构造函数
	 */
	public RunLogWorkerAction(){
		remote = (RunJRunlogWorkerFacadeRemote)factory.getFacadeRemote("RunJRunlogWorkerFacade");
		mremote = (RunJRunlogMainFacadeRemote)factory.getFacadeRemote("RunJRunlogMainFacade");
	}
	/*
	 *根据运行日志id查询该专业及其下级专业的值班人员列表和缺勤人员列表
	 */
	public void findRunLogWorkerList() throws JSONException{
		RunJRunlogMain model=mremote.findById(runlogId);
		List<RunJRunlogWorkerModel> list=remote.findRunLogWorkerAll(employee.getEnterpriseCode(), model.getRunLogno(), model.getSpecialityCode());
		if(method.equals("all"))
		{
			write(JSONUtil.serialize(list));
		}
		else
		{
			List<RunJRunlogWorkerModel> absentlist=new ArrayList();
			if(list.size() > 0)
			{
				for(int i=0;i<list.size();i++)
				{
					RunJRunlogWorkerModel wmodel=list.get(i);
					if("LOGABS".equals(wmodel.getWoWorktype()) || "LOGABG".equals(wmodel.getWoWorktype()))
					{
						absentlist.add(wmodel);
					}
				}
			}
			write(JSONUtil.serialize(absentlist));
		}
	}
	public void addAbsentReason() throws JSONException{
		String str=request.getParameter("data");
		   Object obj = JSONUtil.deserialize(str);
		   List<Map> list = (List<Map>)obj;
		   for(Map data : list)
		   {
		       String id = ((Map) ((Map) data)).get("runlogWorkerId").toString();
		       String value = ((Map) ((Map)data)).get("operateMemo").toString(); 
		       RunJRunlogWorker model=remote.findById(Long.parseLong(id));
		       model.setOperateMemo(value);
		       remote.update(model);
		   } 
		  write("{success:true,id:'-1',msg:'保存成功！'}");
	}
	/*
	 * 增加缺勤人员
	 */
	public void addAbsent(){
		String ids= request.getParameter("ids");
		String [] empids= ids.split(",");
		for(int i=0;i<empids.length;i++)
		{
			RunJRunlogWorker model=remote.findById(Long.parseLong(empids[i]));
			if("LOGAGN".equals(model.getWoWorktype()))
			{
				model.setWoWorktype("LOGABG");
			}
			else
			{
				model.setWoWorktype("LOGABS");
			}
			remote.update(model);
		}
	}
	/*
	 * 修改缺勤原因
	 */
	public void updateAbsentMemo(){
		RunJRunlogWorker model=remote.findById(runlogWorkerId);
		model.setOperateMemo(memo);
		remote.update(model);
	}
	/*
	 * 删除缺勤人员
	 */
	public void deleteAbsent(){
		RunJRunlogWorker model=remote.findById(runlogWorkerId);
		if("LOGABG".equals(model.getWoWorktype()))
		{
			model.setWoWorktype("LOGAGN");
			model.setOperateMemo("");
		}
		else
		{
			model.setWoWorktype("LOGONS");
			model.setOperateMemo("");
		}
		remote.update(model);
	}
	/*
	 * 增加代班人员
	 */
	public void addAgent(){
		String ids= request.getParameter("ids");
		String [] empcodes= ids.split(",");
		for(int i=0;i<empcodes.length;i++)
		{
			if(!remote.isExsit(runlogId, empcodes[i], employee.getEnterpriseCode()))
			{
				RunJRunlogWorker model=new RunJRunlogWorker();
				model.setRunLogid(runlogId);
				model.setWoWorktype("LOGAGN");
				model.setIsUse("Y");
				model.setEnterpriseCode(employee.getEnterpriseCode());
				model.setBookedEmployee(empcodes[i]);
				model.setOperateBy(employee.getWorkerCode());
				model.setOperateTime(new Date());
				remote.save(model);
			}
			else
			{
				
			}
		}
	}
	/*
	 * 删除人员
	 */
	public void deleteAgent(){
		RunJRunlogWorker model=remote.findById(runlogWorkerId);
		model.setIsUse("N");
		remote.update(model);
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Long getRunlogId() {
		return runlogId;
	}
	public void setRunlogId(Long runlogId) {
		this.runlogId = runlogId;
	}
	public Long getRunlogWorkerId() {
		return runlogWorkerId;
	}
	public void setRunlogWorkerId(Long runlogWorkerId) {
		this.runlogWorkerId = runlogWorkerId;
	}
	public String getAgentWorker() {
		return agentWorker;
	}
	public void setAgentWorker(String agentWorker) {
		this.agentWorker = agentWorker;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
