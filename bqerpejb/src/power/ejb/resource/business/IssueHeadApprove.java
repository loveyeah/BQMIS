package power.ejb.resource.business;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface IssueHeadApprove {
	/**
	 * 物资领用上报
	 * @param issueHeadId
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param workerCode
	 */
	public void IssueHeadReport(Long issueHeadId,Long actionId, String approveText, String workerCode);
	/**
	 * 物资领用审批
	 * @param issueHeadId
	 * @param actionId
	 * @param approveText
	 * @param workerCode
	 * @param nrs
	 * @param eventIdentify
	 * @param updateDetails
	 */
//	public void IssueHeadApprove(Long issueHeadId,Long actionId, String approveText, String workerCode,String nrs,String eventIdentify, List<Map> updateDetails);
	public void IssueHeadApprove(Long issueHeadId,Long actionId, String approveText, String workerCode,String nrs,String eventIdentify,String itemCode);
}
