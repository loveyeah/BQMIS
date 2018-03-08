package power.ejb.manage.plan;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpCPlanJobDeptFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCPlanJobDeptFacadeRemote {
	
	/**查询所有一级部门信息
	 * @param deptName
	 * @return
	 */
	public PageObject  getAllDept(String deptName);

	/**修改一级部门的排序号信息
	 * @param updateList
	 */
	public void updateLevalOneDept(List<BpCPlanJobDept> updateList);
	/**增加一级部门排序信息
	 * @param addList
	 */
	public void saveLevelOneDept(List<BpCPlanJobDept>  addList);
	
	/**删除一级部门信息
	 * @param entity
	 */
	public  void deleteDept(String ids);
	public void delete(BpCPlanJobDept entity);
	/**根据id查找一级部门信息
	 * @param id
	 * @return
	 */
	public BpCPlanJobDept findById(Long id);

	
}