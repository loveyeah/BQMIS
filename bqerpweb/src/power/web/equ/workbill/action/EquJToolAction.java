package power.web.equ.workbill.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.workbill.EquJTools;
import power.ejb.equ.workbill.EquJToolsFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class EquJToolAction extends AbstractAction {
	protected int start;
	protected int limit;

	// private String ids;
	private String woCode;
	private String operationStep;

	// private EquCStandardTools baseInfo;

	protected EquJToolsFacadeRemote remote;

	public EquJToolAction() {
		remote = (EquJToolsFacadeRemote) factory
				.getFacadeRemote("EquJToolsFacade");

	}

	/**
	 * 增加新记录
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void saveEquJTools() {

		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			String woCode = request.getParameter("woCode");
			Object obj = JSONUtil.deserialize(str);

			List<EquJTools> addList = new ArrayList<EquJTools>();
			List<EquJTools> updateList = new ArrayList<EquJTools>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {

				String operationStep = null;
				String planToolNum = null;
				String planLocationId = null;
				String planToolQty = null;
				String planToolHrs = null;
				String planToolPrice = null;
				String planToolDescription = null;

				String factToolNum = null;
				String factLocationId = null;
				String factToolQty = null;
				String factToolHrs = null;
				String factToolPrice = null;
				String factToolDescription = null;

				String id = null;
				// String orderby=null;

//				String woCode = data.get("baseInfo.woCode").toString();

				if (data.get("baseInfo.operationStep") != null) {
					operationStep = data.get("baseInfo.operationStep")
							.toString();
				}
				if (data.get("baseInfo.planToolNum") != null)
					planToolNum = data.get("baseInfo.planToolNum").toString();

				if (data.get("baseInfo.planLocationId") != null)
					planLocationId = data.get("baseInfo.planLocationId")
							.toString();

				if (data.get("baseInfo.planToolQty") != null)
					planToolQty = data.get("baseInfo.planToolQty").toString();

				if (data.get("baseInfo.planToolHrs") != null)
					planToolHrs = data.get("baseInfo.planToolHrs").toString();

				if (data.get("baseInfo.planToolPrice") != null)
					planToolPrice = data.get("baseInfo.planToolPrice")
							.toString();

				if (data.get("baseInfo.planToolDescription") != null)
					planToolDescription = data.get(
							"baseInfo.planToolDescription").toString();

				if (data.get("baseInfo.factToolNum") != null)
					factToolNum = data.get("baseInfo.factToolNum").toString();

				if (data.get("baseInfo.factLocationId") != null)
					factLocationId = data.get("baseInfo.factLocationId")
							.toString();

				if (data.get("baseInfo.factToolQty") != null)
					factToolQty = data.get("baseInfo.factToolQty").toString();

				if (data.get("baseInfo.factToolHrs") != null)
					factToolHrs = data.get("baseInfo.factToolHrs").toString();

				if (data.get("baseInfo.factToolPrice") != null)
					factToolPrice = data.get("baseInfo.factToolPrice")
							.toString();

				if (data.get("baseInfo.factToolDescription") != null)
					factToolDescription = data.get(
							"baseInfo.factToolDescription").toString();

				if (data.get("baseInfo.id") != null)
					id = data.get("baseInfo.id").toString();

				EquJTools model = new EquJTools();
				// 增加
				if (id == null) {
					model.setWoCode(woCode);
					model.setOperationStep(operationStep);
					if (planToolQty != null && !planToolQty.equals("")) {
						model.setPlanToolQty(Long.parseLong(planToolQty));
					}
					model.setPlanToolNum(planToolNum);
					model.setPlanLocationId(planLocationId);

					if (planToolHrs != null && !planToolHrs.equals("")) {
						model.setPlanToolHrs(Double.parseDouble(planToolHrs));
					}
					if (planToolPrice != null && !planToolPrice.equals("")) {
						model.setPlanToolPrice(Double
								.parseDouble(planToolPrice));
					}
					model.setPlanToolDescription(planToolDescription);
					model.setEnterprisecode(employee.getEnterpriseCode());

					addList.add(model);
				} else {
					model = remote.findById(Long.parseLong(id));

					if (planToolQty != null && !planToolQty.equals(""))
						model.setPlanToolQty(Long.parseLong(planToolQty));

					if (planToolNum != null && !planToolNum.equals(""))
						model.setPlanToolNum(planToolNum);

					if (planLocationId != null && !planLocationId.equals(""))
						model.setPlanLocationId(planLocationId);

					if (planToolHrs != null && !planToolHrs.equals(""))
						model.setPlanToolHrs(Double.parseDouble(planToolHrs));

					if (planToolPrice != null && !planToolPrice.equals(""))
						model.setPlanToolPrice(Double
								.parseDouble(planToolPrice));

					if (planToolDescription != null
							&& !planToolDescription.equals(""))
						model.setPlanToolDescription(planToolDescription);
					
					if (factToolQty != null && !factToolQty.equals(""))
						model.setFactToolQty(Long.parseLong(factToolQty));

					if (factToolNum != null && !factToolNum.equals(""))
						model.setFactToolNum(factToolNum);

					if (factLocationId != null && !factLocationId.equals(""))
						model.setFactLocationId(factLocationId);

					if (factToolHrs != null && !factToolHrs.equals(""))
						model.setFactToolHrs(Double.parseDouble(factToolHrs));

					if (factToolPrice != null && !factToolPrice.equals(""))
						model.setFactToolPrice(Double
								.parseDouble(factToolPrice));

					if (factToolDescription != null
							&& !factToolDescription.equals(""))
						model.setFactToolDescription(factToolDescription);


					updateList.add(model);
				}
			}

			if ((addList.size() > 0 || updateList.size() > 0)
					|| (deleteIds != null && !deleteIds.trim().equals(""))) {
				remote.save(addList, updateList, deleteIds);
			}

			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
	}

	/**
	 * 获取记录列表
	 * 
	 * @throws JSONException
	 * 
	 */
	public void findAll() throws JSONException {
		 String woCode=request.getParameter("woCode");
		 String operationStep=request.getParameter("operationStep");

		PageObject obj = remote.findAll(employee.getEnterpriseCode(), woCode,
				operationStep, start, limit);
		write(JSONUtil.serialize(obj));
	}
	public void getEquCTools() throws JSONException {
		 String woCode=request.getParameter("woCode");
		 String operationStep=request.getParameter("operationStep");

		PageObject obj = remote.getEquCTools(employee.getEnterpriseCode(), woCode,
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



}
