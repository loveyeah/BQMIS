package power.ejb.hr.salary;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCBasisSalaryFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCBasisSalaryFacadeRemote {
	
	/**
	 * 新增一条基础工资维护记录
	 * @param entity
	 * @return
	 */
	public HrCBasisSalary save(HrCBasisSalary entity);

	/**
	 * 删除一条或多条基础工资记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改一条基础工资记录
	 * @param entity
	 * @return
	 */
	public HrCBasisSalary update(HrCBasisSalary entity);

	/**
	 * 根据ID查找一条基础工资记录详细
	 * @param id
	 * @return
	 */
	public HrCBasisSalary findById(Long id);

	/**
	 * 根据时间段查询所有记录信息
	 * @param sDate
	 * @param eDate
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findBaseSalaryList(String sDate,String enterpriseCode,final int... rowStartIdxAndCount);
}