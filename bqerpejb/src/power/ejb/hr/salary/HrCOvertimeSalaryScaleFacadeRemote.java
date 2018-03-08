package power.ejb.hr.salary;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCOvertimeSalaryScaleFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCOvertimeSalaryScaleFacadeRemote {
	/**
	 * 增加一条加班工资系数记录
	 * @param entity
	 */
	public void save(HrCOvertimeSalaryScale entity);
	/**
	 * 重置方法
	 * @param entity
	 */
	public void deleteAndAddRecord(String ids,String enterpriseCode);

	/**
	 * 修改一条加班工资系数记录
	 * @param entity
	 * @return
	 */
	public HrCOvertimeSalaryScale update(HrCOvertimeSalaryScale entity);

	/**
	 * 根据ID查找一条加班工资系数记录
	 * @param id
	 * @return
	 */
	public HrCOvertimeSalaryScale findById(Long id);

	/**
	 * 批量保存
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void saveOvertimeSalaryRecord(List<Map> list);
	
	/**
	 * 查找加班工资系数记录
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findSalaryScaleList(String enterpriseCode,final int... rowStartIdxAndCount);
}