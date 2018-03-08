package power.ejb.workticket.business;

import java.text.ParseException;
import java.util.List;

import javax.ejb.Remote;

import power.ejb.workticket.form.WorkticketBusiStatus;
import power.ejb.workticket.form.WorkticketHisForPrint;

@Remote 
public interface BqWorkticketApprove {


	
	/**
	 * 审批列表页面的审批状态
	 * @return
	 */
	public List<WorkticketBusiStatus>  findBusiStatusForApprove();
	
/**
 * 上报
 * @param workticketNo
 * @param workflowType
 * @param workerCode
 * @param actionId
 * @param approveText
 */
	public void reportTo(String workticketNo,String workflowType,String workerCode,Long actionId,String approveText,String nextRoles,String eventIdentify);
	/**
	 * 通用的签字审批
	 * @param workticketNo
	 * @param approveText
	 * @param workerCode
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 * @param eventIdentify
	 * @param workticketMemo 选择操作票 add by bjxu 20100113
	 *
	 */
	public String workticketSign(String workticketNo,String approveText,String workerCode,Long actionId,String responseDate,String nextRoles,String eventIdentify,String workticketMemo);
	
	/**
	 * 值长审批
	 * @param workticketNo
	 * @param approveText
	 * @param workerCode
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 * @param eventIdentify
	 * @param approveFinishDate
	 * @throws ParseException
	 */
	public void workticketDutyChargeConfirm(String workticketNo,String approveText,String workerCode,Long actionId,String responseDate,String nextRoles,String eventIdentify,String approveFinishDate) throws ParseException;
	
	/**
	 * 安措办理
	 * @param workticketNo
	 * @param approveText
	 * @param workerCode
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 * @param eventIdentify
	 * @param workticketMemo
	 * @return
	 * @throws ParseException 
	 */
	public String workticketSafetyExe(String workticketNo,String approveText,String workerCode,Long actionId,String responseDate,String nextRoles,String eventIdentify,String workticketMemo) throws ParseException;
	
	/**
	 * 终结
	 * @param endHisModel
	 * @param pemitHisModel
	 * @param changeChargeModel
	 * @param delayHisModel
	 * @param actionId
	 * @param nextRoles
	 * @param eventIdentify
	 * @param workticketMemo
	 * @param responseDate
	 * @return
	 */
	public String workticketEndApprove(RunJWorktickethis endHisModel,RunJWorktickethis pemitHisModel,RunJWorktickethis changeChargeModel,RunJWorktickethis delayHisModel,RunJWorktickethis safetyExeHisModel,Long actionId,String nextRoles,String eventIdentify,String workticketMemo,String responseDate);
	
	/**
	 * 交回恢复列表
	 * @param workticketNo
	 * @return
	 * @throws ParseException
	 */
	public List<WorkticketHisForPrint> findReturnBackList(String workticketNo) throws ParseException;
	
	/**
	 * 动火票安措办理
	 * @param safetyType "repair" "run" "fire"
	 * @param workticketNo
	 * @param approveText
	 * @param workerCode
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 * @param eventIdentify
	 * @return
	 */
	public String  fireSafetyExeApprove(String safetyType, String workticketNo,String approveText,String workerCode,Long actionId,String responseDate,String nextRoles,String eventIdentify);
	/**
	 * 动火票允许动火时间
	 * @param workticketNo
	 * @param approveText
	 * @param workerCode
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 * @param eventIdentify
	 * @param permitStartDate
	 * @throws ParseException
	 */
	public void firePemitStartApprove(String workticketNo,String approveText,String workerCode,Long actionId,String responseDate,String nextRoles,String eventIdentify,String permitStartDate) throws ParseException;
	
	/**
	 * 动火票终结
	 * @param hisModel
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 * @param eventIdentify
	 * @throws ParseException
	 */
	public void fireEndApprove(RunJWorktickethis hisModel,Long actionId,String responseDate,String nextRoles,String eventIdentify) throws ParseException;
	
	/**
	 * 动火票测量
	 * @param measureModel
	 * @param approveText
	 * @param workerCode
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 * @param eventIdentify
	 */
	public void fireMeasureApprove(RunJWorkticketMeasure measureModel,String approveText,String workerCode,Long actionId,String responseDate,String nextRoles,String eventIdentify);
	public List<WorkticketBusiStatus> findBusiStatusList();
	public List<WorkticketBusiStatus> findBusiStatuForReport();
	
}
