package power.web.manage.contract.action;

import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.business.ConJContractInfoFacadeRemote;
import power.ejb.manage.contract.business.ConJPaymentPlan;
import power.ejb.manage.contract.business.ConJPaymentPlanFacadeRemote;
import power.ejb.manage.contract.form.PaymentPlanForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class ConPayPlanModifyAction extends AbstractAction{
private ConJPaymentPlanFacadeRemote remote;
private ConJContractInfoFacadeRemote conRemote;
private ConJPaymentPlan pay;
private String startdate;
private String enddate;
private Long conId;
//private int start;
//private int limit;
	public ConPayPlanModifyAction()
	{
		remote=(ConJPaymentPlanFacadeRemote)factory.getFacadeRemote("ConJPaymentPlanFacade");
		conRemote = (ConJContractInfoFacadeRemote)factory.getFacadeRemote("ConJContractInfoFacade");
	}
	//查询付款计划列表
	public void findPayPlanList() throws JSONException
	{
		
		List<PaymentPlanForm> list=remote.findByConId(conId);
		write(JSONUtil.serialize(list));
//		System.out.print(JSONUtil.serialize(list));
	}
	//增加一条付款计划记录
	public void addPayPlan(){
		pay.setEnterpriseCode(employee.getEnterpriseCode());
		pay.setIsUse("Y");
		pay.setLastModifiedBy(employee.getWorkerCode());
		pay.setLastModifiedDate(new Date());
		pay.setConId(conId);
		remote.save(pay);
		write("{success:true,data:'保存成功!'}");
	}
	//修改一条付款计划记录
	public void updatePayPlan(){
		ConJPaymentPlan model=remote.findById(pay.getPaymentId());
		model.setLastModifiedBy(employee.getWorkerCode());
		model.setLastModifiedDate(new Date());
		model.setMemo(pay.getMemo());
		model.setPayDate(pay.getPayDate());
		model.setPaymentMoment(pay.getPaymentMoment());
		model.setPayPrice(pay.getPayPrice());
		remote.update(model);
		write("{success:true,data:'数据保存成功!'}");
	}
	//删除付款计划记录
	public void deletePayPlan(){
		String ids= request.getParameter("ids");
		System.out.println(ids);
		String [] pays= ids.split(",");
		for(int i=0;i<pays.length;i++)
		{
			if(!pays[i].equals(""))
			{
				ConJPaymentPlan model=remote.findById(Long.parseLong(pays[i]));
				remote.delete(model);
			}
		}
		write("{success:true,data:'删除成功！'}");
	}
	
	//合同付款计划查询
	public void findConPayPlayList() throws JSONException
	{   Long conTypeId = null;
		//add 合同类别
		if (request.getParameter("conTypeId") != null){
		    conTypeId = Long.parseLong(request.getParameter("conTypeId"));
		}
		String conNo= request.getParameter("conNo");
		String conName= request.getParameter("conName");
		String clientName= request.getParameter("clientName");
		String operaterBy= request.getParameter("operaterBy");
		int start=0;
		int limit=99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			 start = Integer.parseInt(request.getParameter("start"));
			 limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject obj=conRemote.findContractPayPlayList(conTypeId,employee.getEnterpriseCode(), startdate, enddate, conNo, conName, clientName, operaterBy,start,limit);
		String str = "{total:" + obj.getTotalCount() + ",root:"+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
	}
	
	public ConJPaymentPlan getPay() {
		return pay;
	}
	public void setPay(ConJPaymentPlan pay) {
		this.pay = pay;
	}
	
	public Long getConId() {
		return conId;
	}
	public void setConId(Long conId) {
		this.conId = conId;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

}
