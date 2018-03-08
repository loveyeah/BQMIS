package power.web.manage.budget.action;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmCCostFormula;
import power.ejb.manage.budget.CbmCCostFormulaFacadeRemote;
import power.ejb.manage.budget.form.CbmCItemFormulaForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class CostFormulaAction extends AbstractAction {
	CbmCCostFormulaFacadeRemote remote;
	private int limit;
	private int start;

	public CostFormulaAction() {
		remote = (CbmCCostFormulaFacadeRemote) factory
				.getFacadeRemote("CbmCCostFormulaFacade");
	}
	
	/** 保存对成本指标公式的修改* */
	@SuppressWarnings("unchecked")
	public void saveCbmCostFormula() throws JSONException {
		try {
			String strUpdate = request.getParameter("isUpdate");
			Object obj = JSONUtil.deserialize(strUpdate);
			List<CbmCCostFormula> addList = new ArrayList<CbmCCostFormula>();
			List<Map> list = (List<Map>) obj;
			for (Map data : list) {
				Long itemId = null;
				Long formulaNo = null;
				String formulaContent = null;
				String fornulaType = null;
				if (data.get("itemId") != null) {
					itemId = Long.parseLong(data.get("itemId").toString());
				}
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
				CbmCCostFormula model = new CbmCCostFormula();
				model.setItemId(itemId);
				model.setFormulaNo(formulaNo);
				model.setEnterpriseCode(employee.getEnterpriseCode());
				model.setFormulaContent(formulaContent);
				model.setFornulaType(fornulaType);
				model.setIsUse("Y");
				addList.add(model);
			}
			String itemId = request.getParameter("itemId");
			remote.delete(itemId);
			if (addList.size() > 0)
				remote.save(addList);
			write("{success: true,msg:'保 存 成 功 ！'}");

		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保 存 失 败 ！'}");
		}
	}
	
	/** 模糊查询获得指标信息* */
	public void getAllCostItemList() throws JSONException {
		String argFuzzy = request.getParameter("argFuzzy");
		PageObject obj = remote.findAllCostItem(argFuzzy, start, limit);
		write(JSONUtil.serialize(obj));
	}
	
	/** 获取成本指标公式列表* */
	public void getCbmCostFormulaList() throws JSONException {
		String itemId = request.getParameter("itemId");
		List<CbmCItemFormulaForm> obj = remote.findAll(employee
				.getEnterpriseCode(), itemId);
		write(JSONUtil.serialize(obj));
	}
	
}
