package power.web.hr.reward;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.reward.HrCTechnicianStandards;
import power.ejb.hr.reward.HrJBigReward;
import power.ejb.hr.reward.HrJBigRewardDetail;
import power.ejb.hr.reward.HrJBigRewardDetailFacadeRemote;
import power.ejb.hr.reward.HrJBigRewardFacadeRemote;
import power.ejb.hr.reward.HrJMonthReward;
import power.ejb.hr.reward.HrJRewardApprove;
import power.ejb.hr.reward.HrJRewardApproveFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.PostMessage;

public class HrJBigRewardAction extends AbstractAction {
	
	private HrJBigRewardFacadeRemote remote;
	private HrJBigRewardDetailFacadeRemote remoteA;
	private HrJRewardApproveFacadeRemote approveFacade;
	private HrJBigReward reward;

	public HrJBigRewardAction() {
		remote = (HrJBigRewardFacadeRemote) factory.getFacadeRemote("HrJBigRewardFacade");
		remoteA = (HrJBigRewardDetailFacadeRemote) factory.getFacadeRemote("HrJBigRewardDetailFacade");
		approveFacade = (HrJRewardApproveFacadeRemote) factory.getFacadeRemote("HrJRewardApproveFacade");
	}
  //add by wpzhu 20100724--------------
	public void queryDetailBigRewardByDept() throws JSONException
	{
		PageObject result=new PageObject();
		String deptId = request.getParameter("deptId");
		String bigRewardId = request.getParameter("bigRewardId");

     result=remoteA.queryDetailBigRewardByDept((bigRewardId==null?0l:Long.parseLong(bigRewardId)),
					(deptId==null?0l:Long.parseLong(deptId)));
     if (result != null) {
			write(JSONUtil.serialize(result));;
//			System.out.println("the result"+JSONUtil.serialize(result));
		} else {
			write("{totalCount : 0,list :[]}");
		}
		
	
		
	}
	public void queryRewardDeptList() throws JSONException
	{
		String bigRewardId = request.getParameter("bigRewardId");
		 
		List  list=remoteA.queryRewardDeptList((bigRewardId==null?0l:Long.parseLong(bigRewardId)));
		if (list != null) {
		  write("{list :"+JSONUtil.serialize(list)+"}");
		
		} else {
			write("{list :[]}");
		}
	}
	//--------------------end----------------------------------------
	public void getAllawardName() throws JSONException {
		String yearMonth = request.getParameter("month");
		String awardID = request.getParameter("awardID");
		PageObject obj = new PageObject();
		obj = remoteA.getAllawardName(yearMonth);
		if (obj != null) {
			write(JSONUtil.serialize(obj));
			// System.out.println("the obj"+JSONUtil.serialize(obj));
		} else {
			write("{totalCount : 0,list :[]}");
		}

	}

	/** 大奖发放的上报 */
	public void bigRewardReport() {
		String mainId = request.getParameter("bigRewardId");
//		String approveText = request.getParameter("approveText");
//		String nextRoles = request.getParameter("nextRoles");
//		String flowCode = request.getParameter("flowCode");
//		String actionId = request.getParameter("actionId");
//		remote.rewardReport(Long.parseLong(mainId), Long.parseLong(actionId),
//				employee.getWorkerCode(), approveText, nextRoles, flowCode);
		if(mainId != null && !"".equals(mainId)) {
//			HrJBigReward entity = remote.findById(Long.parseLong(mainId));
//			entity.setWorkFlowState("1");
//			remote.update(entity);
//			write("{success:true,msg:'上报成功！'}");
			HrJRewardApprove entity = remote.rewardReport(Long.parseLong(mainId), null,employee.getWorkerCode(), "", "", "");
			// 短信通知
			//modify by fyyang 20100731
			String tels =approveFacade.getNextSetpRolesTelephone(entity.getFlowListUrl(), null);// approveFacade.getNextSetpRolesTelephone(null,entity);
			
			if(tels!=null&&!tels.equals(""))
			{
			PostMessage message = new PostMessage();
			message.sendShortMsg(tels, entity.getContent());
			}
			System.out.println("手机号："+tels+"信息："+entity.getContent());
			write("{success:true,msg:'上报成功！'}");
		}
//		write("{success:true,msg:'上报成功！'}");
		// 短信通知
	}

	public void getBigReward() throws JSONException {
		Object sta = request.getParameter("start");
		Object lim = request.getParameter("limit");
		String workflowStatus = request.getParameter("workflowStatus");
		String rewardMonth = request.getParameter("rewardMonth");
		PageObject obj = new PageObject();
		if (sta != null && lim != null) {
			int start = Integer.parseInt(sta.toString());
			int limit = Integer.parseInt(lim.toString());
			obj = remote.getBigReward(workflowStatus,rewardMonth, employee
					.getEnterpriseCode(), start, limit);
		} else {
			obj = remote.getBigReward(workflowStatus,rewardMonth, employee
					.getEnterpriseCode());
		}

		if (obj != null) {
			write(JSONUtil.serialize(obj));

		} else {
			write("{totalCount : 0,list :[]}");
		}

	}

