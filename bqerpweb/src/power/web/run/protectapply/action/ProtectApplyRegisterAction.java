package power.web.run.protectapply.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.protectinoutapply.RunJProtectinoutapply;
import power.ejb.run.protectinoutapply.RunJProtectinoutapplyFacadeRemote;
import power.ejb.run.protectinoutapply.form.ProAppInfoForm;
import power.ejb.run.protectinoutapply.form.ProtectInOutApplyInfo;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class ProtectApplyRegisterAction extends AbstractAction {

	private RunJProtectinoutapply power;
	protected RunJProtectinoutapplyFacadeRemote remote;

	public ProtectApplyRegisterAction() {
		remote = (RunJProtectinoutapplyFacadeRemote) factory
				.getFacadeRemote("RunJProtectinoutapplyFacade");
	}

	public void addProtectApply() {
		String isIn = request.getParameter("isIn");
		String protectOutNo = request.getParameter("protectOutNo");
		power.setIsIn(isIn);
		power.setIsSelect("N");
		power.setEnterpriseCode(employee.getEnterpriseCode());
		power.setLastModifyBy(employee.getWorkerCode());
		power.setRelativeNo(protectOutNo);
		power = remote.save(power);
		if (protectOutNo != null && !protectOutNo.equals("")) {
			RunJProtectinoutapply model = remote.findByProtectNo(protectOutNo,
					employee.getEnterpriseCode());
			model.setIsSelect("Y");
			model.setRelativeNo(power.getProtectNo());
			remote.update(model);
		}
		write("{success:true,no:'" + power.getProtectNo() + "',id:'"
				+ power.getApplyId() + "',msg:'增加成功！'}");

	}

	public void updateProtectApply() {
		RunJProtectinoutapply model = remote.findById(power.getApplyId());
		model.setApplyBy(power.getApplyBy());
		model.setApplyDept(power.getApplyDept());
		model.setApplyDate(power.getApplyDate());
		model.setEquCode(power.getEquCode());
		model.setProtectName(power.getProtectName());
		model.setEquEffect(power.getEquEffect());
		model.setProtectReason(power.getProtectReason());
		model.setOutSafety(power.getOutSafety());
		model.setApplyStartDate(power.getApplyStartDate());
		model.setApplyEndDate(power.getApplyEndDate());
		model.setLastModifyBy(employee.getWorkerCode());
		model.setMemo(power.getMemo());

		remote.update(model);
		write("{success:true,msg:'修改成功！'}");

	}

	public void findRegisterList() throws JSONException {
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String applyDept = request.getParameter("applyDept");
		String status = request.getParameter("status");
		if (status == null || status.equals("")) {
			status = "1,2";
		}
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findRegisterList(startDate, endDate, applyDept,
					status, employee.getEnterpriseCode(), start, limit);
		} else {
			obj = remote.findRegisterList(startDate, endDate, applyDept,
					status, employee.getEnterpriseCode());
		}
		if (obj == null || obj.getList() == null) {
			write("{\"list\":[],\"totalCount\":0}");
		} else {
			String str = JSONUtil.serialize(obj);
			write(str);
		}

	}

	public void deleteProtectApply() {
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}

	public RunJProtectinoutapply getPower() {
		return power;
	}

	public void setPower(RunJProtectinoutapply power) {
		this.power = power;
	}

	public void findByIsinList() throws JSONException {
		PageObject obj = new PageObject();
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String applyDept = request.getParameter("applyDept");
		String status = request.getParameter("status");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		String isIn = request.getParameter("isIn");
		if (isIn != null && !isIn.equals("")) {
			;
		} else {
			isIn = "N";
		}
		obj = remote.findByIsin(startDate, endDate, applyDept, status, employee
				.getEnterpriseCode(), isIn, Integer.parseInt(objstart
				.toString()), Integer.parseInt(objlimit.toString()));
		if (obj == null || obj.getList() == null) {
			write("{\"list\":[],\"totalCount\":0}");
		} else {
			String str = JSONUtil.serialize(obj);
			write(str);
		}
	}

	public void findByNo() throws JSONException {
		String protectNo = request.getParameter("protectNo");
		ProAppInfoForm m = remote.findByNo(protectNo, employee
				.getEnterpriseCode());
		String str = JSONUtil.serialize(m);
		write("{data:" + str + "}");
	}
}
