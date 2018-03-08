package power.ejb.hr.salary;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCSenioritySalaryFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCSenioritySalaryFacadeRemote {

	/**
	 * 增加一条工龄工资记录
	 * @param entity
	 * @return
	 */
	public HrCSenioritySalary save(HrCSenioritySalary entity);

	/**
	 * 删除一条或多条工龄工资记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**、
	 * 修改一条工龄工资记录
	 * @param entity
	 * @return
	 */
	public HrCSenioritySalary update(HrCSenioritySalary entity);

	/**
	 * 根据ID查找一条工龄工资记录详细信息
	 * @param id
	 * @return
	 */
	public HrCSenioritySalary findById(Long id);

	/**
	 * 查找工龄工资所有记录
	 * @param sDate
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findSenioritySalaryList(String sDate,String enterpriseCode,final int... rowStartIdxAndCount);
}