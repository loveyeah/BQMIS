package power.ejb.manage.plan;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for BpJPlanJobMainFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpJPlanJobMainFacadeRemote {
	
	public BpJPlanJobMain save(BpJPlanJobMain entity);

	
	public void delete(BpJPlanJobMain entity);

	
	public BpJPlanJobMain update(BpJPlanJobMain entity);

	public BpJPlanJobMain findById(Date id);

	
	public List<BpJPlanJobMain> findByProperty(String propertyName, Object value);

	
	public List<BpJPlanJobMain> findAll();

	public BpJPlanJobMain approveStep(String prjNo, String workflowType,
			String workerCode, String actionId, String actionWord,
			String approveText, String nextRoles, String stepId,
			String enterpriseCode) throws ParseException;

	public BpJPlanJobMain reportTo(String prjNo, String workflowType,
			String workerCode, String actionId, String approveText,
			String nextRoles, String enterpriseCode) throws ParseException;
	
	/**
	 * 计划完成情况整理上报
	 * @param planTime
	 * @param actionId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param workflowType
	 * @throws ParseException 
	 */
	public void allDeptWorkFinishReport(String planTime, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String workflowType) throws ParseException;
	
	/**
	 * 计划完成情况整理审批
	 * @param planTime
	 * @param actionId
	 * @param entryId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @throws ParseException 
	 */
	public void allDeptWorkFinishApprove(String planTime, Long actionId, Long entryId,
			String workerCode, String approveText, String nextRoles) throws ParseException;
	
	
	public String findNoReadInfo(String planTime,String flag);
}