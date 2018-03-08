package power.web.hr.reward;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.reward.HrJMonthReward;
import power.ejb.hr.reward.HrJMonthRewardDetail;
import power.ejb.hr.reward.HrJMonthRewardDetailFacadeRemote;
import power.ejb.hr.reward.HrJMonthRewardFacadeRemote;
import power.ejb.hr.reward.HrJRewardApprove;
import power.ejb.hr.reward.HrJRewardApproveFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.PostMessage;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class HrMonthRewardAction extends AbstractAction {
	
	private HrJMonthRewardFacadeRemote monthRewardFacade;
	
	private HrJMonthRewardDetailFacadeRemote monthRewardDetailFacade;
	
	private Boolean detailIsNull;
	
	private HrJMonthReward reward;
	
	private HrJMonthRewardDetail rewardDetail;
	
	private HrJRewardApproveFacadeRemote approveFacade;
	
	public HrMonthRewardAction() {
		monthRewardFacade = (HrJMonthRewardFacadeRemote) factory.getFacadeRemote("HrJMonthRewardFacade");
		monthRewardDetailFacade = (HrJMonthRewardDetailFacadeRemote) factory.getFacadeRemote("HrJMonthRewardDetailFacade");
		approveFacade = (HrJRewardApproveFacadeRemote) factory.getFacadeRemote("HrJRewardApproveFacade");
	}
	//add by wpzhu 20100724-------
	public void findDetailMonthRewardByDept() throws JSONException
	{
		PageObject result=new PageObject();
		String deptId = request.getParameter("deptId");
		String rewardId = request.getParameter("rewardId");
		String month="";//add by wpzhu
		HrJMonthReward  entity=    monthRewardFacade.findById((rewardId==null?0l:Long.parseLong(rewardId)));
		if(entity!=null)
		{
         month=entity.getRewardMonth();
		}
     result=monthRewardDetailFacade.findDetailMonthRewardByDept((rewardId==null?0l:Long.parseLong(rewardId)),
					(deptId==null?0l:Long.parseLong(deptId)),month);
     if (result != null) {
			write(JSONUtil.serialize(result));;
//			System.out.println("the result"+JSONUtil.serialize(result));
		} else {
			write("{totalCount : 0,list :[]}");
		}
		
	
		
	}
	public void getRewardDetailDeptList() throws JSONException
	{
		String rewardId = request.getParameter("rewardId");
		 
		List  list=monthRewardDetailFacade.getRewardDetailDeptList((rewardId==null?0l:Long.parseLong(rewardId)));
		if (list != null) {
		  write("{list :"+JSONUtil.serialize(list)+"}");
		
		} else {
			write("{list :[]}");
		}
	}
	//-------------------------end ------------
	
	public void saveOrUpdateMonthReward() throws JSONException{
		String rewardId = request.getParameter("rewardId");
		if(rewardId != null && !"".equals(rewardId)) {
			//modify by fyyang 20100722
			reward.setRewardId(Long.parseLong(rewardId));
			reward.setEnterpriseCode(employee.getEnterpriseCode());
			reward.setIsUse("Y");
			reward.setFillDate(new java.util.Date());
			reward.setWorkFlowState("0");
			monthRewardFacade.update(reward);
			write("{success :true, msg:'保存成功',rewardId:" +reward.getRewardId()+ "}");
		} else {
			reward.setEnterpriseCode(employee.getEnterpriseCode());
			reward.setFillBy(employee.getWorkerCode());
			try {
				HrJMonthReward returnModel =  monthRewardFacade.save(reward, detailIsNull);
				write("{success :true, msg:'保存成功',rewardId:" + returnModel.getRewardId()+ "}");
			} catch (CodeRepeatException e) {
				write("{success:true,msg:'增加失败:当月已经存在,不能重复填写！'}");
			}
		}
	}
	
	public void saveOrUpdateMonthRewardDetail() throws JSONException{
		String rewardId = request.getParameter("rewardId");
		String flag = request.getParameter("flag");
		String isAdd = request.getParameter("isAdd");
		List<Map> addList = (List<Map>) JSONUtil.deserialize(isAdd);
		String isUpdate = request.getParameter("isUpdate");
		List<Map> updateList = (List<Map>) JSONUtil.deserialize(isUpdate);
		List<HrJMonthRewardDetail> addDetailList = null;
		List<HrJMonthRewardDetail> updateDetailList = null;
		if(addList != null && addList.size() > 0) {
			addDetailList = new ArrayList<HrJMonthRewardDetail>();
			for (Map data : addList) {
				HrJMonthRewardDetail detailModel = new HrJMonthRewardDetail();
				detailModel.setRewardId(Long.parseLong(rewardId));
				if (data.get("deptId") != null && !"".equals(data.get("deptId"))) {
					detailModel.setDeptId(Long.parseLong(data.get("deptId").toString()));
				}
				if (data.get("empCount") != null && !"".equals(data.get("empCount"))) {
					detailModel.setEmpCount(Long.parseLong(data.get("empCount").toString()));
				}
				if (data.get("lastMonthNum") != null && !"".equals(data.get("lastMonthNum"))) {
					detailModel.setLastMonthNum(Double.parseDouble(data.get("lastMonthNum").toString()));
				}
				if (data.get("monthRewardNum") != null && !"".equals(data.get("monthRewardNum"))) {
					detailModel.setMonthRewardNum(Double.parseDouble(data.get("monthRewardNum").toString()));
				}
				if (data.get("quantifyCash") != null && !"".equals(data.get("quantifyCash"))) {
					detailModel.setQuantifyCash(Double.parseDouble(data.get("quantifyCash").toString()));
				}
				if (data.get("extraAddNum") != null && !"".equals(data.get("extraAddNum"))) {
					detailModel.setExtraAddNum(Double.parseDouble(data.get("extraAddNum").toString()));
				}
				if (data.get("monthAssessNum") != null && !"".equals(data.get("monthAssessNum"))) {
					detailModel.setMonthAssessNum(Double.parseDouble(data.get("monthAssessNum").toString()));
				}
				if (data.get("otherNum") != null && !"".equals(data.get("otherNum"))) {
					detailModel.setOtherNum(Double.parseDouble(data.get("otherNum").toString()));
				}
				if("report".equals(flag))
				{
					if (data.get("totalNum") != null && !"".equals(data.get("totalNum"))) {
						detailModel.setTotalNum(Double.parseDouble(data.get("totalNum").toString()));
					
				}
				}else  if("approve".equals(flag))
				{
				if (data.get("sumTotalnum") != null && !"".equals(data.get("sumTotalnum"))) {
					detailModel.setTotalNum(Double.parseDouble(data.get("sumTotalnum").toString()));
					
				}
				}
				if (data.get("memo") != null && !"".equals(data.get("memo"))) {
					detailModel.setMemo(data.get("memo").toString());
				}
				detailModel.setWorkFlowState("0");
				detailModel.setIsUse("Y");
				detailModel.setEnterpriseCode(employee.getEnterpriseCode());
				addDetailList.add(detailModel);
			}
		}
		
		if(updateList != null && updateList.size() > 0) {
			updateDetailList = new ArrayList<HrJMonthRewardDetail>();
			for (Map data : updateList) {
				HrJMonthRewardDetail detailModel = new HrJMonthRewardDetail();
				detailModel.setRewardId(Long.parseLong(rewardId));
				if (data.get("detailId") != null && !"".equals(data.get("detailId"))) {
					detailModel.setDetailId(Long.parseLong(data.get("detailId").toString()));
				}
				if (data.get("deptId") != null && !"".equals(data.get("deptId"))) {
					detailModel.setDeptId(Long.parseLong(data.get("deptId").toString()));
				}
				if (data.get("empCount") != null && !"".equals(data.get("empCount"))) {
					detailModel.setEmpCount(Long.parseLong(data.get("empCount").toString()));
				}
				if (data.get("lastMonthNum") != null && !"".equals(data.get("lastMonthNum"))) {
					detailModel.setLastMonthNum(Double.parseDouble(data.get("lastMonthNum").toString()));
				}
				if (data.get("monthRewardNum") != null && !"".equals(data.get("monthRewardNum"))) {
					detailModel.setMonthRewardNum(Double.parseDouble(data.get("monthRewardNum").toString()));
				}
				if (data.get("quantifyCash") != null && !"".equals(data.get("quantifyCash"))) {
					detailModel.setQuantifyCash(Double.parseDouble(data.get("quantifyCash").toString()));
				}
				if (data.get("extraAddNum") != null && !"".equals(data.get("extraAddNum"))) {
					detailModel.setExtraAddNum(Double.parseDouble(data.get("extraAddNum").toString()));
				}
				if (data.get("monthAssessNum") != null && !"".equals(data.get("monthAssessNum"))) {
					detailModel.setMonthAssessNum(Double.parseDouble(data.get("monthAssessNum").toString()));
				}
				if (data.get("otherNum") != null && !"".equals(data.get("otherNum"))) {
					detailModel.setOtherNum(Double.parseDouble(data.get("otherNum").toString()));
				}
				if("report".equals(flag))
				{
					if (data.get("totalNum") != null && !"".equals(data.get("totalNum"))) {
						detailModel.setTotalNum(Double.parseDouble(data.get("totalNum").toString()));
					
				}
				}else  if("approve".equals(flag))
				{
				if (data.get("sumTotalnum") != null && !"".equals(data.get("sumTotalnum"))) {
					detailModel.setTotalNum(Double.parseDouble(data.get("sumTotalnum").toString()));
					
				}
				}
				if (data.get("memo") != null && !"".equals(data.get("memo"))) {
					detailModel.setMemo(data.get("memo").toString());
				}
				if (data.get("isUse") != null && !"".equals(data.get("isUse"))) {
					detailModel.setIsUse(data.get("isUse").toString());
				}
				if (data.get("workFlowState") != null && !"".equals(data.get("workFlowState"))) {
					detailModel.setWorkFlowState(data.get("workFlowState").toString());
				}
				detailModel.setIsUse("Y");
				detailModel.setEnterpriseCode(employee.getEnterpriseCode());
				updateDetailList.add(detailModel);
			}
		}
		String detailIds = request.getParameter("ids");
		if(detailIds != "") {
			monthRewardDetailFacade.delete(detailIds);
		} else {
			monthRewardDetailFacade.saveOrUpdateDetailList(addDetailList, updateDetailList);
		}
		write("{success:true,msg:'操作成功！'}");
	}
	
	public void delateMonthReward(){
		String rewardId = request.getParameter("rewardId");
		monthRewardFacade.delete(rewardId);
		write("{success:true,msg:'删除成功！'}");
	}
	
	public void delateMonthRewardDetail(){
	}
	
	public void queryMonthReward() throws JSONException {
		String rewardMonth = request.getParameter("rewardMonth");
		PageObject obj = monthRewardFacade.findAll(rewardMonth,"0");
		write(JSONUtil.serialize(obj));
	}
	
	public void queryMonthRewardDetail() throws JSONException {
		String workFlowState = request.getParameter("workFlowState");
		String rewardId = request.getParameter("rewardId");
		if(rewardId != null && !"".equals(rewardId)) {
			PageObject obj = monthRewardDetailFacade.findAllByRewardId(rewardId,workFlowState);
			write(JSONUtil.serialize(obj));
		}
	}
	/** 月奖发放的上报 */
	public void monthRewardReport() {
		String rewardId = request.getParameter("rewardId");
		if(rewardId != null && !"".equals(rewardId)) {
			HrJRewardApprove entity = monthRewardFacade.monthRewardReport(rewardId);
			// 短信通知
		//	String tels = approveFacade.getNextSetpRolesTelephone(null,entity);
			//modify by fyyang 20100731
			String tels = approveFacade.getNextSetpRolesTelephone(entity.getFlowListUrl(), null);
			
			if(tels!=null&&!tels.equals(""))
			{
			PostMessage message = new PostMessage();
			message.sendShortMsg(tels, entity.getContent());
			}
			System.out.println("手机号："+tels+"信息："+entity.getContent());
			write("{success:true,msg:'上报成功！'}");
		}
	}
	
	/** 月奖发放审批 */
	public void monthRewardApprove() {
		String detailIds = request.getParameter("detailIds");
		String rewardId = request.getParameter("rewardId");
		List<HrJRewardApprove> approveList = monthRewardDetailFacade.monthRewardApprove(detailIds,rewardId);
		if(approveList!=null&&approveList.size()>0)
		{
		String deptids="";
		for(HrJRewardApprove model:approveList)
		{
			if(deptids.equals(""))
			{
				deptids=model.getDeptId()+"";
			}else
			{
				deptids+=","+model.getDeptId();
			}
		}
		// 短信通知
	//	String tels = approveFacade.getNextSetpRolesTelephone(approveList,null);
		//modify by fyyang 20100731
		String tels = approveFacade.getNextSetpRolesTelephone(approveList.get(0).getFlowListUrl(),deptids);
		if(tels!=null&&!tels.equals(""))
		{
		PostMessage message = new PostMessage();
		message.sendShortMsg(tels, approveList.get(0).getContent());
		
		}
		System.out.println("手机号："+tels+"信息："+approveList.get(0).getContent());
		}

		write("{success:true,msg:'分发成功！'}");
	}
	
	public void monthRewardApproveQuery() throws JSONException {
		String rewardMonth = request.getParameter("rewardMonth");
		PageObject obj = monthRewardFacade.findAll(rewardMonth,"1");
		write(JSONUtil.serialize(obj));
	}
	//add by wpzhu 20100727--------------
	public  void getMaxMonth()
	{
		String workFlowState = request.getParameter("workFlowState");
		String month=monthRewardDetailFacade.getMaxMonth(workFlowState,employee.getDeptId());
		write(month);
		
		
	}
	//--------------------------------------------
	public void administratorQuery() throws JSONException {
		String workFlowState = request.getParameter("workFlowState");
		String rewardMonth = request.getParameter("rewardMonth");
		String deptId = employee.getDeptId().toString();
		PageObject obj = monthRewardDetailFacade.findAllByRewardMonth(rewardMonth, workFlowState, deptId);
		write(JSONUtil.serialize(obj));
	}
	
	public void administratorApprove() {
		String detailId = request.getParameter("detailId");
		String actionId = request.getParameter("actionId");
		if(detailId != null && !"".equals(detailId)) {
			List<HrJRewardApprove> approveList = monthRewardDetailFacade.administratorApprove(detailId,actionId,employee.getDeptId());
			if(approveList!=null&&approveList.size()>0)
			{
				//modify by fyyang 20100731
			String deptids="";
			for(HrJRewardApprove model:approveList)
			{
				if(deptids.equals(""))
				{
					deptids=model.getDeptId()+"";
				}else
				{
					deptids+=","+model.getDeptId();
				}
			}
			// 短信通知
			String tels =approveFacade.getNextSetpRolesTelephone(approveList.get(0).getFlowListUrl(), deptids);
			if(tels!=null&&!tels.equals(""))
			{
			PostMessage message = new PostMessage();
			message.sendShortMsg(tels, approveList.get(0).getContent());
			}
			System.out.println("手机号："+tels+"信息："+approveList.get(0).getContent());
			}

     		write("{success:true,data:'审批成功！'}");
		}
	}
	
	public void isHasShfit() {
		String deptId = employee.getDeptId().toString();
		int count = monthRewardDetailFacade.isHasShfit(deptId);
		write("{success:true,msg:'"+count+"'}");
	}
	
	public void queryMonthRewardDetailByRewardMonth() throws JSONException {
		String rewardMonth = request.getParameter("rewardMonth");
		PageObject obj = monthRewardDetailFacade.queryMonthRewardDetailByRewardMonth(rewardMonth);
		write(JSONUtil.serialize(obj));
	}
	
	public void queryMonthRewardByRewardMonth() throws JSONException{
		String rewardMonth = request.getParameter("rewardMonth");
		PageObject obj = monthRewardFacade.findAll(rewardMonth,"");
		if(obj != null && obj.getList().size() >0) {
//			write("{success :true, msg:'保存成功',reward:" + obj.getList().get(0)+ "}");
			write(JSONUtil.serialize(obj.getList().get(0)));
		} else {
			write(JSONUtil.serialize(null));
		}
	}
	
	public void appvoveMonthAward() {

		String rewardId = request.getParameter("rewardId");
		monthRewardFacade.appvoveMonthAward(rewardId);
		write("{success:true,msg:'审批成功！'}");
	}

	public Boolean getDetailIsNull() {
		return detailIsNull;
	}

	public void setDetailIsNull(Boolean detailIsNull) {
		this.detailIsNull = detailIsNull;
	}

	public HrJMonthReward getReward() {
		return reward;
	}

	public void setReward(HrJMonthReward reward) {
		this.reward = reward;
	}

	public HrJMonthRewardDetail getRewardDetail() {
		return rewardDetail;
	}

	public void setRewardDetail(HrJMonthRewardDetail rewardDetail) {
		this.rewardDetail = rewardDetail;
	}

}
