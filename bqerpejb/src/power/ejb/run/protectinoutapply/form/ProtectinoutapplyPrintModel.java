package power.ejb.run.protectinoutapply.form;

import java.util.List;

import power.ejb.run.protectinoutapply.RunJProtectinoutApprove;
import power.ejb.run.protectinoutapply.RunJProtectinoutapply;

public class ProtectinoutapplyPrintModel {
	
	private List<RunJProtectinoutapply> baseList;
	
	private List<RunJProtectinoutApprove> approveInList;
	
	private List<RunJProtectinoutApprove> approveOutList;

	public List<RunJProtectinoutapply> getBaseList() {
		return baseList;
	}

	public void setBaseList(List<RunJProtectinoutapply> baseList) {
		this.baseList = baseList;
	}

	public List<RunJProtectinoutApprove> getApproveInList() {
		return approveInList;
	}

	public void setApproveInList(List<RunJProtectinoutApprove> approveInList) {
		this.approveInList = approveInList;
	}

	public List<RunJProtectinoutApprove> getApproveOutList() {
		return approveOutList;
	}

	public void setApproveOutList(List<RunJProtectinoutApprove> approveOutList) {
		this.approveOutList = approveOutList;
	}

	
}
