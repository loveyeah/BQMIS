package power.web.operateticket.maint.action;

import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.opticket.RunCOpCheckstatue;
import power.ejb.opticket.RunCOpCheckstatueFacadeRemote;
import power.web.comm.AbstractAction;

public class CheckStatusAction extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RunCOpCheckstatueFacadeRemote remote;
	private RunCOpCheckstatue cs;
	public CheckStatusAction(){
		remote=(RunCOpCheckstatueFacadeRemote)factory.getFacadeRemote("RunCOpCheckstatueFacade");
	}
	public void addCheckStatus(){
		cs.setModifyBy(employee.getEnterpriseCode());
		cs.setModifyDate(new Date());
		cs.setEnterpriseCode(employee.getEnterpriseCode());
		cs.setIsUse("Y");
		remote.save(cs);
	}
	public void updateCheckStatus(){
		RunCOpCheckstatue model=remote.findById(cs.getCheckStatueId());
		model.setCheckBefFlag(cs.getCheckBefFlag());
		model.setCheckStatue(cs.getCheckStatue());
		model.setDisplayNo(cs.getDisplayNo());
		model.setIsRunFlag(cs.getIsRunFlag());
		remote.update(model);
	}
	public void findById() throws JSONException{
		String id=request.getParameter("id");
		RunCOpCheckstatue model=remote.findById(Long.parseLong(id));
		String str = JSONUtil.serialize(model);
		write("{success:true,data:" + str + "}");
	}
	public void deleteCheckStatus(){
		String ids=request.getParameter("ids");
		String[] checkIds=ids.split(",");
		for(int i=0;i<checkIds.length;i++){
			RunCOpCheckstatue model=new RunCOpCheckstatue();
			model.setCheckStatueId(Long.parseLong(checkIds[i]));
			remote.delete(model);
		}
	}
	public void findCheckStatus()  throws JSONException{
		String checkBefFlag=request.getParameter("checkBefFlag");
		String isRunning = request.getParameter("isRunning");
//		if(checkBefFlag.equals("") && checkBefFlag.length()<1){
//			checkBefFlag=null;
//		}
		List<RunCOpCheckstatue> list=remote.findPublic(employee.getEnterpriseCode(), checkBefFlag,isRunning);
		String str="";
		if(list!=null && list.size()>0){
			str+="{total:"+list.size()+",list:"+JSONUtil.serialize(list)+"}";
		}else{
			str+="{total:0,list:[]}";
		}
		write(str);	
	}
	public RunCOpCheckstatue getCs() {
		return cs;
	}
	public void setCs(RunCOpCheckstatue cs) {
		this.cs = cs;
	}
}
