package power.ejb.hr.salary;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 加班工资基数维护
 * 
 * @author drdu 20100203
 */
@Remote
public interface HrCOvertimeSalaryBaseFacadeRemote {

	/**
	 * 增加一条加班工资基数记录
	 * @param entity
	 */
	public void save(HrCOvertimeSalaryBase entity);

	/**
	 * 删除一条或多条加班工资基数记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改一条加班工资基数记录
	 * @param entity
	 * @return
	 */
	public HrCOvertimeSalaryBase update(HrCOvertimeSalaryBase entity);

	/**
	 * 根据ID查找一条加班工资基数记录的详细信息
	 * @param id
	 * @return
	 */
	public HrCOvertimeSalaryBase findById(Long id);
	
	/**
	 * 根据条件查找加班工资基数记录
	 * 	 * @param sDate
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findOverTimeSalaryBaseList(String sDate,String enterpriseCode,final int...rowStartIdxAndCount);
}