package power.ejb.productiontec.chemistry;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtHxjdJNqqxlDetailFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtHxjdJNqqxlDetailFacadeRemote {

	/**
	 * 增加一条凝汽器泄漏明细记录
	 * @param entity
	 * @return
	 */
	public PtHxjdJNqqxlDetail save(PtHxjdJNqqxlDetail entity);

	/**
	 * 删除一条或多条凝汽器泄漏明细记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	public PtHxjdJNqqxlDetail update(PtHxjdJNqqxlDetail entity);

	public PtHxjdJNqqxlDetail findById(Long id);

	/**
	 * 根据主表ID ，企业编码查找列表
	 * @param nqjxlId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String nqjxlId,String enterpriseCode,int... rowStartIdxAndCount);
}