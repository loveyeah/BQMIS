package power.web.run.runlog.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ejb.run.runlog.RunCEqustatus;
import power.ejb.run.runlog.RunCRunWay;
import power.ejb.run.runlog.RunCRunWayFacadeRemote;
import power.web.comm.AbstractAction;

public class RunWayAction extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RunCRunWayFacadeRemote remote;
	private RunCRunWay runway;
	//构造方法
	public RunWayAction()
	{
		remote = (RunCRunWayFacadeRemote)factory.getFacadeRemote("RunCRunWayFacade");
	}
	
	//增加运行方式
	public void addRunWay()
	{
		runway.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			remote.save(runway);
			write("{success:true,msg:'增加成功！'}");
		} catch (CodeRepeatException e) {
			 write("{success:true,msg:'"+e.getMessage()+"'}"); 
		}
	}
	
	//修改运行方式
	public void updateRunWay()
	{
		RunCRunWay model = remote.findById(runway.getRunKeyId());
		
		model.setRunWayCode(runway.getRunWayCode());
		model.setRunWayName(runway.getRunWayName());
		model.setDiaplayNo(runway.getDiaplayNo());
		model.setIsUse("Y");
		try {
			remote.update(model);
			write("{success:true,msg:'修改成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'"+e.getMessage()+"'}"); 
		}
	}
	
   //删除运行方式
	public void deleteRunWay()
	{
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		 write("{success:true,msg:'删除成功！'}");
	}
	
	//列表显示	
	public void findRunWay() throws JSONException{
		List<RunCRunWay> list = remote.findAllList(employee.getEnterpriseCode());
		 write(JSONUtil.serialize(list));
	}

	public RunCRunWay getRunway() {
		return runway;
	}
	public void setRunway(RunCRunWay runway) {
		this.runway = runway;
	}
	
}
