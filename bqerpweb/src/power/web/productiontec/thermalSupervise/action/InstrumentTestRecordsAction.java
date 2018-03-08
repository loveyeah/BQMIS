package power.web.productiontec.thermalSupervise.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.powertest.PtJYqybjycsz;
import power.ejb.productiontec.powertest.PtJYqybjycszFacadeRemote;
import power.ejb.productiontec.powertest.PtJYqybjyjl;
import power.ejb.productiontec.powertest.PtJYqybjyjlFacadeRemote;
import power.ejb.productiontec.powertest.PtJYqybtz;
import power.ejb.productiontec.powertest.PtJYqybtzFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class InstrumentTestRecordsAction extends AbstractAction {
	private PtJYqybjyjl model;
	private int start;
	private int limit;
	private PtJYqybjyjlFacadeRemote jLRemote;
	private PtJYqybjycszFacadeRemote cSZRemote;
	private PtJYqybtzFacadeRemote tZRemote;

	public InstrumentTestRecordsAction() {
		jLRemote = (PtJYqybjyjlFacadeRemote) factory
				.getFacadeRemote("PtJYqybjyjlFacade");
		cSZRemote = (PtJYqybjycszFacadeRemote) factory
				.getFacadeRemote("PtJYqybjycszFacade");
		tZRemote = (PtJYqybtzFacadeRemote) factory
				.getFacadeRemote("PtJYqybtzFacade");
	}

	public PtJYqybjyjl saveJL() {
		try {
			model.setEnterpriseCode(employee.getEnterpriseCode());

			PtJYqybjyjl baseInfo = jLRemote.save(model);
			PtJYqybtz tzModel = tZRemote.findById(model.getRegulatorId());
			tzModel.setLastCheckDate(model.getCheckDate());

			tzModel.setNextCheckDate(model.getNextCheckDate());
			tZRemote.update(tzModel);
			return baseInfo;
		} catch (Exception e) {
			write("{success:false}");
			return null;
		}

	}

	public void updateJL() {
		try {
			// PtJYqybjyjl baseInfo = jLRemote.findById(model.getJyjlId());
			model.setEnterpriseCode(employee.getEnterpriseCode());
			jLRemote.update(model);
			PtJYqybtz tzModel = tZRemote.findById(model.getRegulatorId());
			tzModel.setLastCheckDate(model.getCheckDate());
			tzModel.setNextCheckDate(model.getNextCheckDate());
			tZRemote.update(tzModel);
		} catch (Exception e) {
			write("{success:false}");
		}

	}

	@SuppressWarnings("unchecked")
	public void saveCSZ() throws JSONException {
		Long jyjlId = model.getJyjlId();
		if (jyjlId == null) {
			jyjlId = saveJL().getJyjlId();
		} else {
			updateJL();
		}
		String str = request.getParameter("isUpdate");

		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<PtJYqybjycsz> addList = new ArrayList<PtJYqybjycsz>();
		List<PtJYqybjycsz> updateList = new ArrayList<PtJYqybjycsz>();

		for (Map data : list) {
			String jycszId = null;

			String parameterId = null;
			String parameterValue = null;
			String jdzyId = null;

			if (data.get("jycszId") != null)
				jycszId = data.get("jycszId").toString();

			if (data.get("parameterId") != null)
				parameterId = data.get("parameterId").toString();

			if (data.get("parameterValue") != null)
				parameterValue = data.get("parameterValue").toString();

			if (data.get("jdzyId") != null)
				jdzyId = data.get("jdzyId").toString();

			PtJYqybjycsz model = new PtJYqybjycsz();
			if (jycszId == null) {
				model.setJdzyId(Long.parseLong(jdzyId));
				model.setJyjlId(jyjlId);
				model.setParameterId(Long.parseLong(parameterId));
				model.setParameterValue(parameterValue);
				model.setEnterpriseCode(employee.getEnterpriseCode());
				addList.add(model);
			} else {
				model = cSZRemote.findById(Long.parseLong(jycszId));
				model.setJdzyId(Long.parseLong(jdzyId));
				model.setJyjlId(jyjlId);
				model.setParameterId(Long.parseLong(parameterId));
				model.setParameterValue(parameterValue);
				updateList.add(model);

			}

		}
		if (addList.size() > 0 || updateList.size() > 0) {
			cSZRemote.save(addList, updateList);

		}

	}

	public void deleteJLAndCSZ() {
		String idString = request.getParameter("ids");
		jLRemote.deleteJLAndJYZ(idString);

	}

	public void findCSZ() throws JSONException {
		String regulatorId = request.getParameter("regulatorId");
		String jyjlId = request.getParameter("jyjlId");
		PageObject object = cSZRemote.findCSZ(regulatorId, jyjlId);
		write(JSONUtil.serialize(object));
	}

	public void findJLList() throws JSONException {
		String jdzyId = request.getParameter("jdzyId");
		String fuzzy = request.getParameter("fuzzy");

		PageObject object = jLRemote.findJYJLList(fuzzy, jdzyId, employee
				.getEnterpriseCode(), start, limit);
		write(JSONUtil.serialize(object));
	}

	public void findJLByTz() throws JSONException {
		String regulatorId = request.getParameter("regulatorId");
		

		PtJYqybjyjl model = jLRemote.findLastJl(regulatorId, employee
				.getEnterpriseCode());
		write(JSONUtil.serialize(model));
	}

	public PtJYqybjyjl getModel() {
		return model;
	}

	public void setModel(PtJYqybjyjl model) {
		this.model = model;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
