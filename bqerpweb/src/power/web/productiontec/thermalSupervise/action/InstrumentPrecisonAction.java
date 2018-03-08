
package power.web.productiontec.thermalSupervise.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.powertest.PtCYqybjd;
import power.ejb.productiontec.powertest.PtCYqybjdFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class InstrumentPrecisonAction extends AbstractAction {

	private PtCYqybjdFacadeRemote remote;

	public InstrumentPrecisonAction() {
		remote = (PtCYqybjdFacadeRemote) factory
				.getFacadeRemote("PtCYqybjdFacade");
	}

	@SuppressWarnings("unchecked")
	public void saveJD() throws JSONException {
		String str = request.getParameter("isUpdate");
		String deleteIds = request.getParameter("isDelete");
		Object obj = JSONUtil.deserialize(str);

		List<PtCYqybjd> addList = new ArrayList<PtCYqybjd>();
		List<PtCYqybjd> updateList = new ArrayList<PtCYqybjd>();
		List<Map> list = (List<Map>) obj;
		for (Map data : list) {
			PtCYqybjd model = new PtCYqybjd();
			model.setEnterpriseCode(employee.getEnterpriseCode());
			if (data.get("yqybjdName") != null
					&& !"".equals(data.get("yqybjdName"))) {
				model.setYqybjdName(data.get("yqybjdName").toString());
			}
			if (data.get("yqybjdId") != null && !data.get("yqybjdId").equals("")) {
				model.setYqybjdId(Long.parseLong(data.get("yqybjdId").toString()));
			}
			if (data.get("jdzyId") != null && !data.get("jdzyId").equals("")) {
				model.setJdzyId(Long.parseLong(data.get("jdzyId").toString()));
			}
			// 添加
			if (model.getYqybjdId() == null || model.getYqybjdId().equals("")) {
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

	public void findJDList() throws JSONException {
		String jdzyIdString = request.getParameter("jdzyId");
		PageObject object = remote.findAll(employee.getEnterpriseCode(), Long
				.parseLong(jdzyIdString));
		write(JSONUtil.serialize(object));
	}

}