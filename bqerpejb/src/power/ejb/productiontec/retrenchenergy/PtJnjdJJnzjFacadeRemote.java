package power.ejb.productiontec.retrenchenergy;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 节能总结
 * 090707
 * @author fyyang 
 */
@Remote
public interface PtJnjdJJnzjFacadeRemote {
	
	/**
	 * 增加一条节能总结信息
	 * @param entity
	 * @return
	 */
	public PtJnjdJJnzj save(PtJnjdJJnzj entity);

	/**
	 * 修改一条节能总结信息
	 * @param entity
	 * @return
	 */
	public PtJnjdJJnzj update(PtJnjdJJnzj entity);

	/**
	 * 删除一条或多条节能总结信息
	 * @param ids
	 */
	public void deleteMulti(String ids);
	
	/**
	 * 查找一条节能总结信息
	 * @param id
	 * @return
	 */
	public PtJnjdJJnzj findById(Long id);

	/**
	 * 查询节能总结信息列表
	 * @param dateYear
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String dateYear,String enterpriseCode,final int... rowStartIdxAndCount);
}