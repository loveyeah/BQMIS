package power.web.manage.stat.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.BpCRunFormula;
import power.ejb.manage.stat.BpCRunFormulaFacadeRemote;
import power.ejb.manage.stat.BpCRunFormulaId;
import power.ejb.manage.stat.BpCStatItem;
import power.ejb.manage.stat.BpCStatItemFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class BpCRunFormulaAction extends AbstractAction {
	private BpCRunFormulaFacadeRemote remote;
	private BpCRunFormula runFormulaInfo;
	private String method;

	public BpCRunFormulaAction() {
		remote = (BpCRunFormulaFacadeRemote) factory
				.getFacadeRemote("BpCRunFormulaFacade");
	}

	public void getRunFormulaList() throws JSONException {
		String itemcode = request.getParameter("itemCode");
		String derivedatatype = request.getParameter("deriveDataType");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		int start = 0, limit = 10000;
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(objstart.toString());
			limit = Integer.parseInt(objlimit.toString());
		}
		obj = remote.getRunFormulaList(itemcode, derivedatatype, employee
				.getEnterpriseCode(), start, limit);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	@SuppressWarnings("unchecked")
	public void saveRunFormulaList() throws JSONException {
		try {

			String str = request.getParameter("isUpdate");

			String itemcode = request.getParameter("itemCode");
			Object obj = JSONUtil.deserialize(str);

			boolean ste = true;
			List<BpCRunFormula> addList = new ArrayList<BpCRunFormula>();

			List<Map> list = (List<Map>) obj;
			BpCStatItemFacadeRemote statRemote = (BpCStatItemFacadeRemote) factory
					.getFacadeRemote("BpCStatItemFacade");
			BpCStatItem statItem = statRemote.findById(itemcode);
			String curDeriveDataType = "";
			if (list.size() > 0)
				curDeriveDataType = list.get(0).get(
						"runFormulaInfo.deriveDataType").toString();
			if (!"3".equals(statItem.getDataCollectWay())
					|| !curDeriveDataType.equals(statItem.getDeriveDataType())) {
				statItem.setDataCollectWay("3");
				statItem.setDeriveDataType(curDeriveDataType);
			}

			target1: for (Map data : list) {
				if (data.get("runFormulaInfo.id.runDataCode") != null
						&& !("").equals(data
								.get("runFormulaInfo.id.runDataCode"))) {
					BpCRunFormula model = new BpCRunFormula();
					BpCRunFormulaId idmodel = new BpCRunFormulaId();
					if (itemcode != null)
						idmodel.setItemCode(itemcode);

					if (data.get("runFormulaInfo.id.runDataCode") != null)
						idmodel.setRunDataCode(data.get(
								"runFormulaInfo.id.runDataCode").toString());

					if (data.get("runFormulaInfo.operatorCode") != null)
						model.setOperatorCode(data.get(
								"runFormulaInfo.operatorCode").toString());

					if (data.get("runFormulaInfo.deriveDataType") != null)
						model.setDeriveDataType(data.get(
								"runFormulaInfo.deriveDataType").toString());

					if (data.get("runFormulaInfo.sdType") != null)
						model.setSdType(data.get("runFormulaInfo.sdType")
								.toString());

					if (data.get("runFormulaInfo.enterpriseCode") != null)
						model.setEnterpriseCode(data.get(
								"runFormulaInfo.enterpriseCode").toString());
					else
						model.setEnterpriseCode(employee.getEnterpriseCode());
					if (data.get("runFormulaInfo.displayNo") != null)
						model.setDisplayNo(data.get("runFormulaInfo.displayNo")
								.toString());

					model.setId(idmodel);

					addList.add(model);

				} else {
					{
						ste = false;
						break target1;
					}
				}
			}
			if (ste) {
				remote.saveRunFormulaList(itemcode, addList);

				statRemote.update(statItem);
				write("{success:true,msg:'保 存 成 功 ！'}");
			} else
				write("{success:false,msg:'指 标 不 可 为 空 ！'}");
		} catch (RuntimeException e) {
			write("{success:false,msg:'保 存 失 败 ！'}");
		}
	}

	public void getRunFormulaReword() {
		String itemcode = request.getParameter("itemCode");
		String str = remote.getRunFormulaVchar(itemcode, employee
				.getEnterpriseCode());
		write(str);
	}

	public BpCRunFormula getRunFormulaInfo() {
		return runFormulaInfo;
	}

	public void setRunFormulaInfo(BpCRunFormula runFormulaInfo) {
		this.runFormulaInfo = runFormulaInfo;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}
