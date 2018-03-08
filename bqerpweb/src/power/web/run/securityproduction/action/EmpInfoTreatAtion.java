package power.web.run.securityproduction.action;

import java.util.Date;

import javax.ejb.Remote;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.SpJCorpEmpinfo;
import power.ejb.run.securityproduction.SpJCorpEmpinfoFacadeRemote;
import power.web.comm.AbstractAction;

public class EmpInfoTreatAtion extends AbstractAction
{
	private SpJCorpEmpinfoFacadeRemote remote;
	public EmpInfoTreatAtion()
	{
		remote = (SpJCorpEmpinfoFacadeRemote)factory.getFacadeRemote("SpJCorpEmpinfoFacade");
	}
	
	public void findEmpInfoList() throws JSONException
	{
		PageObject pg = new PageObject();
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String name = request.getParameter("name");
		
		if(start == null || limit == null)
		{
			pg = remote.findEmpInfoList(name,employee.getEnterpriseCode());
		}
		else 
			pg = remote.findEmpInfoList(name,employee.getEnterpriseCode(),
					Integer.parseInt(start),Integer.parseInt(limit));
		String str = JSONUtil.serialize(pg);
		write(str);
	}
	
	public void addEmpInfo()
	{
		SpJCorpEmpinfo temp = new SpJCorpEmpinfo();
		String empName = request.getParameter("empName");
		String empDuty = request.getParameter("empDuty");
		temp.setEmpName(empName);
		temp.setEmpDuty(empDuty);
		temp.setModifyby(employee.getWorkerCode());
		temp.setModifyDate(new Date());
		temp.setIsUse("Y");
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		remote.save(temp);
		write("{success : true, msg : '新增人员信息成功！'}");
	}
	
	public void updateEmpInfo()
	{
		String empId = request.getParameter("empId");
		SpJCorpEmpinfo temp = remote.findById(Long.parseLong(empId));
		String empName = request.getParameter("empName");
		String empDuty = request.getParameter("empDuty");
		temp.setEmpName(empName);
		temp.setEmpDuty(empDuty);
		temp.setModifyby(employee.getWorkerCode());
		temp.setModifyDate(new Date());
		temp.setIsUse("Y");
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		
		remote.update(temp);
		write("{success : true, msg : '修改人员信息成功！'}");
	}
	
	public void deleteEmpInfo()
	{
		String ids = request.getParameter("ids");
		remote.delete(ids);
		write("{success : true, msg : '删除人员信息成功！'}");
	}
}