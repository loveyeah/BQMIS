package power.web.productiontec.thermalSupervise.action;

import java.text.SimpleDateFormat;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.powertest.PtJYqybtz;
import power.ejb.productiontec.powertest.PtJYqybtzFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class InstrumentAccountAction extends AbstractAction {
	private PtJYqybtz model;
	private int start;
	private int limit;
	private PtJYqybtzFacadeRemote TZRemote;

	public InstrumentAccountAction() {

		TZRemote = (PtJYqybtzFacadeRemote) factory
				.getFacadeRemote("PtJYqybtzFacade");
	}

	public void saveTZ() {
		try {
			model.setEnterpriseCode(employee.getEnterpriseCode());
			String lastCheckTime = request.getParameter("lastCheckTime");
			String nextCheckTime = request.getParameter("nextCheckTime");
			String checkResult = request.getParameter("checkResult");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(lastCheckTime != null && !lastCheckTime.equals("")){
				model.setLastCheckDate(sdf.parse(lastCheckTime));
				model.setNextCheckDate(sdf.parse(nextCheckTime));
				model.setCheckResult(checkResult);
			}
			TZRemote.save(model);
		} catch (Exception e) {
			write("{success:false,msg:'保存失败！''}");
		}
	}

	public void updateTz() {
		try {
			model.setEnterpriseCode(employee.getEnterpriseCode());
			String lastCheckTime = request.getParameter("lastCheckTime");
			String nextCheckTime = request.getParameter("nextCheckTime");
			String checkResult = request.getParameter("checkResult");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(lastCheckTime != null && !lastCheckTime.equals("")){
				model.setLastCheckDate(sdf.parse(lastCheckTime));
				model.setNextCheckDate(sdf.parse(nextCheckTime));
				model.setCheckResult(checkResult);
			}else{
				PtJYqybtz entity = TZRemote.findById(model.getRegulatorId());
				model.setLastCheckDate(entity.getLastCheckDate());
				model.setNextCheckDate(entity.getNextCheckDate());
				model.setCheckResult(entity.getCheckResult());
			}
			TZRemote.update(model);
		} catch (Exception e) {
			write("{success:false,msg:'保存失败！''}");
		}

	}

	public void findAccountByNames() throws JSONException {
		String fuzzy = request.getParameter("fuzzy");
		String jdzyIdString = request.getParameter("jdzyId");
		PageObject object = TZRemote.getYaybtzlist(fuzzy, Long
				.parseLong(jdzyIdString), employee.getEnterpriseCode(), start,
				limit);
		write(JSONUtil.serialize(object));
	}

	public void findAccountByMonth() throws JSONException {
		String fuzzy = request.getParameter("fuzzy");
		String jdzyIdString = request.getParameter("jdzyId");
		PageObject object = TZRemote.getInstrumentTestRecord(fuzzy, Long
				.parseLong(jdzyIdString), employee.getEnterpriseCode(), start,
				limit);
		write(JSONUtil.serialize(object));
	}

	public void findOverdueTestRecord() throws JSONException {
		String date = request.getParameter("date");
		String names = request.getParameter("names");
		String jdzyIdString = request.getParameter("jdzyId");
		PageObject object = TZRemote.getOverdueInstrumentTestRecord(date,names, Long
				.parseLong(jdzyIdString), employee.getEnterpriseCode(), start,
				limit);
		write(JSONUtil.serialize(object));
	}

	public void deleteTZ() {
		try {
			String ids = request.getParameter("ids");
			TZRemote.delete(ids);
		} catch (Exception e) {
			write("{success:false,msg:'删除失败！''}");
		}

	}

	public PtJYqybtz getModel() {
		return model;
	}

	public void setModel(PtJYqybtz model) {
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
