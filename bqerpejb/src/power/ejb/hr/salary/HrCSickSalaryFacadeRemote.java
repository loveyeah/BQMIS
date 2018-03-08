package power.ejb.hr.salary;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCSickSalaryFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCSickSalaryFacadeRemote {

	/**
	 * 保存一条病假工资记录
	 * 
	 * @param entity HrCSickSalary
	 */
	public void save(HrCSickSalary entity);

	/**
	 * 删除病假工资记录
	 * 
	 * @param sickSalary String
	 */
	public void delete(String sickSalary);

	/**
	 * 更新一条病假工资记录
	 * @param entity HrCSickSalary
	 * @return HrCSickSalary
	 */
	public HrCSickSalary update(HrCSickSalary entity);

	/**
	 * 根据主key病假工资id检索数据
	 * 
	 * @param id Long
	 * @return HrCSickSalary
	 */
	public HrCSickSalary findById(Long id);

	/**
	 * 查询所有的病假工资记录
	 * 
	 * @param rowStartIdxAndCount int
	 * @return PageObject
	 */
	public PageObject findAll(String enterpriseCode, int... rowStartIdxAndCount);
}