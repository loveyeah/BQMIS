package power.ejb.manage.plan;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpJYearPlanFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
/**
 * @author code_cj
 *
 */
/**
 * @author code_cj
 *
 */
@Remote
public interface BpJYearPlanFacadeRemote {
	
	/**查询年度计划表
	 * @param year
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public  PageObject getYearPlan(String year,String enterpriseCode,int... rowStartIdxAndCount);
	/**保存年度计划
	 * @param entity
	 * @return
	 */
	public BpJYearPlan save(BpJYearPlan entity);

	
	/**删除一条年度计划
	 * @param entity
	 */
	public void delete(BpJYearPlan entity);

	
	/**修改年度计划
	 * @param entity
	 * @return
	 */
	public BpJYearPlan update(BpJYearPlan entity);

	/**通过指定id找到该条记录
	 * @param id
	 * @return
	 */
	public BpJYearPlan findById(Long id);
	
	/**删除多条年度计划记录
	 * @param ids
	 */
	public void deleteYearPlan(String ids);

	
	
	
}