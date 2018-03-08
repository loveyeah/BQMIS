package power.web.resource.plangather.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.components.Else;

import power.ear.comm.ejb.PageObject;
import power.ejb.resource.MrpJPlanGather;
import power.ejb.resource.MrpJPlanGatherFacadeRemote;
import power.ejb.resource.MrpJPlanRequirementDetail;
import power.ejb.resource.MrpJPlanRequirementDetailFacadeRemote;
import power.ejb.resource.MrpJPlanRequirementHeadFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class PlanGatherAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	protected MrpJPlanRequirementHeadFacadeRemote remoteHead;
	protected MrpJPlanGatherFacadeRemote gatherRemote;
	protected MrpJPlanRequirementDetailFacadeRemote remoteDetail;

	public PlanGatherAction() {
		remoteHead = (MrpJPlanRequirementHeadFacadeRemote) factory
				.getFacadeRemote("MrpJPlanRequirementHeadFacade");
		gatherRemote = (MrpJPlanGatherFacadeRemote) factory
				.getFacadeRemote("MrpJPlanGatherFacade");
		remoteDetail = (MrpJPlanRequirementDetailFacadeRemote) factory
				.getFacadeRemote("MrpJPlanRequirementDetailFacade");
	}

	/**
	 * 获取所有审批结束后的物料明细
	 * 
	 * @throws JSONException
	 */
	public void getMRPMaterialDetail() throws JSONException {
		String buyer = request.getParameter("buyer");
		String applyDept = request.getParameter("applyDept");
		/** 开始查询位置 */
		// int start = Integer.parseInt(request.getParameter("start"));
		/** 查询行数 */
		// int limit = Integer.parseInt(request.getParameter("limit"));
		PageObject obj = new PageObject();
		// obj =
		// remoteHead.getMRPMaterialDetail(buyer,applyDept,employee.getEnterpriseCode(),start,limit);
		// modified by liuyi 20100406
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start != null && limit != null)
			obj = remoteHead.getMRPMaterialDetail(buyer, applyDept, employee
					.getEnterpriseCode(), Integer.parseInt(start), Integer
					.parseInt(limit));
		else
			obj = remoteHead.getMRPMaterialDetail(buyer, applyDept, employee
					.getEnterpriseCode());
		if (null == obj.getList()) {
			List<Object> list = new ArrayList<Object>();
			obj.setList(list);
		}
		String str = JSONUtil.serialize(obj);
		// 如果查询返回结果为空，则替换为如下返回结果
		if (Constants.BLANK_STRING.equals(str) || null == str) {
			str = "{\"list\":[],\"totalCount\":null}";
		}
		// 返回结果
		write(str);
	}

	public void addGatherMaterialDetail() {
		// modify by fyyang 091214
		String detailIds = request.getParameter("saveDatail");
		String buyer = request.getParameter("buyer");
		gatherRemote.doGather(detailIds, buyer, employee.getEnterpriseCode(),
				employee.getWorkerCode());
		write(Constants.MODIFY_SUCCESS);
	}

	// public void addGatherMaterialDetail() throws JSONException {
	// String saveDatail = request.getParameter("saveDatail");
	// Object object = JSONUtil.deserialize(saveDatail);
	// if (object != null) {
	// if (List.class.isAssignableFrom(object.getClass())) {
	// // 如果是数组
	// List lst = (List) object;
	// this.checkGatherMaterialInfo(lst);
	// } else {
	// // 保存操作项目
	//    			
	// }
	// }
	// write(Constants.MODIFY_SUCCESS);
	// }
	//    
	// @SuppressWarnings("unchecked")
	// private void checkGatherMaterialInfo(List list) {
	// String buyer=request.getParameter("buyer"); //add by fyyang 090522
	// String materialId = "";
	// Double approvedQty = 0.00;
	// String requirementDetailIds = "";
	// int intLen = list.size();
	// for (int intCnt = 0; intCnt < intLen; intCnt++) {
	//			
	// if(intCnt >0 &&
	// materialId.equals(((String)((List)list.get(intCnt)).get(0)))) {
	//				
	// if(((List)list.get(intCnt)).get(1) != null) {
	// approvedQty = approvedQty + ((Double)((List)list.get(intCnt)).get(1));
	// }
	// requirementDetailIds += "," + (String )((List)list.get(intCnt)).get(2);
	// } else {
	// if(intCnt == 0) {
	// if(((List)list.get(intCnt)).get(0) != null) {
	// materialId = (String )((List)list.get(intCnt)).get(0);
	//						
	// }
	// if(((List)list.get(intCnt)).get(1) != null) {
	// // modified by liuyi 091111
	// // approvedQty = 0.00 + (Double )((List)list.get(intCnt)).get(1);
	// approvedQty = 0.00 +
	// Double.parseDouble(((List)list.get(intCnt)).get(1).toString());
	// }
	// if(((List)list.get(intCnt)).get(2) != null){
	// requirementDetailIds = (String )((List)list.get(intCnt)).get(2);
	// }
	// } else {
	// MrpJPlanGather entity = new MrpJPlanGather();
	// entity.setMaterialId(Long.valueOf(materialId));
	// entity.setAppliedQty(Double.valueOf(approvedQty));
	// entity.setRequirementDetailIds(requirementDetailIds);
	// entity.setGatherBy(employee.getWorkerCode());
	// entity.setEnterpriseCode(employee.getEnterpriseCode());
	// entity.setBuyer(buyer);//add 090522
	// gatherRemote.save(entity);
	// if(requirementDetailIds != null && !"".equals(requirementDetailIds)) {
	// String [] stringArray = requirementDetailIds.split(",");
	// for(int i = 0; i < stringArray.length; i++) {
	// String requirementDetailId = stringArray[i];
	// MrpJPlanRequirementDetail detailEntity =
	// remoteDetail.findById(Long.valueOf(requirementDetailId),
	// employee.getEnterpriseCode());
	// detailEntity.setIsGenerated("G");
	// remoteDetail.update(detailEntity);
	// System.out.println(requirementDetailId);
	// }
	// }
	// materialId = (String )((List)list.get(intCnt)).get(0);
	//				
	// if(((List)list.get(intCnt)).get(1) != null) {
	// // modified by liuyi 091111
	// // approvedQty = 0.00 + (Long )((List)list.get(intCnt)).get(1);
	// approvedQty = 0.00 +
	// Double.parseDouble(((List)list.get(intCnt)).get(1).toString());
	// } else {
	// approvedQty = 0.00;
	// }
	// requirementDetailIds = (String )((List)list.get(intCnt)).get(2);
	//					
	// }
	// }
	// if(intCnt == intLen-1) {
	// MrpJPlanGather gatherentity = new MrpJPlanGather();
	// gatherentity.setMaterialId(Long.valueOf(materialId));
	// gatherentity.setAppliedQty(Double.valueOf(approvedQty));
	// gatherentity.setRequirementDetailIds(requirementDetailIds);
	// gatherentity.setGatherBy(employee.getWorkerCode());
	// gatherentity.setEnterpriseCode(employee.getEnterpriseCode());
	// gatherentity.setBuyer(buyer);//add 090522
	// gatherRemote.save(gatherentity);
	// if(requirementDetailIds != null && !"".equals(requirementDetailIds)) {
	// String [] stringArray = requirementDetailIds.split(",");
	// for(int i = 0; i < stringArray.length; i++) {
	// String requirementDetailId = stringArray[i];
	// MrpJPlanRequirementDetail detailEntity =
	// remoteDetail.findById(Long.valueOf(requirementDetailId),
	// employee.getEnterpriseCode());
	// detailEntity.setIsGenerated("G");
	// remoteDetail.update(detailEntity);
	// System.out.println(requirementDetailId);
	// }
	// }
	// }
	// }
	//		
	// }

	/**
	 * 获取物料汇总明细
	 * 
	 * @throws JSONException
	 */
	public void getMaterialGatherDetail() throws JSONException {
		String flag=request.getParameter("flag"); //add by fyyang 20100505
		// add by fyyang 091222 汇总时间段
		String sdate = request.getParameter("sdate");
		String edate = request.getParameter("edate");
		
		String materialName = request.getParameter("materialName");
		String buyer = request.getParameter("buyer");
		PageObject obj = new PageObject();
		if (request.getParameter("start") != null
				&& request.getParameter("limit") != null) {
			/** 开始查询位置 */
			int start = Integer.parseInt(request.getParameter("start"));
			/** 查询行数 */
			int limit = Integer.parseInt(request.getParameter("limit"));
			
			//增加查询申报人、申报单位查询条件 employee.getWorkerCode(),employee.getDeptCode()
            //add by sychen 20100511 
			obj = gatherRemote.getMaterialGatherDetail(materialName, buyer,
					employee.getEnterpriseCode(), sdate, edate,flag,
					employee.getWorkerCode(),employee.getDeptCode(), start, limit);
		} else {
			obj = gatherRemote.getMaterialGatherDetail(materialName, buyer,
					employee.getEnterpriseCode(), sdate, edate,flag,
					employee.getWorkerCode(),employee.getDeptCode());
		}
		if (null == obj.getList()) {
			List<Object> list = new ArrayList<Object>();
			obj.setList(list);
		}
		String str = JSONUtil.serialize(obj);
		// 如果查询返回结果为空，则替换为如下返回结果
		if (Constants.BLANK_STRING.equals(str) || null == str) {
			str = "{\"list\":[],\"totalCount\":null}";
		}
		// 返回结果
		write(str);
	}

	/**
	 * 计划作废，即对已审批结束的申请单的物资进行作废处理 add by fyyang 090807
	 */
	public void blankOutPlanMaterial() {
		String requirementDetailId = request
				.getParameter("requirementDetailId");
		String cancelReason = request.getParameter("cancelReason");
		MrpJPlanRequirementDetail model = remoteDetail.findById(Long
				.parseLong(requirementDetailId), employee.getEnterpriseCode());
		model.setCancelReason(cancelReason);
		model.setIsUse("C"); // C表示已作废
		model.setCancelDate(new java.util.Date());// 作废时间 add by fyyang
		// 20100407
		remoteDetail.update(model);
		write("{success:true,msg:'作废成功！'}");
	}

	/**
	 * 获得已经执行过计划作废的物资列表 add by fyyang 090807
	 */
	public void getBlankOutMaterialDetail() throws JSONException {
		String applyDept = request.getParameter("applyDept");
		String wzName = request.getParameter("wzName");
		String ggSize = request.getParameter("ggSize");
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		PageObject obj = new PageObject();
		obj = remoteHead.getBlankOutMaterialDetail(applyDept, wzName, ggSize,
				employee.getEnterpriseCode(), start, limit);
		if (null == obj.getList()) {
			List<Object> list = new ArrayList<Object>();
			obj.setList(list);
		}
		String str = JSONUtil.serialize(obj);
		if (Constants.BLANK_STRING.equals(str) || null == str) {
			str = "{\"list\":[],\"totalCount\":null}";
		}
		write(str);
	}

	/**
	 * 取消作废 即对已进行作废处理的物资取消作废
	 */
	public void CancelBlankOutPlanMaterial() {
		String requirementDetailId = request
				.getParameter("requirementDetailId");
		MrpJPlanRequirementDetail model = remoteDetail.findById(Long
				.parseLong(requirementDetailId), employee.getEnterpriseCode());
		model.setCancelReason("");
		model.setIsUse("Y"); // 取消作废，改为Y状态
		remoteDetail.update(model);
		write("{success:true,msg:'取消作废成功！'}");
	}

	/**
	 * add by liuyi 091104 批量修改需求计划汇总中的采购员
	 */
	public void chooserBuyer() {
		String ids = request.getParameter("ids");
		String buyer = request.getParameter("buyer");

		gatherRemote.chooserBuyer(ids, buyer);
	}

	/**
	 * add by liuyi 091107 汇总退回
	 */
	public void gatherTurnPlan() {
		String backReason = request.getParameter("backReason");
		String backId = request.getParameter("backId");
		// if(backId != null)
		// {
		// MrpJPlanGather gather =
		// gatherRemote.findById(Long.parseLong(backId));
		// gather.setIsUse("N");
		// gather.setIsReturn("Y");
		// gather.setReturnReason(backReason);
		// gatherRemote.update(gather);
		// }
		gatherRemote.gatherBack(backId, backReason);
	}
}
