package power.ejb.manage.plan;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpJPlanRepairDetailFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpJPlanRepairDetailFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpJPlanRepairDetail
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            BpJPlanRepairDetail entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpJPlanRepairDetail entity);

	/**
	 * Delete a persistent BpJPlanRepairDetail entity.
	 * 
	 * @param entity
	 *            BpJPlanRepairDetail entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJPlanRepairDetail entity);

	/**
	 * Persist a previously saved BpJPlanRepairDetail entity and return it or a
	 * copy of it to the sender. A copy of the BpJPlanRepairDetail entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJPlanRepairDetail entity to update
	 * @return BpJPlanRepairDetail the persisted BpJPlanRepairDetail entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJPlanRepairDetail update(BpJPlanRepairDetail entity);
	public  void delRepairDetail(String ids );
	public void repairReport(Long mainId, Long actionId, String workerCode,
			String approveText, String nextRoles, String workflowType) ;
	public void repairApprove(Long mainId,Long actionId,Long entryId, String workerCode,
			String approveText, String nextRoles);
	
	public  PageObject getRepairDept(String plantime,String dept);
	public BpJPlanRepairDetail findById(Long id);
	public  void saveRepairList(String flag,List<BpJPlanRepairDetail> addList, List<BpJPlanRepairDetail> updateList,String  planTime,
			String  planDept,String weekStart,String weekEnd,String workCode,String enterpriseCode)throws ParseException;
	public PageObject findRepair( String approve,String entryIds, String planTime, String people, String enterprise,String status ) throws ParseException;
	public PageObject getAllDetail(String deptID,final   int... rowStartIdxAndCount);
};