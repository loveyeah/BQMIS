package power.ejb.productiontec.retrenchenergy;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 中长期节能规划
 * 
 * @author fyyang 090707
 */
@Remote
public interface PtJnjdJZcqjnghFacadeRemote {

	/**
	 * 增加一条中长期节能规划信息
	 * @param entity
	 * @return
	 */
	public PtJnjdJZcqjngh save(PtJnjdJZcqjngh entity);

	/**
	 * 修改一条中长期节能规划信息
	 * @param entity
	 * @return
	 */
	public PtJnjdJZcqjngh update(PtJnjdJZcqjngh entity);
	
	/**
	 * 删除一条或多条中长期节能规划信息
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 查找一条中长期节能规划信息
	 * @param id
	 * @return
	 */
	public PtJnjdJZcqjngh findById(Long id);

	/**
	 * 查询中长期节能规划信息列表
	 * @param strYear
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String strYear,String enterpriseCode,final int... rowStartIdxAndCount);
}