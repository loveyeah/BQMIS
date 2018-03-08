package power.web.productiontec.thermalSupervise.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;

import power.ejb.productiontec.powertest.PtCYqyblb;
import power.ejb.productiontec.powertest.PtCYqyblbFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class InstrumentSortAction extends AbstractAction {

	private PtCYqyblbFacadeRemote remote;

	public InstrumentSortAction() {
		remote = (PtCYqyblbFacadeRemote) factory
				.getFacadeRemote("PtCYqyblbFacade");
	}

	@SuppressWarnings("unchecked")
	public void saveLB() throws JSONException {
		String str = request.getParameter("isUpdate");
		String deleteIds = request.getParameter("isDelete");
		Object obj = JSONUtil.deserialize(str);

		List<PtCYqyblb> addList = new ArrayList<PtCYqyblb>();
		List<PtCYqyblb> updateList = new ArrayList<PtCYqyblb>();
		List<Map> list = (List<Map>) obj;
		for (Map data : list) {
			PtCYqyblb model = new PtCYqyblb();
			model.setEnterpriseCode(employee.getEnterpriseCode());
			if (data.get("yqyblbName") != null
					&& !"".equals(data.get("yqyblbName"))) {
				model.setYqyblbName(data.get("yqyblbName").toString());
			}
			if (data.get("yqyblbId") != null && !data.get("yqyblbId").equals("")) {
				model.setYqyblbId(Long.parseLong(data.get("yqyblbId").toString()));
			}
			if (data.get("jdzyId") != null && !data.get("jdzyId").equals("")) {
				model.setJdzyId(Long.parseLong(data.get("jdzyId").toString()));
			}
			// 添加
			if (model.getYqyblbId() == null || model.getYqyblbId().equals("")) {
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

	public void findLBList() throws JSONException {
		String jdzyIdString = request.getParameter("jdzyId");
		PageObject object = remote.findAll(employee.getEnterpriseCode(), Long
				.parseLong(jdzyIdString));
		write(JSONUtil.serialize(object));
	}

}