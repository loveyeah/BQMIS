package power.web.manage.client.action;

import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.client.ConJAppraise;
import power.ejb.manage.client.ConJAppraiseFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class ClientAppraiseAction extends AbstractAction{

	private ConJAppraise appraise;
	private ConJAppraiseFacadeRemote remote;
	public ClientAppraiseAction()
	{
		remote = (ConJAppraiseFacadeRemote)factory.getFacadeRemote("ConJAppraiseFacade");
	}
	
	/**
	 * 合作伙伴评价汇总列表
	 * @throws JSONException
	 */
	public void findAppraiseList() throws JSONException
	{
		String clientId=request.getParameter("clientId");
		String intervalId=request.getParameter("intervalId");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findAll(clientId, intervalId, employee.getEnterpriseCode(), start,limit);
		} else {
			obj = remote.findAll(clientId, intervalId, employee.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}
	
	/**
	 * 合作伙伴评价已经汇总信息
	 * @throws JSONException
	 */
	public void findAppraiseGatherList() throws JSONException
	{
		String clientId=request.getParameter("clientId");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findGatherAll(clientId, employee.getEnterpriseCode(), start,limit);
		} else {
			obj = remote.findGatherAll(clientId, employee.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}
	
	/**
	 * 评价汇总
	 */
	public void updateAppraiseSummary()
	{
		ConJAppraise model = remote.findById(appraise.getAppraisalId());
		model.setAppraisalResult(appraise.getAppraisalResult());
		model.setGatherBy(employee.getWorkerCode());
		model.setGatherDate(new Date());
		model.setGatherFlag("Y");
		remote.update(model);
		write("{success:true,id:'"+appraise.getAppraisalId()+"',msg:'汇总成功！'}");		
	}

	public ConJAppraise getAppraise() {
		return appraise;
	}

	public void setAppraise(ConJAppraise appraise) {
		this.appraise = appraise;
	}
}
