package power.web.equ.standardpackage.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.standardpackage.EquCStandardMainmat;
import power.ejb.equ.standardpackage.EquCStandardMainmatFacadeRemote;

import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class EquCStandardMainmatAction extends AbstractAction {
	private EquCStandardMainmatFacadeRemote remote;
	private int start;
	private int limit;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public EquCStandardMainmatAction() {
		remote = (EquCStandardMainmatFacadeRemote) factory
				.getFacadeRemote("EquCStandardMainmatFacade");
	}

	/**
	 * 增加新记录
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void saveEquCStandardMainmat() {
		
		try {
			String Updatestr = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object updateObj = JSONUtil.deserialize(Updatestr);
			List<EquCStandardMainmat> addList = new ArrayList<EquCStandardMainmat>();
			List<EquCStandardMainmat> updateList = new ArrayList<EquCStandardMainmat>();
			List<Map> list = (List<Map>) updateObj;
			for (Map data : list) {
				String id = null;
				String woCode = data.get("woCode").toString();
				String operationStep = null;
				String materialId = null;
				String planItemQty = null;
				String planMaterialPrice = null;
				String planVendor = null;
				String orderBy = null;
				String directReq = null;
				if (data.get("operationStep") != null) {
					operationStep = data.get("operationStep")
							.toString();
				}
				if (data.get("materialId") != null) {
					materialId = data.get("materialId").toString();
				}

				if (data.get("planItemQty") != null) {
					planItemQty = data.get("planItemQty").toString();
				}
				if (data.get("planMaterialPrice") != null) {
					planMaterialPrice = data.get("planMaterialPrice")
							.toString();
				}
				if (data.get("planVendor") != null) {
					planVendor = data.get("planVendor").toString();
				}

				if (data.get("orderBy") != null) {
					orderBy = data.get("orderBy").toString();
				}
				/*if (data.get("baseInfo.directReq") != null) {
					directReq = data.get("baseInfo.directReq").toString();
				}
*/
				if (data.get("id") != null) {
					id = data.get("id").toString();
				}

				EquCStandardMainmat model = new EquCStandardMainmat();
				// 增加
				if (id == null||id.equals("")) {
					model.setWoCode(woCode);
					model.setOperationStep(operationStep);
					if (materialId != null && !"".equals(materialId)) {
						model.setMaterialId(Long.parseLong(materialId));
					}
					if (planItemQty != null && !planItemQty.equals("")) {
						model.setPlanItemQty(Double.parseDouble(planItemQty));
					}
					if (planMaterialPrice != null
							&& !planMaterialPrice.equals("")) {
						model.setPlanMaterialPrice(Double
								.parseDouble(planMaterialPrice));
					}
					if (planVendor != null && !"".equals(planVendor)) {
						model.setPlanVendor(Long.parseLong(planVendor));
					}
					if (orderBy != null && !"".equals(orderBy)) {
						model.setOrderBy(Long.parseLong(orderBy));
					}
//					model.setDirectReq(directReq);
					model.setEnterprisecode(employee.getEnterpriseCode());
					model.setIfUse("Y");

					addList.add(model);
				} else {
					model = remote.findById(Long.parseLong(id));
					if (materialId != null && !"".equals(materialId)) {
						model.setMaterialId(Long.parseLong(materialId));
					}
					if (planItemQty != null && !planItemQty.equals("")) {
						model.setPlanItemQty(Double.parseDouble(planItemQty));
					}
					if (planMaterialPrice != null
							&& !planMaterialPrice.equals("")) {
						model.setPlanMaterialPrice(Double
								.parseDouble(planMaterialPrice));
					}
					if (planVendor != null && !"".equals(planVendor)) {
						model.setPlanVendor(Long.parseLong(planVendor));
					}
					if (orderBy != null && !"".equals(orderBy)) {
						model.setOrderBy(Long.parseLong(orderBy));
					}

					if (planMaterialPrice != null
							&& !planMaterialPrice.equals("")) {
						model.setPlanMaterialPrice(Double
								.parseDouble(planMaterialPrice));
					}
//					model.setDirectReq(directReq);
					updateList.add(model);
				}
			}

			if (addList.size() > 0)

				remote.saveEquCStandardMainmat(addList);

			if (updateList.size() > 0)

				remote.update(updateList);

			if (deleteIds != null && !deleteIds.trim().equals(""))

				remote.deleteMainmat(deleteIds);

			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}

	}

	
	/* *//**
	 * 删除记录
	 *//*
	
	 public void deleteEquCStandardMainmat() {
	 try {
		 String ids = request.getParameter("ids");
	 if (remote.deleteMainmat(ids))
	 write("{success:true,msg:'删除成功'}");
	 else
	 write("{success:false,msg:'删除失败'}");
	 } catch (Exception e) {
	 write("{success:false,msg:'删除失败'}");
	 }
	 }*/

	/**
	 * 取列表
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getEquCStandardMainmatList() throws JSONException {
		 String woCode = request.getParameter("woCode");
		 String opCode = request.getParameter("opCode");
		 PageObject obj = remote.findAllmainmat(employee.getEnterpriseCode(), woCode,
		 opCode, start, limit);
		 write(JSONUtil.serialize(obj));
	}


	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

}
