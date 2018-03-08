package power.ejb.equ.base;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCBugsolutionFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCBugsolutionFacadeRemote {

	
	public Long save(EquCBugsolution entity);

	/**
	 *通过id删除一条故障解决方案
	 * @param solutionId
	 */
	public void delete(Long solutionId);

	/**
	 * 修改一条故障解决方案
	 * @param entity
	 * @return
	 */
	public EquCBugsolution update(EquCBugsolution entity);

	/**
	 * 通过id查询一条故障解决方案
	 * @param id
	 * @return
	 */
	public EquCBugsolution findById(Long id);

	/**
	 * 查询所有故障解决方案
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public List<EquCBugsolution> findAll(int... rowStartIdxAndCount);
	
	/**
	 * 通过原因查找对应的解决方案
	 * @param solutinDesc 方案描述
	 * @param reasonId 原因id
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findBugSolutionList(String solutinDesc,String reasonId,String enterpriseCode,final int... rowStartIdxAndCount);
	
	/**
	 * 删除某个原因对应的解决方案
	 * @param reasonId
	 * @param enterpriseCode
	 */
	public void deleteAllByReasonId(Long reasonId,String enterpriseCode);
}