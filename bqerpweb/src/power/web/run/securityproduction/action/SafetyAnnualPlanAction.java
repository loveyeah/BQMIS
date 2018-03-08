package power.web.run.securityproduction.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.run.securityproduction.SpJSafetyAnnualplan;
import power.ejb.run.securityproduction.SpJSafetyAnnualplanFacadeRemote;
import power.ejb.run.securityproduction.form.SafetyAnnualPlanForm;
import power.web.comm.AbstractAction;

public class SafetyAnnualPlanAction extends AbstractAction{
	private SpJSafetyAnnualplanFacadeRemote remote;
	
	private SpJSafetyAnnualplan safetyAnnualPlan;
	/**
	 * 
	 *  构造方法
	 */
	public SafetyAnnualPlanAction(){
		remote = (SpJSafetyAnnualplanFacadeRemote) factory
		      .getFacadeRemote("SpJSafetyAnnualplanFacade");
	}
	/**
	 *增加一条记录 
	 * 
	 */
	public void addSafetyAnnualPlan(){
		String year = request.getParameter("year");
		safetyAnnualPlan.setPlanYear(year);
		safetyAnnualPlan.setEnterpriseCode(employee.getEnterpriseCode());
		Long annualplanId = remote.save(safetyAnnualPlan).getAnnualplanId();
		write("{success : true,msg : '增加成功!',annualplanId :"+annualplanId+"}");
	}
	/**
	 * 
	 * 删除一条记录
	 */
	public void deleteSafeAnnualPlan(){
		String annualplanId = request.getParameter("annualplanId");
		remote.delete(remote.findById(Long.parseLong(annualplanId)));
		write("{success : true,msg : '删除成功!'}");
	}
	/**
	 * 
	 * 修改一条记录并更新
	 */
	public void updateSafetyAnnualPlan(){
		String  annualplanId =request.getParameter("annualplanId"); 
		String year = request.getParameter("year");
		safetyAnnualPlan.setPlanYear(year);
		SpJSafetyAnnualplan entity = remote.findById(Long.parseLong(annualplanId));
		entity.setDepCode(safetyAnnualPlan.getDepCode());
		entity.setFillBy(safetyAnnualPlan.getFillBy());
		entity.setFillTime(safetyAnnualPlan.getFillTime());
		entity.setMemo(safetyAnnualPlan.getMemo());
		entity.setPlanContent(safetyAnnualPlan.getPlanContent());
		entity.setPlanYear(safetyAnnualPlan.getPlanYear());
		remote.update(entity);
		write("{success : true,msg : '修改成功！'}");
	}
	/**
	 * 获得记录列表
	 */
	public void getsafetyAnnualPlan() throws JSONException{
		String year = request.getParameter("fuzzy");
		List<SafetyAnnualPlanForm> str = remote.findAll(year);
		write(JSONUtil.serialize(str));
		System.out.println(JSONUtil.serialize(str));
	}
	public SpJSafetyAnnualplan getSafetyAnnualPlan() {
		return safetyAnnualPlan;
	}
	public void setSafetyAnnualPlan(SpJSafetyAnnualplan safetyAnnualPlan) {
		this.safetyAnnualPlan = safetyAnnualPlan;
	}

}