	public void saveBigReward() throws NumberFormatException, ParseException {
		String method = request.getParameter("method");
		String rewardname = request.getParameter("rewardname");
		if (method.equals("add")) {
//			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
			// reward.setHandedDate(df.parse(handDate));
			reward.setEnterpriseCode(employee.getEnterpriseCode());
			reward.setBigAwardName(rewardname);
			reward.setWorkFlowState("0");
			reward.setIsUse("Y");
			reward.setFillBy(employee.getWorkerCode());
			reward.setFillDate(new Date());
			Long bigRewardId;
			try {
				bigRewardId = remote.save(reward);
				write("{success:'true',msg:'保存成功',bigRewardId:'" + bigRewardId + "'}");
			} catch (CodeRepeatException e) {
				write("{success:true,msg:'增加失败:当月大奖已经存在,不能重复填写！'}");
			}
		} else if (method.equals("update")) {
			HrJBigReward entity = remote.findById(reward.getBigRewardId());
			entity.setBigAwardName(reward.getBigAwardName());
			entity.setBigRewardBase(reward.getBigRewardBase());
			entity.setBigRewardId(reward.getBigRewardId());
			entity.setBigRewardMonth(reward.getBigRewardMonth());
			entity.setBigAwardName(rewardname);
			entity.setWorkFlowState("0");
			// entity.setFillDate(fillDate)填写日期为第一次保存日期
			entity.setHandedDate(reward.getHandedDate());
			entity.setIsUse("Y");
			remote.update(entity);
			write("{success:'true',msg:'保存成功'}");
		}

	}

	public void delBigReward() {
		String id = request.getParameter("id");
		remote.delBigReward(Long.parseLong(id));
		write("{success:'true',msg:'删除成功'}");
	}

	// --------------------大奖发放明细表发放-----------------start-------------------
	public void getBigRewardDetail() throws JSONException {
		PageObject obj = new PageObject();
		String mainId = request.getParameter("mainId");
		String workFlowState = request.getParameter("workFlowState");
		if (mainId != "" && !mainId.equals("")) {
			obj = remoteA.getBigRewardDetail(Long.parseLong(mainId),workFlowState);
			if (obj != null) {
				write(JSONUtil.serialize(obj));

			} else {
				write("{totalCount : 0,list :[]}");
			}

		} else {

		}
	}

	@SuppressWarnings("unchecked")
	public void saveBigRewardDetail() throws java.text.ParseException,
			JSONException {
		String rewardId = request.getParameter("rewardId");
		String str = request.getParameter("isUpdate");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<HrJBigRewardDetail> addList = null;
		List<HrJBigRewardDetail> updateList = null;
		if (list != null && list.size() > 0) {
			addList = new ArrayList<HrJBigRewardDetail>();
			updateList = new ArrayList<HrJBigRewardDetail>();
			try {
				for (Map data : list) {
					String bigDetailId = null;
					String bigRewardId = null;
					String deptId = null;
					String empCount = null;
					String bigRewardNum = null;
					String memo = null;
					String workFlowNo = null;
					String workFlowState = null;
					if (data.get("bigDetailId") != null
							&& !"".equals(data.get("bigDetailId")))
						bigDetailId = data.get("bigDetailId").toString();
					if (data.get("bigRewardId") != null
							&& !"".equals(data.get("bigRewardId"))) {
						bigRewardId = data.get("bigRewardId").toString();
					} else {
						bigRewardId = rewardId;
					}
					if (data.get("deptId") != null
							&& !"".equals(data.get("deptId")))
						deptId = data.get("deptId").toString();
					if (data.get("empCount") != null
							&& !"".equals(data.get("empCount")))
						empCount = data.get("empCount").toString();
					if (data.get("bigRewardNum") != null
							&& !"".equals(data.get("bigRewardNum")))
						bigRewardNum = data.get("bigRewardNum").toString();
					if (data.get("memo") != null
							&& !"".equals(data.get("memo")))
						memo = data.get("memo").toString();

					if (data.get("workFlowNo") != null
							&& !"".equals(data.get("workFlowNo"))) {
						workFlowNo = data.get("workFlowNo").toString();
					}
					if (data.get("workFlowState") != null
							&& !"".equals(data.get("workFlowState"))) {
						workFlowState = data.get("workFlowState")
								.toString();
					} else {
						workFlowState = "0";
					}
					HrJBigRewardDetail model = new HrJBigRewardDetail();
					if (bigDetailId == null) {
						if (bigRewardId != null) {
							model.setBigRewardId(Long
									.parseLong(bigRewardId));
						}
						if (deptId != null) {
							model.setDeptId(Long.parseLong(deptId));
						}
						if (empCount != null) {

							model.setEmpCount(Long.parseLong(empCount));
						}
						if (bigRewardNum != null) {
							model.setBigRewardNum(Double
									.parseDouble(bigRewardNum));
						}
						if (memo != null)
							model.setMemo(memo);
						if (workFlowNo != null) {

							model.setWorkFlowNo(Long.parseLong(workFlowNo));
						}
						if (workFlowState != null) {
							model.setWorkFlowState(workFlowState);
						}

						model.setEnterpriseCode(employee
								.getEnterpriseCode());
						model.setIsUse("Y");
						addList.add(model);
					} else {
						model = remoteA.findById(Long
								.parseLong(bigDetailId));
						if (bigRewardId != null) {
							model.setBigRewardId(Long
									.parseLong(bigRewardId));
						}
						if (deptId != null) {
							model.setDeptId(Long.parseLong(deptId));
						}
						if (empCount != null) {

							model.setEmpCount(Long.parseLong(empCount));
						}
						if (bigRewardNum != null) {
							model.setBigRewardNum(Double
									.parseDouble(bigRewardNum));
						}
						if (memo != null)
							model.setMemo(memo);
						if (workFlowNo != null) {

							model.setWorkFlowNo(Long.parseLong(workFlowNo));
						}
						if (workFlowState != null) {
							model.setWorkFlowState(workFlowState);
						}

						model.setEnterpriseCode(employee
								.getEnterpriseCode());
						model.setIsUse("Y");
						updateList.add(model);
					}
				}
				remoteA.saveBigRewardDetail(addList, updateList);
				write("{success:true,msg:'操作成功！'}");
			} catch (CodeRepeatException e) {
				String out = "{success:true,msg :'" + e.getMessage() + "'}";
				write(out);
			}
		}
	}
	
