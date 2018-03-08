package power.web.run.securityproduction.action.safesuperviseaction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.safesupervise.SpJViolationRule;
import power.ejb.run.securityproduction.safesupervise.SpJViolationRuleFacadeRemote;
import power.web.comm.AbstractAction;

public class ViolationRuleAction extends AbstractAction{

	private SpJViolationRuleFacadeRemote remote;
	private SpJViolationRule rule;
	
	public ViolationRuleAction()
	{
		remote=(SpJViolationRuleFacadeRemote)factory.getFacadeRemote("SpJViolationRuleFacade");
	}

	public SpJViolationRule getRule() {
		return rule;
	}

	public void setRule(SpJViolationRule rule) {
		this.rule = rule;
	}
	
	public void findRuleList() throws JSONException
	{
		PageObject obj = new PageObject();
		String strDate=request.getParameter("strDate");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.findViolationRuleList(strDate, employee.getEnterpriseCode(), start,limit);
		}else
		{
			obj=remote.findViolationRuleList(strDate, employee.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
		
	}
	
	public void saveRuleInfo()
	{
		rule.setEnterpriseCode(employee.getEnterpriseCode());
		rule.setLastModifiedBy(employee.getWorkerCode());
		remote.save(rule);
		write("{success:true,msg:'增加成功！'}");
	}
	
	public void updateRuleInfo()
	{
		SpJViolationRule model=remote.findById(rule.getRuleId());
		model.setCheckBy(rule.getCheckBy());
		model.setDeptCode(rule.getDeptCode());
		model.setEmpCode(rule.getEmpCode());
		model.setEntryDate(rule.getEntryDate());
		model.setExamineDate(rule.getExamineDate());
		model.setExamineMoney(rule.getExamineMoney());
		model.setLastModifiedBy(employee.getWorkerCode());
		model.setPhenomenon(rule.getPhenomenon());
		remote.update(model);
		write("{success:true,msg:'修改成功！'}");
		
	}
	
	public void deleteRuleInfo()
	{
	  String ids=request.getParameter("ids");
	  remote.delete(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	
	public void queryRuleList() throws JSONException
	{
		PageObject obj = new PageObject();
		String strMonth=request.getParameter("strMonth");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.queryViolationRuleList(strMonth, employee.getEnterpriseCode(), start,limit);
		}else
		{
			obj=remote.queryViolationRuleList(strMonth, employee.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
		
	}
	
	
}
