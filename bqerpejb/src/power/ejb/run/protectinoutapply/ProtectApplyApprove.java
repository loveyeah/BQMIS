package power.ejb.run.protectinoutapply;

import java.util.List;

import javax.ejb.Remote;

import power.ejb.run.protectinoutapply.form.ProtectinoutapplyPrintModel;

@Remote 
public interface ProtectApplyApprove {
	
	/**
	 * 上报
	 * @param protectNo
	 * @param workflowType
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param eventIdentify
	 */
	public void reportTo(String protectNo,String workflowType,
			String workerCode,Long actionId,String approveText,
			String nextRoles,String eventIdentify,String enterpriseCode);
	
	/**
	 * 签字审批
	 * @param model
	 * @param hisModel
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 * @param eventIdentify
	 * @return
	 */
	public String protectApplySign(RunJProtectinoutapply model,RunJProtectinoutApprove hisModel,
			Long actionId,String responseDate,String nextRoles,String eventIdentify);
	/**
	 * 报表数据
	 * @param protectNo
	 * @return ProtectinoutapplyPrintModel
	 */
	public ProtectinoutapplyPrintModel getModel(String protectNo); 
}
