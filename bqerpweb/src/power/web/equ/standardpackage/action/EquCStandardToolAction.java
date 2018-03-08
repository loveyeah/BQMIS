package power.web.equ.standardpackage.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;


import power.ejb.equ.standardpackage.EquCStandardTools;
import power.ejb.equ.standardpackage.EquCStandardToolsFacadeRemote;

import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class EquCStandardToolAction extends AbstractAction {

	protected int start;
	protected int limit;

//	private String ids;
	private String woCode;
	private String operationStep;

//	private EquCStandardTools baseInfo;

	protected EquCStandardToolsFacadeRemote remote;

	public EquCStandardToolAction() {
		remote = (EquCStandardToolsFacadeRemote) factory
				.getFacadeRemote("EquCStandardToolsFacade");

	}

	/**
	 * 增加新记录
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void addEquCStandardTools() {
//		try {
//
//			baseInfo.setEnterprisecode(employee.getEnterpriseCode());
//
//			if (remote.save(baseInfo)) {
//
//				write("{success:true,msg:'添加成功'}");
//			} else
//				write("{success:false,msg:'添加失败'}");
//		} catch (Exception e) {
//			write("{success:false,msg:'添加11失败'}");
//		}
		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<EquCStandardTools> addList = new ArrayList<EquCStandardTools>();
			List<EquCStandardTools> updateList = new ArrayList<EquCStandardTools>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {
				
				
				String operationStep =null;
				String planToolCode =null;
				String planLocationId =null;
				String planToolQty =null;
				String planToolHrs =null;
				String planToolPrice =null;
				String planToolDescription=null;
				String id=null;
//				String orderby=null;
				
				String woCode=data.get("baseInfo.woCode").toString();
				
	
		
			
				if(data.get("baseInfo.operationStep")!=null){
					operationStep = data.get("baseInfo.operationStep").toString();
				}
				if(data.get("baseInfo.planToolCode")!=null){
					planToolCode  = data.get("baseInfo.planToolCode").toString();
				}
			
				if(data.get("baseInfo.planLocationId")!=null){
					planLocationId = data.get("baseInfo.planLocationId").toString();
				}
				if(data.get("baseInfo.planToolQty")!=null){
					planToolQty = data.get("baseInfo.planToolQty").toString();
					}
				if(data.get("baseInfo.planToolHrs")!=null){
					planToolHrs = data.get("baseInfo.planToolHrs").toString();
					}

				if(data.get("baseInfo.planToolPrice")!=null){
					planToolPrice = data.get("baseInfo.planToolPrice").toString();
					}
				if(data.get("baseInfo.planToolDescription")!=null){
					planToolDescription = data.get("baseInfo.planToolDescription").toString();
					}
				if(data.get("baseInfo.id")!=null){
					id = data.get("baseInfo.id").toString();
					}

				EquCStandardTools model = new EquCStandardTools();
				// 增加
				if (id==null) {
					model.setWoCode(woCode);
					model.setOperationStep(operationStep);
					if (planToolQty!=null&&!planToolQty.equals("")) {
						model.setPlanToolQty(Long.parseLong(planToolQty));
					}
					model.setPlanToolCode(planToolCode);
					model.setPlanLocationId(planLocationId);
					
					if (planToolHrs!=null&&!planToolHrs.equals("")) {
						model.setPlanToolHrs(Double.parseDouble(planToolHrs));
					}
					if (planToolPrice!=null&&!planToolPrice.equals("")) {
						model.setPlanToolPrice(Double.parseDouble(planToolPrice));
					}
					model.setPlanToolDescription(planToolDescription);
					model.setEnterprisecode(employee.getEnterpriseCode());

					addList.add(model);
				} else {
					model = remote.findById(Long.parseLong(id));
					
				
					if (planToolQty!=null&&!planToolQty.equals("")) {
						model.setPlanToolQty(Long.parseLong(planToolQty));
					}
					model.setPlanToolCode(planToolCode);
					model.setPlanLocationId(planLocationId);
					
					if (planToolHrs!=null&&!planToolHrs.equals("")) {
						model.setPlanToolHrs(Double.parseDouble(planToolHrs));
					}
					if (planToolPrice!=null&&!planToolPrice.equals("")) {
						model.setPlanToolPrice(Double.parseDouble(planToolPrice));
					}
					model.setPlanToolDescription(planToolDescription);
					
					updateList.add(model);
				}
			}

		
			if (addList.size() > 0) 			
					remote.save(addList);
			

			if (updateList.size() > 0) 
				
					remote.update(updateList);
			
			if (deleteIds != null && !deleteIds.trim().equals("")) 
				
					remote.delete(deleteIds);
				

			

			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
	}
//
//	/**
//	 * 变更记录
//	 * 
//	 */
//	public void updateEquCStandardTools() {
//		try {
//			
//			EquCStandardTools model = new EquCStandardTools();
//			model = remote.findById(baseInfo.getId());
//			model.setWoCode(baseInfo.getWoCode());
//			model.setOperationStep(baseInfo.getOperationStep());
//			model.setPlanLocationId(baseInfo.getPlanLocationId());
//			model.setPlanToolCode(baseInfo.getPlanToolCode());
//			model.setPlanToolDescription(baseInfo.getPlanToolDescription());
//			model.setPlanToolHrs(baseInfo.getPlanToolHrs());
//			model.setPlanToolPrice(baseInfo.getPlanToolPrice());
//			model.setPlanToolQty(baseInfo.getPlanToolQty());
//
//			model.setEnterprisecode(employee.getEnterpriseCode());
//			remote.update(model);
//			write("{success:true,msg:'更新成功'}");
//		} catch (Exception e) {
//			write("{success:false,msg:'更新失败'}");
//		}
//	}
//
//	/**
//	 * 删除单条记录
//	 * 
//	 */
//	public void deleteEquCStandardTool() {
//		try {
//			remote.delete(Long.parseLong(ids));
//			write("{success:true,msg:'删除成功'}");
//		} catch (Exception e) {
//			write("{success:false,msg:'删除失败'}");
//		}
//	}
//
//	/**
//	 * 批量删除记录
//	 * 
//	 */
//	public void deleteEquCStandardTools() {
//		try {
//			remote.delete(ids);
//			write("{success:true,msg:'删除成功'}");
//		} catch (Exception e) {
//			write("{success:false,msg:'删除失败'}");
//		}
//	}

	/**
	 * 获取记录列表
	 * 
	 * @throws JSONException
	 * 
	 */
	public void findAll() throws JSONException {
		// String woCode=request.getParameter("woCode");
		// String operationStep=request.getParameter("operationStep");

		PageObject obj = remote.findAll(employee.getEnterpriseCode(), woCode,
				operationStep, start, limit);
		write(JSONUtil.serialize(obj));
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getWoCode() {
		return woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	public String getOperationStep() {
		return operationStep;
	}

	public void setOperationStep(String operationStep) {
		this.operationStep = operationStep;
	}

//	public String getIds() {
//		return ids;
//	}
//
//	public void setIds(String ids) {
//		this.ids = ids;
//	}

//	public EquCStandardTools getBaseInfo() {
//		return baseInfo;
//	}
//
//	public void setBaseInfo(EquCStandardTools baseInfo) {
//		this.baseInfo = baseInfo;
//	}

}
