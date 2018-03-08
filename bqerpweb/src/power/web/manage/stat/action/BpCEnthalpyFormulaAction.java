package power.web.manage.stat.action;

import power.ejb.manage.stat.BpCEnthalpyFormula;
import power.ejb.manage.stat.BpCEnthalpyFormulaFacadeRemote;
import power.ejb.manage.stat.BpCStatItem;
import power.ejb.manage.stat.BpCStatItemFacadeRemote;
import power.ejb.manage.stat.form.BpCEnthalpyFormulaForm;

import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class BpCEnthalpyFormulaAction extends AbstractAction {
	private BpCEnthalpyFormulaFacadeRemote remote;
	private BpCEnthalpyFormula statItem;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public BpCEnthalpyFormulaAction() {
		remote = (BpCEnthalpyFormulaFacadeRemote) factory
				.getFacadeRemote("BpCEnthalpyFormulaFacade");
	}

	/**
	 * 增加新记录
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void saveBpCEnthalpyFormula() {
		try {
			BpCStatItemFacadeRemote statRemote = (BpCStatItemFacadeRemote) factory
					.getFacadeRemote("BpCStatItemFacade");
			BpCStatItem statItemUpdate = statRemote.findById(statItem
					.getItemCode());

			if (!"3".equals(statItemUpdate.getDataCollectWay())
					|| !"6".equals(statItemUpdate.getDeriveDataType())) {
				statItemUpdate.setDataCollectWay("3");
				statItemUpdate.setDeriveDataType("6");
				
			}

			if (remote.findById(statItem.getItemCode()) == null) {
				statItem.setEnterpriseCode(employee.getEnterpriseCode());
				remote.save(statItem);
			}

			else
				remote.update(statItem);
			
			
			statRemote.update(statItemUpdate);
			write("{success:true ,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
	}

	public void getBpCEnthalpyFormula() throws JSONException {
		String itemCode = request.getParameter("itemCode");
		BpCEnthalpyFormulaForm obj = new BpCEnthalpyFormulaForm();
		obj = remote.getBpCEnthalpyFormula(itemCode, employee
				.getEnterpriseCode());

		write(JSONUtil.serialize(obj));

	}

	public void deleteBpCEnthalpyFormula() throws JSONException {
		String itemCode = request.getParameter("itemCode");
		remote.delete(remote.findById(itemCode));
	}

	public BpCEnthalpyFormula getStatItem() {
		return statItem;
	}

	public void setStatItem(BpCEnthalpyFormula statItem) {
		this.statItem = statItem;
	}

}
