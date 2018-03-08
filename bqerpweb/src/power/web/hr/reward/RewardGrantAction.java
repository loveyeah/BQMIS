package power.web.hr.reward;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.hr.reward.HrJBigRewardGrant;
import power.ejb.hr.reward.HrJRewardApprove;
import power.ejb.hr.reward.HrJRewardApproveFacadeRemote;
import power.ejb.hr.reward.HrJRewardGrant;
import power.ejb.hr.reward.HrJRewardGrantDetail;
import power.ejb.hr.reward.rewardGrant;
import power.web.comm.AbstractAction;
import power.web.comm.PostMessage;

@SuppressWarnings("serial")
public class RewardGrantAction extends AbstractAction {

	private HrJRewardGrant rwGrant;
	// private HrJRewardGrantDetail rDetail;

	private rewardGrant rGrantRemote;
	private HrJRewardApproveFacadeRemote msgRemote;

	public RewardGrantAction() {
		rGrantRemote = (rewardGrant) Ejb3Factory.getInstance().getFacadeRemote(
				"rewardGrantImpl");
		msgRemote=(HrJRewardApproveFacadeRemote)factory.getFacadeRemote("HrJRewardApproveFacade");
	}

	/** *******************操作主表*********************** */

	public void saveRewardGrand() {
		List<HrJRewardGrant> list = rGrantRemote.getInintRewardGrand(rwGrant
				.getDeptId().toString(), rwGrant.getGrantMonth(), rwGrant
				.getGroupId().toString());
		if (list.size() > 0) {
			HrJRewardGrant model = list.get(0);
			if ("0".equals(model.getWorkFlowState())) {
				write("{success :true, msg:'该部门奖金已经存在!',grantId:"
						+ model.getGrantId() + "}");
			} else {
				write("{success :true, msg:'该部门奖金当前月份已经上报!',grantId:" + null
						+ "}");
			}
		} else {
			rwGrant.setEnterpriseCode(employee.getEnterpriseCode());
			rwGrant.setWorkFlowState("0");
			HrJRewardGrant entity = rGrantRemote.save(rwGrant);
			write("{success :true, msg:'保存成功',grantId:" + entity.getGrantId()
					+ "}");
		}
	}

	public void deleteRewardGrand() {
		String grantId = request.getParameter("grantId");
		rGrantRemote.delete(grantId);
		rGrantRemote.deleteDetail(grantId);
		write("{success :true, msg:'删除成功!'}");
	}

	// 奖金上报
	public void updateRewardGrand() {
		HrJRewardGrant entity = new HrJRewardGrant();
		entity = rGrantRemote.findById(rwGrant.getGrantId());
		entity.setWorkFlowState("1");
		rGrantRemote.update(entity);
		//-------add by fyyang 2010-7-15
		//加入消息表
		String url="hr/reward/monthAward/approve/gather/rewardGrantGather.jsp";
		HrJRewardApprove msgModel=new HrJRewardApprove();
		msgModel.setDetailId(entity.getGrantId());
		msgModel.setDeptId(entity.getDeptId());
		msgModel.setContent(entity.getGrantMonth()+"月奖上报等待汇总!");
		msgModel.setFlag("4");
		msgModel.setFlowListUrl(url);
		msgRemote.save(msgModel);
		//短信通知
		String tels=msgRemote.getNextSetpRolesTelForReport(url, entity.getDeptId());
		PostMessage postMsg=new PostMessage();
		postMsg.sendShortMsg(tels, entity.getGrantMonth()+"月奖上报已发送，等待您的汇总!");
		
		write("{success :true, msg:'上报成功!'}");
	}

