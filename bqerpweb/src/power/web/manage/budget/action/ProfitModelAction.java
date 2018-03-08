package power.web.manage.budget.action;

import java.util.List;

import power.ejb.comm.TreeNode;
import power.ejb.manage.budget.CbmCModel;
import power.ejb.manage.budget.CbmCModelFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class ProfitModelAction extends AbstractAction {
	CbmCModelFacadeRemote remote;
	CbmCModel cbModel;

	public ProfitModelAction() {
		remote = (CbmCModelFacadeRemote) factory
				.getFacadeRemote("CbmCModelFacade");
	}

	public void findModelTreeList() throws JSONException {
		String pid = request.getParameter("pid");
		if (pid.equals("00")) {
			String str = "[{'id' :'6666666666','text':'正向（利润）预测','leaf' :false,'code' :'1','description' : 'N'},"
					+ "{'id' :'7777777777','text':'反向（电量）预测','leaf' :false,'code' :'2','description' : 'N'},"
					+ "{'id' :'8888888888','text':'反向（固本）预测','leaf' :false,'code' :'3','description' : 'N'},"
					+ "{'id' :'9999999999','text':'反向（变本）预测','leaf' :false,'code' :'4','description' : 'N'}]";
			write(str);
		} else {
			List<TreeNode> list = remote.findModelTreeList(pid, employee
					.getEnterpriseCode());
			if (list != null)
				write(JSONUtil.serialize(list));
			else
				write("[]");
		}
	}

	/**
	 * 增加修改指标信息
	 * 
	 * @throws JSONException
	 */
	public void saveOrupdateModelItem() throws JSONException {
		String method = request.getParameter("method");
		String isItem = request.getParameter("isItem");
		String DaddyItemId = request.getParameter("DaddyItemId");
		CbmCModel cModel = null;

		// 非指标
		if (isItem.equals("N")) {
			if (method.equals("add")) {
				cModel = new CbmCModel();
				cModel.setDaddyItemId(Long.parseLong(DaddyItemId));
				cModel.setEnterpriseCode(employee.getEnterpriseCode());
				cModel.setModelType(cbModel.getModelType());
				cModel.setModelItemName(cbModel.getModelItemName());
				cModel.setIsItem(isItem);
				remote.save(cModel);
			} else {
				CbmCModel entity = remote.findById(cbModel.getModelItemId());
				entity.setDisplayNo(cbModel.getDisplayNo());
				entity.setModelItemName(cbModel.getModelItemName());
				remote.update(entity);

			}
		}
		// 指标
		else {
			if (method.equals("add")) {
				String itemName = request.getParameter("itemName");
				cbModel.setDaddyItemId(Long.parseLong(DaddyItemId));
				cbModel.setIsItem(isItem);
				cbModel.setEnterpriseCode(employee.getEnterpriseCode());
				cbModel.setModelItemName(itemName);
				remote.save(cbModel);
			} else {
				CbmCModel entity = remote.findById(cbModel.getModelItemId());
				String itemName = request.getParameter("itemName");
				cbModel.setModelItemId(entity.getModelItemId());
				cbModel.setModelItemCode(entity.getModelItemCode());
				cbModel.setDaddyItemId(entity.getDaddyItemId());
				cbModel.setIsItem(entity.getIsItem());
				cbModel.setModelItemName(itemName);
				cbModel.setEnterpriseCode(entity.getEnterpriseCode());
				cbModel.setModelOrder(entity.getModelOrder());
				cbModel.setIsUse(entity.getIsUse());
				remote.update(cbModel);
			}
		}
//		write("{success:true,msg:'操作成功!'}");
	}

	// 删除
	public void delModelItem() {
		String modelId = request.getParameter("id");
		CbmCModel entity = remote.findById(Long.parseLong(modelId));
		remote.delete(entity);
	}

	// 指标信息
	public void getModelItemInfo() throws NumberFormatException, JSONException {
		String modelId = request.getParameter("id");
		write(JSONUtil.serialize(remote.findById(Long.parseLong(modelId))));
	}

	//所有指标
	public void getAllmodelItemList() throws JSONException{
		String argFuzzy = request.getParameter("argFuzzy");
		write(JSONUtil.serialize(remote.findAll(argFuzzy)));
	}
	public CbmCModel getCbModel() {
		return cbModel;
	}

	public void setCbModel(CbmCModel cbModel) {
		this.cbModel = cbModel;
	}
}
