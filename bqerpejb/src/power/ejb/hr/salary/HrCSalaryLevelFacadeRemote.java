package power.ejb.hr.salary;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCSalaryLevelFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCSalaryLevelFacadeRemote {

	/**
	 * 增加一条岗位薪级记录
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public HrCSalaryLevel save(HrCSalaryLevel entity) throws CodeRepeatException;

	/**
	 * 删除一条或多条岗位薪级记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改一条岗位薪级记录
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public HrCSalaryLevel update(HrCSalaryLevel entity) throws CodeRepeatException;

	/**
	 * 根据ID查找一条岗位薪级记录的详细信息
	 * @param id
	 * @return
	 */
	public HrCSalaryLevel findById(Long id);

	/**
	 * 根据企业编码，薪级名称查询列表记录
	 * @param enterpriseCode
	 * @param salaryLevelName
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findSalaryLevelList(String enterpriseCode,String salaryLevelName,final int... rowStartIdxAndCount);
	
	/**
	 * add by liuyi 090929 获得薪级列表，用做页面上组合框的数据
	 * @return
	 */
	public List getSalaryLevelList();
}