	// 奖金主表列表
	@SuppressWarnings("unchecked")
	public void getRewardGrandList() {
		String month = request.getParameter("strGrandMonth");
		String deptId = request.getParameter("deptId");
		String groupId = request.getParameter("groupId");
		List list = rGrantRemote.getRewardGrandList(month, deptId, groupId,
				employee.getWorkerCode(), employee.getEnterpriseCode());
		try {
			write(JSONUtil.serialize(list));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/** *******************操作明细表*********************** */

	@SuppressWarnings("unchecked")
	public void saveOrUpdateRewardDetail() {

		String addStr = request.getParameter("isAdd");
		String updateStr = request.getParameter("isUpdate");
		String ids = request.getParameter("ids");
		String workFlowState = request.getParameter("workFlowState");
		try {
			List<Map> addMapList = (List<Map>) JSONUtil.deserialize(addStr);
			List<Map> updateMapList = (List<Map>) JSONUtil
					.deserialize(updateStr);
			List<HrJRewardGrantDetail> addList = new ArrayList<HrJRewardGrantDetail>();
			List<HrJRewardGrantDetail> updateList = new ArrayList<HrJRewardGrantDetail>();

			for (Map map : addMapList) {
				HrJRewardGrantDetail temp = this
						.parseRewardGrantDetailInstance(map, workFlowState);
				addList.add(temp);
			}
			for (Map map : updateMapList) {
				HrJRewardGrantDetail temp = this
						.parseRewardGrantDetailInstance(map, workFlowState);
				updateList.add(temp);
			}
			if (addList.size() > 0 || updateList.size() > 0 || ids != null) {
				rGrantRemote.saveOrUpdateRewardDetail(addList, updateList, ids);
			}
			write("{success:true,msg:'数据保存修改成功！'}");
		} catch (JSONException e) {
			// TODO: handle exception
		}

	}

	/**
	 * 将一映射转化为奖金明细对象
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HrJRewardGrantDetail parseRewardGrantDetailInstance(Map map,
			String workFlowState) {
		HrJRewardGrantDetail temp = new HrJRewardGrantDetail();

		Long detailId = null;
		Long grantId = null;
		Long empId = null;
		Double coefficientNum = null;
		Double baseNum = null;
		Double amountNum = null;
		Double awardNum = null;
		Double monthRewardNum = 0.00;
		Double quantifyCash = 0.00;
		Double monthAssessNum = 0.00;
		Double quantifyAssessNum = 0.00;
		Double addValue = 0.00;//add by sychen 20100903
		Double totalNum = null;
		String signBy = null;
		String memo = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();

		if (map.get("detailId") != null)
			detailId = Long.parseLong(map.get("detailId").toString());
		if (map.get("grantId") != null)
			grantId = Long.parseLong(map.get("grantId").toString());
		if (map.get("empId") != null)
			empId = Long.parseLong(map.get("empId").toString());
		
		//update by sychen 20100902
		if (map.get("coefficientNum") != null&&!map.get("coefficientNum").equals(""))
			coefficientNum = Double.parseDouble(map.get("coefficientNum")
					.toString());
        if (map.get("baseNum") != null&&!map.get("baseNum").equals(""))
			baseNum = Double.parseDouble(map.get("baseNum").toString());
		if (!"0".equals(workFlowState)) {
			if (map.get("amountNum") != null&&!map.get("amountNum").equals(""))
				amountNum = Double.parseDouble(map.get("amountNum").toString());
		}
		if (map.get("awardNum") != null&&!map.get("awardNum").equals(""))
			awardNum = Double.parseDouble(map.get("awardNum").toString());
		if (map.get("monthRewardNum") != null&&!map.get("monthRewardNum").equals(""))
			monthRewardNum = Double.parseDouble(map.get("monthRewardNum").toString());
		if (map.get("quantifyCash") != null&&!map.get("quantifyCash").equals(""))
			quantifyCash = Double.parseDouble(map.get("quantifyCash").toString());
		if (map.get("monthAssessNum") != null&&!map.get("monthAssessNum").equals(""))
			monthAssessNum = Double.parseDouble(map.get("monthAssessNum").toString());
		if (map.get("quantifyAssessNum") != null&&!map.get("quantifyAssessNum").equals(""))
			quantifyAssessNum = Double.parseDouble(map.get("quantifyAssessNum").toString());
		if (map.get("addValue") != null&&!map.get("addValue").equals(""))
			addValue = Double.parseDouble(map.get("addValue").toString());
		//update by sychen 20100902 end 
		
//		if (map.get("coefficientNum") != null)
//			coefficientNum = Double.parseDouble(map.get("coefficientNum")
//					.toString());
//		if (map.get("baseNum") != null)
//			baseNum = Double.parseDouble(map.get("baseNum").toString());
//		if (!"0".equals(workFlowState)) {
//			if (map.get("amountNum") != null)
//				amountNum = Double.parseDouble(map.get("amountNum").toString());
//		}
//		if (map.get("awardNum") != null)
//			awardNum = Double.parseDouble(map.get("awardNum").toString());
//		// modify by ywliu 20100813
//		if (map.get("monthRewardNum") != null)
//			monthRewardNum = Double.parseDouble(map.get("monthRewardNum").toString());
//		if (map.get("quantifyCash") != null)
//			quantifyCash = Double.parseDouble(map.get("quantifyCash").toString());
//		if (map.get("monthAssessNum") != null)
//			monthAssessNum = Double.parseDouble(map.get("monthAssessNum").toString());
//		if (map.get("quantifyAssessNum") != null)
//			quantifyAssessNum = Double.parseDouble(map.get("quantifyAssessNum").toString());
		
		// if (map.get("totalNum") != null)
		// totalNum = Double.parseDouble(map.get("totalNum").toString());
		if (map.get("signBy") != null)
			signBy = map.get("signBy").toString();
		if (map.get("memo") != null)
			memo = map.get("memo").toString();
		if ("0".equals(workFlowState)) {
			// 金额
			amountNum = coefficientNum * baseNum;
		}
		// 合计
//		totalNum = amountNum + awardNum;
//		totalNum = monthRewardNum + quantifyCash + monthAssessNum + quantifyAssessNum;//update by sychen 20100903
        totalNum = monthRewardNum + quantifyCash + monthAssessNum + quantifyAssessNum+addValue;
		temp.setDetailId(detailId);
		temp.setGrantId(grantId);
		temp.setEmpId(empId);
		temp.setCoefficientNum(coefficientNum);
		temp.setBaseNum(baseNum);
		temp.setAmountNum(amountNum);
		temp.setAwardNum(awardNum);
		temp.setMonthRewardNum(monthRewardNum);
		temp.setQuantifyCash(quantifyCash);
		temp.setMonthAssessNum(monthAssessNum);
		temp.setQuantifyAssessNum(quantifyAssessNum);
		temp.setAddValue(addValue);//add by sychen 20100903
		temp.setTotalNum(totalNum);
		temp.setSignBy(signBy);
		temp.setMemo(memo);
		temp.setIsUse(isUse);
		temp.setEnterpriseCode(enterpriseCode);
		return temp;
	}

	// 明细列表
	@SuppressWarnings("unchecked")
	public void getRewardDetailList() {
		String grantId = request.getParameter("grantId");
		List list = rGrantRemote.getRewardGrantDetailList(grantId);
		try {
			write(JSONUtil.serialize(list));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/** *********公共******** */

	// 初始化部门级别
	@SuppressWarnings("unchecked")
	public void initRewardGrantDept() {
		String deptId = request.getParameter("deptId");
		try {
			List list = rGrantRemote.getInitRewardGrantDept(deptId);
			if (list.size() > 0) {
				write(JSONUtil.serialize(list.get(0)));
			} else {
				write("[]");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 班组列表
	@SuppressWarnings("unchecked")
	public void getGroupNameList() {
		String deptId = request.getParameter("deptId");
		List list = rGrantRemote.getGroupNameList(deptId, employee
				.getEnterpriseCode());
		try {
			System.out.println(JSONUtil.serialize(list));
			write(JSONUtil.serialize(list));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据人员id 取人的奖金系数
	 */
	public void getRewardMonthAward() {
		String empId = request.getParameter("empId");
		String grantId = request.getParameter("grantId");
		try {
			write(JSONUtil.serialize(rGrantRemote.getRewardMonthAward(empId,
					grantId)));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取最大时间
	 */
	public void getMaxGarntMonth() {
		try {
			write(JSONUtil.serialize(rGrantRemote.getMaxGarntMonth()));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// 取待审批班组
	@SuppressWarnings("unchecked")
	public void getApproveGroup() {
		String monthDate = request.getParameter("monthDate");
		String deptId = request.getParameter("deptId");
		List list = null;
		if (deptId != null && !"".equals(deptId)) {
			list = rGrantRemote.getApproveGroup(monthDate, deptId, employee
					.getEnterpriseCode());
		} else {
			list = rGrantRemote.getApproveGroup(monthDate, employee.getDeptId()
					.toString(), employee.getEnterpriseCode());
		}
		try {
			write(JSONUtil.serialize(list));
			System.out.println(JSONUtil.serialize(list));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 审批明细列表
	@SuppressWarnings("unchecked")
	public void getApproveRewardGrandList() {
		String month = request.getParameter("monthDate");
		String deptId = request.getParameter("deptId");
//		System.out.println("the deptId"+deptId);
		String groupId = request.getParameter("groupId");
		// add by ywliu 20100819 用于厂长工作部特殊处理
		String roleId = request.getParameter("roleId");
		String workFlowState = request.getParameter("workFlowState");
		List list = null;
//		if (deptId != null && !"".equals(deptId)) {
			list = rGrantRemote.getApproveRewardGrandList(month, deptId,
					groupId, roleId, workFlowState, employee.getEnterpriseCode());
//		} else {//modify by wpzhu 20100728
//			list = rGrantRemote.getApproveRewardGrandList(month, employee
//					.getDeptId().toString(), groupId, workFlowState, employee
//					.getEnterpriseCode());
//		}
		try {
			write(JSONUtil.serialize(list));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 汇总 审批
	@SuppressWarnings("unchecked")
	public void approveReward() {
		String month = request.getParameter("monthDate");
		String deptId = request.getParameter("deptId");
		// add by ywliu 20100819
		String groupId = request.getParameter("groupId");
		String workFlowState = request.getParameter("workFlowState");
		String roleId = request.getParameter("roleId");
		List<HrJRewardGrant> list=rGrantRemote.rewardApprove(month, deptId,groupId, roleId, workFlowState, employee
				.getEnterpriseCode());
		if(list!=null&&list.size()>0)
		{
			String flag=list.get(0).getWorkFlowState();
			String msg="";
			String newUrl="";
			String ids="";
			if(flag.equals("1"))
			{
			 msg=month+"月奖上报已汇总，等待审批!";
			 newUrl="hr/reward/monthAward/approve/approve/rewardGrantApprove.jsp";
			}else if(flag.equals("2"))
			{
				 msg=month+"月奖上报已审核，等待审批!";
				 newUrl="hr/reward/monthAward/approve/mimeograph/rewardGrantMimeograph.jsp";
			}else if(flag.equals("3"))
			{
				 msg=month+"月奖上报已打印，等待审批!";
				 newUrl="hr/reward/monthAward/approve/manager/rewardGrantManager.jsp";
			}
			else
			{
				msg="";
				 newUrl="";
			}
			for(HrJRewardGrant model:list)
			{
				if(ids.equals("")) ids=model.getGrantId()+"";
				else ids+=","+model.getGrantId();
			}
			if(!ids.equals(""))
			{
				List<HrJRewardApprove> msgList= msgRemote.findListByDetailId(ids, "4");
				
				for(HrJRewardApprove msgModel:msgList)
				{
					if(flag.equals("4"))
					{
						msgRemote.delete(msgModel);
					}
					else{
						
						msgModel.setContent(msg);
						msgModel.setFlowListUrl(newUrl);
						msgRemote.update(msgModel);
						
					}
				}
			}
			
		// String url="hr/reward/bigReward/bigRewardReport/approve/gather/bigRewardGrantGather.jsp";
		//短信通知
		String tels=msgRemote.getNextSetpRolesTelForReport(newUrl, list.get(0).getDeptId());
		PostMessage postMsg=new PostMessage();
		postMsg.sendShortMsg(tels, msg);
		}
		write("{success:true,msg:'操作成功！'}");
	}

	/**
	 * 取部门下应有人数
	 */
	public void getDeptPeopleNum() {
		String deptId = request.getParameter("deptId");
		try {
			write(JSONUtil.serialize(rGrantRemote.getDeptPeopleNum(deptId,
					employee.getEnterpriseCode())));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// 取所有一级部门
	@SuppressWarnings("unchecked")
	public void getAllFirstDept() {
		List list = rGrantRemote.getAllFirstDept(employee.getEnterpriseCode());
		try {
			write(JSONUtil.serialize(list));
//			System.out.println(JSONUtil.serialize(list));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void getMonthRewardNum() {
		String monthDate = request.getParameter("monthDate");
		String deptId = request.getParameter("deptId");
		String rewardNum = rGrantRemote.getMonthRewardNum(monthDate, deptId,
				employee.getEnterpriseCode());
		write("{success:true,rewardNum:'" + rewardNum + "'}");
	}
	
	/**
	 * 根据当前登陆人workerID等到对应角色ID
	 * add by ywliu 20100819
	 * @throws JSONException 
	 */
	public void getRoleIDByWorkerId() throws JSONException {
		List list = rGrantRemote.findRoleIDByWorkerId(employee.getEmpId(), employee.getEnterpriseCode());
		write(JSONUtil.serialize(list));
	}

	public HrJRewardGrant getRwGrant() {
		return rwGrant;
	}

	public void setRwGrant(HrJRewardGrant rwGrant) {
		this.rwGrant = rwGrant;
	}

}
