package power.ejb.productiontec.insulation;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtJyjdJSybglhFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJyjdJSybglhFacadeRemote {
	
	/**
	 * 增加一条绝缘监督实验报告
	 * @param entity
	 * @return
	 */
	public PtJyjdJSybglh save(PtJyjdJSybglh entity);

	/**
	 * 删除一条或多条绝缘监督实验报告
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改一条绝缘监督实验报告
	 * @param entity
	 * @return
	 */
	public PtJyjdJSybglh update(PtJyjdJSybglh entity);

	/**
	 * 根据ID查找一条记录的信息
	 * @param id
	 * @return
	 */
	public PtJyjdJSybglh findById(Long id);

	/**
	 * 查找绝缘监督实验报告列表
	 * @param equName
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String equName,String enterpriseCode,final int... rowStartIdxAndCount);
}