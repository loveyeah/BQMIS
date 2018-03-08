package power.web.hr.reward;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.taglibs.standard.tag.common.core.CatchTag;
import org.jaxen.function.IdFunction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.hr.reward.HrJBigRewardGrant;
import power.ejb.hr.reward.HrJBigRewardGrantRemote;
import power.ejb.hr.reward.HrJBigRewardGrantDetail;
import power.ejb.hr.reward.HrJRewardApprove;
import power.ejb.hr.reward.HrJRewardApproveFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.PostMessage;

@SuppressWarnings("serial")
public class HrJBigRewardGrantAction extends AbstractAction {

	private HrJBigRewardGrant rwGrant;
	// private HrJBigRewardGrantDetail rDetail;

	private HrJBigRewardGrantRemote rGrantRemote;
	private HrJRewardApproveFacadeRemote msgRemote;

	public HrJBigRewardGrantAction() {
		rGrantRemote = (HrJBigRewardGrantRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("HrJBigRewardGrantImpl");
		msgRemote=(HrJRewardApproveFacadeRemote)factory.getFacadeRemote("HrJRewardApproveFacade");
	}

	/** *******************操作主表*********************** */

	@SuppressWarnings("unchecked")
	public void saveBigRewardGrand() {
		List<HrJBigRewardGrant> list = rGrantRemote.getInintBigRewardGrand(
				rwGrant.getDeptId().toString(), rwGrant.getBigGrantMonth(),
				rwGrant.getGroupId().toString());
		if (list.size() > 0) {
			HrJBigRewardGrant model = list.get(0);
			if ("0".equals(model.getWorkFlowState())) {
				write("{success :true, msg:'该部门奖金已经存在!',bigGrantId:"
						+ model.getBigGrantId() + "}");
			} else {
				write("{success :true, msg:'该部门奖金当前月份已经上报!',bigGrantId:" + null
						+ "}");
			}

		} else {
			rwGrant.setEnterpriseCode(employee.getEnterpriseCode());
			rwGrant.setWorkFlowState("0");
			HrJBigRewardGrant entity = rGrantRemote.save(rwGrant);
			write("{success :true, msg:'保存成功',bigGrantId:"
					+ entity.getBigGrantId() + "}");
		}
	}

	public void deleteBigRewardGrand() {
		String bigGrantId = request.getParameter("bigGrantId");
		rGrantRemote.delete(bigGrantId);
		rGrantRemote.deleteBigDetail(bigGrantId);
		write("{success :true, msg:'删除成功!'}");
	}

	// 大奖上报
	public void updateBigRewardGrand() {
		HrJBigRewardGrant entity = new HrJBigRewardGrant();
		entity = rGrantRemote.findById(rwGrant.getBigGrantId());
		entity.setWorkFlowState("1");
		rGrantRemote.update(entity);
		//加入消息表
		String url="hr/reward/bigReward/bigRewardReport/approve/gather/bigRewardGrantGather.jsp";
		HrJRewardApprove msgModel=new HrJRewardApprove();
		msgModel.setDetailId(entity.getBigGrantId());
		msgModel.setDeptId(entity.getDeptId());
		msgModel.setContent(entity.getBigGrantMonth()+"大奖上报等待汇总!");
		msgModel.setFlag("3");
		msgModel.setFlowListUrl(url);
		msgRemote.save(msgModel);
		//短信通知
		String tels=msgRemote.getNextSetpRolesTelForReport(url, entity.getDeptId());
		PostMessage postMsg=new PostMessage();
		postMsg.sendShortMsg(tels, entity.getBigGrantMonth()+"大奖上报已发送，等待您的汇总!");
		write("{success :true, msg:'上报成功!'}");
	}

	// 大奖主表列表
	@SuppressWarnings("unchecked")
	public void getBigRewardGrandList() {
		String month = request.getParameter("strGrandMonth");
		List list = rGrantRemote.getBigRewardGrandList(month, employee
				.getWorkerCode(), employee.getEnterpriseCode());
		try {
			write(JSONUtil.serialize(list));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 明细列表
	@SuppressWarnings("unchecked")
	public void getBigRewardDetailList() {
		String bigGrantId = request.getParameter("grantId");
		List list = rGrantRemote.getBigRewardGrantDetailList(bigGrantId,
				employee.getEnterpriseCode());
		try {
			write(JSONUtil.serialize(list));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/** *******************操作明细表*********************** */

	@SuppressWarnings("unchecked")
	public void saveOrUpdateBigRewardDetail() {

		String addStr = request.getParameter("isAdd");
		String updateStr = request.getParameter("isUpdate");
		String ids = request.getParameter("ids");
		String workFlowState = request.getParameter("workFlowState");
		try {
			List<Map> addMapList = (List<Map>) JSONUtil.deserialize(addStr);
			List<Map> updateMapList = (List<Map>) JSONUtil
					.deserialize(updateStr);
			List<HrJBigRewardGrantDetail> addList = new ArrayList<HrJBigRewardGrantDetail>();
			List<HrJBigRewardGrantDetail> updateList = new ArrayList<HrJBigRewardGrantDetail>();

			for (Map map : addMapList) {
				HrJBigRewardGrantDetail temp = this
						.parseRewardGrantDetailInstance(map, workFlowState);
				addList.add(temp);
			}
			for (Map map : updateMapList) {
				HrJBigRewardGrantDetail temp = this
						.parseRewardGrantDetailInstance(map, workFlowState);
				updateList.add(temp);
			}
			if (addList.size() > 0 || updateList.size() > 0 || ids != null) {
				rGrantRemote.saveOrUpdateBigRewardDetail(addList, updateList,
						ids);
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
	public HrJBigRewardGrantDetail parseRewardGrantDetailInstance(Map map,
			String workFlowState) {
		HrJBigRewardGrantDetail temp = new HrJBigRewardGrantDetail();

		Long bigDetailId = null;
		Long bigGrantId = null;
		Long empId = null;
		Double coefficientNum = null;
		Double baseNum = null;
		Double amountNum = null;
		String signBy = null;
		String memo = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();

		if (map.get("bigDetailId") != null)
			bigDetailId = Long.parseLong(map.get("bigDetailId").toString());
		if (map.get("bigGrantId") != null)
			bigGrantId = Long.parseLong(map.get("bigGrantId").toString());
		if (map.get("empId") != null)
			empId = Long.parseLong(map.get("empId").toString());
		if (map.get("coefficientNum") != null)
			coefficientNum = Double.parseDouble(map.get("coefficientNum")
					.toString());
		if (map.get("baseNum") != null)
			baseNum = Double.parseDouble(map.get("baseNum").toString());
		if (!"0".equals(workFlowState)) {
			if (map.get("amountNum") != null)
				amountNum = Double.parseDouble(map.get("amountNum").toString());
		}
		if (map.get("signBy") != null)
			signBy = map.get("signBy").toString();
		if (map.get("memo") != null)
			memo = map.get("memo").toString();
		if ("0".equals(workFlowState)) {
			// 金额
			// modify by ywliu 20100810
			if (map.get("amountNum") != null) {
				amountNum = Double.parseDouble(map.get("amountNum").toString());
			} else {
				amountNum = coefficientNum * baseNum;
			}
		}
		temp.setBigDetailId(bigDetailId);
		temp.setBigGrantId(bigGrantId);
		temp.setEmpId(empId);
		temp.setCoefficientNum(coefficientNum);
		temp.setBaseNum(baseNum);
		temp.setAmountNum(amountNum);
		temp.setSignBy(signBy);
		temp.setMemo(memo);
		temp.setIsUse(isUse);
		temp.setEnterpriseCode(enterpriseCode);
		return temp;
	}

	/** *********公共******** */

	// 初始化部门级别
	@SuppressWarnings("unchecked")
	public void initBigRewardGrantDept() {
		String deptId = request.getParameter("deptId");
		try {
			List list = rGrantRemote.getInitBigRewardGrantDept(deptId);
			if (list.size() > 0) {
				write(JSONUtil.serialize(list.get(0)));
			} else {
				write("[]");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据人员id 取人的奖金系数
	 */
	public void getBigRewardMonthAward() {
		String empId = request.getParameter("empId");
		String bigGrantId = request.getParameter("grantId");
		try {
			write(JSONUtil.serialize(rGrantRemote.getBigRewardMonthAward(empId,
					bigGrantId)));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取最大时间
	 */
	public void getMaxBigGarntMonth() {
		try {
			write(JSONUtil.serialize(rGrantRemote.getMaxBigGarntMonth()));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// 取待审批班组
	@SuppressWarnings("unchecked")
	public void getBigApproveGroup() {
		String monthDate = request.getParameter("monthDate");
		String deptId = request.getParameter("deptId");
		String workFlowState = request.getParameter("workFlowState");
		List list = null;
		if (deptId != null && !"".equals(deptId)) {
			list = rGrantRemote.getBigApproveGroup(monthDate, deptId, employee
					.getEnterpriseCode());
		} else {
			list = rGrantRemote.getBigApproveGroup(monthDate, employee
					.getDeptId().toString(), employee.getEnterpriseCode());
		}
		try {
			write(JSONUtil.serialize(list));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 审批明细列表
	@SuppressWarnings("unchecked")
	public void getApproveBigRewardGrandList() {
		String month = request.getParameter("monthDate");
		String deptId = request.getParameter("deptId");
		String groupId = request.getParameter("groupId");
		// add by ywliu 20100819 用于厂长工作部特殊处理
		String roleId = request.getParameter("roleId");
		String workFlowState = request.getParameter("workFlowState");
		String bigAwardId = request.getParameter("bigAwardId");
		List list = null;
		//modify by fyyang 20100729
//		if (deptId != null && !"".equals(deptId)) {
			list = rGrantRemote.getApproveBigRewardGrandList(month, deptId,
					groupId, roleId, workFlowState, employee.getEnterpriseCode(),
					bigAwardId);
//		} else {
//			list = rGrantRemote.getApproveBigRewardGrandList(month, employee
//					.getDeptId().toString(), groupId, workFlowState, employee
//					.getEnterpriseCode(), bigAwardId);
//		}
		try {
			write(JSONUtil.serialize(list));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 汇总 审批 modify by fyyang 20100715
	@SuppressWarnings("unchecked")
	public void approveBigReward() {
		String month = request.getParameter("monthDate");
		String deptId = request.getParameter("deptId");
		String workFlowState = request.getParameter("workFlowState");
		String bigAwardId = request.getParameter("bigAwardId");
		// add by ywliu 20100819 用于厂长工作部特殊处理
		String roleId = request.getParameter("roleId");
		List<HrJBigRewardGrant> list=rGrantRemote.bigRewardApprove(month, deptId, roleId, workFlowState, employee
				.getEnterpriseCode(), bigAwardId);
		if(list!=null&&list.size()>0)
		{
			String flag=list.get(0).getWorkFlowState();
			String msg="";
			String newUrl="";
			String ids="";
			if(flag.equals("1"))
			{
			 msg=month+"大奖上报已汇总，等待审批!";
			 newUrl="hr/reward/bigReward/bigRewardReport/approve/approve/bigRewardGrantApprove.jsp";
			}else if(flag.equals("2"))
			{
				 msg=month+"大奖上报已审核，等待审批!";
				 newUrl="hr/reward/bigReward/bigRewardReport/approve/mimeograph/bigRewardGrantMimeograph.jsp";
			}else if(flag.equals("3"))
			{
				 msg=month+"大奖上报已打印，等待审批!";
				 newUrl="hr/reward/bigReward/bigRewardReport/approve/manager/bigRewardGrantManager.jsp";
			}
			else
			{
				msg="";
				 newUrl="";
			}
			for(HrJBigRewardGrant model:list)
			{
				if(ids.equals("")) ids=model.getBigGrantId()+"";
				else ids+=","+model.getBigGrantId();
			}
			if(!ids.equals(""))
			{
				List<HrJRewardApprove> msgList= msgRemote.findListByDetailId(ids, "3");
				
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

	@SuppressWarnings("unchecked")
	public void getBigAwareNameList() {
		String monthDate = request.getParameter("monthDate");
		String deptId = request.getParameter("deptId");
		List list = rGrantRemote.getBigAwareNameList(monthDate, employee
				.getEnterpriseCode(), deptId);
		try {
			write(JSONUtil.serialize(list));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void getBigRewardNum() {
		String monthDate = request.getParameter("monthDate");
		//String bigAwardId = request.getParameter("bigAwardId");
		String deptId = request.getParameter("deptId");
		String rewardNum = rGrantRemote.getBigRewardNum(monthDate, null,
				deptId, employee.getEnterpriseCode());
		write("{success:true,rewardNum:'" + rewardNum + "'}");
	}

	// 取待审批班组
	@SuppressWarnings("unchecked")
	public void getBigRewardApproveAwareList() {
		String monthDate = request.getParameter("monthDate");
		String deptId = request.getParameter("deptId");
		String groupId = request.getParameter("groupId");
		String workFlowState = request.getParameter("workFlowState");
		List list = null;
		if (deptId != null && !"".equals(deptId)) {
			list = rGrantRemote.getBigRewardApproveAwareList(monthDate, deptId,
					groupId, workFlowState, employee.getEnterpriseCode());
		} else {
			list = rGrantRemote.getBigRewardApproveAwareList(monthDate,
					employee.getDeptId().toString(), groupId, workFlowState,
					employee.getEnterpriseCode());
		}
		try {
			write(JSONUtil.serialize(list));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public HrJBigRewardGrant getRwGrant() {
		return rwGrant;
	}

	public void setRwGrant(HrJBigRewardGrant rwGrant) {
		this.rwGrant = rwGrant;
	}

}
