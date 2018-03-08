package power.web.equ.workbill.action;

import java.text.ParseException;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.equ.workbill.EquJOtma;
import power.ejb.equ.workbill.EquJOtmaFacadeRemote;
import power.ejb.equ.workbill.EquJWo;
import power.ejb.equ.workbill.EquJWoFacadeRemote;
import power.ejb.equ.workbill.EquJWorktickets;
import power.ejb.equ.workbill.EquJWorkticketsFacadeRemote;
import power.ejb.equ.workbill.EquwoInterface;
import power.ejb.resource.InvJIssueHead;
import power.ejb.resource.InvJIssueHeadFacadeRemote;
import power.ejb.workticket.business.RunJWorktickets;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class equipmentWokeBillAction extends AbstractAction {

	EquJWoFacadeRemote remote;
	EquJWorkticketsFacadeRemote wRemote;
	RunJWorkticketsFacadeRemote rwRemote;
	EquJOtmaFacadeRemote oRemot;
	InvJIssueHeadFacadeRemote iRemote;
	EquwoInterface bll;
	private EquJWo equJWo;
	private String failureCode;
	private String woCode;

	public String getWoCode() {
		return woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	public equipmentWokeBillAction() {
		rwRemote = (RunJWorkticketsFacadeRemote) factory.getFacadeRemote("RunJWorkticketsFacade");
		remote = (EquJWoFacadeRemote) factory.getFacadeRemote("EquJWoFacade");
		wRemote = (EquJWorkticketsFacadeRemote) factory
				.getFacadeRemote("EquJWorkticketsFacade");
		oRemot = (EquJOtmaFacadeRemote) factory
				.getFacadeRemote("EquJOtmaFacade");
		iRemote = (InvJIssueHeadFacadeRemote) factory
				.getFacadeRemote("InvJIssueHeadFacade");
		bll = (EquwoInterface) factory.getFacadeRemote("EquwoImp");
	}

	//单挑工单取得
	public void findbywoCode() throws JSONException{
			String woCode = request.getParameter("woCode");
			List<EquJWo> bill = remote.findbill(woCode);
			if(bill != null){
				write(JSONUtil.serialize(bill));
				System.out.print(JSONUtil.serialize(bill));
			}
	}
	// 标准包
	public void findBystdPKage() throws JSONException {
		String kksCode = request.getParameter("kksCode");
		String queryKey = request.getParameter("queryKey");
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		PageObject pg = remote.findstdPKageBykks(kksCode,queryKey,start, limit);
		if (pg != null) {
			String Str = JSONUtil.serialize(pg.getList());
			write("{totalProperty:" + pg.getTotalCount() + ",root:" + Str+ "}");
		} else {
			write("{list : [],totalCount : 0}");
		}
	}

	// 查询工单
	public void findByCondition() throws JSONException {
		String reqBeginTime = request.getParameter("starttime");
		String reqEndTime = request.getParameter("endtime");
		String ifWorkticket = request.getParameter("ifWorkticket");
		String ifMaterials = request.getParameter("ifMaterials");
		String workorderType = request.getParameter("reqBeginTime");
		String workorderStatus = request.getParameter("reqBeginTime");
		String professionCode = request.getParameter("professionCode");
		String repairDepartment = request.getParameter("repairDepartment");
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		PageObject pg = remote.findByCondition(reqBeginTime, reqEndTime,
				ifWorkticket, ifMaterials, workorderType, workorderStatus,
				employee.getEnterpriseCode(), start, limit);
		if (pg != null) {
			write(JSONUtil.serialize(pg));
		} else {
			write("{list : [],totalCount : 0}");
		}
	}

	//	查询父工单
	public void findByFaWoCode() throws JSONException, ParseException{
		int start;
		int limit;
		String reqBeginTime = request.getParameter("starttime");
		String reqEndTime = request.getParameter("endtime");
		String ifWorkticket = request.getParameter("ifWorkticket");
		String ifMaterials = request.getParameter("ifMaterials");
		String workorderType = request.getParameter("workorderType");
		String workorderStatus = request.getParameter("workorderStatus");
		String professionCode = request.getParameter("professionCode");
		String repairDepartment = request.getParameter("repairDepartment");
		String argFuzzy = request.getParameter("argFuzzy");
		String woCode = request.getParameter("woCode");
		String editWoCode = request.getParameter("editWoCode");
		if(editWoCode != null ){
			 start =0;
			 limit =1;
		} else{
			 start = Integer.parseInt(request.getParameter("start"));
			 limit = Integer.parseInt(request.getParameter("limit"));
		}
		
		PageObject pg = remote.findByFaWoCode(reqBeginTime, reqEndTime,
				ifWorkticket, ifMaterials, workorderType,repairDepartment,professionCode, workorderStatus,
				employee.getEnterpriseCode(), argFuzzy,woCode,editWoCode,start, limit);
		if (pg != null) {
	//		String reStr = JSONUtil.serialize(pg.getList());
			write(JSONUtil.serialize(pg));
	//		System.out.print(JSONUtil.serialize(pg));
		} else {
			write("{list : [],totalCount : 0}");
		}
	
	}
	// 增加工单
	public void addWorkBill()  throws Exception {
		try{
		String deptCode = request.getParameter("deptCode");
		String workChargeCode = request.getParameter("workChargeCode");
		String faWoCode = request.getParameter("faWoCode");
		String stdWoCode = request.getParameter("stdWoCode");
		String kkscode=request.getParameter("kkscode");
		java.text.SimpleDateFormat tempDate = new java.text.SimpleDateFormat(
				"yyyyMMdd" + "hhmmss");
		String woCode = "WO" + tempDate.format(new java.util.Date());
		equJWo.setFaWoCode(faWoCode);
		equJWo.setProjectNum(equJWo.getProjectNum().replace(", ", ""));
		equJWo.setWorkChargeCode(workChargeCode);
		equJWo.setWoCode(woCode);
		equJWo.setRequireManCode(employee.getWorkerCode());
		equJWo.setRepairDepartment(employee.getWorkerCode());
		equJWo.setRepairDepartment(deptCode);
		equJWo.setEnterprisecode(employee.getEnterpriseCode());
		equJWo.setKksCode(kkscode);
		
		equJWo.setIfUse("Y");
		/* 0标识开始工作，1标识完成工作 */
		equJWo.setWorkorderStatus("0");
		remote.save(equJWo, failureCode,stdWoCode);
		
        
        
		write("{success : true,msg :'操作成功',woCode : '" + woCode + "',Status :'"+0+"'}");
		}catch(CodeRepeatException e){
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}


	
	
	// 修改工单
	public void updateWorkBill() {
		String deptCode = request.getParameter("deptCode");
		String woCode = request.getParameter("woCode");
		String workChargeCode = request.getParameter("workChargeCode");
		String faWoCode = request.getParameter("faWoCode");
		EquJWo entity = remote.findBywoCode(woCode, employee
				.getEnterpriseCode());
		entity.setFaWoCode(faWoCode);
		entity.setWoCodeShow(equJWo.getWoCodeShow());
		entity.setWorkorderContent(equJWo.getWorkorderContent());
		entity.setWorkorderType(equJWo.getWorkorderType());
		entity.setWorkChargeCode(workChargeCode);
		entity.setRepairDepartment(deptCode);
		entity.setProfessionCode(equJWo.getProfessionCode());
		entity.setRepairModel(equJWo.getRepairModel());
		entity.setProjectNum(equJWo.getProjectNum().replace(", ", ""));
		entity.setKksCode(equJWo.getKksCode());
		entity.setEquPostionCode(equJWo.getEquPostionCode());
		entity.setRemark(equJWo.getRemark());
//		entity.setRequireStarttime(equJWo.getRequireStarttime());
//		entity.setRequireEndtime(equJWo.getRequireEndtime());
		entity.setFactStarttime(equJWo.getFactStarttime());
		entity.setFactEndtime(equJWo.getFactEndtime());
		remote.update(entity, failureCode);
		write("{success : true,msg :'操作成功',woCode : '" + woCode + "',Status :'"+0+"'}");
	}

	// 工作完成
	public void finWorkbill() {
		String woCode = request.getParameter("woCode");
//		String woCode = "WO20090427020816";
		List<EquJWorktickets> list_1 = wRemote.findBywoCode(woCode);
		List<EquJOtma> list_2 = oRemot.findBywoCode(woCode);
		int size_1 = 0;
		int size_2 = 0;
		if (list_1 != null) {
			size_1 = list_1.size();
		}
		if (list_2 != null) {
			size_2 = list_2.size();
		}
		String[] workticketsCodes = new String[size_1];
		String[] matCodes = new String[size_2];
		if (list_1 != null) {
			for (int i = 0; i < list_1.size(); i++) {
				workticketsCodes[i] = list_1.get(i).getWoticketCode();
			}
		}
		if (list_2 != null) {
			for (int i = 0; i < list_2.size(); i++) {
				matCodes[i] = list_2.get(i).getMatCode();
			}
		}
		boolean ob = isAllowEnd(workticketsCodes, matCodes);
		if (ob) {
			String deptCode = request.getParameter("deptCode");
			String workChargeCode = request.getParameter("workChargeCode");
			String faWoCode = request.getParameter("faWoCode");
			EquJWo entity = remote.findBywoCode(woCode, employee
					.getEnterpriseCode());
			entity.setRepairDepartment(deptCode);
			entity.setFaWoCode(faWoCode);
			entity.setWorkChargeCode(workChargeCode);
			entity.setWoCodeShow(equJWo.getWoCodeShow());
			entity.setWorkorderContent(equJWo.getWorkorderContent());
			entity.setWorkorderType(equJWo.getWorkorderType());
			/* 0标识开始工作，1标识完成工作 */
			entity.setWorkorderStatus("1");
			entity.setProfessionCode(equJWo.getProfessionCode());
			entity.setRepairModel(equJWo.getRepairModel());
			entity.setProjectNum(equJWo.getProjectNum());
			entity.setKksCode(equJWo.getKksCode());
			entity.setEquPostionCode(equJWo.getEquPostionCode());
			entity.setRemark(equJWo.getRemark());
//			entity.setRequireStarttime(equJWo.getRequireStarttime());
//			entity.setRequireEndtime(equJWo.getRequireEndtime());
			entity.setPlanStarttime(equJWo.getPlanStarttime());
			entity.setPlanEndtime(equJWo.getPlanEndtime());
			entity.setFactStarttime(equJWo.getFactStarttime());
			entity.setFactEndtime(equJWo.getFactEndtime());
			remote.update(entity, failureCode);
			write("{success : true,msg :'操作成功',woCode : '" + woCode + "',Status :'"+1+"'}");
		} else {
			write("{failure : true,msg:'工单处于工作状态，不能完成工作'}");
		}
	}

	private boolean isAllowEnd(String[] workticketsCodes, String[] matCodes) {
		boolean isallow = true;// 判断是否能结束工单，true标识能结束，false标识不能结束
		if (workticketsCodes != null || workticketsCodes.length > 0) {
			for (String wCode : workticketsCodes) {
				RunJWorktickets entity_1 = rwRemote.findById(wCode);
				// modified by liuyi 091118 状态id存的是数字，非字符串
//				if (entity_1.getWorkticketStausId().equals("1")
//						|| entity_1.getWorkticketStausId().equals("9")
//						|| entity_1.getWorkticketStausId().equals("14")) {
				if (entity_1.getWorkticketStausId() == 1
						|| entity_1.getWorkticketStausId() == 9
						|| entity_1.getWorkticketStausId() == 14) {
//					rwRemote.delete(wCode);
					isallow = true;
					
//				} else if (entity_1.getWorkticketStausId().equals("8")) {
				} else if (entity_1.getWorkticketStausId() == 8) {
					isallow = true;
				} else {
					isallow = false;
					break;
				}
			}
		}
		if (isallow) {
			for (String matCode : matCodes) {
				InvJIssueHead entity_2 = iRemote.findByMatCode(matCode,
						employee.getEnterpriseCode());
				if (entity_2.getIssueStatus().equals("0")
						|| entity_2.getIssueStatus().equals("9")) {
//					entity_2.setIsUse("N");
//					iRemote.update(entity_2);
					isallow = true;
					
				} else {
					isallow = false;
					break;
				}
			}
		}
		
		// add by liuyi 091118 对可以完成的工单处理相关联的工作票，领料单
		if (isallow) {
			for (String wCode : workticketsCodes) {
				RunJWorktickets entity_1 = rwRemote.findById(wCode);
				if (entity_1.getWorkticketStausId() == 1
						|| entity_1.getWorkticketStausId() == 9
						|| entity_1.getWorkticketStausId() == 14) {
					rwRemote.delete(wCode);
				}
			}
			for (String matCode : matCodes) {
				InvJIssueHead entity_2 = iRemote.findByMatCode(matCode,
						employee.getEnterpriseCode());
				if (entity_2.getIssueStatus().equals("0")
						|| entity_2.getIssueStatus().equals("9")) {
					entity_2.setIsUse("N");
					iRemote.update(entity_2);
				}
			}
		}
				
				
		return isallow;
	}

	public String getFailureCode() {
		return failureCode;
	}

	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}

	public EquJWo getequJWo() {
		return equJWo;
	}

	public void setequJWo(EquJWo equJWo) {
		this.equJWo = equJWo;
	}

//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
}
