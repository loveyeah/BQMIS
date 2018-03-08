package power.web.run.runlog.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ejb.run.runlog.RunCEqustatus;
import power.ejb.run.runlog.RunCEqustatusFacadeRemote;
import power.web.comm.AbstractAction;

public class RunEqustatusAction extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RunCEqustatusFacadeRemote remote;
	private RunCEqustatus status;
	
	//构造函数
	public RunEqustatusAction()
	{
		remote = (RunCEqustatusFacadeRemote)factory.getFacadeRemote("RunCEqustatusFacade");
	}

	public void addEquStatus()
	{
		status.setEnterpriseCode(employee.getEnterpriseCode());
		try{
		remote.save(status);
		write("{success:true,msg:'增加成功！'}");
		}catch(CodeRepeatException e)
		{
			write("{success:true,msg:'"+e.getMessage()+"'}"); 
		}
	}
	
	public void updateEquStatus() {
		RunCEqustatus model = remote.findById(status.getEqustatusId());
		model.setStatusName(status.getStatusName());
		model.setStatusDesc(status.getStatusDesc());
		model.setColorValue(status.getColorValue());
		model.setIsUse("Y");
		model.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			remote.update(model);
			write("{success:true,msg:'修改成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	public void deleteEquStatus()
	{
		RunCEqustatus model = remote.findById(status.getEqustatusId());
		model.setIsUse("N");
		 try {
				remote.update(model);
			} catch (CodeRepeatException e) {
				write("{success:true,msg:'删除成功！'}");
			}
		
	}
	
	public void findEquStatus() throws JSONException
	{
		List<RunCEqustatus> list = remote.findList(employee.getEnterpriseCode());
		String equStr = JSONUtil.serialize(list);
		write("{equStatusList:"+equStr+"}");
	}
	
	public RunCEqustatus getStatus() {
		return status;
	}

	public void setStatus(RunCEqustatus status) {
		this.status = status;
	}
}
