package power.ejb.hr.salary;

import java.util.List;
import javax.ejb.Remote;

/**
 * 薪酬明细表
 * @author liuyi 090928
 */
@Remote
public interface HrJSalaryDetailFacadeRemote {
	/**
	 * 新增一条薪酬明细表数据
	 */
	public void save(HrJSalaryDetail entity);

	/**删除一条薪酬明细表数据
	 */
	public void delete(HrJSalaryDetail entity);

	/**
	 * 更新一条薪酬明细表数据
	 */
	public HrJSalaryDetail update(HrJSalaryDetail entity);

	public HrJSalaryDetail findById(Long id);

	
	public List<HrJSalaryDetail> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<HrJSalaryDetail> findAll(int... rowStartIdxAndCount);
}