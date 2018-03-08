package power.web.run.runlog.action;

import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.runlog.RunCMainItemFacadeRemote;
import power.ejb.run.runlog.RunJRunlogMainFacadeRemote;
import power.ejb.run.runlog.shift.RunJShiftRecord;
import power.ejb.run.runlog.shift.RunJShiftRecordFacadeRemote;
import power.ejb.run.runlog.shift.RunJShiftRecrodForm;
import power.web.comm.AbstractAction;

public class RunShiftRecordAction extends AbstractAction {
	private RunJShiftRecord runrec;
	protected RunJShiftRecordFacadeRemote remote;
	protected RunCMainItemFacadeRemote itemremote;
	protected RunJRunlogMainFacadeRemote logremote;

	public RunShiftRecordAction() {
		remote = (RunJShiftRecordFacadeRemote) factory
				.getFacadeRemote("RunJShiftRecordFacade");
		itemremote = (RunCMainItemFacadeRemote) factory
				.getFacadeRemote("RunCMainItemFacade");
		logremote = (RunJRunlogMainFacadeRemote) factory
				.getFacadeRemote("RunJRunlogMainFacade");
	}

	// 增加值班记事
	public void addRunShiftRecord() {
		if (runrec != null) {
			runrec.setIsUse("Y");
			runrec.setEnterpriseCode(employee.getEnterpriseCode());
			remote.save(runrec);
			write("{success:true}");
		}
	}

	// 修改值班记事
	public void updateRunShiftRecord() {
		RunJShiftRecord model = remote.findById(runrec.getShiftRecordId());
		model.setMainItemCode(runrec.getMainItemCode());
		model.setRecordContent(runrec.getRecordContent());
		model.setIsCompletion(runrec.getIsCompletion());
		model.setIsUse("Y");
		model.setRecordBy(runrec.getRecordBy());
		model.setRecordTime(runrec.getRecordTime());
		model.setEnterpriseCode(employee.getEnterpriseCode());
		remote.update(model);
		write("{success:true}");
	}

	// 标记成未完成
	public void notCompletionRunShiftRecord() {
		RunJShiftRecord model = remote.findById(runrec.getShiftRecordId());
		model.setNotCompletionId(runrec.getShiftRecordId());
		model.setIsCompletion("N");
		remote.update(model);
		write("{success:true}");
	}

	// 标记为完成项
	public void completionRunShiftRecord() {
		RunJShiftRecord model = remote.findById(runrec.getShiftRecordId());
		model.setRecordContent(runrec.getRecordContent());
		model.setIsCompletion("Y");
		model.setIsUse("Y");
		model.setRecordBy(runrec.getRecordBy());
		model.setRecordTime(new Date());
		model.setEnterpriseCode(employee.getEnterpriseCode());
		model.setCheckMemo(runrec.getCheckMemo());
		model.setCheckBy(runrec.getCheckBy());
		model.setCheckTime(new java.util.Date());
		remote.update(model);
		write("{success:true}");
	}

	// 删除值班记事
	public void deleteRunShiftRecord() {
		RunJShiftRecord model = remote.findById(runrec.getShiftRecordId());
		if (model != null) {
			model.setIsUse("N");
			remote.update(model);
		}
		write("{success:true}");
	}

	// 根据属性查询
	public void findByProperty() throws Exception {
		int start = 0;
		int limit = 0;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		List<RunJShiftRecord> list = remote.findByProperty("", runrec
				.getMainItemCode(), start, limit);

		for (int i = 0; i < list.size(); i++) {
		}

		StringBuffer sb = new StringBuffer();
		sb.append("{total:");
		sb.append(list.size());
		sb.append(",root:");
		sb.append(JSONUtil.serialize(list));
		sb.append("}");
		write(sb.toString());
	}

	// 显示当前值班记事
	public void findRunShiftRecord() throws Exception {
		int start = 0;
		int limit = 10000;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		Object runLogId = request.getParameter("runlogid");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject list = remote.findListByRunLogId(Long.parseLong(runLogId.toString()),
				employee.getEnterpriseCode(), start, limit);
		String str = "{total:"+list.getTotalCount()+",root:"+JSONUtil.serialize(list.getList())+"}";
		write(str);
	}

