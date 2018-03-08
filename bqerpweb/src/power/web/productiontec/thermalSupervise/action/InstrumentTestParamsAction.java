package power.web.productiontec.thermalSupervise.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.productiontec.powertest.PtCYqybjycs;
import power.ejb.productiontec.powertest.PtCYqybjycsFacadeRemote;

import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class InstrumentTestParamsAction extends AbstractAction {
	private PtCYqybjycsFacadeRemote jycsRemote;

	public InstrumentTestParamsAction() {
		jycsRemote = (PtCYqybjycsFacadeRemote) factory
				.getFacadeRemote("PtCYqybjycsFacade");

	}

	@SuppressWarnings("unchecked")
	public void save() throws JSONException {
		String str = request.getParameter("isUpdate");
		String deleteId = request.getParameter("isDelete");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<PtCYqybjycs> addList = new ArrayList<PtCYqybjycs>();
		List<PtCYqybjycs> updateList = new ArrayList<PtCYqybjycs>();
		for (Map data : list) {
			String parameterId = null;
			String yqyblbId = null;
			String parameterNames = null;
			String memo = null;
			String jdzyId = null;
			if (data.get("parameterId") != null)
				parameterId = data.get("parameterId").toString();
			if (data.get("yqyblbId") != null)
				yqyblbId = data.get("yqyblbId").toString();
			if (data.get("parameterNames") != null)
				parameterNames = data.get("parameterNames").toString();
			if (data.get("memo") != null)
				memo = data.get("memo").toString();
			if (data.get("jdzyId") != null)
				jdzyId = data.get("jdzyId").toString();

			PtCYqybjycs model = new PtCYqybjycs();
			if (parameterId == null) {
				model.setJdzyId(Long.parseLong(jdzyId));
				model.setMemo(memo);
				model.setParameterNames(parameterNames);
				model.setYqyblbId(Long.parseLong(yqyblbId));
				model.setEnterpriseCode(employee.getEnterpriseCode());
				addList.add(model);
			} else {
				model = jycsRemote.findById(Long.parseLong(parameterId));
				model.setMemo(memo);
				model.setParameterNames(parameterNames);
				updateList.add(model);
			}

		}
		if (addList.size() > 0 || updateList.size() > 0
				|| deleteId.length() > 0)
			jycsRemote.save(addList, updateList, deleteId);
	}

	public void findJycsBySort() throws JSONException {
		String yqyblbId = request.getParameter("yqyblbId");
		String jdzyIdString = request.getParameter("jdzyId");
		List<PtCYqybjycs> list = jycsRemote.findByLb(Long.parseLong(jdzyIdString), Long
				.parseLong(yqyblbId), employee.getEnterpriseCode());
		write(JSONUtil.serialize(list));
	}
}