	public void buildBigRewardDetailData() {
		String bigRewardId = request.getParameter("bigRewardId");
		String rewardMonth = request.getParameter("rewardMonth");
		String bigRewardBase = request.getParameter("bigRewardBase");
		remoteA.buildData(bigRewardId, rewardMonth, bigRewardBase);
		write("{success:true,msg:'明细生成成功！'}");
	}

	public void deleteBigRewardDetail() {
		String ids = request.getParameter("ids");
		remoteA.deleteBigRewardDetail(ids);
		write("{success:true,msg:'删除成功！'}");

	}

	/** 大奖发放审批 */
	public void bigRewardDetailReport() {
		String detailIds = request.getParameter("detailIds");
		String rewardId = request.getParameter("rewardId");
		List<HrJRewardApprove> approveList = remoteA.rewardDetailReport(detailIds,rewardId);
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
	
		
		write("{success:true,msg:'分发成功！'}");
	}
	
	public void bigRewardAdministratorQuery() throws JSONException {
		String workFlowState = request.getParameter("workFlowState");
		String bigRewardId = request.getParameter("bigRewardId");
		String deptId = employee.getDeptId().toString();
		PageObject obj = remoteA.findAllByBigAwardName(bigRewardId, workFlowState, deptId);
		write(JSONUtil.serialize(obj));
	}
	
	public void bigRewardAdministratorApprove() {
		String detailId = request.getParameter("detailId");
		String actionId = request.getParameter("actionId");
		if(detailId != null && !"".equals(detailId)) {
			List<HrJRewardApprove> approveList = remoteA.administratorApprove(detailId,actionId,employee.getDeptId());
			// 短信通知
			if(approveList != null && approveList.size() > 0) {
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
				String tels = approveFacade.getNextSetpRolesTelephone(approveList.get(0).getFlowListUrl(), deptids);
				
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

	public void queryBigRewardDetailByRewardMonth() throws JSONException {
		String rewardMonth = request.getParameter("rewardMonth");
		PageObject obj = remoteA.queryBigRewardDetailByRewardMonth(rewardMonth);
		write(JSONUtil.serialize(obj));
	}
	
	public void queryBigRewardByRewardMonth() throws JSONException{
		String rewardMonth = request.getParameter("rewardMonth");
		PageObject obj = remote.getBigReward("",rewardMonth,employee.getEnterpriseCode());
		if(obj != null && obj.getList().size() >0) {
//			write("{success :true, msg:'保存成功',reward:" + obj.getList().get(0)+ "}");
			write(JSONUtil.serialize(obj.getList().get(0)));
		} else {
			write(JSONUtil.serialize(null));
		}
	}

	
	public void appvoveBigAward() {

		String bigRewardId = request.getParameter("bigRewardId");
		remote.appvoveBigAward(bigRewardId);
		write("{success:true,msg:'审批成功！'}");
	}
	
	
	// ----------------------------end---------------------------
	public HrJBigReward getReward() {
		return reward;
	}

	public void setReward(HrJBigReward reward) {
		this.reward = reward;
	}

}