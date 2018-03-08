package power.web.run.securityproduction.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.struts2.components.Else;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.SpJAntiAccidentAmend;
import power.ejb.run.securityproduction.SpJAntiAccidentAmendFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SafeAmendAction extends AbstractAction
{
	private SpJAntiAccidentAmendFacadeRemote remote;
	private SpJAntiAccidentAmend amend;
	
	public SpJAntiAccidentAmend getAmend() {
		return amend;
	}

	public void setAmend(SpJAntiAccidentAmend amend) {
		this.amend = amend;
	}

	public SafeAmendAction()
	{
		remote = (SpJAntiAccidentAmendFacadeRemote)factory.getFacadeRemote("SpJAntiAccidentAmendFacade");
	}
	
	/**
	 * 获得25项反措整改计划中符合条件的数据
	 * @throws JSONException 
	 */
	public void getSafeAmendList() throws JSONException
	{
		String checkupIdStr = request.getParameter("checkupId");
		Long checkupId = null;
		if(checkupIdStr != null)
			checkupId = Long.parseLong(checkupIdStr);
		PageObject pg = new PageObject();
		pg = remote.findAllCheckupPlan(checkupId, employee.getEnterpriseCode());
		String str = JSONUtil.serialize(pg);
		write(str);
	}
	
	/** 保存25项反措整改计划数据
	 * 
	 */
	public void saveAmend() {
		String planFinishDate = request.getParameter("planFinishDate");
		String amendFinishDate = request.getParameter("amendFinishDate");
		SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
		if (planFinishDate != null && !planFinishDate.equals(""))
			try {
				amend.setPlanFinishDate(sbf.parse(planFinishDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			else 
				amend.setPlanFinishDate(null);
		if (amendFinishDate != null && !amendFinishDate.equals(""))
			try {
				amend.setAmendFinishDate(sbf.parse(amendFinishDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			else
				amend.setAmendFinishDate(null);
		amend.setIsUse("Y");
		amend.setEnterpriseCode(employee.getEnterpriseCode());
		amend.setModifyBy(employee.getWorkerCode());
		amend.setModifyDate(new Date());

		remote.save(amend);
	}
	
	/**
	 * 删除25项反措整改计划数据
	 */
	public void deleteAmend()
	{
		String ids = request.getParameter("ids");
		remote.delete(ids);
	}
	
	/**
	 * 修改25项反措整改计划数据
	 */
	public void updateAmend() {
		String planFinishDate = request.getParameter("planFinishDate");
		String amendFinishDate = request.getParameter("amendFinishDate");
		SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
		SpJAntiAccidentAmend temp = remote.findById(amend.getAmendId());
		if (planFinishDate != null && !planFinishDate.equals(""))
			try {
				temp.setPlanFinishDate(sbf.parse(planFinishDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			else 
				temp.setPlanFinishDate(null);
		if (amendFinishDate != null && !amendFinishDate.equals(""))
			try {
				temp.setAmendFinishDate(sbf.parse(amendFinishDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			else
				temp.setAmendFinishDate(null);
		temp.setIsUse("Y");
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		temp.setModifyBy(employee.getWorkerCode());
		temp.setModifyDate(new Date());

		temp.setExistProblem(amend.getExistProblem());
		temp.setAmendMeasure(amend.getAmendMeasure());
		temp.setBeforeAmendMeasure(amend.getBeforeAmendMeasure());
		temp.setChargeBy(amend.getChargeBy());
		temp.setChargeDept(amend.getChargeDept());
		temp.setSuperviseBy(amend.getSuperviseBy());
		temp.setSuperviseDept(amend.getSuperviseDept());
		temp.setNoAmendReason(amend.getNoAmendReason());
		temp.setProblemKind(amend.getProblemKind());
		remote.update(temp);
	}
}