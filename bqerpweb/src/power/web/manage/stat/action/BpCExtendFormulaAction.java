package power.web.manage.stat.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.BpCExtendFormula;
import power.ejb.manage.stat.BpCExtendFormulaFacadeRemote;
import power.ejb.manage.stat.BpCExtendFormulaId;
import power.ejb.manage.stat.BpCStatItem;
import power.ejb.manage.stat.BpCStatItemFacadeRemote;

import power.ejb.manage.stat.form.StatItemFormula;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class BpCExtendFormulaAction extends AbstractAction {
	private BpCExtendFormulaFacadeRemote remote;
	private int start;
	private int limit;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public BpCExtendFormulaAction() {
		remote = (BpCExtendFormulaFacadeRemote) factory
				.getFacadeRemote("BpCExtendFormulaFacade");
	}
	
	public void checkFormula()
	{
		String formulaContent = request.getParameter("formulaContent");
		boolean result = remote.checkFormulaCorrect(formulaContent);
		write( result?"true":"false" ); 
	}

	/**
	 * 增加新记录
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void saveBpCExtendFormula() {
		try {
			String str = request.getParameter("isUpdate");
			String curItemCode = request.getParameter("curItemCode");
			Object obj = JSONUtil.deserialize(str);
			List<BpCExtendFormula> addList = new ArrayList<BpCExtendFormula>();
			List<Map> list = (List<Map>) obj;

			BpCStatItemFacadeRemote statRemote = (BpCStatItemFacadeRemote) factory
					.getFacadeRemote("BpCStatItemFacade");
			BpCStatItem statItem = statRemote.findById(curItemCode);
			if (!"3".equals(statItem.getDataCollectWay())
					|| !"3".equals(statItem.getDeriveDataType())) {
				statItem.setDataCollectWay("3");
				statItem.setDeriveDataType("3");

			}

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
				BpCExtendFormula model = new BpCExtendFormula();
				BpCExtendFormulaId id = new BpCExtendFormulaId();
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
			

			statRemote.update(statItem);

			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
	}

	public void getBpCExtendFormulaList() throws JSONException {
		String itemCode = request.getParameter("itemCode");
		List<StatItemFormula> obj = remote.findAll(
				employee.getEnterpriseCode(), itemCode);

		write(JSONUtil.serialize(obj));
	}

	public void getAllStatItemaList() throws JSONException {

		String argFuzzy = request.getParameter("argFuzzy");
		String dataTimeType = request.getParameter("dataTimeType");
		String itemType = request.getParameter("itemType");
		PageObject obj = remote.findAllStatItem(argFuzzy, dataTimeType,itemType, start,
				limit);

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
