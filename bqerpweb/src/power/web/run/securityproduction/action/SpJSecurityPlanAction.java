package power.web.run.securityproduction.action;

import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.SpJSecurityPlan;
import power.ejb.run.securityproduction.SpJSecurityPlanFacadeRemote;
import power.ejb.run.securityproduction.SpJSpecialoperators;
import power.ejb.run.securityproduction.SpJSpecialoperatorsFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * 安措计划项目
 * @author liuyi 090603
 *
 */
public class SpJSecurityPlanAction extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 146546525664L;
	private SpJSecurityPlan spjs;
	private SpJSecurityPlanFacadeRemote remote; 
	
	public SpJSecurityPlanAction()
	{
		remote = (SpJSecurityPlanFacadeRemote)factory.getFacadeRemote("SpJSecurityPlanFacade");
	}
	
	/**
	 * 增加一条安措计划项目信息
	 */
	public void addSpJSecurityPlanInfo()
	{
		
		spjs.setEnterpriseCode(employee.getEnterpriseCode());
		spjs = remote.save(spjs);
		if(spjs == null)
		{
			write("{failure:true,msg:'项目名称不能重复！'}");
		}
		else
		write("{success:true,id:'"+spjs.getSecurityPlanId()+"',msg:'数据增加成功！'}");
	}
	
	/**
	 * 修改一条安措计划项目信息
	 */
	public void updateSpJSecurityPlanInfo()
	{
		SpJSecurityPlan temp = remote.findById(spjs.getSecurityPlanId());
		if(spjs.getPlanName() != null && !(spjs.getPlanName().equals("")))
		{
			temp.setPlanName(spjs.getPlanName());
		}
		if(spjs.getPlanBasis() != null && !(spjs.getPlanBasis().equals("")))
		{
			temp.setPlanBasis(spjs.getPlanBasis());
		}
		if(spjs.getFee() != null && !(spjs.getFee().equals("")))
		{
			temp.setFee(spjs.getFee());
		}
		if(spjs.getYear() != null && !(spjs.getYear().equals("")))
		{
			temp.setYear(spjs.getYear());
		}
		if(spjs.getFinishDate() != null && !(spjs.getFinishDate().equals("")))
		{
			temp.setFinishDate(spjs.getFinishDate());
		}
		if(spjs.getChargeBy() != null && !(spjs.getChargeBy().equals("")))
		{
			temp.setChargeBy(spjs.getChargeBy());
		}
		if(spjs.getChargeDep() != null && !(spjs.getChargeDep().equals("")))
		{
			temp.setChargeDep(spjs.getChargeDep());
		}
		if(spjs.getMemo() != null && !(spjs.getMemo().equals("")))
		{
			temp.setMemo(spjs.getMemo());
		}
		if(spjs.getFillBy() != null && !(spjs.getFillBy().equals("")))
		{
			temp.setFillBy(spjs.getFillBy());
		}
		if(spjs.getFillDep() != null && !(spjs.getFillDep().equals("")))
		{
			temp.setFillDep(spjs.getFillDep());
		}
		if(spjs.getFillDate() != null && !(spjs.getFillDate().equals("")))
		{
			temp.setFillDate(spjs.getFillDate());
		}
		if(spjs.getFinishState() != null && !(spjs.getFinishState().equals("")))
		{
			temp.setFinishState(spjs.getFinishState());
		}
		if(spjs.getFinishAppraisal() != null && !(spjs.getFinishAppraisal().equals("")))
		{
			temp.setFinishAppraisal(spjs.getFinishAppraisal());
		}
		if(spjs.getAppraisalBy() != null && !(spjs.getAppraisalBy().equals("")))
		{
			temp.setAppraisalBy(spjs.getAppraisalBy());
		}
		if(spjs.getAppraisalDate() != null && !(spjs.getAppraisalDate().equals("")))
		{
			temp.setAppraisalDate(spjs.getAppraisalDate());
		}
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		
		spjs = remote.update(temp);
		if(spjs == null)
		{
			write("{failure:true,msg:'项目名称不能重复！'}");
		}
		else
		write("{success:true,msg:'数据修改成功！'}");
	}
	
	/**
	 * 删除一条或多条安措计划项目信息
	 */
	public void deleteSpJSecurityPlanInfo()
	{
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'数据删除成功！'}");
	}
	
	/**
	 * 根据查询条件和企业编码查询安措计划项目列表
	 * @throws JSONException 
	 */
	public void findSpJSecurityPlanList() throws JSONException
	{
        PageObject obj=new  PageObject();
		
		String workName = request.getParameter("queryString");
		
		Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.findAll(workName, employee.getEnterpriseCode(), start,limit);
	    }
	    else
	    {
	    	obj=remote.findAll(workName, employee.getEnterpriseCode());
	    }
	    String str=JSONUtil.serialize(obj);
		write(str);
	}

	public SpJSecurityPlan getSpjs() {
		return spjs;
	}

	public void setSpjs(SpJSecurityPlan spjs) {
		this.spjs = spjs;
	}


}
