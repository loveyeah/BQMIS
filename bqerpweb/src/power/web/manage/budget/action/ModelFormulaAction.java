package power.web.manage.budget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.manage.budget.CbmCModelFormula;
import power.ejb.manage.budget.CbmCModelFormulaFacadeRemote;
import power.ejb.manage.budget.form.ModelFormulaForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class ModelFormulaAction extends AbstractAction {
	CbmCModelFormulaFacadeRemote remote;

	public ModelFormulaAction() {
		remote = (CbmCModelFormulaFacadeRemote) factory
				.getFacadeRemote("CbmCModelFormulaFacade");
	}

	@SuppressWarnings("unchecked")
	public void saveOrupdateModelFormula() throws JSONException {
		
		String modelFormulaId = request.getParameter("modelItemId");
		String strUpdate = request.getParameter("isUpdate");
		Object obj = JSONUtil.deserialize(strUpdate);
		List<CbmCModelFormula> addList = new ArrayList<CbmCModelFormula>();
		List<Map> list = (List<Map>) obj;
		for (Map data : list) {
//			Long modelFormulaId = null;
//			Long modelItemId = null;
			Long formulaNo = null;
			String formulaContent = null;
			String fornulaType = null;
//			if (data.get("modelItemId") != null) {
//				modelItemId = Long.parseLong(data.get("modelItemId").toString());
//			}
			if (data.get("formulaNo") != null) {
				formulaNo = Long.parseLong(data.get("formulaNo").toString());
			}
			if (data.get("formulaContent") != null) {
				formulaContent = data.get("formulaContent").toString();
			}
			if (data.get("fornulaType") != null) {
				fornulaType = data.get("fornulaType").toString();
			}
			CbmCModelFormula model = new CbmCModelFormula();
			model.setModelItemId(Long.parseLong(modelFormulaId));
			model.setFormulaNo(formulaNo);
			model.setEnterpriseCode(employee.getEnterpriseCode());
			model.setFormulaContent(formulaContent);
			model.setFornulaType(fornulaType);
			model.setIsUse("Y");
			addList.add(model);
		}
		 remote.delete(modelFormulaId);
		 if (addList.size() > 0)
		 remote.save(addList);
	}

	public void getModelFormulaList() throws JSONException{
		String modelItemId = request.getParameter("modelItemId");
		List<ModelFormulaForm> obj = remote.findAll(employee.getEnterpriseCode(), modelItemId);
		write(JSONUtil.serialize(obj));
	}
}
