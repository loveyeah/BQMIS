package power.ejb.manage.plan;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpJPlanRepairDepFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpJPlanRepairDepFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpJPlanRepairDep entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpJPlanRepairDep entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpJPlanRepairDep entity);

	/**
	 * Delete a persistent BpJPlanRepairDep entity.
	 * 
	 * @param entity
	 *            BpJPlanRepairDep entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJPlanRepairDep entity);

	/**
	 * Persist a previously saved BpJPlanRepairDep entity and return it or a
	 * copy of it to the sender. A copy of the BpJPlanRepairDep entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJPlanRepairDep entity to update
	 * @return BpJPlanRepairDep the persisted BpJPlanRepairDep entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJPlanRepairDep update(BpJPlanRepairDep entity);

	public BpJPlanRepairDep findById(Long id);

	public PageObject  findAlldept(String time,final   int... rowIndexAndCount);

	/**
	 * 查询审批中最大月份下最大周的数据 add by sychen 20100319
	 * @param enterpriseCode
	 * @return
	 */
	public String getRepairApproveList(String enterpriseCode);
	
	/**
	 * 判断审批页面时间下主表Id是否存在 add by sychen 20100320
	 * @param planTime
	 * @param planWeek
	 * @param enterpriseCode
	 * @return
	 */
	public String getRepairApproveDeptId(String planTime,String planWeek,String enterpriseCode,String people);
}