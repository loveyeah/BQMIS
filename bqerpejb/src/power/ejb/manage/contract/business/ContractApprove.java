package power.ejb.manage.contract.business;

import java.text.ParseException;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ejb.manage.contract.form.ConApproveBean;

@Remote
public interface ContractApprove {
	/**
	 * 采购合同会签审批上报
	 * 
	 * @param conId
	 *            合同id
	 * @param workercode
	 *            上报人工号
	 */
	public void contractReport(Long conId, String workercode,
			String approveText, String nextRoles) throws CodeRepeatException;

	/**
	 * 项目合同会签审批上报
	 * 
	 * @param conId合同id
	 * @param workercode上报人工号
	 */
//	public void prjContractReport(Long conId, String workercode,
//			String secondcharge, String approveText, String nextRoles)
//			throws CodeRepeatException;
	public void prjContractReport(Long conId, String workercode,String actionId,
			String secondcharge, String approveText, String nextRolePs,String nextRoles,String enterPriseCode)throws CodeRepeatException;
	/**
	 * 采购合同会签审批
	 * 
	 * @param entryId
	 *            实例编号
	 * @param workerCode
	 *            审批人
	 * @param actionId
	 *            审批动作
	 * @param approveText
	 *            审批意见
	 * @param nextRoles
	 *            角色
	 */
	public void contractApprove(Long entryId, String workerCode, Long actionId,
			String approveText, String nextRoles, Long conid,
			String approveDepts, String stepId) throws CodeRepeatException;

	/**
	 * 项目合同会签审批
	 * 
	 * @param entryId实例编号
	 * @param workerCode审批人
	 * @param actionId审批动作
	 * @param approveText审批意见
	 * @param nextRoles角色
	 * @param conid
	 */
	public void prjContractApprove(Long entryId, String workerCode,
			Long actionId, String approveText, String nextRoles, Long conid,
			String approveDepts) throws CodeRepeatException;

	/**
	 * 采购合同会签报表数据
	 * 
	 * @param conid
	 *            合同Id
	 * @return conApproveBean
	 */
	public ConApproveBean getSignReportData(Long conid);

	/**
	 * 采购合同变更审批上报
	 * 
	 * @param conModId
	 *            合同变更id
	 * @param workercode
	 *            上报人工号
	 */
	public void conModifyReport(Long conModId, String workercode,
			String approveText, String nextRoles);

	/**
	 * 项目合同变更审批上报
	 * 
	 * @param conModId
	 *            合同变更id
	 * @param workercode
	 *            上报人工号
	 */
	public void prjConModifyReport(Long conModId, String workercode,
			String approveText, String nextRoles);

	/**
	 * 采购合同变更会签审批
	 * 
	 * @param entryId
	 *            实例编号
	 * @param workerCode
	 *            审批人
	 * @param actionId
	 *            审批动作
	 * @param approveText
	 *            审批意见
	 * @param nextRoles
	 *            角色
	 * @param conModId
	 *            合同变更id
	 */
	public void conModApprove(Long entryId, String workerCode, Long actionId,
			String approveText, String nextRoles, Long conModId,
			String approveDepts, String stepId) throws CodeRepeatException;

	/**
	 * 项目合同变更会签审批
	 * 
	 * @param entryId
	 *            实例编号
	 * @param workerCode
	 *            审批人
	 * @param actionId
	 *            审批动作
	 * @param approveText
	 *            审批意见
	 * @param nextRoles
	 *            角色
	 * @param conModId
	 *            合同变更id
	 */
	public void prjConModApprove(Long entryId, String workerCode,
			Long actionId, String approveText, String nextRoles, Long conModId)
			throws CodeRepeatException;

	/**
	 * 采购合同结算审批上报 bq
	 * 
	 * @param bal
	 * @param workercode
	 */
	public void conBalanceReport(ConJBalance bal, String workercode);

	/**
	 * 项目合同结算审批上报
	 * 
	 * @param bal
	 * @param workercode
	 */
	public void prjConBalanceReport(String balanceId, String workercode,
			String approveText, String nextRoles, String passPrice,
			String workflowType);

	/**
	 * 合同结算审批
	 * 
	 * @param entryId
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param bal
	 * @param hismodel
	 */
	public void conBalanceApprove(Long entryId, String workerCode,
			Long actionId, String approveText, String nextRoles,
			ConJBalance bal, ConJBalaApprove hismodel)
			throws CodeRepeatException;

	/**
	 * 项目合同结算审批
	 * 
	 * @param entryId
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param bal
	 * @param hismodel
	 */
	public void prjConBalanceApprove(Long entryId, String workerCode,
			Long actionId, String approveText, String nextRoles,
			ConJBalance bal, ConJBalaApprove hismodel)
			throws CodeRepeatException;

	/**
	 * 项目合同委托审批 add by drdu 091109
	 * 
	 * @param entryId
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param conid
	 * @param prosyBy
	 * @param sDate
	 * @param eDate
	 * @throws CodeRepeatException
	 * @throws ParseException
	 */
	public void delegationApproveSign(Long entryId, String workerCode,
			Long actionId, String approveText, String nextRoles, Long conid,
			String prosyBy, String sDate, String eDate)
			throws CodeRepeatException, ParseException;

	/**
	 * 采购合同委托审批 add by drdu 091119
	 * 
	 * @param entryId
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextPerson
	 * @param conid
	 * @param prosyBy
	 * @param sDate
	 * @param eDate
	 * @throws CodeRepeatException
	 * @throws ParseException
	 */
	public void conDelegationApproveSign(Long entryId, String workerCode,
			Long actionId, String approveText, String nextPerson, Long conid,
			String prosyBy, String sDate, String eDate)
			throws CodeRepeatException, ParseException;

	/**
	 * 采购合同结算审批上报 bq
	 * 
	 * @param bal
	 * @param workercode
	 */
	public void bqConBalanceReport(String appId, String enterpriseCode,
			String workercode);
/**
 *  采购合同结算审批 bq
 * @param appId
 * @param entryId
 * @param actionId
 * @param approveText
 * @param nextRoles
 * @param approveDepts
 * @param enterpriseCode
 * @param workercode
 */
	public void bqConBalanceApprove(String appId, Long entryId,
			Long actionId, String approveText, String nextRoles,
			String approveDepts, String enterpriseCode, String workercode);
}
