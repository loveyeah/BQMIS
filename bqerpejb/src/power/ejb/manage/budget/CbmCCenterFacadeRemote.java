package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for CbmCCenterFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmCCenterFacadeRemote {

	/**
	 * 增加一条预算部门记录
	 * 
	 * @param entity
	 * @return
	 */
	public CbmCCenter save(CbmCCenter entity) throws CodeRepeatException;

	/**
	 * 删除一条或多条预算部门记录
	 * 
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改一条预算部门记录
	 * 
	 * @param entity
	 * @return
	 */
	public CbmCCenter update(CbmCCenter entity) throws CodeRepeatException;

	/**
	 * 通过ID查找一条预算部门记录的详细信息
	 * 
	 * @param id
	 * @return
	 */
	public CbmCCenter findById(Long id);

	/**
	 * 通过部门名称查询预算部门列表
	 * 
	 * @param enterpriseCode
	 * @param deptName
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String enterpriseCode, String deptName,
			final int... rowStartIdxAndCount);

	/**
	 * 通过部门名称查询预算部门列表 责任部门为Y add by liuyi 090805
	 * 
	 * @param enterpriseCode
	 * @param deptName
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAllDuty(String enterpriseCode, String deptName,
			final int... rowStartIdxAndCount);
}