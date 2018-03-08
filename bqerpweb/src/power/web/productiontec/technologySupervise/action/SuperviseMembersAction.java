package power.web.productiontec.technologySupervise.action;

import java.text.ParseException;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.technologySupervise.PtJJdwcy;
import power.ejb.productiontec.technologySupervise.PtJJdwcyFacadeRemote;
import power.ejb.system.SysJUr;
import power.ejb.system.SysJUrFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SuperviseMembersAction extends AbstractAction{
	private PtJJdwcy model;
	private PtJJdwcyFacadeRemote remote;
	public SuperviseMembersAction(){
		remote = (PtJJdwcyFacadeRemote)factory.getFacadeRemote("PtJJdwcyFacade");
	}
	public PtJJdwcy getModel() {
		return model;
	}
	public void setModel(PtJJdwcy model) {
		this.model = model;
	}
	public void findPtJJdwcyList() throws  JSONException{
		String jdzyId = request.getParameter("jdzyId");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null){
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findAll(jdzyId,employee.getEnterpriseCode(),start,limit);
		}else{
			object = remote.findAll(jdzyId, employee.getEnterpriseCode());
		}
		String strOutput = "";
		if (object == null || object.getList() == null){
			strOutput = "{\"list\":[],\"totalCount\":0}";
		}else{
			strOutput = JSONUtil.serialize(object);
		}
		System.out.println(strOutput);
		write(strOutput);
	}
	public void addPtJJdwcyInfo() throws ParseException{
		String workId=request.getParameter("workId");
		String jdzyId = request.getParameter("jdzyId");
		model.setJdzyId(Long.parseLong(jdzyId));
		model.setEnterpriseCode(employee.getEnterpriseCode());
		remote.save(model);
		//-----------add by fyyang 091030------------------------------------
		SysJUrFacadeRemote urRemote=(SysJUrFacadeRemote) factory.getFacadeRemote("SysJUrFacade");
		SysJUr urModel=new SysJUr();
		urModel.setModifyBy(employee.getWorkerCode());
		urModel.setWorkerId(Long.parseLong(workId));
		if(jdzyId.equals("1")) urModel.setRoleId(106l); //绝缘监督
		else if(jdzyId.equals("2")) urModel.setRoleId(112l); //节能监督
		else if(jdzyId.equals("3")) urModel.setRoleId(107l); //金属监督
		else if(jdzyId.equals("4")) urModel.setRoleId(111l); //环保监督
		else if(jdzyId.equals("5")) urModel.setRoleId(113l); //电能质量监督
		else if(jdzyId.equals("6")) urModel.setRoleId(109l); //热控监督
		else if(jdzyId.equals("7")) urModel.setRoleId(105l); //化学监督
		else if(jdzyId.equals("8")) urModel.setRoleId(108l); //电测监督
		else if(jdzyId.equals("9")) urModel.setRoleId(110l); //继电保护监督
		else if(jdzyId.equals("10")) urModel.setRoleId(118l); //振动监督
		else if(jdzyId.equals("11")) urModel.setRoleId(115l); //励磁监督
		urRemote.save(urModel);
		//---------------------------------------------------------------------
		write("{success : true,msg :'增加成功！'}");
	}
	public void updatePtJJdwcyInfo() throws ParseException{
		PtJJdwcy entity=remote.findById(model.getJdwcyId());
		if (entity != null) {
			entity.setWorkerCode(model.getWorkerCode());
			entity.setNetDuty(model.getNetDuty());
			remote.update(entity);
			write("{success : true,msg :'修改成功！'}");
		}
	}
	public void deletePtJJdwcyInfo(){
		//modify by fyyang 091102
		String ids=request.getParameter("ids");
		String [] memberId=ids.split(",");
		SysJUrFacadeRemote urRemote=(SysJUrFacadeRemote) factory.getFacadeRemote("SysJUrFacade");
		for(int i=0;i<memberId.length;i++)
		{
			PtJJdwcy entity=remote.findById(Long.parseLong(memberId[i]));
			String jdzyId=entity.getJdzyId().toString();
			Long roleId=0l;
			if(jdzyId.equals("1")) roleId=106l; //绝缘监督
			else if(jdzyId.equals("2")) roleId=112l; //节能监督
			else if(jdzyId.equals("3")) roleId=107l; //金属监督
			else if(jdzyId.equals("4")) roleId=111l; //环保监督
			else if(jdzyId.equals("5")) roleId=113l; //电能质量监督
			else if(jdzyId.equals("6")) roleId=109l; //热控监督
			else if(jdzyId.equals("7")) roleId=105l; //化学监督
			else if(jdzyId.equals("8")) roleId=108l; //电测监督
			else if(jdzyId.equals("9")) roleId=110l; //继电保护监督
			else if(jdzyId.equals("10")) roleId=118l; //振动监督
			else if(jdzyId.equals("11")) roleId=115l; //励磁监督
			urRemote.deleteByWorkCodeAndRoleId(entity.getWorkerCode(),roleId);
		}
		remote.deleteMulti(ids);
		write("{success : true,msg :'删除成功！'}");
	}
}
