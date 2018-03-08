package power.ejb.hr.salary;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 岗位薪点工资维护
 *@author liuyi 090927
 */
@Remote
public interface HrCPointSalaryFacadeRemote {
	/**
	 *新增一条岗位薪点工资维护记录
	 */
	public void save(HrCPointSalary entity);

	/**
	 * 删除一条岗位薪点工资维护记录
	 */
	public void delete(HrCPointSalary entity);
	
	/**
	 * 删除一条或多条岗位薪点工资维护记录
	 */
	public void delete(String ids);

	/**
	 * P更新一条岗位薪点工资维护记录
	 */
	public HrCPointSalary update(HrCPointSalary entity);

	public HrCPointSalary findById(Long id);

	/**
	 * 查找所有符合条件的岗位薪点工资维护记录
	 */
	public PageObject findAll(Long checkGrade,String enterpriseCode,int... rowStartIdxAndCount);
	
	public void savePointSalaryRec(List<HrCPointSalary> addList,List<HrCPointSalary> updateList);
}