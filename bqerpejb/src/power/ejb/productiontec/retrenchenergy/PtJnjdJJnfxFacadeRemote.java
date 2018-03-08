package power.ejb.productiontec.retrenchenergy;


import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 节能分析管理
 * 
 * @author fyyang 090706
 */
@Remote
public interface PtJnjdJJnfxFacadeRemote {
	
	/**
	 * 增加一条节能分析记录
	 * @param entity
	 * @return
	 */
	public PtJnjdJJnfx save(PtJnjdJJnfx entity);
	
	/**
	 * 修改一条节能分析记录
	 * @param entity
	 * @return
	 */
	public PtJnjdJJnfx update(PtJnjdJJnfx entity);
	
	/**
	 * 删除一条或多条节能分析记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 查找一条节能分析记录
	 * @param id
	 * @return
	 */
	public PtJnjdJJnfx findById(Long id);

	/**
	 * 查询节能分析记录列表
	 * @param dateMonth
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String dateMonth,String enterpriseCode,final int... rowStartIdxAndCount);
	

}