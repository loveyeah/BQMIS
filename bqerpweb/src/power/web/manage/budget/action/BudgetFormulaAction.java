package power.web.manage.budget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmCItem;
import power.ejb.manage.budget.CbmCItemFacadeRemote;
import power.ejb.manage.budget.CbmCItemFormula;
import power.ejb.manage.budget.CbmCItemFormulaFacadeRemote;
import power.ejb.manage.budget.form.CbmCItemFormulaForm;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class BudgetFormulaAction extends AbstractAction {
	private CbmCItemFormulaFacadeRemote remote;
	private CbmCItemFacadeRemote itemfinaceRemote;
	private int limit;
	private int start;

	/**
	 * 构造方法
	 */
	public BudgetFormulaAction() {
		remote = (CbmCItemFormulaFacadeRemote) factory
				.getFacadeRemote("CbmCItemFormulaFacade");
		itemfinaceRemote = (CbmCItemFacadeRemote) factory
		.getFacadeRemote("CbmCItemFacade");
	}

	/** 获取预算指标公式列表* */
	public void getCbmCItemFormulaList() throws JSONException {
		String itemId = request.getParameter("itemId");
		List<CbmCItemFormulaForm> obj = remote.findAll(employee
				.getEnterpriseCode(), itemId);
		write(JSONUtil.serialize(obj));

	}

	/** 保存对预算指标公式的修改* */
	@SuppressWarnings("unchecked")
	public void saveCbmCItemFormula() throws JSONException {
		String ItemId=request.getParameter("itemId");
		String itemExplain=request.getParameter("itemExplain");
		itemExplain=itemExplain.substring(2, itemExplain.length()-2);
		try {
			String strUpdate = request.getParameter("isUpdate");
			Object obj = JSONUtil.deserialize(strUpdate);
			List<CbmCItemFormula> addList = new ArrayList<CbmCItemFormula>();
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
				CbmCItemFormula model = new CbmCItemFormula();
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
			CbmCItem bubgetItem=itemfinaceRemote.findById(Long.parseLong(ItemId));
			if(itemExplain==null||"[]".equals(itemExplain)){
				itemExplain="";
			}
			bubgetItem.setItemExplain(itemExplain);
			bubgetItem.setComeFrom("2");
			itemfinaceRemote.update(bubgetItem);
			write("{success: true,msg:'保 存 成 功 ！'}");

		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保 存 失 败 ！'}");
		}
	}

	/** 模糊查询获得指标信息* */
	public void getAllStatItemList() throws JSONException {
		String argFuzzy = request.getParameter("argFuzzy");
		PageObject obj = remote.findAllStatItem(argFuzzy, start, limit);
		write(JSONUtil.serialize(obj));
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
}
