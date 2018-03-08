package power.web.manage.plan.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.plan.BpCPlanFormula;
import power.ejb.manage.plan.BpCPlanFormulaFacadeRemote;
import power.ejb.manage.plan.BpCPlanFormulaId;
import power.ejb.manage.plan.form.PlanItemFormula;

import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class FormulaAction extends AbstractAction {
	private BpCPlanFormulaFacadeRemote remote;
	private int start;
	private int limit;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public FormulaAction() {
		remote = (BpCPlanFormulaFacadeRemote) factory
				.getFacadeRemote("BpCPlanFormulaFacade");
	}

	/**
	 * 增加新记录
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void saveBpCPlanFormula() {
		try {
			String str = request.getParameter("isUpdate");
			String curItemCode = request.getParameter("curItemCode");
			Object obj = JSONUtil.deserialize(str);
			List<BpCPlanFormula> addList = new ArrayList<BpCPlanFormula>();
			List<Map> list = (List<Map>) obj;

			for (Map data : list) {
				Long formulaNo = null;
				String formulaContent = null;
				String fornulaType = null;
				if (data.get("formulaNo") != null) {
					formulaNo = Long
							.parseLong(data.get("formulaNo").toString());
				}
				if (data.get("formulaContent") != null) {
					formulaContent = data.get("formulaContent").toString();
				}
				if (data.get("fornulaType") != null) {
					fornulaType = data.get("fornulaType").toString();
				}
				BpCPlanFormula model = new BpCPlanFormula();
				BpCPlanFormulaId id = new BpCPlanFormulaId();
				id.setItemCode(curItemCode);
				id.setFormulaNo(formulaNo);
				model.setId(id);
				model.setEnterpriseCode(employee.getEnterpriseCode());
				model.setFormulaContent(formulaContent);
				model.setFornulaType(fornulaType);
				addList.add(model);
			}
			remote.delete(curItemCode);
			if (addList.size() > 0)
				remote.save(addList);

			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
	}

	public void getBpCPlanFormulaList() throws JSONException {
		String itemCode = request.getParameter("itemCode");
		List<PlanItemFormula> obj = remote.findAll(
				employee.getEnterpriseCode(), itemCode);

		write(JSONUtil.serialize(obj));
	}

	public void getPlanItemList() throws JSONException {

		String argFuzzy = request.getParameter("argFuzzy");

		PageObject obj = remote.findAllPlanItem(argFuzzy, start, limit);

		write(JSONUtil.serialize(obj));
	}

	// ******************************************get/set变量方法******************************************

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
