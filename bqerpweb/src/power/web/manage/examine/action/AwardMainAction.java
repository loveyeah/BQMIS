package power.web.manage.examine.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.components.Else;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.exam.BpJCbmAwardDetail;
import power.ejb.manage.exam.BpJCbmAwardMainFacadeRemote;
import power.ejb.manage.exam.form.CashModel;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class AwardMainAction extends AbstractAction {
	private  String  datetime  ;
	private  BpJCbmAwardMainFacadeRemote    remoteA ; 
	private  String  type ;
	private   Long     AffiliatedID;
	public AwardMainAction() {
		remoteA = (BpJCbmAwardMainFacadeRemote) factory
				.getFacadeRemote("BpJCbmAwardMainFacade");}
	public void getAwardList() throws JSONException {  
		List<CashModel> list = remoteA.getAwardValueList(datetime);
	    if(list != null && list.size()>0)
	    {
		write(JSONUtil.serialize(list));
	    }
	    else
	    {
	    	write("[]");
	    }
		 
	}
	@SuppressWarnings("unchecked")
	public void saveawardMainTable() throws JSONException {
		try {
			String str = request.getParameter("UpdateRecords"); 
			Object obj = JSONUtil.deserialize(str);
			List<Map> list = (List<Map>) obj;
			List<BpJCbmAwardDetail> postlist = new ArrayList<BpJCbmAwardDetail>();
			for (Map data : list) {
	
				BpJCbmAwardDetail model = new BpJCbmAwardDetail();
//				model.setDeclareDetailId(getMaxId());
				if(data.get("declarDetailId") !=null)
					model.setDeclareDetailId(Long.parseLong(data.get("declarDetailId").toString()));
				if(data.get("affiliatedId") !=null)
				model.setAffiliatedId(Long.parseLong(data.get("affiliatedId").toString()));
				if (data.get("cash") != null && !data.get("cash").toString().trim().equals(""))
				    model.setCashBonus(Double.valueOf(data.get("cash").toString()));
				if (data.get("affiliatedId") != null)
				    model.setAffiliatedId(Long.valueOf(data.get("affiliatedId").toString()));
				if (data.get("memo") != null)
				    model.setMemo(data.get("memo").toString());
				
				model.setEnterpriseCode(employee.getEnterpriseCode());
				model.setIsUse("Y");
				model.setYearMonth(datetime);
				postlist.add(model);
			}
			
			remoteA.saveAwardValuelist(postlist, datetime,employee.getEnterpriseCode(),employee.getWorkerCode());
			write("{success:true'}");
		} catch (RuntimeException e) {
			e.printStackTrace();
			write("{success:false'}");
		}
	}
	
	/**
	 * 查找奖金申报审批列表
	 * add by drdu 091130
	 * @throws JSONException
	 */
	public void findAwardMainApproveList() throws JSONException
	{
		String month=request.getParameter("month");
		PageObject list = remoteA.findApproveList(employee.getEnterpriseCode(), month);
		write(JSONUtil.serialize(list));
	}
	
	/**
	 * 奖金申报上报
	 * add by drdu 091130
	 */
	public void awardReport()
	{
		 String actionId=request.getParameter("actionId");
		  String month=request.getParameter("busitNo");
		  String flowCode=request.getParameter("flowCode");
		  String workerCode=request.getParameter("workerCode");
		  remoteA.reportTo(month, flowCode, workerCode,Long.parseLong(actionId));
		  write("{success:true,msg:'&nbsp&nbsp&nbsp上报成功&nbsp&nbsp&nbsp'}");
	}
	
	/**
	 * 奖金申报审批
	 * add by drdu 091130
	 */
	public void awardApproveSign() {
		String declareId = request.getParameter("declareId");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String date = request.getParameter("month");
		String eventIdentify = request.getParameter("eventIdentify");
		String responseDate = request.getParameter("responseDate");
		remoteA.awardApproveSign(declareId,Long.parseLong(entryId), workerCode, Long.parseLong(actionId), approveText, nextRoles,eventIdentify,date,responseDate);			
		write("{success:true,msg:'审批成功！'}");
		
	}

//	public  long getMaxId(){
//		return remoteA.getMaxAwardId();
//	} 
	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getAffiliatedID() {
		return AffiliatedID;
	}
	public void setAffiliatedID(Long affiliatedID) {
		AffiliatedID = affiliatedID;
	}

	
	public void getAdvanceCashRegiter() throws JSONException{
		String month = request.getParameter("month");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = new PageObject();
		if(start != null && limit != null)
			pg = remoteA.getAllCahsResiter(month, 
					Integer.parseInt(start),Integer.parseInt(limit));
		else 
			pg = remoteA.getAllCahsResiter(month);
		write(JSONUtil.serialize(pg));
	}
	
	
	
	
	
	
}