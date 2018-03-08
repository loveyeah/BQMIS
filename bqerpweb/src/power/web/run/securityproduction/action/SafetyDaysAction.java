package power.web.run.securityproduction.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.SpJSafetyDaysrecord;
import power.ejb.run.securityproduction.SpJSafetyDaysrecordFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SafetyDaysAction extends AbstractAction {
	protected SpJSafetyDaysrecordFacadeRemote remote;
	private SpJSafetyDaysrecord safeDays;
	private Long safeId;

	public SafetyDaysAction() {
		remote = (SpJSafetyDaysrecordFacadeRemote) factory
				.getFacadeRemote("SpJSafetyDaysrecordFacade");
	}

	/**
	 * 
	 * 获得所有安全生产天记录列表
	 * 
	 * @throws JSONException
	 */
	public void getSafetyDaysRecordList() throws JSONException {
		Object objStart = request.getParameter("start");
		Object objLimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		int start = 0;
		int limit = 100000;
		if (objStart != null && objLimit != null) {
			start = Integer.parseInt(objStart.toString());
			limit = Integer.parseInt(objLimit.toString());
		}
		obj = remote.findAll(employee.getEnterpriseCode(), start, limit);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 保存安全生产天记录
	 */
	public void addSafeDays() {
		if (safeId == null) {
			safeDays.setEnterpriseCode(employee.getEnterpriseCode());
			safeDays.setIfBreak("N");
			Long safeId = remote.save(safeDays).getRecordId();
			write("{success:true,safeId:"+safeId+"}");
		} else {
			SpJSafetyDaysrecord model = remote.findById(safeId);
			model.setStartDate(safeDays.getStartDate());
			model.setEndDate(safeDays.getEndDate());
			model.setSafetyDays(safeDays.getSafetyDays());
			model.setMemo(safeDays.getMemo());
			model.setRecordBy(safeDays.getRecordBy());
			model.setRecordTime(safeDays.getRecordTime());
			model.setEnterpriseCode(employee.getEnterpriseCode());
			model.setIfBreak("N");
			Long safeId =remote.update(model).getRecordId();
			write("{success:true,safeId:"+safeId+"}");
		}
		
	}

	/**
	 * 中断安全生产天记录
	 */
	public void stopSafeDays() {
		if (safeId == null) {
			safeDays.setEnterpriseCode(employee.getEnterpriseCode());
			safeDays.setIfBreak("Y");
			Long safeId = remote.save(safeDays).getRecordId();
			write("{success:true,safeId:"+safeId+"}");
		} else {
			SpJSafetyDaysrecord model = remote.findById(safeId);
			model.setStartDate(safeDays.getStartDate());
			model.setEndDate(safeDays.getEndDate());
			model.setSafetyDays(safeDays.getSafetyDays());
			model.setMemo(safeDays.getMemo());
			model.setRecordBy(safeDays.getRecordBy());
			model.setRecordTime(safeDays.getRecordTime());
			model.setEnterpriseCode(employee.getEnterpriseCode());
			model.setIfBreak("Y");
			Long safeId = remote.update(model).getRecordId();
			write("{success:true,safeId:"+safeId+"}");
		}
	}

	public SpJSafetyDaysrecord getSafeDays() {
		return safeDays;
	}

	public void setSafeDays(SpJSafetyDaysrecord safeDays) {
		this.safeDays = safeDays;
	}

	public Long getSafeId() {
		return safeId;
	}

	public void setSafeId(Long safeId) {
		this.safeId = safeId;
	}

}
