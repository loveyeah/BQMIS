
package power.web.productiontec.thermalSupervise.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.powertest.PtCYqybdj;
import power.ejb.productiontec.powertest.PtCYqybdjFacadeRemote;

import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class InstrumentGradeAction extends AbstractAction {

	private PtCYqybdjFacadeRemote remote;

	public InstrumentGradeAction() {
		remote = (PtCYqybdjFacadeRemote) factory
				.getFacadeRemote("PtCYqybdjFacade");
	}

	@SuppressWarnings("unchecked")
	public void saveDJ() throws JSONException {
		String str = request.getParameter("isUpdate");
		String deleteIds = request.getParameter("isDelete");
		Object obj = JSONUtil.deserialize(str);

		List<PtCYqybdj> addList = new ArrayList<PtCYqybdj>();
		List<PtCYqybdj> updateList = new ArrayList<PtCYqybdj>();
		List<Map> list = (List<Map>) obj;
		for (Map data : list) {
			PtCYqybdj model = new PtCYqybdj();
			model.setEnterpriseCode(employee.getEnterpriseCode());
			if (data.get("yqybdjName") != null
					&& !"".equals(data.get("yqybdjName"))) {
				model.setYqybdjName(data.get("yqybdjName").toString());
			}
			if (data.get("yqybdjId") != null && !data.get("yqybdjId").equals("")) {
				model.setYqybdjId(Long.parseLong(data.get("yqybdjId").toString()));
			}
			if (data.get("jdzyId") != null && !data.get("jdzyId").equals("")) {
				model.setJdzyId(Long.parseLong(data.get("jdzyId").toString()));
			}
			// 添加
			if (model.getYqybdjId() == null || model.getYqybdjId().equals("")) {
				addList.add(model);
			} else {
				updateList.add(model);
			}
		}
		if (addList.size() > 0 || updateList.size() > 0
				|| deleteIds.trim().length() > 0) {
			remote.save(addList, updateList, deleteIds);
		}
	}

	public void findDJList() throws JSONException {
		String jdzyIdString = request.getParameter("jdzyId");
		PageObject object = remote.findAll(employee.getEnterpriseCode(), Long
				.parseLong(jdzyIdString));
		write(JSONUtil.serialize(object));
	}

}