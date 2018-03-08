package power.ejb.hr.salary;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 运行岗位倾斜政策维护
 * @author liuyi 090927 
 */
@Remote
public interface HrCSalaryPolicyFacadeRemote {
	/**
	 * 新增运行岗位倾斜政策维护数据
	 */
	public HrCSalaryPolicy save(HrCSalaryPolicy entity);

	/**
	 * 删除一条运行岗位倾斜政策维护
	 */
	public void delete(HrCSalaryPolicy entity);
	
	/**
	 * 删除一条或多条运行岗位倾斜政策维护数据
	 */
	public void delete(String ids);

	/**
	 * 更新一条运行岗位倾斜政策维护数据
	 */
	public HrCSalaryPolicy update(HrCSalaryPolicy entity);

	public HrCSalaryPolicy findById(Long id);
	

	/**
	 * 查询所有运行岗位倾斜政策维护数据
	 */
	public PageObject findAll(String stationName,String enterpriseCode,int... rowStartIdxAndCount);
}