	// 显示未完成项
	public void findNotCompletionRunShiftRecord() throws Exception {
		int start = 0;
		int limit = 10000;
		Long runLogId = Long.parseLong("-1");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		Object objrunLogId = request.getParameter("runlogid");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		if (objrunLogId != null) {
			runLogId = Long.parseLong(objrunLogId.toString());
		}
		PageObject list = remote.findNotCompletionList(Long.parseLong(runLogId.toString()),
				employee.getEnterpriseCode(), start, limit);
		String str = "{total:"+list.getTotalCount()+",root:"+JSONUtil.serialize(list.getList())+"}";
		write(str);
	}

	/**
	 * 值班记事查询
	 */
	public void queryShiftRecordList() throws Exception {
		int start = 0;
		int limit = 10000;
		String startDate = new java.util.Date().toString();
		String endDate = new java.util.Date().toString();
		String specialCode = "";
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		Object objStartDate = request.getParameter("fromDate");
		Object objEndDate = request.getParameter("toDate");
		Object objSpecialCode = request.getParameter("specialcode");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		if (objStartDate != null) {
			startDate = objStartDate.toString();
		}
		if (objEndDate != null) {
			endDate = objEndDate.toString();
		}
		if (objSpecialCode != null) {
			specialCode = objSpecialCode.toString();
		}
		PageObject list = remote.getShiftRecordListByDate(startDate, endDate, specialCode,
				employee.getEnterpriseCode(), start, limit);
		String str = "{total:"+list.getTotalCount()+",root:"+JSONUtil.serialize(list.getList())+"}";
		write(str);
	}

	/**
	 * 未完成项查询
	 */
	public void queryNotCompletionList() throws Exception {
		int start = 0;
		int limit = 10000;
		String startDate = new java.util.Date().toString();
		String endDate = new java.util.Date().toString();
		String specialCode = "";
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		Object objStartDate = request.getParameter("fromDate");
		Object objEndDate = request.getParameter("toDate");
		Object objSpecialCode = request.getParameter("specialcode");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		if (objStartDate != null) {
			startDate = objStartDate.toString();
		}
		if (objEndDate != null) {
			endDate = objEndDate.toString();
		}
		if (objSpecialCode != null) {
			specialCode = objSpecialCode.toString();
		}
		PageObject list = remote.getNotCompletionListByDate(startDate, endDate,
				specialCode, employee.getEnterpriseCode(), start, limit);
		String str = "{total:"+list.getTotalCount()+",root:"+JSONUtil.serialize(list.getList())+"}";
		write(str);
	}

	/**
	 * 其他班组值班记事
	 */
	public void getOtherRecordList() throws Exception {
		int start = 0;
		int limit = 10000;
		Long runLogId = Long.parseLong("-1");
		String startDate = new java.util.Date().toString();
		String endDate = new java.util.Date().toString();
		String specialCode = "-1";
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		Object objSpecialCode = request.getParameter("specialcode");
		Object objrunLogId = request.getParameter("runlogid");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		if (objSpecialCode != null) {
			specialCode = objSpecialCode.toString();
		}
		if (objrunLogId != null) {
			runLogId = Long.parseLong(objrunLogId.toString());
		}
		PageObject list = remote.getOtherRecordListByDate(runLogId, specialCode, "hfdc", start, limit);
		String str = "{total:"+list.getTotalCount()+",root:"+JSONUtil.serialize(list.getList())+"}";
		write(str);
	}

	/**
	 * 导入其它班组值班记事
	 */
	public void impRecord() {
		try {
			String[] ids = request.getParameter("ids").split(",");
			Long runLogId = Long.parseLong("-1");
			Object objrunLogId = request.getParameter("runlogid");
			if (objrunLogId != null) {
				runLogId = Long.parseLong(objrunLogId.toString());
			}
			remote.impOtherRecord(employee.getWorkerCode(), runLogId, ids);
			write("{success : true}");
		} catch (Exception ex) {
			write("{success : false}");
		}
	}

	/**
	 * 未完成项追溯
	 */
	public void reviewNotCompletion(){
		int start = 0;
		int limit = 10000;
		Long recordId = Long.parseLong("-1");
		String startDate = new java.util.Date().toString();
		String endDate = new java.util.Date().toString();
		String specialCode = "-1";
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		Object objrecordId = request.getParameter("recordid");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		if (objrecordId != null) {
			recordId = Long.parseLong(objrecordId.toString());
		}
		String str = "";
		str = remote.reviewNotCompletion(recordId, employee
				.getEnterpriseCode(), start, limit);
		str = str.replace("\r", "<br/>");
		str = str.replace("\n", "<br/>");
		write(str);
	}
	
	public RunJShiftRecord getRunrec() {
		return runrec;
	}

	public void setRunrec(RunJShiftRecord runrec) {
		this.runrec = runrec;
	}
}